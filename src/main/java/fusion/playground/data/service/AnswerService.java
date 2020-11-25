package fusion.playground.data.service;

import fusion.playground.domain.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class AnswerService extends CrudService<Answer, Integer>
{
    private AnswerRepository repository;

    public AnswerService(@Autowired AnswerRepository repository)
    {
        this.repository = repository;
    }

    @Override
    protected AnswerRepository getRepository() {
        return repository;
    }

}
