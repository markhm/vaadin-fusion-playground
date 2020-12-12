package fusion.playground.data.service;

import fusion.playground.data.entity.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResponseService extends MongoCrudService<Response, String>
{
    private static Log log = LogFactory.getLog(ResponseService.class);

    private ResponseRepository repository;
    private UserService userService;
    private QuestionService questionService;
    private PossibleAnswerService possibleAnswerService;

    public ResponseService(@Autowired ResponseRepository repository,
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
    protected ResponseRepository getRepository() {
        return repository;
    }

    public Response save(Response response)
    {
        return getRepository().save(response);
    }

    public List<QuestionResponse> getResponses(String userId, String surveyName)
    {
        User user = userService.findByUsername("testuser").get();

        Response probe = new Response();
        probe.user(user);
        probe.surveyName(surveyName);

        List<Response> responses = getRepository().findAll(Example.of(probe));

        List<QuestionResponse> questionResponses = new ArrayList<>();

        responses.forEach(response -> {

            QuestionResponse questionResponse = new QuestionResponse();
            questionResponse.questionText(response.question().text());
            questionResponse.questionNumber(response.question().orderNumber());

            PossibleAnswer possibleAnswer = possibleAnswerService.get(response.response()).get();
            questionResponse.responseText(possibleAnswer.text());

            questionResponses.add(questionResponse);
        });

        return questionResponses;
    }

}
