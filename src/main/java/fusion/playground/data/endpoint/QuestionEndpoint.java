package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import fusion.playground.data.CrudEndpoint;
import fusion.playground.data.entity.Survey;
import fusion.playground.data.service.ResponseService;
import fusion.playground.data.service.QuestionService;
import fusion.playground.data.service.SurveyService;
import fusion.playground.data.service.UserService;
import fusion.playground.data.entity.Question;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Endpoint
@AnonymousAllowed
public class QuestionEndpoint extends CrudEndpoint<Question, String>
{
    private static Log log = LogFactory.getLog(QuestionEndpoint.class);

    private QuestionService questionService;
    private SurveyService surveyService;
    private UserService userService;

    @Autowired
    public QuestionEndpoint(QuestionService questionService,
                            SurveyService surveyService,
                            UserService userService)
    {
        this.questionService = questionService;
        this.surveyService = surveyService;
        this.userService = userService;
    }

    protected QuestionService getService() {
        return questionService;
    }

    public List<Question> getQuestions(String surveyId)
    {
        return surveyService.get(surveyId).get().questions();
    }

//    public Question addQuestion(Question question)
//    {
//        // save the question
//        questionService.update(question);
//
//        // retrieve the survey, add the new question at the right location and update
//        Survey survey = surveyService.get(question.survey().id()).get();
//        survey.questions().add(question.orderNumber(), question);
//        surveyService.update(survey);
//
//        // TODO: This messes up the overall ordering of the questions
//        return question;
//    }
}
