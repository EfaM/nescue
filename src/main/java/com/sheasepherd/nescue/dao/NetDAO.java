package com.sheasepherd.nescue.dao;

import com.sheasepherd.nescue.model.Net;
import com.sheasepherd.nescue.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NetDAO extends JpaRepository<Net, UUID> {

    List<Net> findByStatusNotOrderByCoordinatesLatitudeAsc (Status status);
}
