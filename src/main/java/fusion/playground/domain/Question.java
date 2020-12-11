package fusion.playground.domain;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString
public class Question extends AbstractEntity
{
    private static final long serialVersionUID = 1L;

    private String category;

    private Integer orderNumber;

    private String text;

    private List<PossibleAnswer> possibleAnswers = new ArrayList<>();

    public void addPossibleAnswer(PossibleAnswer possibleAnswer)
    {
        if (!possibleAnswers.contains(possibleAnswer))
        {
            possibleAnswers.add(possibleAnswer);
        }
    }

}
