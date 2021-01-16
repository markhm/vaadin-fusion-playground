package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import fusion.playground.data.CrudEndpoint;
import fusion.playground.data.entity.*;
import fusion.playground.data.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Endpoint
public class SurveySessionEndpoint extends CrudEndpoint<SurveyResult, String>
{
    private static Log log = LogFactory.getLog(SurveySessionEndpoint.class);

    private SurveyService surveyService;
    private SurveySessionService surveySessionService;
    private UserService userService;
    private SurveyStepService surveyStepService;

    @Autowired
    public SurveySessionEndpoint(SurveyService surveyService, SurveySessionService surveySessionService,
                                 UserService userService, SurveyStepService surveyStepService)
    {
        this.surveyService = surveyService;
        this.surveySessionService = surveySessionService;
        this.userService = userService;
        this.surveyStepService = surveyStepService;
    }

    @Override
    protected SurveySessionService getService() {
        return surveySessionService;
    }

    public String beginSurvey(String surveyId, String oktaUserId)
    {
        // EndpointUtil.logPrincipal("SurveyResultEndpoint.beginSurvey(..)");

        log.info("Beginning begin survey " + surveyId + " for userClaims with oktaUserId: "+oktaUserId);
        User user = userService.findByOktaUserId(oktaUserId);
        Survey survey = surveyService.get(surveyId).get();
        return surveySessionService.beginSurvey(user, survey);
    }

    public SurveyStep getNextSurveyStep(String surveyResultId)
    {
        // EndpointUtil.logPrincipal("getNextQuestion(..)");
        log.info("at SurveyEndpoint.getNextQuestion(" + surveyResultId + ")...");

        return surveySessionService.getNextSurveyStep(surveyResultId);
//        SurveyResponse surveyResponse = surveyResponseService.get(surveyResultId).get();
//        int lastCompletedQuestion = surveyResponse.lastCompletedQuestion();
//        Survey survey = surveyResponse.survey();
//        return survey.getQuestion(lastCompletedQuestion);
    }

    public void saveResponse(String surveyResponseId, String questionId, String responseId)
    {
        surveySessionService.saveResponse(surveyResponseId, questionId, responseId);
    }

//    public void saveResponse(String surveyResponseId, String questionId, String responseId, String location)
//    {
//        log.info("Response was given on location: " + location);
//        surveySessionService.saveResponse(surveyResponseId, questionId, responseId);
//    }

    public SurveyResult confirmResponses(String surveyResultId)
    {
        return surveySessionService.confirmResponses(surveyResultId);
    }

    public SurveyResult rejectResponses(String surveyResultId)
    {
        return surveySessionService.rejectResponses(surveyResultId);
    }

    public boolean userHasConfirmedResponses(String surveyResultId)
    {
        return surveySessionService.get(surveyResultId).get().userHasConfirmed();
    }

    public List<SurveyResult> getCompletedSurveys(String oktaUserId)
    {
        User user = userService.findByOktaUserId(oktaUserId);
        return surveySessionService.getCompletedSurveys(user.id());
    }

    public List<QuestionResponse> getSurveyResponses(String surveyResultId)
    {
        return surveySessionService.getSurveyResponses(surveyResultId);
    }

    public SurveyInfo getSurveyInfo(String surveyResultId)
    {
        SurveyResult surveyResult = surveySessionService.get(surveyResultId).get();
        Survey survey = surveyResult.survey();
        return SurveyInfo.createFrom(survey);
    }

}
