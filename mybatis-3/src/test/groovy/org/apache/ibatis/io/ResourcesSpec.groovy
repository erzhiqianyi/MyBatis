package org.apache.ibatis.io


import spock.lang.*

@Title("测试Resources")
@Narrative(""" 使用spock 重写 ResourcesTest ,用于读取资源  """)
@Subject(ClassLoaderWrapper)
@Unroll
class ResourcesSpec extends Specification {

    def " should Get Url For Resource"() {
        when: " get Resource URL"

        URL url = Resources.getResourceURL(resource);

        then: " url should end with "
        url.toString().endsWith(target)

        where: " resource are"
        resource                                                            || target
        'org/apache/ibatis/databases/jpetstore/jpetstore-hsqldb.properties' || 'jpetstore/jpetstore-hsqldb.properties'
    }

    def "should Get Url As Properties"() {
        when: " get Resource URL"
        URL url = Resources.getResourceURL(resource);

        and: "get Url As Properties "
        Properties props = Resources.getUrlAsProperties(url.toString())

        and: " get property"
        String propertyValue = props.getProperty(property)

        then: " property should be  "
        propertyValue == value
        where: " resource are"
        resource                                                            | property || value
        'org/apache/ibatis/databases/jpetstore/jpetstore-hsqldb.properties' | 'driver' || 'org.hsqldb.jdbcDriver'
    }

    def "should Get Resource As Properties"() {
        when: "get  Resource  As Properties "
        Properties props = Resources.getResourceAsProperties(resource)

        and: " get property"
        String propertyValue = props.getProperty(property)

        then: " property should be  "
        propertyValue == value
        where: " resource are"
        resource                                                            | property || value
        'org/apache/ibatis/databases/jpetstore/jpetstore-hsqldb.properties' | 'driver' || 'org.hsqldb.jdbcDriver'
    }

    def "should Get Url As Stream"() {
        when: "  getResourceURL "
        URL url = Resources.getResourceURL(resource)

        and: " get url as stream "
        InputStream inputStream = Resources.getUrlAsStream(url.toString())

        then: " property should be  "
        inputStream != null
        where: " resource are"
        resource = 'org/apache/ibatis/databases/jpetstore/jpetstore-hsqldb.properties'
    }

    def "should Get Url As Reader"() {
        when: "  getResourceURL "
        URL url = Resources.getResourceURL(resource)

        and: " get url as reader "
        Reader reader = Resources.getUrlAsReader(url.toString())

        then: " property should be  "
        reader != null

        where: " resource are"
        resource = 'org/apache/ibatis/databases/jpetstore/jpetstore-hsqldb.properties'
    }

    def "should Get Resource As Stream"() {
        when: "get  Resource  As Properties "
        InputStream inputStream = Resources.getResourceAsStream(resource)

        then: " inputStream should not be null "
        inputStream != null

        where: " resource are"
        resource = 'org/apache/ibatis/databases/jpetstore/jpetstore-hsqldb.properties'
    }

    def "should Get Resource As Reader"() {
        when: "get  Resource  As Properties "
        Reader reader = Resources.getResourceAsReader(resource)

        then: " reader should not be null "
        reader != null

        where: " resource are"
        resource = 'org/apache/ibatis/databases/jpetstore/jpetstore-hsqldb.properties'
    }


    def "should Get Resource As File "() {
        when: "get  Resource  As Properties "
        File file = Resources.getResourceAsFile(resource)

        then: " file should not be null "
        file != null

        and: " file should end with "
        file.getAbsolutePath().endsWith(fileName)

        where: " resource are"
        resource                                                            || fileName
        'org/apache/ibatis/databases/jpetstore/jpetstore-hsqldb.properties' || 'jpetstore-hsqldb.properties'
    }

}