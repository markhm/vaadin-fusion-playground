package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import fusion.playground.data.entity.Question;
import fusion.playground.data.service.SurveyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

@Endpoint
@AnonymousAllowed
public class SurveyEndpoint
{
    private static Log log = LogFactory.getLog(SurveyEndpoint.class);

    private SurveyService surveyService;

    public SurveyEndpoint(SurveyService surveyService)
    {
        this.surveyService = surveyService;
    }

    public List<String> getAvailableSurveys()
    {
        log.info("getAvailableSurveys() was called");
        List<String> availableSurveys = surveyService.getAvailableSurveys();

        availableSurveys.forEach(survey -> log.info("found survey: "+survey));
        return availableSurveys;
    }

    public Question getNextQuestion(String userId, String surveyName)
    {
        return surveyService.getNextQuestion(userId, surveyName);
    }

    public int getTotalNumberOfQuestions(String surveyName)
    {
        return surveyService.countAllBySurveyName(surveyName);
    }


}
