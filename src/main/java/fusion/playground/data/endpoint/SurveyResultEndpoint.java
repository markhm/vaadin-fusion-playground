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
public class SurveyResultEndpoint extends CrudEndpoint<SurveyResult, String>
{
    private static Log log = LogFactory.getLog(SurveyResultEndpoint.class);

    private SurveyService surveyService;
    private SurveyResultService surveyResultService;
    private UserService userService;
    private QuestionService questionService;

    public SurveyResultEndpoint(@Autowired SurveyService surveyService, SurveyResultService surveyResultService,
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

    public SurveyResult confirmResponses(String surveyResultId)
    {
        return surveyResultService.confirmResponses(surveyResultId);
    }

    public SurveyResult rejectResponses(String surveyResultId)
    {
        return surveyResultService.rejectResponses(surveyResultId);
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

}
