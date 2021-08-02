package org.apache.ibatis.type


import spock.lang.Specification

import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData

class BaseTypeHandlerSpec extends Specification {

    protected ResultSet rs;

    protected PreparedStatement ps;

    protected CallableStatement cs;

    protected ResultSetMetaData rsmd;

   def  setup() {
        rs = Mock()
        ps = Mock()
        cs = Mock()
        rsmd = Mock()
    }
}