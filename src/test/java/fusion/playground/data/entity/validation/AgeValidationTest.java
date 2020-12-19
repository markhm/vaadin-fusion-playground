package fusion.playground.data.entity.validation;


import fusion.playground.data.entity.User;
import fusion.playground.views.user.UserVO;
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
// https://dzone.com/articles/create-your-own-constraint-with-bean-validation-20

public class AgeValidationTest
{
    private static Log log = LogFactory.getLog(AgeValidationTest.class);

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
        UserVO user = new UserVO();
        user.dateOfBirth(LocalDate.now().minusYears(4));
        // user.emailAddress("something");

        Set<ConstraintViolation<UserVO>> violations = validator.validate(user);

        log.info("# violations found: " + violations.size());

        violations.forEach(violation -> log.info("Violation found: " + violation));
    }

}
