package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import fusion.playground.data.entity.Person;

import java.util.ArrayList;
import java.util.List;

@Endpoint
public class PeopleEndpoint
{
    // We'll use a simple list to hold data
    private final List<Person> people = new ArrayList<>();

    public PeopleEndpoint()
    {
        // Add one person so we can see that everything works
        Person person = new Person();
        person.firstName("Jane");
        person.lastName("Doe");

        people.add(person);
    }

    public List<Person> getPeople()
    {
        return people;
    }

    public Person addPerson(Person person)
    {
        people.add(person);
        return person;
    }
}
