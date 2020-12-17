package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import fusion.playground.data.service.SurveyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Accessors(fluent=true)
@ToString(callSuper = true)
public class SurveyResult extends AbstractEntity
{
    private static Log log = LogFactory.getLog(SurveyService.class);

    private User user = null;
    private Survey survey;

    private int lastCompletedQuestion = 0;

    private LocalDateTime startTime;
    private LocalDateTime endTime = null;

    private List<Response> responses;

    private SurveyResultStatus status;

    int score;

    public SurveyResult()
    {
        status = SurveyResultStatus.created;
    }

    public void addResponse(Response response)
    {
        if (responses == null)
        {
            responses = new ArrayList<>();
            status = SurveyResultStatus.in_progress;
        }
        if (responses.size() > 0)
        {
            Response previousResponse = responses.get(responses.size() - 1);
            Question previousQuestion = previousResponse.question();

            // Check if response to question has already been saved
            if (previousQuestion.equals(response.question()))
            {
                log.warn("Response already saved. Ignoring. ");
                return;
            }

            // Check if response is next in line -> To be implemented in case of a bug
        }
        responses.add(response);
        lastCompletedQuestion++;

        log.info("lastCompleted question: " + lastCompletedQuestion);
        if (lastCompletedQuestion == survey.questions().size())
        {
            registerCompletion();
        }
    }

    public boolean isComplete()
    {
        if (endTime == null) return false;
        else return true;
    }

    public void registerCompletion()
    {
//        log.info("status: " + status);
//        log.info("survey.questions().size(): " + survey.questions().size());
//        log.info("responses().size(): " + responses().size());

        if (status == SurveyResultStatus.in_progress && responses().size() == survey.questions().size())
        {
            this.endTime = LocalDateTime.now();
            status = SurveyResultStatus.complete;
        }
        else
        {
             log.error("Cannot register completion, please investigate.");
        }
    }

    public void registerUserConfirmation()
    {
        if (status == SurveyResultStatus.complete)
        {
            status = SurveyResultStatus.confirmed;
        }
        else
        {
            log.error("Cannot register confirmation, please investigate.");
        }
    }

    public void registerUserRejection()
    {
        if (status == SurveyResultStatus.complete)
        {
            status = SurveyResultStatus.rejected;
        }
        else
        {
            log.error("Cannot register rejection, please investigate.");
        }
    }

    /**
     * created - newly created
     * in_process - user is answering questions
     * complete - user has answered all questions
     * confirmed - user has confirmed responses and result is ready for evaluation
     * evaluated - the surveyResult has been checked and the score is available
     */
    public enum SurveyResultStatus {
        created,
        in_progress,
        complete,
        confirmed,
        rejected,
        evaluated
    }

}