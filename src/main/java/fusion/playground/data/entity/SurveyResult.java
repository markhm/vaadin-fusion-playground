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
        if (status == SurveyResultStatus.in_progress && responses().size() == survey.questions().size())
        {
            this.endTime = LocalDateTime.now();
            status = SurveyResultStatus.complete;
        }
        else
        {
            log.error("Cannot register completion.");
        }
    }

    public void registerUserApproval()
    {
        if (status == SurveyResultStatus.complete)
        {
            status = SurveyResultStatus.approved;
        }
        else
        {
            log.error("Cannot register completion");
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
            log.error("Cannot register rejection");
        }
    }

    /**
     * created - newly created
     * in_process - user is answering questions
     * complete - user has answered all questions
     * approved - user has approved responses and result is ready for evaluation
     * evaluateds
     */
    public enum SurveyResultStatus {
        created,
        in_progress,
        complete,
        approved,
        rejected,
        evaluated
    }

}
