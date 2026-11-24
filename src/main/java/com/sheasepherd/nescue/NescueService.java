package com.sheasepherd.nescue;

import com.sheasepherd.nescue.dao.NetDAO;
import com.sheasepherd.nescue.dao.NetEventDAO;
import com.sheasepherd.nescue.dao.PersonDAO;
import com.sheasepherd.nescue.exception.NetNotFoundException;
import com.sheasepherd.nescue.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class NescueService {

    private final NetDAO netDAO;
    private final NetEventDAO netEventDAO;
    private final PersonDAO personDAO;

    public NescueService(NetDAO netDAO, NetEventDAO netEventDAO, PersonDAO personDAO) {
        this.netDAO = netDAO;
        this.netEventDAO = netEventDAO;
        this.personDAO = personDAO;
    }

    @Transactional
    public Net reportNet(String firstname, String lastname, String phoneNumber, Size size, Coordinates coordinates){

        Net currentNet = netDAO.save(new Net(coordinates, size));
        Person currentPerson = null;

        if (phoneNumber != null && !phoneNumber.isBlank()) {
            currentPerson = createOrFindAndUpdatePersonHelper(firstname, lastname, phoneNumber);
        }

        netEventDAO.save(new NetEvent(currentNet, currentPerson, Event.REPORT, coordinates)); // Beim anonymen Melden netFinder bleibt null, da nichts eingetragen oder gefunden wurde
        return currentNet;
    }

    @Transactional
    public Net assignNet(String firstname, String lastname, String phoneNumber, UUID net){

        Net currentNet = netDAO.findById(net).orElseThrow(() -> new NetNotFoundException(net));

        if(currentNet.getStatus() != Status.REPORTED){
            throw new IllegalStateException("Nur gemeldete und nicht zugeordnete Netze können zugewiesen werden.");
        }

        Person currentPerson = createOrFindAndUpdatePersonHelper(firstname, lastname, phoneNumber);

        currentNet.setStatus(Status.RECOVERY); // Netzstatus: In Bergung
        currentNet.setAssignedPerson(currentPerson); // Netz: Neue zugeordnete Person

        netEventDAO.save(new NetEvent(currentNet, currentPerson, Event.ASSIGN, null)); // Neues Netz-Event
        return currentNet;
    }

    private Person createOrFindAndUpdatePersonHelper(String firstname, String lastname, String phoneNumber) {
        return personDAO.findByPhoneNumber(phoneNumber)
                .map(currentPerson -> {
                    currentPerson.setFirstname(firstname);
                    currentPerson.setLastname(lastname);
                    return currentPerson;
                })
                .orElseGet(()-> personDAO.save(new Person(firstname, lastname, phoneNumber))
        );
    }

    public List<Net> getNetsList(){
        return netDAO.findByStatusNotOrderByCoordinatesLatitudeAsc(Status.RECOVERED); // Damit können geborgene Netze bereits ausgeklammert werden
    }
}
