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
public class PossibleAnswer extends AbstractEntity
{
    // Not sure how much this still adds.
    public static final PossibleAnswer YES = new PossibleAnswer("Yes");
    public static final PossibleAnswer NO = new PossibleAnswer("No");
    public static final PossibleAnswer MAYBE = new PossibleAnswer("Maybe");

    private @NonNull String text;
}
