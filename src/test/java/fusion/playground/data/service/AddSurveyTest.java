package fusion.playground.data.service;

import fusion.playground.data.entity.PossibleAnswer;
import fusion.playground.data.entity.Question;
import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AddSurveyTest
{
    private static Log log = LogFactory.getLog(AddSurveyTest.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private SurveyResultService surveyResultService;

    @Autowired
    private ResponseService responseService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private PossibleAnswerService possibleAnswerService;

    private User user = null;

    public AddSurveyTest()
    {}

    @Before
    public void before()
    {
        user = userService.findByUsername("testuser").get();
    }

    @Test
    public void addSurvey()
    {
        Survey survey = surveyService.update(new Survey("test", "test_category"));

        addQuestion(survey, "This is the first question. What is your answer...?", "Yes", "No");

        addQuestion(survey, "This is the second question. Are you happy...?", "Yes", "No");

        addQuestion(survey, "Are you a desert person or a soup person...?", "Soup", "Desert");

        log.info("Survey: " + survey);
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
