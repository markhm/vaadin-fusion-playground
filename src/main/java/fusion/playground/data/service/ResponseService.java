package fusion.playground.data.service;

import fusion.playground.data.entity.*;
import fusion.playground.data.repository.ResponseRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

@Service
public class ResponseService extends MongoCrudService<Response, String>
{
    private static Log log = LogFactory.getLog(ResponseService.class);

    private ResponseRepository repository;

    public ResponseService(@Autowired ResponseRepository repository)
    {
        this.repository = repository;
    }

    @Override
    public ResponseRepository getRepository()
    {
        return repository;
    }
}
