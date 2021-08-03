package org.apache.ibatis.io.singleton;


import lombok.extern.log4j.Log4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Log4j
public class SingletonTest {


    @Order(1)
    @Execution(ExecutionMode.CONCURRENT)
    @DisplayName("多线程执行10次")
    @RepeatedTest(value = 10, name = "完成度：{currentRepetition}/{totalRepetitions}")
    void testSingleton() {
        LazyManSingleton lazyManSingleton = LazyManSingleton.getInstance();
        HungryManSingleton hungryManSingleton = HungryManSingleton.getInstance();
        DclSingleton dclSingleton = DclSingleton.getInstance();
        EnumSingleton enumSingleton = EnumSingleton.Instance;
        log.info("lazy man singleton: " + lazyManSingleton);
        log.info("hungry Man Singleton" + hungryManSingleton);
        log.info("dcl Singleton" + dclSingleton);
        log.info("enum Singleton" + enumSingleton);

    }
}
