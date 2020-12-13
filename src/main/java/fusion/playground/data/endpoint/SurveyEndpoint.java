package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import fusion.playground.data.entity.Question;
import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.SurveyResponse;
import fusion.playground.data.entity.User;
import fusion.playground.data.service.SurveyResponseService;
import fusion.playground.data.service.SurveyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Endpoint
@AnonymousAllowed
public class SurveyEndpoint
{
    private static Log log = LogFactory.getLog(SurveyEndpoint.class);

    private SurveyService surveyService;
    private SurveyResponseService surveyResponseService;

    public SurveyEndpoint(@Autowired SurveyService surveyService, SurveyResponseService surveyResponseService)
    {
        this.surveyService = surveyService;
        this.surveyResponseService = surveyResponseService;
    }

    public List<String> getAvailableSurveys()
    {
        log.info("getAvailableSurveys() was called");
        List<String> availableSurveys = surveyService.getAvailableSurveys();

        availableSurveys.forEach(survey -> log.info("found surveyName: "+survey));
        return availableSurveys;
    }

//    public Question getNextQuestion(String userId, String surveyName)
//    {
//        return surveyService.getNextQuestion(userId, surveyName);
//    }

    public Question getNextQuestion(String surveyResponseId)
    {
        return surveyService.getNextQuestion(surveyResponseId);
//        SurveyResponse surveyResponse = surveyResponseService.get(surveyResponseId).get();
//        int lastCompletedQuestion = surveyResponse.lastCompletedQuestion();
//        Survey survey = surveyResponse.survey();
//        return survey.getQuestion(lastCompletedQuestion);
    }

    public int getTotalNumberOfQuestions(String surveyName)
    {
        return surveyService.countAllBySurveyName(surveyName);
    }


}
