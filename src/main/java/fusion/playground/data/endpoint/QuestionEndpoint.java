package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import fusion.playground.data.CrudEndpoint;
import fusion.playground.data.service.AnswerService;
import fusion.playground.data.service.QuestionService;
import fusion.playground.data.service.UserService;
import fusion.playground.domain.PossibleAnswer;
import fusion.playground.domain.Question;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Endpoint
@AnonymousAllowed
public class QuestionEndpoint extends CrudEndpoint<Question, Integer>
{
    private static Log log = LogFactory.getLog(QuestionEndpoint.class);

    private QuestionService questionService;
    private AnswerService answerService;
    private UserService userService;

    private int questionPointer = 1;

    public QuestionEndpoint(@Autowired QuestionService questionService,
                            AnswerService answerService,
                            UserService userService)
    {
        this.questionService = questionService;
        this.answerService = answerService;
        this.userService = userService;
    }

    protected QuestionService getService() {
        return questionService;
    }

    public Question getNextQuestion(int userId, String category)
    {
        // check if questions have already been answered today

        // boolean alreadyComplete = answerService.answersAlreadyCompleted(category, userId, LocalDate.now());

        Question nextQuestion = null;
        if (true)
        {
            int totalNumberOfQuestions = questionService.countAllByCategory(category);
            if (questionPointer > totalNumberOfQuestions)
            {
                questionPointer = 1;
            }

            Optional<Question> optionalQuestion = questionService.getByCategoryAndNumber(category, questionPointer);
            questionPointer++;
            nextQuestion = optionalQuestion.get();
        }
        else
        {
            nextQuestion = new Question();
            nextQuestion.id(-1);
            nextQuestion.text("You already answered this category today");
        }

        return nextQuestion;
    }

    public int getTotalNumberOfQuestions(String category)
    {
        return questionService.countAllByCategory(category);
    }
}
