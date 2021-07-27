package org.apache.ibatis.reflection;

import org.apache.ibatis.reflection.domain.Reflect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionTest {

    private String reflectClassPackage;

    @BeforeEach
    public void setUp() {
        reflectClassPackage = "org.apache.ibatis.reflection.domain.Reflect";
    }

    @Test
    public void testGetClass() throws ClassNotFoundException {
        Class reflectClass = Class.forName(reflectClassPackage);
        Class reflectClass2 = Reflect.class;
        Reflect reflect = new Reflect();
        Class reflectClass3 = reflect.getClass();
        assertEquals(reflectClass, reflectClass2);
        assertEquals(reflectClass, reflectClass3);
        assertEquals(reflectClass2, reflectClass3);
    }

    @Test
    public void testGetFiled() throws ClassNotFoundException {
        Class reflectClass = Class.forName(reflectClassPackage);
        Field[] fields = reflectClass.getFields();
        Field[] declaredFields = reflectClass.getDeclaredFields();
        System.out.println(" public field ");
        assertEquals(2, fields.length);
        for (Field field : fields) {
            System.out.println(field);
        }

        System.out.println(" all  field ");
        assertEquals(2, declaredFields.length);
        for (Field field : declaredFields) {
            System.out.println(field);
        }

    }

    @Test
    public void testGetConstructor() throws ClassNotFoundException {
        Class reflectClass = Class.forName(reflectClassPackage);
        Constructor[] constructors = reflectClass.getConstructors();
        Constructor[] declaredConstructors = reflectClass.getDeclaredConstructors();
        System.out.println(" public constructor ");
        assertEquals(2, constructors.length);
        for (Constructor constructor : constructors) {
            System.out.println(constructor);
        }

        System.out.println(" all  constructor ");
        assertEquals(3, declaredConstructors.length);
        for (Constructor field : declaredConstructors) {
            System.out.println(field);
        }

    }

    @Test
    public void testGetMethod() throws ClassNotFoundException {
        Class reflectClass = Class.forName(reflectClassPackage);
        Method[] methods = reflectClass.getMethods();
        Method[] declaredMethods = reflectClass.getDeclaredMethods();
        System.out.println(" public method ");
        assertEquals(19, methods.length);
        for (Method method : methods) {
            System.out.println(method);
        }

        System.out.println(" all  method ");
        assertEquals(6, declaredMethods.length);
        for (Method method : declaredMethods) {
            System.out.println(method);
        }

    }

    @Test
    public void testGetAnnotation() throws ClassNotFoundException, NoSuchFieldException {
        Class reflectClass = Class.forName(reflectClassPackage);
        Annotation[] annotations = reflectClass.getAnnotations();
        assertEquals(1, annotations.length);
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }

        Field[] fields = reflectClass.getDeclaredFields();
        for (Field field : fields) {
            annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                System.out.println(annotation);
            }
        }

        Method[] methods = reflectClass.getDeclaredMethods();
        for (Method method : methods) {
            annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                System.out.println(annotation);
            }
        }


    }

    @Test
    public void testInvoke() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        String info = "hello";
        Class reflectClass = Class.forName(reflectClassPackage);
        Object object = reflectClass.newInstance();
        Method method = reflectClass.getMethod("returnValue", String.class);
        String result = (String) method.invoke(object, info);
        assertEquals(info, result);
    }

    @Test
    public void testCreateObject() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        String info = "hello";
        Class reflectClass = Class.forName(reflectClassPackage);
        Object objectOne = reflectClass.newInstance();
        assertTrue(objectOne instanceof Reflect);
        Object objectTwo = reflectClass.getConstructor(String.class);
        assertTrue(objectOne instanceof Reflect);
        assertNotEquals(objectOne,objectTwo);

    }


}
