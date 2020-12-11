package fusion.playground.domain;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(fluent=true)
@EqualsAndHashCode
@ToString
@Entity
@Table(name="possible_answer")
public class PossibleAnswer extends AbstractEntity
{
    private @NonNull String text;
}
