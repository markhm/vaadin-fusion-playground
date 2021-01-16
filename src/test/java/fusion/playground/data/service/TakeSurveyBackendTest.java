package fusion.playground.data.service;

import fusion.playground.data.entity.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// @SpringJUnitConfig
// @DataMongoTest
@SpringBootTest
public class TakeSurveyBackendTest extends AbstractServiceLayerTest
{
    private static Log log = LogFactory.getLog(TakeSurveyBackendTest.class);

    public TakeSurveyBackendTest()
    {}

    @Test
    public void surveyLoading()
    {
        Survey exampleSurvey = surveyService.findSurveyByName("example");
        assertEquals(8, exampleSurvey.surveySteps().size());
        // exampleSurvey.questions().forEach(surveyStep -> log.info("Question: "+surveyStep));

        Survey weatherSurvey = surveyService.findSurveyByName("weather");
        assertEquals(6, weatherSurvey.surveySteps().size());
    }

    @Test
    public void thoroughHappyPath()
    {
        String surveyName = "example";

        Survey survey = surveyService.findSurveyByName(surveyName);
        String surveyResultId = surveySessionService.beginSurvey(user, survey);

        // starting the survey
        SurveyResult surveyResult = surveySessionService.get(surveyResultId).get();
        // log.info("Started survey with surveyResultId: " + surveyResultId);

        // retrieving the first surveyStep
        SurveyStep firstQuestion = surveySessionService.getNextSurveyStep(surveyResultId);
        assertEquals(1, firstQuestion.questionNumber());
        PossibleAnswer firstAnswer = firstQuestion.possibleAnswers().get(0);

        // saving the response
        Response firstResponse = surveySessionService.saveResponse(surveyResult.id(), firstQuestion.id(), firstAnswer.id());

        // verifying the response is correctly saved
        SurveyResult reloadedResponse = surveySessionService.get(surveyResultId).get();
        assertEquals(1, reloadedResponse.lastCompletedQuestion());
        Response retrievedResponse = responseService.get(firstResponse.id()).get();
        assertEquals(firstResponse, retrievedResponse);

        // retrieving the second surveyStep
        SurveyStep secondQuestion = surveySessionService.getNextSurveyStep(surveyResultId);
        // log.info("Second surveyStep: " + secondQuestion.text());
        assertEquals(2, secondQuestion.questionNumber());
        PossibleAnswer secondAnswer = secondQuestion.possibleAnswers().get(1);

        // retrieving the second surveyStep again, since we did not submit our answer yet
        SurveyStep secondQuestionAgain = surveySessionService.getNextSurveyStep(surveyResultId);
        assertEquals(2, secondQuestionAgain.questionNumber());
        surveySessionService.saveResponse(surveyResult.id(), secondQuestion.id(), secondAnswer.id());

        SurveyStep thirdQuestion = surveySessionService.getNextSurveyStep(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), thirdQuestion.id(), thirdQuestion.possibleAnswers().get(1).id());

        SurveyStep fourthQuestion = surveySessionService.getNextSurveyStep(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), fourthQuestion.id(), fourthQuestion.possibleAnswers().get(1).id());

        // confirming that the survey is not yet complete
        surveyResult = surveySessionService.get(surveyResultId).get();
        assertEquals(false, surveyResult.isComplete());

        List<SurveyResult> surveyResults = surveySessionService.getCompletedSurveys(user.id());
        assertEquals(0, surveyResults.size());

        SurveyStep fifthQuestion = surveySessionService.getNextSurveyStep(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), fifthQuestion.id(), fifthQuestion.possibleAnswers().get(0).id());

        SurveyStep sixthQuestion = surveySessionService.getNextSurveyStep(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), sixthQuestion.id(), sixthQuestion.possibleAnswers().get(0).id());

        SurveyStep seventhQuestion = surveySessionService.getNextSurveyStep(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), seventhQuestion.id(), seventhQuestion.possibleAnswers().get(0).id());

        SurveyStep eighthQuestion = surveySessionService.getNextSurveyStep(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), eighthQuestion.id(), eighthQuestion.possibleAnswers().get(0).id());

        surveyResult = surveySessionService.get(surveyResultId).get();

        log.info("surveyResult: "+surveyResult);

        // confirming that the survey is complete now
        assertEquals(true, surveyResult.isComplete());

        // now the surveyResult is found as completed
        List<SurveyResult> completedSurveys = surveySessionService.getCompletedSurveys(user.id());
        assertEquals(1, completedSurveys.size());

        // the ninth surveyStep does not exist, so a dummy surveyStep with orderNumber -1 is returned
        SurveyStep ninthQuestion = surveySessionService.getNextSurveyStep(surveyResultId);
        assertEquals(-1, ninthQuestion.questionNumber());

        // the responses are available now the survey has been completed
        List<QuestionResponse> responses = surveySessionService.getSurveyResponses(surveyResultId);
        responses.forEach(finalResponse -> log.info("saved: " + finalResponse));

        // we confirm the responses are correct
        surveySessionService.confirmResponses(surveyResultId);

        surveyResult = surveySessionService.get(surveyResultId).get();
        assertEquals(SurveyResult.SurveyResultStatus.confirmed, surveyResult.status());

    }
}
