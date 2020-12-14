package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString
public class User extends AbstractEntity
{
    /* Unique property that defines a user in Okta */
    private String sub;

    private String firstName;
    private String lastName;

    private @NonNull String username;
    private @NonNull String emailAddress;
}
