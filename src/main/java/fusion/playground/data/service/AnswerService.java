package fusion.playground.data.service;

import fusion.playground.data.entity.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

@Service
public class AnswerService extends MongoCrudService<Answer, String>
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

    public Answer save(Answer answer)
    {
        return getRepository().save(answer);
    }


}
