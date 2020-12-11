package fusion.playground.data.service;

import fusion.playground.data.entity.Answer;
import fusion.playground.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

import java.util.List;

@Service
public class AnswerService extends MongoCrudService<Answer, String>
{
    private AnswerRepository repository;
    private UserService userService;

    public AnswerService(@Autowired AnswerRepository repository, @Autowired UserService userService)
    {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    protected AnswerRepository getRepository() {
        return repository;
    }

    public Answer save(Answer answer)
    {
        return getRepository().save(answer);
    }

    public List<Answer> getAnswers(String userId, String category)
    {
        User user = userService.get(userId).get();

        Answer probe = new Answer();
        probe.user(user);
        probe.category(category);

        return getRepository().findAll(Example.of(probe));
    }

}
