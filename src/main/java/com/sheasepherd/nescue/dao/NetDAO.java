package com.sheasepherd.nescue.dao;

import com.sheasepherd.nescue.model.Net;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NetDAO extends JpaRepository<Net, UUID> {
}
