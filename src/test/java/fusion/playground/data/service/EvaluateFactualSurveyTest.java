package fusion.playground.data.service;

import fusion.playground.data.entity.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EvaluateFactualSurveyTest
{
    private static Log log = LogFactory.getLog(EvaluateFactualSurveyTest.class);

    @Autowired private UserService userService;
    @Autowired private SurveyService surveyService;
    @Autowired private SurveyResultService surveyResultService;
    @Autowired private ResponseService responseService;
    @Autowired private QuestionService questionService;

    private User user = null;

    public EvaluateFactualSurveyTest()
    {}

    @Before
    public void before()
    {
        user = userService.findByUsername("testuser").get();
    }

    @Test
    public void testFactualTest()
    {
        String surveyName = "maths_1";

        Survey survey = surveyService.findSurveyByName(surveyName);
        String surveyResultId = surveyResultService.beginSurvey(user, survey);

        SurveyResult surveyResult = surveyResultService.get(surveyResultId).get();

        // retrieving the first question
        Question firstQuestion = surveyService.getNextQuestion(surveyResultId);
        surveyResultService.saveResponse(surveyResult.id(), firstQuestion.id(), firstQuestion.possibleAnswers().get(0).id());

        Question secondQuestion = surveyService.getNextQuestion(surveyResultId);
        surveyResultService.saveResponse(surveyResult.id(), secondQuestion.id(), secondQuestion.possibleAnswers().get(1).id());

        Question thirdQuestion = surveyService.getNextQuestion(surveyResultId);
        surveyResultService.saveResponse(surveyResult.id(), thirdQuestion.id(), thirdQuestion.possibleAnswers().get(1).id());

        Question fourthQuestion = surveyService.getNextQuestion(surveyResultId);
        surveyResultService.saveResponse(surveyResult.id(), fourthQuestion.id(), fourthQuestion.possibleAnswers().get(1).id());

        // grade the FactualQuestions
        surveyResult = surveyResultService.get(surveyResultId).get();
        SurveyResult gradedSurveyResult = surveyResultService.gradeSurveyResult(surveyResult);

        gradedSurveyResult.responses().forEach(response -> log.info(response.response() + " is correct: " + response.correct()));

        log.info("Number of correct answers: "+gradedSurveyResult.score());
    }

}
