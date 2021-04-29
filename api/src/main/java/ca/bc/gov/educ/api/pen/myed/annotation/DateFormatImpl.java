package ca.bc.gov.educ.api.pen.myed.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class DateFormatImpl implements ConstraintValidator<DateFormat, String> {
  private String format;

  @Override
  public void initialize(final DateFormat constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    this.format = constraintAnnotation.format();
  }

  @Override
  public boolean isValid(final String date, final ConstraintValidatorContext context) {
    if (date == null) {
      return true;
    }
    try {
      LocalDate.parse(date, DateTimeFormatter.ofPattern(this.format).withResolverStyle(ResolverStyle.STRICT));
      return true;
    } catch (final Exception e) {
      return false;
    }
  }


}
