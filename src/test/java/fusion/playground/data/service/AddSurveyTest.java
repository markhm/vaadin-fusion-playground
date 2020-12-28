package fusion.playground.data.service;

import fusion.playground.data.entity.*;
import fusion.playground.service.SomeOktaUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AddSurveyTest extends AbstractServiceLayerTest
{
    private static Log log = LogFactory.getLog(AddSurveyTest.class);

    @Test
    public void addSurvey()
    {
        // create a new survey and save it, so we get an id
        Optional<User> hiddenOwner = userRepository.findByOktaUserId(SomeOktaUser.HIDDEN_USER_OKTA_ID);
        Survey survey = surveyService.createDraftSurvey(hiddenOwner.get().id(), SurveyCategory.example.toString(), "test");
        Assertions.assertNotNull(survey.id());

        String surveyId = survey.id();
        addQuestion(surveyId, "This is the first question. What is your answer...?", "Yes", "No");
        Survey retrievedSurvey = surveyService.get(surveyId).get();

        Question question = retrievedSurvey.questions().get(0);
        Assertions.assertEquals("This is the first question. What is your answer...?", question.text());

        Assertions.assertEquals("Yes", question.possibleAnswers().get(0).text());
        Assertions.assertEquals("No", question.possibleAnswers().get(1).text());

        log.info("retrievedSurvey: "+retrievedSurvey);

        assertCompleteness(surveyId);

        addQuestion(surveyId, "This is the second question. Are you happy...?", "Yes", "No");
        addQuestion(surveyId, "Are you a desert person or a soup person...?", "Soup", "Desert");

        surveyService.rebuildSurvey(surveyId);

        printSurvey(surveyId);
    }

    private void addQuestion(String surveyId, String questionText, String... possibleAnswerTexts)
    {
        Question unsavedQuestion = new Question();
        unsavedQuestion.text(questionText);
        unsavedQuestion.surveyId(surveyId);

        Question question = questionService.update(unsavedQuestion);

        String questionId = question.id();

        for (String possibleAnswerText: possibleAnswerTexts)
        {
            questionService.addPossibleAnswer(question.id(), possibleAnswerText);
        }

        question = questionService.get(questionId).get();

        surveyService.addQuestion(question);
    }

    private void printSurvey(String surveyId)
    {
        Survey survey = surveyService.get(surveyId).get();

        log.info("********************************************************");
        log.info("");
        log.info("Survey: " + survey.name() + " (" + survey.category() + ")");
        log.info("");
        for (Question question : survey.questions())
        {
            log.info("Question " + question.orderNumber() + ": " + question.text());

            for (PossibleAnswer possibleAnswer : question.possibleAnswers())
            {
                log.info("Possible answer: " + possibleAnswer + ".");
            }

            log.info("");
        }
    }

    private void assertCompleteness(String surveyId)
    {
        Survey survey = surveyService.get(surveyId).get();
        Assertions.assertNotNull(survey.id(), "The survey should have been saved.");

        Assertions.assertNotNull(survey.name(), "The survey name should not be null.");
        Assertions.assertNotNull(survey.category(), "The survey category should not be null.");

        for (Question question : survey.questions())
        {
            Assertions.assertNotNull(question.id(), "The question should have been saved.");
            Assertions.assertNotNull(question.text(), "The question not be null.");
            Assertions.assertNotNull(question.orderNumber(), "The order number should not be null.");
            Assertions.assertTrue(question.orderNumber() > 0, "The order number should be > 0.");
            Assertions.assertEquals(survey.id(), question.surveyId());

            for (PossibleAnswer possibleAnswer : question.possibleAnswers())
            {
                Assertions.assertNotNull(possibleAnswer.id(), "The possibleAnswer should have been saved.");
                Assertions.assertNotNull(possibleAnswer.text(), "The possibleAnswer should contain text.");
            }
        }
    }


}
