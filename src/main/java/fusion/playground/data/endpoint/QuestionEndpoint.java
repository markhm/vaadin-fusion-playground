package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import fusion.playground.data.CrudEndpoint;
import fusion.playground.data.service.ResponseService;
import fusion.playground.data.service.QuestionService;
import fusion.playground.data.service.UserService;
import fusion.playground.data.entity.Question;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Endpoint
@AnonymousAllowed
@Deprecated
public class QuestionEndpoint extends CrudEndpoint<Question, String>
{
    private static Log log = LogFactory.getLog(QuestionEndpoint.class);

    private QuestionService questionService;
    private ResponseService responseService;
    private UserService userService;

    public QuestionEndpoint(@Autowired QuestionService questionService,
                            ResponseService responseService,
                            UserService userService)
    {
        this.questionService = questionService;
        this.responseService = responseService;
        this.userService = userService;
    }

    protected QuestionService getService() {
        return questionService;
    }
}
