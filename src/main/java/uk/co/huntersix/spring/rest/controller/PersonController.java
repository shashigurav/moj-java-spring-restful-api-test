package uk.co.huntersix.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.net.URI;
import java.util.List;

@RestController
public class PersonController {

    private PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    //Exercise :2 updated controller to check if result is not null
    @GetMapping("/person/{lastName}/{firstName}")
    public ResponseEntity<Person> person(@PathVariable(value="lastName") String lastName,
                                         @PathVariable(value="firstName") String firstName) {
         Person person = personDataService.findPerson(lastName, firstName);
         if (person == null ){
             return ResponseEntity.notFound().build();
         }
        return ResponseEntity.ok(person);
    }

    //Exercise :3
    @GetMapping("/person/{lastName}")
    public ResponseEntity<List<Person>> personWithSurname(@PathVariable(value="lastName") String lastName) {
        List<Person> personList = personDataService.findPersonWithSurname(lastName);
        if (personList == null || personList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(personList);
    }
    //Exercise :4
    @PostMapping("/person/{lastName}/{firstName}")
    public ResponseEntity<Person> addPerson(@PathVariable(value="lastName") String lastName,
                            @PathVariable(value="firstName") String firstName) {
        Person createdPerson = personDataService.addPerson(lastName, firstName);
        if (createdPerson == null ){
            return ResponseEntity.status(409).build();
        }
        return ResponseEntity.created(URI.create(createdPerson.getId().toString())).body(createdPerson);
    }
}