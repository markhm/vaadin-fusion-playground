package fusion.playground.data.service;

import fusion.playground.data.endpoint.QuestionEndpoint;
import fusion.playground.data.entity.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerService extends MongoCrudService<Answer, String>
{
    private static Log log = LogFactory.getLog(AnswerService.class);

    private AnswerRepository repository;
    private UserService userService;
    private QuestionService questionService;
    private PossibleAnswerService possibleAnswerService;

    public AnswerService(@Autowired AnswerRepository repository,
                         @Autowired UserService userService,
                         @Autowired QuestionService questionService,
                         @Autowired PossibleAnswerService possibleAnswerService)
    {
        this.repository = repository;
        this.userService = userService;
        this.questionService = questionService;
        this.possibleAnswerService = possibleAnswerService;
    }

    @Override
    protected AnswerRepository getRepository() {
        return repository;
    }

    public Answer save(Answer answer)
    {
        return getRepository().save(answer);
    }

    public List<AnsweredQuestion> getAnswers(String userId, String category)
    {
        User user = userService.findByUsername("testuser").get();

        Answer probe = new Answer();
        probe.user(user);
        probe.category(category);

        List<Answer> answers = getRepository().findAll(Example.of(probe));

        List<AnsweredQuestion> answeredQuestions = new ArrayList<>();

        answers.forEach(answer -> {

            AnsweredQuestion answeredQuestion = new AnsweredQuestion();
            answeredQuestion.questionText(answer.question().text());
            answeredQuestion.questionNumber(answer.question().orderNumber());

            PossibleAnswer possibleAnswer = possibleAnswerService.get(answer.answer()).get();
            answeredQuestion.answerText(possibleAnswer.text());

            answeredQuestions.add(answeredQuestion);
        });

        return answeredQuestions;
    }

}
