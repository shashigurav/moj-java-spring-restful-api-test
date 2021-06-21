package uk.co.huntersix.spring.rest.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    @Test
    public void shouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(new Person("Mary", "Smith"));
        this.mockMvc.perform(get("/person/smith/mary"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("firstName").value("Mary"))
            .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    public void NoReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(null);
        this.mockMvc.perform(get("/person/smith/mary"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnPersonWithLastname() throws Exception {
        when(personDataService.findPersonWithSurname(any())).thenReturn(Collections
                .singletonList(new Person("Mary", "Smith")));
        ResultActions resultActions = this.mockMvc.perform(get("/person/smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Smith"));
    }

    @Test
    public void shouldReturnMultiplePersonsWithLastname() throws Exception {
        List<Person> list = new ArrayList<>();
        list.add(new Person("Mary", "Smith"));
        list.add(new Person("Mark", "Smith"));
        when(personDataService.findPersonWithSurname(any())).thenReturn((list));
        ResultActions resultActions = this.mockMvc.perform(get("/person/smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));
    }
    @Test
    public void personsNotFoundWithLastname() throws Exception {
        when(personDataService.findPersonWithSurname(any())).thenReturn((Collections.emptyList()));
        ResultActions resultActions = this.mockMvc.perform(get("/person/smith"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddPersonToService() throws Exception {
        when(personDataService.addPerson(any(), any())).thenReturn(new Person("Mary", "Smith"));
        this.mockMvc.perform(post("/person/smith/mary"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("firstName").value("Mary"))
                .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    public void notAddingPersonToService() throws Exception {
        when(personDataService.addPerson(any(), any())).thenReturn(null);
        this.mockMvc.perform(post("/person/smith/mary"))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}