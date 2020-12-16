package fusion.playground.data.generator;

import fusion.playground.data.entity.FactualQuestion;
import fusion.playground.data.entity.Survey;
import fusion.playground.data.service.PossibleAnswerRepository;
import fusion.playground.data.service.QuestionRepository;
import fusion.playground.data.entity.PossibleAnswer;
import fusion.playground.data.entity.Question;
import fusion.playground.data.service.SurveyRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractSurveyQuestionsLoader
{
    private static Log log = LogFactory.getLog(AbstractSurveyQuestionsLoader.class);

    protected String category;
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

    public void createSurvey(String name, String category)
    {
        survey = new Survey(name, category);
    }

    public void saveSurvey()
    {
        surveyRepository.save(survey);
    }

    protected void addQuestion(String questionText, String... possibleAnswerTexts)
    {
        Question question = new Question();
        question.text(questionText);
        // question.surveyName(survey.name());
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
        // question.surveyName(survey.name());
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

    public abstract void loadQuestions();
}
