package fusion.playground.data.initialization;

import fusion.playground.data.entity.*;
import fusion.playground.data.service.PossibleAnswerRepository;
import fusion.playground.data.service.QuestionRepository;
import fusion.playground.data.service.SurveyRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractSurveyQuestionsLoader
{
    private static Log log = LogFactory.getLog(AbstractSurveyQuestionsLoader.class);

    private int orderCounter = 1;

    protected SurveyRepository surveyRepository;
    protected QuestionRepository questionRepository;
    protected PossibleAnswerRepository possibleAnswerRepository;

    protected Survey survey;

    public AbstractSurveyQuestionsLoader(SurveyRepository surveyRepository,
                                         QuestionRepository questionRepository,
                                         PossibleAnswerRepository possibleAnswerRepository)
    {
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.possibleAnswerRepository = possibleAnswerRepository;
    }

    public abstract void loadQuestions();

    public void createSurvey(SurveyCategory category, String name)
    {
        survey = new Survey(category, name);
    }

    public void createSurvey(SurveyCategory category, String name, String title)
    {
        survey = new Survey(category, name);
        survey.title(title);
    }

    public void saveSurvey()
    {
        surveyRepository.save(survey);
    }

    protected void addQuestion(String questionText, String... possibleAnswerTexts)
    {
        Question question = new Question();
        question.text(questionText);
        question.orderNumber(orderCounter);

        for (String possibleAnswerText: possibleAnswerTexts)
        {
            PossibleAnswer possibleAnswer = new PossibleAnswer(possibleAnswerText);
            question.addPossibleAnswer(possibleAnswerRepository.save(possibleAnswer));
        }

        questionRepository.save(question);
        survey.addQuestion(question);

        orderCounter++;
    }

    protected void addFactualQuestion(String questionText, int correctAnswer, String... possibleAnswerTexts)
    {
        this.survey.gradable(true);

        FactualQuestion question = new FactualQuestion();
        question.text(questionText);
        question.orderNumber(orderCounter);

        int counter = 1;
        for (String possibleAnswerText: possibleAnswerTexts)
        {
            PossibleAnswer possibleAnswer = new PossibleAnswer(possibleAnswerText);
            question.addPossibleAnswer(possibleAnswerRepository.save(possibleAnswer));

            if (counter == correctAnswer)
            {
                question.correctAnswer(possibleAnswer);
            }
            counter++;
        }

        questionRepository.save(question);
        survey.addQuestion(question);

        orderCounter++;
    }
}
