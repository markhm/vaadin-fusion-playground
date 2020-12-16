package fusion.playground.data.entity.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AgeValidator implements ConstraintValidator<Age, LocalDate>
{
    protected long minAge;

    @Override
    public void initialize(Age ageValue)
    {
        this.minAge = ageValue.value();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext)
    {
        if (date == null)
        {
            return true;
        }

        LocalDate today = LocalDate.now();

        return ChronoUnit.YEARS.between(date, today) > minAge;
    }

}
