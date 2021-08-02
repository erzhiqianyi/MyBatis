/*
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.autoconstructor;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class AutoConstructorTest {
    private static SqlSessionFactory sqlSessionFactory;

    @BeforeAll
    static void setUp() throws Exception {
        // create a SqlSessionFactory ,从配置文件中 创建 SqlSessionFactory
        try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/autoconstructor/mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        }

        // populate in-memory database, 创建内存数据库
        BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
                "org/apache/ibatis/autoconstructor/CreateDB.sql");
    }

    @Test
    void fullyPopulatedSubject() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            final AutoConstructorMapper mapper = sqlSession.getMapper(AutoConstructorMapper.class);
            final Object subject = mapper.getSubject(1);
            assertNotNull(subject);
        }
    }

    @Test
    void primitiveSubjects() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            final AutoConstructorMapper mapper = sqlSession.getMapper(AutoConstructorMapper.class);
            //数据有问题,(2, 'b', 10, NULL, 45, 1, CURRENT_TIMESTAMP)有个不存在，导致使用构造器去构造对象时，无法生成对应对象,
            assertThrows(PersistenceException.class, mapper::getSubjects);
        }
    }

    @Test
    void testPrimitiveConstructor() {
        ObjectFactory factory = new DefaultObjectFactory();
        List<Class<?>> constructorArgTypes = new ArrayList<>();
        constructorArgTypes.add(int.class);
        constructorArgTypes.add(String.class);
        constructorArgTypes.add(int.class);
        constructorArgTypes.add(int.class);
        constructorArgTypes.add(int.class);
        constructorArgTypes.add(boolean.class);
        constructorArgTypes.add(Date.class);

        List<Object> constructorArgs = new ArrayList<>();
        constructorArgs.add(2);
        constructorArgs.add('b');
        constructorArgs.add(10);
        constructorArgs.add(null);
        constructorArgs.add(45);
        constructorArgs.add(true);
        constructorArgs.add(new Date());
        assertThrows(PersistenceException.class,
                () -> factory.create(PrimitiveSubject.class, constructorArgTypes, constructorArgs));

    }




    @Test
    void annotatedSubject() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            final AutoConstructorMapper mapper = sqlSession.getMapper(AutoConstructorMapper.class);

            //AnnotatedSubject 用AutomapConstructor 指定了默认解析构造器，使用指定的构造器进行构造，
            //height 改成用Integer ，为null也可以处理
            verifySubjects(mapper.getAnnotatedSubjects());
        }
    }

    @Test
    void badSubject() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            final AutoConstructorMapper mapper = sqlSession.getMapper(AutoConstructorMapper.class);
            //类型不匹配
            assertThrows(PersistenceException.class, mapper::getBadSubjects);
        }
    }

    @Test
    void extensiveSubject() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            final AutoConstructorMapper mapper = sqlSession.getMapper(AutoConstructorMapper.class);
            verifySubjects(mapper.getExtensiveSubjects());
        }
    }

    private void verifySubjects(final List<?> subjects) {
        assertNotNull(subjects);
        Assertions.assertThat(subjects.size()).isEqualTo(3);
    }
}
