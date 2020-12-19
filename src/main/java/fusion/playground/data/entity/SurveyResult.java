package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import fusion.playground.data.service.SurveyService;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
@AllArgsConstructor
@Accessors(fluent=true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SurveyResult extends AbstractEntity
{
    private static Log log = LogFactory.getLog(SurveyResult.class);

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

        // log.info("lastCompleted question: " + lastCompletedQuestion);
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
             log.error("status == SurveyResultStatus.in_progress && responses().size() == survey.questions().size(): " +
                     (status == SurveyResultStatus.in_progress && responses().size() == survey.questions().size()));
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
            log.error("Cannot register confirmation, please investigate, because status is: '" + status + "'.");
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
     * in_process - userClaims is answering questions
     * complete - userClaims has answered all questions
     * confirmed - userClaims has confirmed responses and result is ready for evaluation
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
