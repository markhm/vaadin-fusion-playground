package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import fusion.playground.data.entity.validation.Age;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import fusion.playground.data.entity.validation.AgeValidator;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString(callSuper = true)
public class User extends AbstractEntity
{
    /* Unique property that defines a user in Okta (sub or accessToken.claims.uid) */
    private String oktaUserId;

    private @NonNull String username;

    @Email(message = "Please enter a valid e-mail address.")
    private @NonNull String emailAddress;
    private @NonNull boolean emailConfirmed = false;

    // a user should be at least 13 years old
    @Age(value = 13)
    private @NonNull LocalDate dateOfBirth;
}
