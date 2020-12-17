package fusion.playground.data.service;

import fusion.playground.data.entity.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// @SpringJUnitConfig
// @DataMongoTest
@SpringBootTest
public class TakeSurveyBackendTest
{
    private static Log log = LogFactory.getLog(TakeSurveyBackendTest.class);

    @Autowired private UserService userService;
    @Autowired private SurveyService surveyService;
    @Autowired private SurveySessionService surveySessionService;
    @Autowired private ResponseService responseService;
    @Autowired private QuestionService questionService;

    private User user = null;

    public TakeSurveyBackendTest()
    {}

    @Before
    public void before()
    {
        user = userService.findByUsername("testuser").get();
    }

    @Test
    public void surveyLoading()
    {
        Survey exampleSurvey = surveyService.findSurveyByName("example");
        assertEquals(exampleSurvey.questions().size(), 8);
        // exampleSurvey.questions().forEach(question -> log.info("Question: "+question));

        Survey weatherSurvey = surveyService.findSurveyByName("weather");
        assertEquals(weatherSurvey.questions().size(), 4);

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

        // retrieving the first question
        Question firstQuestion = surveyService.getNextQuestion(surveyResultId);
        assertEquals(1, firstQuestion.orderNumber());
        PossibleAnswer firstAnswer = firstQuestion.possibleAnswers().get(0);

        // saving the response
        Response firstResponse = surveySessionService.saveResponse(surveyResult.id(), firstQuestion.id(), firstAnswer.id());

        // verifying the response is correctly saved
        SurveyResult reloadedResponse = surveySessionService.get(surveyResultId).get();
        assertEquals(1, reloadedResponse.lastCompletedQuestion());
        Response retrievedResponse = responseService.get(firstResponse.id()).get();
        assertEquals(firstResponse, retrievedResponse);

        // retrieving the second question
        Question secondQuestion = surveyService.getNextQuestion(surveyResultId);
        // log.info("Second question: " + secondQuestion.text());
        assertEquals(2, secondQuestion.orderNumber());
        PossibleAnswer secondAnswer = secondQuestion.possibleAnswers().get(1);

        // retrieving the second question again, since we did not submit our answer yet
        Question secondQuestionAgain = surveyService.getNextQuestion(surveyResultId);
        assertEquals(2, secondQuestionAgain.orderNumber());
        surveySessionService.saveResponse(surveyResult.id(), secondQuestion.id(), secondAnswer.id());

        Question thirdQuestion = surveyService.getNextQuestion(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), thirdQuestion.id(), thirdQuestion.possibleAnswers().get(1).id());

        Question fourthQuestion = surveyService.getNextQuestion(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), fourthQuestion.id(), fourthQuestion.possibleAnswers().get(1).id());

        // confirming that the survey is not yet complete
        surveyResult = surveySessionService.get(surveyResultId).get();
        assertEquals(false, surveyResult.isComplete());

        Question fifthQuestion = surveyService.getNextQuestion(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), fifthQuestion.id(), fifthQuestion.possibleAnswers().get(0).id());

        Question sixthQuestion = surveyService.getNextQuestion(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), sixthQuestion.id(), sixthQuestion.possibleAnswers().get(0).id());

        Question seventhQuestion = surveyService.getNextQuestion(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), seventhQuestion.id(), seventhQuestion.possibleAnswers().get(0).id());

        Question eighthQuestion = surveyService.getNextQuestion(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), eighthQuestion.id(), eighthQuestion.possibleAnswers().get(0).id());

        surveyResult = surveySessionService.get(surveyResultId).get();

        log.info("surveyResult: "+surveyResult);

        // confirming that the survey is complete now
        assertEquals(true, surveyResult.isComplete());

        // the ninth question does not exist, so a dummy question with orderNumber -1 is returned
        Question ninthQuestion = surveyService.getNextQuestion(surveyResultId);
        assertEquals(-1, ninthQuestion.orderNumber());

        // the responses are available now the survey has been completed
        List<QuestionResponse> responses = surveySessionService.getSurveyResponses(surveyResultId);
        responses.forEach(finalResponse -> log.info("saved: " + finalResponse));

        // we confirm the responses are correct
        surveySessionService.confirmResponses(surveyResultId);

        surveyResult = surveySessionService.get(surveyResultId).get();
        assertEquals(SurveyResult.SurveyResultStatus.confirmed, surveyResult.status());
    }
}