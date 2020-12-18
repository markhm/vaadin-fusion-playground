package fusion.playground.data.service;

import fusion.playground.data.entity.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AddSurveyTest extends AbstractServiceLayerTest
{
    private static Log log = LogFactory.getLog(AddSurveyTest.class);

    @Test
    public void addSurvey()
    {
        Survey newSurvey = surveyService.update(new Survey(SurveyCategory.example, "test"));

        addQuestion(newSurvey, "This is the first question. What is your answer...?", "Yes", "No");
        addQuestion(newSurvey, "This is the second question. Are you happy...?", "Yes", "No");
        addQuestion(newSurvey, "Are you a desert person or a soup person...?", "Soup", "Desert");

        Survey survey = surveyService.findSurveyByName("test");
        log.info("Found survey: "+survey);
    }

    private void addQuestion(Survey survey, String questionText, String... possibleAnswerTexts)
    {
        Question questionUnsaved = new Question();
        questionUnsaved.text(questionText);

        for (String possibleAnswerText: possibleAnswerTexts)
        {
            PossibleAnswer possibleAnswer = new PossibleAnswer(possibleAnswerText);
            possibleAnswerService.update(possibleAnswer);
            questionUnsaved.addPossibleAnswer(possibleAnswer);
        }

        questionUnsaved.addPossibleAnswer(possibleAnswerService.update(PossibleAnswer.YES));
        questionUnsaved.addPossibleAnswer(possibleAnswerService.update(PossibleAnswer.NO));

        Question question = questionService.update(questionUnsaved);
        survey.addQuestion(question);

        surveyService.update(survey);
    }

}
