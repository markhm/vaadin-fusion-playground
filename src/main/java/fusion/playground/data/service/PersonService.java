package fusion.playground.data.service;

import fusion.playground.domain.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import org.vaadin.artur.helpers.MongoCrudService;

@Service
public class PersonService extends MongoCrudService<Person, String>
{

    private PersonRepository repository;

    public PersonService(@Autowired PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    protected PersonRepository getRepository() {
        return repository;
    }

}
