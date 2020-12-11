package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import fusion.playground.data.CrudEndpoint;
import fusion.playground.data.service.AnswerService;
import fusion.playground.data.service.QuestionService;
import fusion.playground.data.service.UserService;
import fusion.playground.data.entity.Answer;
import fusion.playground.data.entity.Question;
import fusion.playground.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Endpoint
@AnonymousAllowed
public class AnswerEndpoint extends CrudEndpoint<Answer, String>
{
    private List<Answer> answers = new ArrayList<>();

    public List<Answer> getAnswers()
    {
        return answers;
    }

    private AnswerService answerService;
    private UserService userService;
    private QuestionService questionService;

    public AnswerEndpoint(@Autowired AnswerService answerService, UserService userService, QuestionService questionService) {
        this.answerService = answerService;
        this.userService = userService;
        this.questionService = questionService;
    }

    @Override
    protected AnswerService getService() {
        return answerService;
    }

    public Answer addAnswer(String questionId, String userId, String answerId)
    {
        Answer answer = new Answer();

        User user = userService.findByUsername("testuser").get();
        Question question = questionService.get(questionId).get();

        answer.user(user);
        answer.question(question);
        answer.answer(answerId);

        answers.add(answer);

        return answer;
    }
}
