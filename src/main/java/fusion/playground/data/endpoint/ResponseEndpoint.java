package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import fusion.playground.data.CrudEndpoint;
import fusion.playground.data.entity.*;
import fusion.playground.data.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Endpoint
@AnonymousAllowed
public class ResponseEndpoint extends CrudEndpoint<SurveyResponse, String>
{
    private static Log log = LogFactory.getLog(ResponseEndpoint.class);

    private SurveyService surveyService;
    private SurveyResponseService surveyResponseService;
    private UserService userService;
    private QuestionService questionService;

    public ResponseEndpoint(@Autowired SurveyService surveyService, SurveyResponseService surveyResponseService,
                            UserService userService, QuestionService questionService) {
        this.surveyService = surveyService;
        this.surveyResponseService = surveyResponseService;
        this.userService = userService;
        this.questionService = questionService;
    }

    @Override
    protected SurveyResponseService getService() {
        return surveyResponseService;
    }

    public String beginSurvey(String surveyName, String userId)
    {
        User user = userService.findByUsername("testuser").get();

        Survey survey = surveyService.findSurveyByName(surveyName);
        return surveyResponseService.beginSurvey(user, survey);
    }

    public void saveResponse(String surveyResponseId, String questionId, String userId, String responseId)
    {
        surveyResponseService.saveResponse(surveyResponseId, questionId, responseId);
    }

    public List<QuestionResponse> getSurveyAnswers(String userId, String surveyName)
    {
        User user = null;
        return surveyResponseService.getSurveyResponses(user, surveyName);
    }

//    public void saveResponse(String surveyResponseId, String questionId, String userId, String responseId)
//    {
//        log.info("Trying to saveResponse(...) with:");
//        log.info("surveyResponseId: " + surveyResponseId);
//        log.info("questionId: " + questionId);
//        log.info("userId: " + userId);
//        log.info("responseId: " + responseId);
//
//        SurveyResponse surveyResponse = surveyResponseService.get(surveyResponseId).get();
//        int lastCompletedQuestion = surveyResponse.lastCompletedQuestion();
//
//        Response response = new Response();
//        User user = userService.findByUsername("testuser").get();
//        Question question = questionService.get(questionId).get();
//
//        response.user(user);
//        response.question(question);
//        response.response(responseId);
//        response.surveyName(question.surveyName());
//
//        surveyResponseService.save(surveyResponse, response);
//    }

}
