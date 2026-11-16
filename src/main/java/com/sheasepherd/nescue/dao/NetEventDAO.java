package com.sheasepherd.nescue.dao;

import com.sheasepherd.nescue.model.NetEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NetEventDAO extends JpaRepository<NetEvent, UUID> {
}
