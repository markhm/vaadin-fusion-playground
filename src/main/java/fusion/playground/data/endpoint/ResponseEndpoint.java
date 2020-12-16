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
public class ResponseEndpoint extends CrudEndpoint<SurveyResult, String>
{
    private static Log log = LogFactory.getLog(ResponseEndpoint.class);

    private SurveyService surveyService;
    private SurveyResultService surveyResultService;
    private UserService userService;
    private QuestionService questionService;

    public ResponseEndpoint(@Autowired SurveyService surveyService, SurveyResultService surveyResultService,
                            UserService userService, QuestionService questionService) {
        this.surveyService = surveyService;
        this.surveyResultService = surveyResultService;
        this.userService = userService;
        this.questionService = questionService;
    }

    @Override
    protected SurveyResultService getService() {
        return surveyResultService;
    }

    public String beginSurvey(String surveyName, String oktaUserId)
    {
        log.info("Trying to begin survey for user with oktaUserId: "+oktaUserId);
        User user = userService.findByOktaUserId(oktaUserId).get();
        Survey survey = surveyService.findSurveyByName(surveyName);
        return surveyResultService.beginSurvey(user, survey);
    }

    public void saveResponse(String surveyResponseId, String questionId, String responseId)
    {
        surveyResultService.saveResponse(surveyResponseId, questionId, responseId);
    }

    public void approveResponses(String surveyResultId)
    {
        surveyResultService.approveResponses(surveyResultId);
    }

    public void rejectResponses(String surveyResultId)
    {
        surveyResultService.rejectResponses(surveyResultId);
    }

    public List<SurveyResult> getCompletedSurveys(String oktaUserId)
    {
        User user = userService.findByOktaUserId(oktaUserId).get();
        return surveyResultService.getCompletedSurveys(user);
    }

    public List<QuestionResponse> getSurveyResponses(String surveyResultId)
    {
        return surveyResultService.getSurveyResponses(surveyResultId);
    }

//    public void saveResponse(String surveyResultId, String questionId, String userId, String responseId)
//    {
//        log.info("Trying to saveResponse(...) with:");
//        log.info("surveyResultId: " + surveyResultId);
//        log.info("questionId: " + questionId);
//        log.info("userId: " + userId);
//        log.info("responseId: " + responseId);
//
//        SurveyResponse surveyResponse = surveyResponseService.get(surveyResultId).get();
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
