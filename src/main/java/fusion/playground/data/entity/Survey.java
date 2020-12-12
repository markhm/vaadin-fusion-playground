package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString
public class Survey extends AbstractEntity
{
    String type;
}
