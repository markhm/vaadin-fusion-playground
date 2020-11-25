package fusion.playground.data.endpoint;


import fusion.playground.data.CrudEndpoint;
import fusion.playground.data.entity.Person;
import fusion.playground.data.service.PersonService;
import com.vaadin.flow.server.connect.Endpoint;

import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Endpoint
@AnonymousAllowed
public class PersonEndpoint extends CrudEndpoint<Person, Integer> {

    private PersonService service;

    public PersonEndpoint(@Autowired PersonService service) {
        this.service = service;
    }

    @Override
    protected PersonService getService() {
        return service;
    }

}
