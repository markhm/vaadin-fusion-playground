package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import fusion.playground.domain.Answer;
import fusion.playground.domain.Question;
import fusion.playground.domain.User;

import java.util.ArrayList;
import java.util.List;

@Endpoint
@AnonymousAllowed
public class AnswerEndpoint // extends CrudEndpoint<Answer, Integer>
{
    private List<Answer> answers = new ArrayList<>();

    public List<Answer> getAnswers()
    {
        return answers;
    }

    public Answer addAnswer(int questionId, int userId, int answerNumber)
    {
        Answer answer = new Answer();

        User user = new User();
        user.id(userId);
        answer.user(user);

        Question question = new Question();
        question.id(questionId);
        answer.question(question);

        answer.answer(answerNumber);

        answers.add(answer);

        return answer;
    }

    // private AnswerService service;

//    public AnswerEndpoint(@Autowired AnswerService service) {
//        this.service = service;
//    }

//    @Override
//    protected AnswerService getService() {
//        return service;
//    }

}
