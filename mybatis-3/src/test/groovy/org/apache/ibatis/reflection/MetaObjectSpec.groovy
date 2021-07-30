package org.apache.ibatis.reflection

import org.apache.ibatis.domain.blog.Author
import org.apache.ibatis.domain.blog.Section
import org.apache.ibatis.domain.misc.RichType
import spock.lang.*

@Title("测试 MetaObject ")
@Narrative(""" 使用spock 重写 MetaObjectTest   获取或修改MetaObject  属性""")
@Subject(MetaClass)
@Unroll
class MetaObjectSpec extends Specification {


    def " should Get And Set Field or NestedField   #original #field  "() {
        given: " a object  and MetaObject from it  "
        MetaObject metaObject = SystemMetaObject.forObject(original)

        when: " set value for MetaObject "
        metaObject.setValue(field, value)

        then: " MetaObject get value should equals to value "
        value == metaObject.getValue(field)

        where: " object and field are  "
        original       | field                   | value
        new RichType() | 'richField'             | 'foo'
        new RichType() | 'richType.richField'    | 'foo'
        new RichType() | 'richProperty'          | 'foo'
        new RichType() | 'richType.richProperty' | 'foo'
        new RichType() | 'richType.richMap[key]' | 'foo'
        new RichType() | 'richList[0]'           | 'foo'
        new RichType() | 'richType.richList[0]'  | 'foo'
        new RichType() | 'richType.richProperty' | 'foo'
        new RichType() | 'richType.richProperty' | null
        new Author()   | 'email'                 | 'test'
    }

    def " should Verify Has Readable Properties Returned By GetReadable PropertyNames  #readable  "() {

        when: " check has getter "
        boolean hasGetter = metaObject.hasSetter(readable)

        then: "hasGetter for property  should be true "
        hasGetter


        where: " readable Properties  are  "
        metaObject = SystemMetaObject.forObject(new Author())
        readable << SystemMetaObject.forObject(new Author()).getGetterNames()

    }

    def " should Verify Has Writeable Properties Returned By GetWriteable PropertyNames  #writeable  "() {

        when: " check has getter "
        boolean hasSetter = metaObject.hasSetter(writeable)

        then: "hasGetter for property  should be true "
        hasSetter


        where: " readable Properties  are  "
        metaObject = SystemMetaObject.forObject(new Author())
        writeable << SystemMetaObject.forObject(new Author()).getSetterNames()

    }


    def " should Verify Property Types  #original #field   "() {
        given: " a object  and MetaObject from it  "
        MetaObject metaObject = SystemMetaObject.forObject(original)

        when: " get getter type of field "

        Class fieldType = metaObject.getGetterType(field)

        then: " getter type should be "
        type == fieldType

        where: " object and field are  "
        original     | field              || type
        new Author() | 'id'               || int.class
        new Author() | 'username'         || String.class
        new Author() | 'password'         || String.class
        new Author() | 'email'            || String.class
        new Author() | 'bio'              || String.class
        new Author() | 'favouriteSection' || Section.class
    }

    def " should Demonstrate Deeply Nested Map Properties  #original #field   "() {
        given: " a  hashmap object  and MetaObject from it  "
        HashMap<String, String> map = new HashMap<>();
        MetaObject metaObject = SystemMetaObject.forObject(map)

        when: " set value"
        keyValue.forEach({ item ->
            metaObject.setValue(item[0], item[1])
        })

        then: " value should be set "
        keyValue[0][1] == metaObject.getValue(keyValue[0][0])
        keyValue[1][1] == metaObject.getValue(keyValue[1][0])
        keyValue[2][1] == metaObject.getValue(keyValue[2][0])
        keyValue[3][1] == metaObject.getValue(keyValue[3][0])
        keyValue[4][1] == metaObject.getValue(keyValue[4][0])
        keyValue[5][1] == metaObject.getValue(keyValue[5][0])
        keyValue[6][1] == metaObject.getValue(keyValue[6][0])

        where: " object and field are  "
        sort | keyValue
        1    | [['id', '100'], ['name.first', 'Clinton'], ['name.last', 'Begin'], ['address.street', '1 Some Street'], ['address.city', 'This City'], ['address.province', 'A Province'], ['address.postal_code', '1A3 4B6']]

    }


}