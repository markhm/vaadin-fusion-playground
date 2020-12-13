package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import fusion.playground.data.service.SurveyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString
public class SurveyResponse extends AbstractEntity
{
    private static Log log = LogFactory.getLog(SurveyService.class);

    private User user = null;
    private Survey survey;

    private int lastCompletedQuestion = 0;

    private LocalDateTime startTime;
    private LocalDateTime endTime = null;

    private List<Response> responses;

    public void addResponse(Response response)
    {
        if (responses == null)
        {
            responses = new ArrayList<>();
        }
        if (responses.size() > 0)
        {
            Response previousResponse = responses.get(responses.size() - 1);
            if (previousResponse.question().orderNumber() != response.question().orderNumber() - 1)
            {
                log.error("The question to be saved is nót the next in line. IGNORING");
                return;
            }
        }
        responses.add(response);
        lastCompletedQuestion++;

        if (lastCompletedQuestion == survey.questions().size())
        {
            endTime = LocalDateTime.now();
        }
    }

    public boolean isComplete()
    {
        if (endTime == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

}
