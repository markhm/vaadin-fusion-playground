package fusion.playground.domain;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter @Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(fluent=true)
@EqualsAndHashCode @ToString
@Entity
@Table(name="app_user")
public class User extends AbstractEntity
{
    private @NonNull String firstName;
    private @NonNull String lastName;

    private @NonNull String username;
    private @NonNull String emailAddress;

    private @NonNull String passwordHash;
    private @NonNull Integer questionPointer;

}
