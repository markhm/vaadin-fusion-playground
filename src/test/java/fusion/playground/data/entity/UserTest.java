package fusion.playground.data.entity;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

// https://farenda.com/java/bean-validation-unit-testing/

public class UserTest
{
    private static Log log = LogFactory.getLog(UserTest.class);

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {

        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testAgeValidation()
    {
        User user = new User();
        user.dateOfBirth(LocalDate.now().minusYears(4));
        user.emailAddress("something");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        log.info("# violations found: " + violations.size());

        violations.forEach(violation -> log.info("Violation found: " + violation));
    }

}
