package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@Accessors(fluent=true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Survey extends AbstractEntity
{
    private @NonNull String category;
    private @NonNull String name;
    private String title;
    private String description = "There is no description available for this survey.";
    private boolean gradable = false;
    private boolean randomizeAnswerOrder = false;

    private SurveyStatus status = null;

    /** The owner's userId */
    private String ownerId;
    private Visibility visibility = Visibility.personal;

    /** userId's that this survey is visible to */
    private List<String> visibleTo = new ArrayList<>();

    private List<SurveyStep> surveySteps = new ArrayList<>();

    private Survey(String ownerId, SurveyCategory category, String name)
    {
        this.ownerId = ownerId;
        this.category = category.toString();
        this.name = name;
    }

    public static Survey createDraftSurvey(String ownerId, SurveyCategory category, String name)
    {
        Survey survey = new Survey(ownerId, category, name);
        survey.status(SurveyStatus.draft);
        survey.visibility(Visibility.personal);

        return survey;
    }

    public static Survey createPublishedSurvey(String ownerId, SurveyCategory category, String name)
    {
        Survey survey = new Survey(ownerId, category, name);
        survey.status(SurveyStatus.published);
        survey.visibility(Visibility.general);

        return survey;
    }

    public void addSurveyStep(SurveyStep surveyStep)
    {
        if (surveySteps == null) {
            surveySteps = new ArrayList<>();
        }

        surveySteps.add(surveyStep);
    }

    public SurveyStep getStep(int orderNumber)
    {
        return surveySteps.get(orderNumber - 1);
    }

    /**
     * created - newly created
     * published - published
     */
    public enum SurveyStatus {
        draft,
        published,
    }
}
