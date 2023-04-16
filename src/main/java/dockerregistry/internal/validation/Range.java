package dockerregistry.internal.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Pattern(regexp = "^[0-9]+-[0-9]+$")
@Target({ PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = { })
@Documented
public @interface Range {

    String message() default "{dockerregistry.validation.Namespace.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
