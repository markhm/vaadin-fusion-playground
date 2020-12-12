package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import fusion.playground.data.CrudEndpoint;
import fusion.playground.data.entity.QuestionResponse;
import fusion.playground.data.service.ResponseService;
import fusion.playground.data.service.QuestionService;
import fusion.playground.data.service.UserService;
import fusion.playground.data.entity.Response;
import fusion.playground.data.entity.Question;
import fusion.playground.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Endpoint
@AnonymousAllowed
public class ResponseEndpoint extends CrudEndpoint<Response, String>
{
    private ResponseService responseService;
    private UserService userService;
    private QuestionService questionService;

    public ResponseEndpoint(@Autowired ResponseService responseService, UserService userService, QuestionService questionService) {
        this.responseService = responseService;
        this.userService = userService;
        this.questionService = questionService;
    }

    @Override
    protected ResponseService getService() {
        return responseService;
    }

    public List<QuestionResponse> getSurveyAnswers(String userId, String surveyName)
    {
        return responseService.getResponses(userId, surveyName);
    }

    public Response saveResponse(String questionId, String userId, String responseId)
    {
        Response response = new Response();

        User user = userService.findByUsername("testuser").get();
        Question question = questionService.get(questionId).get();

        response.user(user);
        response.question(question);
        response.response(responseId);
        response.surveyName(question.surveyName());

        Response savedResponse = responseService.save(response);

        return savedResponse;
    }

}
