package com.sheasepherd.nescue.controller;

import com.sheasepherd.nescue.NescueService;
import com.sheasepherd.nescue.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(NetListController.class)
class NetListControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @MockitoBean
    private NescueService nescueService;


    @Test
    void assignedPersonIsSaved() {
        UUID netId = UUID.randomUUID();

        assertThat(mockMvc.post().uri("/ghost-nets/{id}/assign", netId)
                .param("firstname", "Clara")
                .param("lastname", "Testfall")
                .param("phoneNumber", "+12724513124")
        ).hasStatus3xxRedirection().hasRedirectedUrl("/ghost-nets?success");

        verify(nescueService).assignNet(eq("Clara"), eq("Testfall"), eq("+12724513124"), eq(netId));
    }

    @Test
    void assignedPersonMissingFirstname() { // Fachliches Kriterium: Fehlende Angaben zeigt eine Fehlermeldung
        assertThat(mockMvc.post().uri("/ghost-nets/{id}/assign", UUID.randomUUID())
                .param("lastname", "Testfall")
                .param("phoneNumber", "+12724513124")
        ).model().extractingBindingResult("assignForm").hasFieldErrors("firstname");
        verify(nescueService, never()).assignNet(any(), any(), any(), any());
    }

    @Test
    void assignedPersonMissingLastname() { // Fachliches Kriterium: Fehlende Angaben zeigt eine Fehlermeldung
        assertThat(mockMvc.post().uri("/ghost-nets/{id}/assign", UUID.randomUUID())
                .param("firstname", "Clara")
                .param("phoneNumber", "+12724513124")
        ).model().extractingBindingResult("assignForm").hasFieldErrors("lastname");
        verify(nescueService, never()).assignNet(any(), any(), any(), any());
    }

    @Test
    void assignedPersonMissingPhoneNumber() { // Fachliches Kriterium: Fehlende Angaben zeigt eine Fehlermeldung
        assertThat(mockMvc.post().uri("/ghost-nets/{id}/assign", UUID.randomUUID())
                .param("firstname", "Clara")
                .param("lastname", "Testfall")
        ).model().extractingBindingResult("assignForm").hasFieldErrors("phoneNumber");
        verify(nescueService, never()).assignNet(any(), any(), any(), any());
    }

    @Test
    void assignedPersonPhoneNumberWithoutPlus() {
        assertThat(mockMvc.post().uri("/ghost-nets/{id}/assign", UUID.randomUUID())
                .param("firstname", "Clara")
                .param("lastname", "Testfall")
                .param("phoneNumber", "12724513124")
        ).model().extractingBindingResult("assignForm").hasFieldErrors("phoneNumber");
        verify(nescueService, never()).assignNet(any(), any(), any(), any());
    }

    @Test
    void assignedPersonPhoneNumberWithLetters() { // Fachliches Kriterium: Das Telefonnummernfeld ist validierbar und standardisiert
        assertThat(mockMvc.post().uri("/ghost-nets/{id}/assign", UUID.randomUUID())
                .param("firstname", "Clara")
                .param("lastname", "Testfall")
                .param("phoneNumber", "+127A45B3ADV")
        ).model().extractingBindingResult("assignForm").hasFieldErrors("phoneNumber");
        verify(nescueService, never()).assignNet(any(), any(), any(), any());
    }

    @Test
    void assignedAnetWichIsAlreadyAssigned(){
        UUID netId = UUID.randomUUID();

        doThrow(new IllegalStateException()).when(nescueService).assignNet(any(), any(), any(), eq(netId));

        assertThat(mockMvc.post().uri("/ghost-nets/{id}/assign", netId)
                .param("firstname", "Clara")
                .param("lastname", "Testfall")
                .param("phoneNumber","+12724513124")
        ).hasStatus3xxRedirection().hasRedirectedUrl("/ghost-nets?alreadyAssigned");
    }

    @Test
    void modalReopenAfterMissingInputsAndDontChangesDatabase(){
        UUID netId = UUID.randomUUID();

        assertThat(mockMvc.post().uri("/ghost-nets/{id}/assign", netId)
                .param("firstname", "Clara") // Nachname fehlt
                .param("phoneNumber", "+12724513124")
        )   .hasStatusOk()
            .hasViewName("ghost-nets")
            .model().containsEntry("assignedNetId", netId);

        verify(nescueService, never()).assignNet(any(), any(), any(), any());
    }
}