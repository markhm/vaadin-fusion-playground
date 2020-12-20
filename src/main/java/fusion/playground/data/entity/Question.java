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
public class Question extends AbstractEntity
{
    private static final long serialVersionUID = 1L;

    protected Integer orderNumber;

    /* Description of the question */
    protected String text;

    /* Possible answers to the question */
    @EqualsAndHashCode.Exclude
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
