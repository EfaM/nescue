package com.sheasepherd.nescue.dao;

import com.sheasepherd.nescue.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonDAO extends JpaRepository <Person, UUID> {
}
