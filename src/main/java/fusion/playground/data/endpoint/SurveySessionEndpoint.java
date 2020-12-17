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
public class SurveySessionEndpoint extends CrudEndpoint<SurveyResult, String>
{
    private static Log log = LogFactory.getLog(SurveySessionEndpoint.class);

    private SurveyService surveyService;
    private SurveySessionService surveySessionService;
    private UserService userService;
    private QuestionService questionService;

    @Autowired
    public SurveySessionEndpoint(SurveyService surveyService, SurveySessionService surveySessionService,
                                 UserService userService, QuestionService questionService)
    {
        this.surveyService = surveyService;
        this.surveySessionService = surveySessionService;
        this.userService = userService;
        this.questionService = questionService;
    }

    @Override
    protected SurveySessionService getService() {
        return surveySessionService;
    }

    public String beginSurvey(String surveyName, String oktaUserId)
    {
        // EndpointUtil.logPrincipal("SurveyResultEndpoint.beginSurvey(..)");

        log.info("Beginning begin survey " + surveyName + " for user with oktaUserId: "+oktaUserId);
        User user = userService.findByOktaUserId(oktaUserId).get();
        Survey survey = surveyService.findSurveyByName(surveyName);
        return surveySessionService.beginSurvey(user, survey);
    }

    public Question getNextQuestion(String surveyResultId)
    {
        // EndpointUtil.logPrincipal("getNextQuestion(..)");
        log.info("at SurveyEndpoint.getNextQuestion(" + surveyResultId + ")...");

        return surveyService.getNextQuestion(surveyResultId);
//        SurveyResponse surveyResponse = surveyResponseService.get(surveyResultId).get();
//        int lastCompletedQuestion = surveyResponse.lastCompletedQuestion();
//        Survey survey = surveyResponse.survey();
//        return survey.getQuestion(lastCompletedQuestion);
    }

    public void saveResponse(String surveyResponseId, String questionId, String responseId)
    {
        surveySessionService.saveResponse(surveyResponseId, questionId, responseId);
    }

    public SurveyResult confirmResponses(String surveyResultId)
    {
        return surveySessionService.confirmResponses(surveyResultId);
    }

    public SurveyResult rejectResponses(String surveyResultId)
    {
        return surveySessionService.rejectResponses(surveyResultId);
    }

    public List<SurveyResult> getCompletedSurveys(String oktaUserId)
    {
        User user = userService.findByOktaUserId(oktaUserId).get();
        return surveySessionService.getCompletedSurveys(user);
    }

    public List<QuestionResponse> getSurveyResponses(String surveyResultId)
    {
        return surveySessionService.getSurveyResponses(surveyResultId);
    }

}
