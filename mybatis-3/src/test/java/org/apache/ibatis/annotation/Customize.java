package org.apache.ibatis.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)//注解保留到运行阶段，可以在运行阶段获取到注解信息
@Documented
public @interface Customize {
    String className() default "customizeClass";

    int filedSort() default 1;

    boolean ignoreMethod() default false;

    long paramId() default 1l;

    String constructorName() default "";

}
