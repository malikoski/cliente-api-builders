package springfox.documentation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
public @interface ApiIgnore {
    /**
     * A brief description of why this parameter/operation is ignored
     * @return  the description of why it is ignored
     */
    String value() default "";
}