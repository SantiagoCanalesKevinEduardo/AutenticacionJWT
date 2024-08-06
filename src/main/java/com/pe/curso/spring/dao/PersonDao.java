package com.pe.curso.spring.dao;

import com.pe.curso.spring.models.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonDao extends CrudRepository<Person, Long> {

}
