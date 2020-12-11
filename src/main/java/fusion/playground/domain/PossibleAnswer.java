package fusion.playground.domain;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString
public class PossibleAnswer extends AbstractEntity
{
    private @NonNull String text;
}
