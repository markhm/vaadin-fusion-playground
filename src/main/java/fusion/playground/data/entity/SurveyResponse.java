package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString
public class SurveyResponse extends AbstractEntity
{
    private User user = null;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Survey survey;

    private List<Response> responses;

}
