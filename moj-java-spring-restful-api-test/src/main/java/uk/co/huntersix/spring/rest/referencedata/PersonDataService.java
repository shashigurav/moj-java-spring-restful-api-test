package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonDataService {
    public static final List<Person> PERSON_DATA = Arrays.asList(
        new Person("Mary", "Smith"),
        new Person("Brian", "Archer"),
        new Person("Collin", "Brown")
    );

    public Person findPerson(String lastName, String firstName) {
        return PERSON_DATA.stream()
            .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                && p.getLastName().equalsIgnoreCase(lastName))
            .collect(Collectors.toList()).get(0);
    }

    /**
     * Find Person with lastName
     * @param lastName
     * @return
     */
    public List<Person> findPersonWithSurname(String lastName) {
        return PERSON_DATA.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    /**
     * Add Person
     * @param lastName
     * @param firstName
     * @return
     */

    public Person addPerson(String lastName, String firstName) {

        boolean isPersonPresent = PERSON_DATA.stream()
                .anyMatch(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName));
        if(isPersonPresent) {
            return null;
        }

        Person newPerson = new Person(firstName, lastName);
        PERSON_DATA.add(newPerson);
        return newPerson;
    }
}
