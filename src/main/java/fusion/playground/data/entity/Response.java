package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Response extends AbstractEntity
{
    private User user;
    private Question question;

    /** This should probably be the id of the possibleAnswer */
    private String response;

    private boolean correct;

    public void evaluateResponse()
    {
        PossibleAnswer correctAnswer = ((FactualQuestion) question).correctAnswer();

        if (response().equals(correctAnswer.id()))
        {
            correct(true);
        }
    }

    public int gradeResponse()
    {
        if (correct())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}
