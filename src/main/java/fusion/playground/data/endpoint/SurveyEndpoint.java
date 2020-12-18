package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import fusion.playground.data.CrudEndpoint;
import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.SurveyResult;
import fusion.playground.data.service.SurveySessionService;
import fusion.playground.data.service.SurveyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Endpoint
@AnonymousAllowed
public class SurveyEndpoint extends CrudEndpoint<Survey, String>
{
    private static Log log = LogFactory.getLog(SurveyEndpoint.class);

    private SurveyService surveyService;
    private SurveySessionService surveySessionService;

    @Autowired
    public SurveyEndpoint(SurveyService surveyService, SurveySessionService surveySessionService)
    {
        this.surveyService = surveyService;
        this.surveySessionService = surveySessionService;
    }

    public SurveyService getService()
    {
        return surveyService;
    }

    public String[] getAvailableSurveys()
    {
        // EndpointUtil.logPrincipal("getSurveyNames()");

        List<String> availableSurveys = surveyService.getAvailableSurveys();
        availableSurveys.forEach(survey -> log.info("found surveyName: "+survey));

        return availableSurveys.stream().toArray(String[]::new);
    }

    public String getSurveyDescription(String category, String name)
    {
        return surveyService.getSurveyDescription(category, name);
    }

    public int getTotalNumberOfQuestions(String surveyResultId)
    {
        // EndpointUtil.logPrincipal("getTotalNumberOfQuestions(..)");

        SurveyResult surveyResponse = surveySessionService.get(surveyResultId).get();
        Survey survey = surveyResponse.survey();
        return survey.questions().size();
        // return surveyService.countAllBySurveyName(survey);
    }


}
