package fusion.playground.data.service;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import fusion.playground.Application;
import fusion.playground.data.entity.*;

import fusion.playground.data.generator.DatabaseInitializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// @SpringJUnitConfig
// @DataMongoTest
@SpringBootTest
public class SurveyBackendTest
{
    private static Log log = LogFactory.getLog(SurveyBackendTest.class);

    @Autowired private UserService userService;
    @Autowired private SurveyService surveyService;
    @Autowired private SurveyResponseService surveyResponseService;
    @Autowired private ResponseService responseService;

    @Autowired private QuestionService questionService;

    private User user = null;

    public SurveyBackendTest()
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
        String surveyResponseId = surveyResponseService.beginSurvey(user, survey);

        // starting the survey
        SurveyResponse surveyResponse = surveyResponseService.get(surveyResponseId).get();
        // log.info("Started survey with surveyResponseId: " + surveyResponseId);

        // retrieving the first question
        Question firstQuestion = surveyService.getNextQuestion(surveyResponseId);
        assertEquals(1, firstQuestion.orderNumber());
        PossibleAnswer firstAnswer = firstQuestion.possibleAnswers().get(0);

        // saving the response
        Response response = surveyResponseService.saveResponse(surveyResponse.id(), firstQuestion.id(), firstAnswer.id());

        // verifying the response is correctly saved
        SurveyResponse reloadedResponse = surveyResponseService.get(surveyResponseId).get();
        assertEquals(1, reloadedResponse.lastCompletedQuestion());
        Response retrievedResponse = responseService.get(response.id()).get();
        assertEquals(response, retrievedResponse);

        // retrieving the second question
        Question secondQuestion = surveyService.getNextQuestion(surveyResponseId);
        // log.info("Second question: " + secondQuestion.text());
        assertEquals(2, secondQuestion.orderNumber());
        PossibleAnswer secondAnswer = secondQuestion.possibleAnswers().get(1);
        Question secondQuestionAgain = surveyService.getNextQuestion(surveyResponseId);
        assertEquals(2, secondQuestionAgain.orderNumber());
        surveyResponseService.saveResponse(surveyResponse.id(), secondQuestion.id(), secondAnswer.id());

        Question thirdQuestion = surveyService.getNextQuestion(surveyResponseId);
        surveyResponseService.saveResponse(surveyResponse.id(), thirdQuestion.id(), thirdQuestion.possibleAnswers().get(1).id());

        Question fourthQuestion = surveyService.getNextQuestion(surveyResponseId);
        surveyResponseService.saveResponse(surveyResponse.id(), fourthQuestion.id(), fourthQuestion.possibleAnswers().get(1).id());

        surveyResponse = surveyResponseService.get(surveyResponseId).get();
        assertEquals(false, surveyResponse.isComplete());

        Question fifthQuestion = surveyService.getNextQuestion(surveyResponseId);
        surveyResponseService.saveResponse(surveyResponse.id(), fifthQuestion.id(), fifthQuestion.possibleAnswers().get(0).id());

        Question sixthQuestion = surveyService.getNextQuestion(surveyResponseId);
        surveyResponseService.saveResponse(surveyResponse.id(), sixthQuestion.id(), sixthQuestion.possibleAnswers().get(0).id());

        Question seventhQuestion = surveyService.getNextQuestion(surveyResponseId);
        surveyResponseService.saveResponse(surveyResponse.id(), seventhQuestion.id(), seventhQuestion.possibleAnswers().get(0).id());

        Question eighthQuestion = surveyService.getNextQuestion(surveyResponseId);
        surveyResponseService.saveResponse(surveyResponse.id(), eighthQuestion.id(), eighthQuestion.possibleAnswers().get(0).id());

        surveyResponse = surveyResponseService.get(surveyResponseId).get();
        assertEquals(true, surveyResponse.isComplete());

        Question ninthQuestion = surveyService.getNextQuestion(surveyResponseId);

        List<QuestionResponse> responses = surveyResponseService.getSurveyResponses(user, surveyName);
        responses.forEach(finalResponse -> log.info("finalResponse: " + finalResponse));
    }

    @Test
    public void testSurveys()
    {
        List<String> availableSurveys = surveyService.getAvailableSurveys();
        assertEquals(2, availableSurveys.size());

        availableSurveys.forEach(surveyName -> log.info("Found survey "+surveyName));
    }
}
