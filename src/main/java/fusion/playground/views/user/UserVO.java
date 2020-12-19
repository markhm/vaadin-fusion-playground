package fusion.playground.views.user;

import fusion.playground.data.entity.validation.Age;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import java.time.LocalDate;

/**
 * Value object for passing user creation only.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString
public class UserVO
{
    private @NonNull String firstName;
    private @NonNull String username;

    @Email(message = "Please enter a valid e-mail address.")
    private @NonNull String emailAddress;
    private @NonNull String password;

    // a user should be at least 13 years old
    @Age(value = 13)
    private @NonNull LocalDate dateOfBirth;
}
