package com.sheasepherd.nescue;

import com.sheasepherd.nescue.dao.NetDAO;
import com.sheasepherd.nescue.dao.NetEventDAO;
import com.sheasepherd.nescue.dao.PersonDAO;
import com.sheasepherd.nescue.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

        Net net = netDAO.save(new Net(coordinates, size));

        Person netFinder = null;
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            Optional<Person> formerReporter = personDAO.findByPhoneNumber(phoneNumber);
            if (formerReporter.isEmpty()) {
                netFinder = personDAO.save(new Person(firstname, lastname, phoneNumber));
            } else {
                netFinder = formerReporter.get();
                netFinder.setFirstname(firstname);  // Updated den aktuellen Vornamen
                netFinder.setLastname(lastname);  // Updated den aktuellen Nachnamen
            }
        }
        netEventDAO.save(new NetEvent(net, netFinder, Event.REPORT, coordinates)); // Beim anonymen Melden netFinder bleibt null, da nichts eingetragen oder gefunden wurde

        return net;
    }

    public List<Net> getNetsList(){
        return netDAO.findByStatusNotOrderByCoordinatesLatitudeAsc(Status.RECOVERED);// Damit können geborgende Netze bereits ausgeklammert werden
    }
}
