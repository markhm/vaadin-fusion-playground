package fusion.playground.domain;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter @Setter
@NoArgsConstructor
@Accessors(fluent=true)
@EqualsAndHashCode @ToString
@Entity
public class User
{
    @Id
    private Integer id;

    private @NonNull String username;
    private @NonNull String passwordHash;

}
