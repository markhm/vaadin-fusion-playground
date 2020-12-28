package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import fusion.playground.data.CrudEndpoint;
import fusion.playground.data.entity.PossibleAnswer;
import fusion.playground.data.entity.Survey;
import fusion.playground.data.service.*;
import fusion.playground.data.entity.Question;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Endpoint
public class QuestionEndpoint extends CrudEndpoint<Question, String>
{
    private static Log log = LogFactory.getLog(QuestionEndpoint.class);

    private QuestionService questionService;
    private PossibleAnswerService possibleAnswerService = null;
    // private SurveyService surveyService;
    private UserService userService;

    @Autowired
    public QuestionEndpoint(QuestionService questionService,
                            PossibleAnswerService possibleAnswerService,
                            UserService userService)
    {
        this.questionService = questionService;
        this.possibleAnswerService = possibleAnswerService;
        this.userService = userService;
    }

    protected QuestionService getService() {
        return questionService;
    }

    public PossibleAnswer addPossibleAnswer(String questionId, String possibleAnswerText)
    {
        log.info("addPossibleAnswer(" + questionId + ", " + possibleAnswerText + ")");

        Question question = questionService.get(questionId).get();

        PossibleAnswer possibleAnswer = new PossibleAnswer(possibleAnswerText);
        PossibleAnswer savedPossibleAnswer = possibleAnswerService.update(possibleAnswer);
        question.addPossibleAnswer(savedPossibleAnswer);
        questionService.update(question);

        // surveyService.rebuildSurvey(question.surveyId());

        return savedPossibleAnswer;
    }

}
