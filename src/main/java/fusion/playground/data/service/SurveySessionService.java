package fusion.playground.data.service;

import fusion.playground.data.entity.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SurveySessionService extends MongoCrudService<SurveyResult, String>
{
    private static Log log = LogFactory.getLog(SurveySessionService.class);

    private UserService userService;
    private QuestionService questionService;
    private SurveyResultRepository surveyResultRepository;
    private PossibleAnswerService possibleAnswerService;
    private ResponseService responseService;

    @Override
    protected SurveyResultRepository getRepository()
    {
        return this.surveyResultRepository;
    }

    @Autowired
    public SurveySessionService(UserService userService, QuestionService questionService,
                                SurveyResultRepository surveyResultRepository,
                                PossibleAnswerService possibleAnswerService, ResponseService responseService)
    {
        this.userService = userService;
        this.questionService = questionService;
        this.surveyResultRepository = surveyResultRepository;
        this.possibleAnswerService = possibleAnswerService;
        this.responseService = responseService;
    }

    public String beginSurvey(User user, Survey survey)
    {
        SurveyResult surveyResult = new SurveyResult();
        surveyResult.user(user);
        surveyResult.survey(survey);
        surveyResult.startTime(LocalDateTime.now());

        SurveyResult savedSurveyResult = surveyResultRepository.save(surveyResult);

        return savedSurveyResult.id();
    }

    public SurveyResult confirmResponses(String surveyResultId)
    {
        SurveyResult surveyResult = get(surveyResultId).get();
        surveyResult.registerUserConfirmation();
        if (surveyResult.survey().gradable())
        {
            surveyResult = gradeSurveyResult(surveyResult);
        }
        surveyResultRepository.save(surveyResult);

        return surveyResult;
    }

    public SurveyResult rejectResponses(String surveyResultId)
    {
        SurveyResult surveyResult = get(surveyResultId).get();
        surveyResult.registerUserRejection();
        surveyResultRepository.save(surveyResult);

        return surveyResult;
    }

    public List<QuestionResponse> getSurveyResponses(String surveyResultId)
    {
        SurveyResult surveyResult = get(surveyResultId).get();
        List<Response> responses = surveyResult.responses();

        List<QuestionResponse> questionResponses = new ArrayList<>();

        responses.forEach(response -> {

            QuestionResponse questionResponse = new QuestionResponse();
            questionResponse.questionText(response.question().text());
            questionResponse.questionNumber(response.question().orderNumber());
            questionResponse.correct(response.correct());

            PossibleAnswer possibleAnswer = possibleAnswerService.get(response.response()).get();
            questionResponse.responseText(possibleAnswer.text());

            questionResponses.add(questionResponse);
        });

        return questionResponses;
    }

    public Response saveResponse(String surveyResponseId, String questionId, String responseId)
    {
        SurveyResult surveyResult = get(surveyResponseId).get();
        if (surveyResult.isComplete())
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

        response.user(surveyResult.user());
        response.question(question);
        response.response(responseId);
        Response savedResponse = save(surveyResult, response);

        return savedResponse;
    }

    public SurveyResult gradeSurveyResult(SurveyResult surveyResult)
    {
        if (!surveyResult.survey().gradable())
        {
            log.warn("Survey '"+ surveyResult.survey().name() + "' is not gradable. Returning ungraded result.");
            return null;
        }

        // evaluate every individual answer, store the results.
        surveyResult.responses().forEach(response ->
        {
            response.evaluateResponse();
            responseService.update(response);
        });

        int surveyScore = surveyResult.responses().stream().mapToInt(Response::gradeResponse).sum();

        // save the score and the result
        surveyResult.score(surveyScore);
        surveyResultRepository.save(surveyResult);

        return surveyResult;
    }

    public List<SurveyResult> getCompletedSurveys(User user)
    {
        return surveyResultRepository.findAllByUser(user);
    }

    private Response save(SurveyResult surveyResult, Response response)
    {
        Response savedResponse = responseService.update(response);
        surveyResult.addResponse(savedResponse);
        surveyResultRepository.save(surveyResult);

        return savedResponse;
    }
}