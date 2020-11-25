package fusion.playground.data.service;

import fusion.playground.domain.Question;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;


@Service
public class QuestionService extends CrudService<Question, Integer>
{
    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository)
    {
        this.repository = repository;
    }

    @Override
    protected QuestionRepository getRepository() {
        return repository;
    }
}
