package fusion.playground.domain;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Accessors(fluent=true)
@EqualsAndHashCode
@ToString
@Entity (name = "Question")
@Table( name = "question")
public class Question extends AbstractEntity
{
    private static final long serialVersionUID = 1L;

    @Column(name = "category")
    private String category;

    @Column(name = "number")
    private Integer number;

    @EqualsAndHashCode.Exclude
    @Column(name = "text")
    private String text;

    @EqualsAndHashCode.Exclude
    @OneToMany (fetch = FetchType.EAGER ) // cascade = CascadeType.ALL,
    @JoinColumn(name = "possible_answer_id")
    @OrderBy("id ASC")
    private List<PossibleAnswer> possibleAnswers = new ArrayList<>();

    public void addPossibleAnswer(PossibleAnswer possibleAnswer)
    {
        if (!possibleAnswers.contains(possibleAnswer))
        {
            possibleAnswers.add(possibleAnswer);
        }
    }

}
