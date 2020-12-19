package fusion.playground.data.entity;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(fluent=true)
@ToString()
public class SurveyInfo
{
    private @NonNull String surveyId;
    private @NonNull String category;
    private @NonNull String name;

    private String title;
    private String description = "There is no description available for this survey.";

    private boolean gradable = false;
    private int numberOfQuestions;

    public static SurveyInfo createFrom(Survey survey)
    {
        SurveyInfo surveyInfo = new SurveyInfo();

        surveyInfo.surveyId(survey.id());
        surveyInfo.category(survey.category());
        surveyInfo.name(survey.name());
        surveyInfo.title(survey.title());

        surveyInfo.description(survey.description());
        surveyInfo.gradable(survey.gradable());
        surveyInfo.numberOfQuestions(survey.questions().size());

        return surveyInfo;
    }

}
