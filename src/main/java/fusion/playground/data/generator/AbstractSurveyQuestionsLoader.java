package fusion.playground.data.generator;

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
        question.surveyName(survey.name());
        question.orderNumber(orderCounter);

        for (String possibleAnswerText: possibleAnswerTexts)
        {
            PossibleAnswer possibleAnswer = new PossibleAnswer(possibleAnswerText);
            possibleAnswerRepository.save(possibleAnswer);
//            log.info("PossibleAnswer added: "+ possibleAnswer.toString());
//            log.info("with .id(): "+ possibleAnswer.id());
            question.addPossibleAnswer(possibleAnswer);
        }

        questionRepository.save(question);
        survey.addQuestion(question);

//        log.info("Question added: "+ question.toString());
//        log.info("");

        orderCounter++;
    }

    public abstract void loadQuestions();
}
