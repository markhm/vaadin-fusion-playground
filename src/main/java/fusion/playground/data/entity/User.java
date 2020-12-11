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
    private @NonNull String firstName;
    private @NonNull String lastName;

    private @NonNull String username;
    private @NonNull String emailAddress;

    private @NonNull String passwordHash;
    private @NonNull Integer questionPointer;

}
