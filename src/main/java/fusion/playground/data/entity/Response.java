package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString(callSuper = true)
public class Response extends AbstractEntity
{
    private User user;
    private Question question;

    /** This should probably be the id of the possibleAnswer */
    private String response;

    private boolean correct;
}
