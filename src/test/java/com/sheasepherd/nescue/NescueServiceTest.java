package com.sheasepherd.nescue;

import com.sheasepherd.nescue.dao.NetDAO;
import com.sheasepherd.nescue.dao.NetEventDAO;
import com.sheasepherd.nescue.dao.PersonDAO;
import com.sheasepherd.nescue.model.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NescueServiceTest {

    @Mock
    private NetDAO netDAO;

    @Mock
    private NetEventDAO netEventDAO;

    @Mock
    private PersonDAO personDAO;

    @InjectMocks
    private NescueService nescueService;

    @Test
    void maxOnePersonAssignedToANet(){ // Fachliches Kriterium: Jedes Netz nur einer Person zugeordnet
        UUID netId = UUID.randomUUID();
        Net net = new Net(new Coordinates(BigDecimal.ONE, BigDecimal.ONE), Size.LARGE);
        net.setStatus(Status.RECOVERY);
        when(netDAO.findById(netId)).thenReturn(Optional.of(net));

        assertThatThrownBy(() ->
                nescueService.assignNet("Clara", "Testfall", "+12724513124", netId)).isInstanceOf(IllegalStateException.class);
        verifyNoInteractions(personDAO, netEventDAO);
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = "REPORTED", mode=EnumSource.Mode.EXCLUDE)
    void assignAPersonToANetOnlyWorksForReportedNets(Status status){ // Fachliches Kriterium: Eintragen erst nach Status gemeldet möglich
        UUID netId = UUID.randomUUID();
        Net net = new Net(new Coordinates(BigDecimal.ONE, BigDecimal.ONE), Size.LARGE);
        net.setStatus(status);
        when(netDAO.findById(netId)).thenReturn(Optional.of(net));

        assertThatThrownBy(() ->
                nescueService.assignNet("Clara", "Testfall", "+12724513124", netId)).isInstanceOf(IllegalStateException.class);
        verifyNoInteractions(personDAO, netEventDAO);
    }

    @Test
    void assignedPersonOnlyOnceSaved(){ //Fachliches Kriterium: Person wird durch Telefonnummer wiedererkannt
        UUID netId = UUID.randomUUID();
        Net net = new Net(new Coordinates(BigDecimal.ONE, BigDecimal.ONE), Size.LARGE);
        Person knownPerson = new Person("Clara", "Testfall", "+12724513124");

        when(netDAO.findById(netId)).thenReturn(Optional.of(net));
        when(personDAO.findByPhoneNumber("+12724513124")).thenReturn(Optional.of(knownPerson));

        nescueService.assignNet( "Clara", "Neuername","+12724513124", netId);

        verify(personDAO, never()).save(any());
        assertThat(net.getAssignedPerson()).isSameAs(knownPerson);
        assertThat(knownPerson.getLastname()).isEqualTo("Neuername");
    }

    @Test
    void assignedPersonIsUnknown(){ // Fachliches Kriterium: Person bisher unbekannt, wird abgespeichert
        UUID netId = UUID.randomUUID();
        Net net = new Net(new Coordinates(BigDecimal.ONE, BigDecimal.ONE), Size.LARGE);

        when(netDAO.findById(netId)).thenReturn(Optional.of(net));
        when(personDAO.findByPhoneNumber("+17224513124")).thenReturn(Optional.empty());
        when(personDAO.save(any(Person.class))).thenAnswer(call -> call.getArgument(0));

        nescueService.assignNet( "Clarissa", "Tasthall", "+17224513124", netId);

        verify(personDAO).save(any(Person.class));
        assertThat(net.getAssignedPerson()).isNotNull();
        assertThat(net.getAssignedPerson().getPhoneNumber()).isEqualTo("+17224513124");
    }

    @Test
    void assignmentCreatesNewEventAndChangesNetStatus(){ // Kriterium zur Datenhaltung: Netz, Person, Aktion und Zeitpunkt werden gespeichert
        UUID netId = UUID.randomUUID();
        Net net = new Net(new Coordinates(BigDecimal.ONE, BigDecimal.ONE), Size.LARGE);
        Person knownPerson = new Person("Clara", "Testfall", "+12724513124");

        when(netDAO.findById(netId)).thenReturn(Optional.of(net));
        when(personDAO.findByPhoneNumber("+12724513124")).thenReturn(Optional.of(knownPerson));

        nescueService.assignNet( "Clara", "Neuername","+12724513124", netId);

        ArgumentCaptor<NetEvent> eventArgumentCaptor = ArgumentCaptor.forClass(NetEvent.class);
        verify(netEventDAO).save(eventArgumentCaptor.capture());
        NetEvent netEvent = eventArgumentCaptor.getValue();

        assertThat(net.getStatus()).isEqualTo(Status.RECOVERY);
        assertThat(netEvent.getEvent()).isEqualTo(Event.ASSIGN);
        assertThat(netEvent.getPerson()).isSameAs(knownPerson);
        assertThat(netEvent.getNet()).isSameAs(net);
        assertThat(netEvent.getTimestamp()).isNotNull();
    }

}