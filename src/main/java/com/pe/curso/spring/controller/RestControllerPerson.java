package com.pe.curso.spring.controller;

import com.pe.curso.spring.models.Person;
import com.pe.curso.spring.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class RestControllerPerson {
    @Autowired
    private PersonService personService;

    @GetMapping(value = "/list", headers = "Accept=application/json")
    public List<Person> listPerson(){
        return personService.listAllPerson();
    }

    @PostMapping(value = "/create", headers = "Accept=application/json")
    public String createPerson(@RequestBody Person person){
        personService.createPerson(person);
        return "Person created";
    }

    @PostMapping(value = "/update", headers = "Accept=application/json")
    public String updatePerson(@RequestBody Person person){
        personService.updatePerson(person);
        return "Person updated";
    }

    @PostMapping(value = "/delete", headers = "Accept=application/json")
    public String deletePerson(Person person){
        personService.deletePerson(person);
        return "Person deleted";
    }

    @PostMapping(value = "/find", headers = "Accept=application/json")
    public Person findPerson(Long idPerson){
        return personService.findById(idPerson);
    }


}
