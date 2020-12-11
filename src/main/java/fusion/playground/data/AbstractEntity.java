package fusion.playground.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Getter
@RequiredArgsConstructor
@Accessors(fluent=true)
@EqualsAndHashCode
public class AbstractEntity
{
    @Id
    private String id;
}
