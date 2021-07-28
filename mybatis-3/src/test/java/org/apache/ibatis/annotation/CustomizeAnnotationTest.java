package org.apache.ibatis.annotation;

import org.apache.ibatis.reflection.ParamNameResolver;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomizeAnnotationTest {

    @Test
    public void testGetAnnotation() throws NoSuchFieldException, NoSuchMethodException {
        Class clazz = CustomizeTest.class;
        Customize typeAnnotation = (Customize) clazz.getAnnotation(Customize.class);
        assertNotNull(typeAnnotation);
        assertEquals("CustomizeTest", typeAnnotation.className());

        Customize filedAnnotation = clazz.getDeclaredField("name").getAnnotation(Customize.class);
        assertNotNull(filedAnnotation);
        assertEquals(filedAnnotation.filedSort(), 0);

        Customize methodIgnoreTrueAnnotation = clazz.getMethod("getName").getAnnotation(Customize.class);
        assertNotNull(methodIgnoreTrueAnnotation);
        assertEquals(methodIgnoreTrueAnnotation.ignoreMethod(), true);

        Customize methodIgnoreFalseAnnotation = clazz.getMethod("setName", String.class).getAnnotation(Customize.class);
        assertNotNull(methodIgnoreFalseAnnotation);
        assertEquals(methodIgnoreFalseAnnotation.ignoreMethod(), false);

        Method method = clazz.getMethod("changeName", String.class);
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for (Annotation[] annotations : paramAnnotations) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Customize) {
                    Customize paramAnnotation = (Customize) annotation;
                    assertEquals(0l, paramAnnotation.paramId());
                }
            }
        }
        Constructor<CustomizeTest> constructor = clazz.getConstructor(String.class);
        Annotation[][] constructorAnnotations = constructor.getParameterAnnotations();
        for (Annotation[] annotations : constructorAnnotations) {
            for (Annotation annotation : annotations){
                if (annotation instanceof  Customize){
                    Customize paramAnnotation = (Customize) annotation;
                    assertEquals("create", paramAnnotation.constructorName());
                }
            }
        }

    }

}
