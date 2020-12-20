package fusion.playground.data.entity;

import com.okta.sdk.resource.user.UserProfile;
import fusion.playground.data.AbstractEntity;
import fusion.playground.data.entity.validation.Age;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import fusion.playground.data.entity.validation.AgeValidator;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Data
@Document
@NoArgsConstructor
@Accessors(fluent=true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractEntity
{
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String DISPLAY_NAME = "displayName";

    /* Unique property that defines a user in Okta (sub or accessToken.claims.uid) */
    private String oktaUserId;

    private @NonNull String username;

    public static User createFrom(com.okta.sdk.resource.user.User oktaUser)
    {
        User user = new User();
        user.oktaUserId(oktaUser.getId());

        UserProfile profile = oktaUser.getProfile();
        user.username((String) profile.get(User.DISPLAY_NAME));

        return user;
    }

}
