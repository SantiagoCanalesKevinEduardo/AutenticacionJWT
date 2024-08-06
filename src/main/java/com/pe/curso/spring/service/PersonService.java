package com.pe.curso.spring.service;


import com.pe.curso.spring.models.Person;

import java.util.List;

public interface PersonService {
    public void createPerson(Person person);
    public List<Person> listAllPerson();
    public void updatePerson(Person person);
    public void deletePerson(Person person);
    public Person findById(Long idPerson);
}
