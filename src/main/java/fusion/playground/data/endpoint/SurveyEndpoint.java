package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import fusion.playground.data.CrudEndpoint;
import fusion.playground.data.entity.Question;
import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.SurveyResult;
import fusion.playground.data.service.SurveyResultService;
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
    private SurveyResultService surveyResultService;

    @Autowired
    public SurveyEndpoint(SurveyService surveyService, SurveyResultService surveyResultService)
    {
        this.surveyService = surveyService;
        this.surveyResultService = surveyResultService;
    }

    public SurveyService getService()
    {
        return surveyService;
    }

    public List<String> getSurveyNames()
    {
        log.info("getSurveyNames() was called");
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
//        SurveyResponse surveyResponse = surveyResponseService.get(surveyResultId).get();
//        int lastCompletedQuestion = surveyResponse.lastCompletedQuestion();
//        Survey survey = surveyResponse.survey();
//        return survey.getQuestion(lastCompletedQuestion);
    }

    public int getTotalNumberOfQuestions(String surveyReponseId)
    {
        SurveyResult surveyResponse = surveyResultService.get(surveyReponseId).get();
        Survey survey = surveyResponse.survey();
        return survey.questions().size();
        // return surveyService.countAllBySurveyName(survey);
    }


}
