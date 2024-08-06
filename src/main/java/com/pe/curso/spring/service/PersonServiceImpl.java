package com.pe.curso.spring.service;

import com.pe.curso.spring.dao.PersonDao;
import com.pe.curso.spring.models.Person;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonServiceImpl  implements PersonService {

    private final PersonDao personDao;

    public PersonServiceImpl(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Override
    @Transactional
    public void createPerson(Person person) {
        personDao.save(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> listAllPerson() {
        return (List<Person>) personDao.findAll();
    }

    @Override
    @Transactional
    public void updatePerson(Person person) {
        personDao.save(person);
    }

    @Override
    @Transactional
    public void deletePerson(Person person) {
        personDao.delete(person);
    }

    @Override
    @Transactional(readOnly = true)
    public Person findById(Long idPerson) {
        return personDao.findById(idPerson).orElse(null);
    }
}
