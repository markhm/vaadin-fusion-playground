package fusion.playground.data.service;

import fusion.playground.data.entity.*;
import fusion.playground.data.repository.SurveyResultRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SurveySessionService extends MongoCrudService<SurveyResult, String>
{
    private static Log log = LogFactory.getLog(SurveySessionService.class);

    private UserService userService;
    private SurveyStepService surveyStepService;
    private SurveyService surveyService;
    private SurveyResultRepository surveyResultRepository;
    private PossibleAnswerService possibleAnswerService;
    private ResponseService responseService;

    @Override
    protected SurveyResultRepository getRepository()
    {
        return this.surveyResultRepository;
    }

    @Autowired
    public SurveySessionService(UserService userService, SurveyStepService surveyStepService,
                                SurveyService surveyService, PossibleAnswerService possibleAnswerService,
                                ResponseService responseService, SurveyResultRepository surveyResultRepository)
    {
        this.userService = userService;
        this.surveyStepService = surveyStepService;
        this.surveyService = surveyService;
        this.surveyResultRepository = surveyResultRepository;
        this.possibleAnswerService = possibleAnswerService;
        this.responseService = responseService;
    }

    public String beginSurvey(User user, Survey survey)
    {
        log.info("Beginning survey "+survey.id() + " for user " + user.id() + ".");

        SurveyResult surveyResult = new SurveyResult();
        surveyResult.user(user);
        surveyResult.survey(survey);
        surveyResult.startTime(LocalDateTime.now());

        SurveyResult savedSurveyResult = surveyResultRepository.save(surveyResult);

        return savedSurveyResult.id();
    }

    public SurveyStep getNextSurveyStep(String surveyResultId)
    {
        log.info("Retrieving next surveyStep for surverResultId " + surveyResultId + ".");
        SurveyStep result = null;

        SurveyResult surveyResult = get(surveyResultId).get();
        if (surveyResult.isComplete())
        {
            result = new SurveyStep();
            result.questionNumber(-1);
            result.text("Survey already completed.");
        }
        else
        {
            int lastCompletedStep = surveyResult.lastCompletedQuestion();

            Survey survey = surveyResult.survey();
            result = surveyService.getSurveyStepFromSurvey(survey, lastCompletedStep + 1);
        }
        if (surveyResult.survey().randomizeAnswerOrder())
        {
            // randomize the order of the possible answers
            List<PossibleAnswer> possibleAnswers = result.possibleAnswers();
            Collections.shuffle(possibleAnswers);
            result.possibleAnswers(possibleAnswers);
        }

        return result;
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
        SurveyStep question = surveyStepService.get(questionId).get();

        Set<String> possibleAnswerIds = question.possibleAnswers().stream().map(possibleAnswer -> possibleAnswer.id()).collect(Collectors.toSet());
        if (! possibleAnswerIds.contains(responseId))
        {
            log.error("Impossible response for this surveyStep. Ignoring.");
            return null;
        }

        response.user(surveyResult.user());
        response.question(question);
        response.response(responseId);
        Response savedResponse = save(surveyResult, response);

        return savedResponse;
    }

    public List<QuestionResponse> getSurveyResponses(String surveyResultId)
    {
        SurveyResult surveyResult = get(surveyResultId).get();
        List<Response> responses = surveyResult.responses();

        List<QuestionResponse> questionResponses = new ArrayList<>();

        responses.forEach(response -> {

            QuestionResponse questionResponse = new QuestionResponse();
            questionResponse.questionText(response.question().text());
            questionResponse.questionNumber(response.question().questionNumber());
            if (surveyResult.userHasConfirmed())
            {
                questionResponse.correct(response.correct());
            }

            PossibleAnswer possibleAnswer = possibleAnswerService.get(response.response()).get();
            questionResponse.responseText(possibleAnswer.text());

            questionResponses.add(questionResponse);
        });

        return questionResponses;
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

    public List<SurveyResult> getCompletedSurveys(String userId)
    {
        User user = userService.get(userId).get();
        return surveyResultRepository.findAllByUserAndComplete(user, true);
    }

    private Response save(SurveyResult surveyResult, Response response)
    {
        Response savedResponse = responseService.update(response);

        surveyResult.addResponse(savedResponse);
        surveyResultRepository.save(surveyResult);

        return savedResponse;
    }
}
