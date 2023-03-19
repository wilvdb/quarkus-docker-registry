package dockerregistry.internal.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull
@Size(max = 128)
@Pattern(regexp = "[a-zA-Z0-9_][a-zA-Z0-9._-]{0,127}")
@Target({ PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = { })
@Documented
public @interface Reference {

    String message() default "{dockerregistry.validation.Namespace.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
