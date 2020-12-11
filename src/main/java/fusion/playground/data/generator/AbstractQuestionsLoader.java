package fusion.playground.data.generator;

import fusion.playground.data.service.PossibleAnswerRepository;
import fusion.playground.data.service.QuestionRepository;
import fusion.playground.data.entity.PossibleAnswer;
import fusion.playground.data.entity.Question;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractQuestionsLoader
{
    private static Log log = LogFactory.getLog(AbstractQuestionsLoader.class);

    protected String category;
    private int orderCounter = 1;

    protected QuestionRepository questionRepository;
    protected PossibleAnswerRepository possibleAnswerRepository;

    public AbstractQuestionsLoader(String category, QuestionRepository questionRepository, PossibleAnswerRepository possibleAnswerRepository)
    {
        this.category = category;
        this.questionRepository = questionRepository;
        this.possibleAnswerRepository = possibleAnswerRepository;
    }

    protected void addQuestion(String questionText, String... possibleAnswerTexts)
    {
        Question question = new Question();
        question.text(questionText);
        question.category(category);
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

//        log.info("Question added: "+ question.toString());
//        log.info("");

        orderCounter++;
    }

    public abstract void loadQuestions();
}
