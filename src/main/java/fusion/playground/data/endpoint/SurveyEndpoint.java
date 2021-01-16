package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import fusion.playground.data.CrudEndpoint;
import fusion.playground.data.entity.*;
import fusion.playground.data.service.SurveySessionService;
import fusion.playground.data.service.SurveyService;
import fusion.playground.data.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Endpoint
@AnonymousAllowed
// @Secured(value= {"USER"})
// @RolesAllowed(value = {"openid"})
public class SurveyEndpoint extends CrudEndpoint<Survey, String>
{
    private static Log log = LogFactory.getLog(SurveyEndpoint.class);

    private UserService userService;
    private SurveyService surveyService;
    private SurveySessionService surveySessionService;

    @Autowired
    public SurveyEndpoint(UserService userService, SurveyService surveyService, SurveySessionService surveySessionService)
    {
        this.userService = userService;
        this.surveyService = surveyService;
        this.surveySessionService = surveySessionService;
    }

    public SurveyService getService()
    {
        return surveyService;
    }

    /**
     * Get the surveys that a user can take
     * @param oktaUserId
     * @return
     */
    public List<SurveyInfo> getAvailableSurveys(String oktaUserId)
    {
        // EndpointUtil.logPrincipal("getSurveyNames()");

        List<SurveyInfo> availableSurveys = surveyService.getAvailableSurveysForOktaUserId(oktaUserId);
        availableSurveys.forEach(survey -> log.info("Returning surveyInfo: "+survey));

        return availableSurveys;
    }

    public List<Survey> getOwnedSurveys(String oktaUserId)
    {
        List<Survey> ownSurveys = surveyService.getOwnedSurveys(oktaUserId);

        return ownSurveys;
    }

    public String createSurvey(String oktaUserId, String category, String name)
    {
        // EndpointUtil.logAuthenticationContext("createSurvey(..)");
        User user = userService.findByOktaUserId(oktaUserId);

        return surveyService.createDraftSurvey(user.id(), category, name).id();

    }

    // Let's rethink this.
//    public List<Question> getQuestions(String surveyId)
//    {
//        return get(surveyId).get().surveySteps();
//    }

    public SurveyStep addQuestion(SurveyStep question)
    {
        return surveyService.addQuestion(question);
    }

    public void publishSurvey(String surveyId)
    {
        surveyService.publish(surveyId);
    }

    // Moved from the Abstract CrudEndpoint to have better control
    public Survey update(Survey entity) {
        return getService().update(entity);
    }

    public void delete(String surveyId) {
        getService().delete(surveyId);
    }


}
