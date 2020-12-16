package fusion.playground.data;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Getter
@RequiredArgsConstructor
@Accessors(fluent=true)
@EqualsAndHashCode
@ToString
public class AbstractEntity
{
    @Id
    private String id;
}
