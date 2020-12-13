package fusion.playground.data.service;

import fusion.playground.data.endpoint.QuestionEndpoint;
import fusion.playground.data.entity.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SurveyResponseService extends MongoCrudService<SurveyResponse, String>
{
    private static Log log = LogFactory.getLog(SurveyResponseService.class);

    private UserService userService;
    private QuestionService questionService;
    private SurveyResponseRepository surveyResponseRepository;
    private PossibleAnswerService possibleAnswerService;
    private ResponseService responseService;

    @Override
    protected SurveyResponseRepository getRepository()
    {
        return this.surveyResponseRepository;
    }

    public SurveyResponseService(@Autowired UserService userService,
                                 @Autowired QuestionService questionService,
                                 @Autowired SurveyResponseRepository surveyResponseRepository,
                                 @Autowired PossibleAnswerService possibleAnswerService,
                                 @Autowired ResponseService responseService)
    {
        this.userService = userService;
        this.questionService = questionService;
        this.surveyResponseRepository = surveyResponseRepository;
        this.possibleAnswerService = possibleAnswerService;
        this.responseService = responseService;
    }

    public String beginSurvey(User user, Survey survey)
    {
        SurveyResponse surveyResponse = new SurveyResponse();
        surveyResponse.user(user);
        surveyResponse.survey(survey);
        surveyResponse.startTime(LocalDateTime.now());

        SurveyResponse savedSurveyResponse = surveyResponseRepository.save(surveyResponse);

        return savedSurveyResponse.id();
    }

    public void endSurvey(SurveyResponse surveyResponse)
    {
        surveyResponse.endTime(LocalDateTime.now());

        surveyResponseRepository.save(surveyResponse);
    }

    public List<QuestionResponse> getSurveyResponses(User user, String surveyName)
    {
//        Response probe = new Response();
//        probe.user(user);
//        probe.surveyName(surveyName);

        //  = getRepository().findAll(Example.of(probe));

        List<SurveyResponse> surveyResponses = getRepository().findAll();

        List<SurveyResponse> completedResponses = surveyResponses.stream().filter(response -> response.isComplete()).collect(Collectors.toList());

        // taking the latest for now
        SurveyResponse latestSurveyResponse = completedResponses.get(completedResponses.size() - 1);
        List<Response> responses = latestSurveyResponse.responses();

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

    public Response saveResponse(String surveyResponseId, String questionId, String responseId)
    {
        SurveyResponse surveyResponse = get(surveyResponseId).get();
        if (surveyResponse.isComplete())
        {
            Response dummyResponse = new Response();
            dummyResponse.response("Survey already complete.");
            return dummyResponse;
        }

        Response response = new Response();
        Question question = questionService.get(questionId).get();

        Set<String> possibleAnswerIds = question.possibleAnswers().stream().map(possibleAnswer -> possibleAnswer.id()).collect(Collectors.toSet());
        if (! possibleAnswerIds.contains(responseId))
        {
            log.error("Impossible response for this question. Ignoring.");
            return null;
        }

        response.user(surveyResponse.user());
        response.question(question);
        response.response(responseId);
        response.surveyName(question.surveyName());

        Response savedResponse = save(surveyResponse, response);

        return savedResponse;
    }

    private Response save(SurveyResponse surveyResponse, Response response)
    {
        Response savedResponse = responseService.update(response);
        surveyResponse.addResponse(savedResponse);
        surveyResponseRepository.save(surveyResponse);

        return savedResponse;
    }



}
