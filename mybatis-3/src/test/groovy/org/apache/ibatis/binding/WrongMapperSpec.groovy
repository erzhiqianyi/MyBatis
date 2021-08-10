package org.apache.ibatis.binding

import org.apache.ibatis.builder.BuilderException
import org.apache.ibatis.session.Configuration
import spock.lang.Specification

class WrongMapperSpec extends Specification {

    def "should Fail For Both One And Many"() {
        given: " Configuration "
        Configuration configuration = new Configuration();

        when: " add mapper"
        configuration.addMapper(MapperWithOneAndMany.class)

        then: ""
        thrown(BuilderException.class)
    }

}