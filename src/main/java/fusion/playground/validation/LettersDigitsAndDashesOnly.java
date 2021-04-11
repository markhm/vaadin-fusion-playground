package fusion.playground.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Pattern(regexp = "^[a-zA-Z0-9-\\s]+$", message = "Must have a valid tagname consisting only of letters, digits and dashes")
@Constraint(validatedBy = {})
public @interface LettersDigitsAndDashesOnly {
    String message() default "{com.acme.constraint.MyConstraint.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
