package dockerregistry.internal.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull
@Pattern(regexp = "[a-z0-9]+([._-][a-z0-9]+)*(/[a-z0-9]+([._-][a-z0-9]+)*)*")
@Target({ PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = { })
@Documented
public @interface Namespace {

    String message() default "{dockerregistry.validation.Namespace.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
