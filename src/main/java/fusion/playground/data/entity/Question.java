package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString(callSuper = true)
public class Question extends AbstractEntity
{
    private static final long serialVersionUID = 1L;

    private Integer orderNumber;

    /* Description of the question */
    protected String text;

    /* Possible answers to the question */
    protected List<PossibleAnswer> possibleAnswers = new ArrayList<>();

    /**
     * Add a possible answer to the question.
    * @param possibleAnswer one of the possible multiple-choice answers
    * */
    public void addPossibleAnswer(PossibleAnswer possibleAnswer)
    {
        if (!possibleAnswers.contains(possibleAnswer))
        {
            possibleAnswers.add(possibleAnswer);
        }
    }

}
