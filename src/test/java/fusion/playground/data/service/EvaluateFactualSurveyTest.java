package fusion.playground.data.service;

import fusion.playground.data.entity.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EvaluateFactualSurveyTest extends AbstractServiceLayerTest
{
    private static Log log = LogFactory.getLog(EvaluateFactualSurveyTest.class);

    public EvaluateFactualSurveyTest()
    {}

    @Test
    public void testFactualTest()
    {
        String surveyName = "maths";

        Survey survey = surveyService.findSurveyByName(surveyName);
        String surveyResultId = surveySessionService.beginSurvey(user, survey);

        SurveyResult surveyResult = surveySessionService.get(surveyResultId).get();

        // retrieving the first surveyStep
        SurveyStep firstQuestion = surveySessionService.getNextSurveyStep(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), firstQuestion.id(), firstQuestion.possibleAnswers().get(0).id());

        SurveyStep secondQuestion = surveySessionService.getNextSurveyStep(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), secondQuestion.id(), secondQuestion.possibleAnswers().get(1).id());

        SurveyStep thirdQuestion = surveySessionService.getNextSurveyStep(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), thirdQuestion.id(), thirdQuestion.possibleAnswers().get(1).id());

        SurveyStep fourthQuestion = surveySessionService.getNextSurveyStep(surveyResultId);
        surveySessionService.saveResponse(surveyResult.id(), fourthQuestion.id(), fourthQuestion.possibleAnswers().get(1).id());

        // grade the FactualQuestions
        surveyResult = surveySessionService.get(surveyResultId).get();
        SurveyResult gradedSurveyResult = surveySessionService.gradeSurveyResult(surveyResult);

        gradedSurveyResult.responses().forEach(response -> log.info(response.response() + " is correct: " + response.correct()));

        log.info("Number of correct answers: "+gradedSurveyResult.score());
    }

}
