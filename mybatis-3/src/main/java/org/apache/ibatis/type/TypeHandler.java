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
package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  类型处理接口
 * @author Clinton Begin
 */
public interface TypeHandler<T> {


  /**
   * 处理输入参数到SQL语句参数的映射
   * @param ps
   * @param i
   * @param parameter
   * @param jdbcType
   * @throws SQLException
   */
  void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

  /**
   * Gets the result.
   * 处理返回结果,通过字段名
   * @param rs
   *          the rs
   * @param columnName
   *          Column name, when configuration <code>useColumnLabel</code> is <code>false</code>
   * @return the result
   * @throws SQLException
   *           the SQL exception
   */
  T getResult(ResultSet rs, String columnName) throws SQLException;

  /**
   * 处理返回结果 通过字段顺序
   * @param rs
   * @param columnIndex
   * @return
   * @throws SQLException
   */
  T getResult(ResultSet rs, int columnIndex) throws SQLException;

  /**
   *  处理返回结果，根据字段顺序.用于处理 RDBMSs结果
   * @param cs
   * @param columnIndex
   * @return
   * @throws SQLException
   */
  T getResult(CallableStatement cs, int columnIndex) throws SQLException;

}
