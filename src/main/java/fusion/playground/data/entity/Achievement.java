package fusion.playground.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString(callSuper = true)
public class Achievement
{
    private User user;
    private AchievementType type;

    private String description;

    private SurveyResult surveyResult;
}
