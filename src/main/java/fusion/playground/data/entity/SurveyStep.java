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
public class SurveyStep extends AbstractEntity
{
    private static final long serialVersionUID = 1L;

    private String surveyId;

    private StepType type;

    protected Integer questionNumber;

    protected String introduction;

    /* Description of the surveyStep */
    protected String text;

    /* Possible answers to the surveyStep */
    @EqualsAndHashCode.Exclude
    protected List<PossibleAnswer> possibleAnswers = new ArrayList<>();

    /**
     * Add a possible answer to the surveyStep.
    * @param possibleAnswer one of the possible multiple-choice answers
    * */
    public void addPossibleAnswer(PossibleAnswer possibleAnswer)
    {
        if (!possibleAnswers.contains(possibleAnswer))
        {
            possibleAnswers.add(possibleAnswer);
        }
    }

    public enum StepType
    {
        text,
        question
    }

}
