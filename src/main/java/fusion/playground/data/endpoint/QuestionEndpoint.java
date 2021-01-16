package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import fusion.playground.data.CrudEndpoint;
import fusion.playground.data.entity.PossibleAnswer;
import fusion.playground.data.entity.SurveyStep;
import fusion.playground.data.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Endpoint
public class QuestionEndpoint extends CrudEndpoint<SurveyStep, String>
{
    private static Log log = LogFactory.getLog(QuestionEndpoint.class);

    private SurveyStepService surveyStepService;
    private PossibleAnswerService possibleAnswerService = null;
    // private SurveyService surveyService;
    private UserService userService;

    @Autowired
    public QuestionEndpoint(SurveyStepService surveyStepService,
                            PossibleAnswerService possibleAnswerService,
                            UserService userService)
    {
        this.surveyStepService = surveyStepService;
        this.possibleAnswerService = possibleAnswerService;
        this.userService = userService;
    }

    protected SurveyStepService getService() {
        return surveyStepService;
    }

    public PossibleAnswer addPossibleAnswer(String questionId, String possibleAnswerText)
    {
        log.info("addPossibleAnswer(" + questionId + ", " + possibleAnswerText + ")");

        SurveyStep question = surveyStepService.get(questionId).get();

        PossibleAnswer possibleAnswer = new PossibleAnswer(possibleAnswerText);
        PossibleAnswer savedPossibleAnswer = possibleAnswerService.update(possibleAnswer);
        question.addPossibleAnswer(savedPossibleAnswer);
        surveyStepService.update(question);

        // surveyService.rebuildSurvey(surveyStep.surveyId());

        return savedPossibleAnswer;
    }

}
