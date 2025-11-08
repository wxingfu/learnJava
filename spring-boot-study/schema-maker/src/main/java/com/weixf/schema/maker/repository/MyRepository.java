package com.weixf.schema.maker.repository;

import com.weixf.schema.maker.utility.CommonUtils;
import com.weixf.schema.maker.utility.DataCollection;
import com.weixf.schema.maker.utility.FDate;
import com.weixf.schema.maker.utility.StringUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @since 2022-06-17
 */
@Repository
public class MyRepository {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private FDate fDate;

    public Connection getConnection() {
        DataSource dataSource = jdbcTemplate.getDataSource();
        try {
            if (dataSource != null) {
                return dataSource.getConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getOneValue(String sql) {
        List<String> resultList = new ArrayList<>();
        jdbcTemplate.query(sql, rs -> {
            resultList.add(rs.getString(1));
        });
        return resultList.get(0);
    }

    public DataCollection execSQL(String sql) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        SqlRowSetMetaData sqlRowSetMetaData = sqlRowSet.getMetaData();
        int columnCount = sqlRowSetMetaData.getColumnCount();
        DataCollection tSSRS = new DataCollection(columnCount);
        while (sqlRowSet.next()) {
            for (int j = 1; j <= columnCount; j++) {
                tSSRS.SetText(getDataValue(sqlRowSetMetaData, sqlRowSet, j));
            }
        }
        return tSSRS;
    }

    public String getDataValue(SqlRowSetMetaData sqlRowSetMetaData, SqlRowSet sqlRowSet, int i) {
        String strValue = "";
        int dataType = sqlRowSetMetaData.getColumnType(i);
        int dataScale = sqlRowSetMetaData.getScale(i);
        int dataPrecision = sqlRowSetMetaData.getPrecision(i);
        // 数据类型为字符
        if ((dataType == Types.CHAR)
                || (dataType == Types.VARCHAR)) {
            strValue = sqlRowSet.getString(i);
        }
        // 数据类型为日期、时间
        else if ((dataType == Types.TIMESTAMP)
                || (dataType == Types.DATE)) {
            strValue = fDate.getString(sqlRowSet.getDate(i));
        }
        // 数据类型为浮点
        else if ((dataType == Types.DECIMAL)
                || (dataType == Types.FLOAT)) {
            // 采用下面的方法使得数据输出的时候不会产生科学计数法样式
            strValue = String.valueOf(sqlRowSet.getBigDecimal(i));
            // 去零处理
            strValue = CommonUtils.getInt(strValue);
        }
        // 数据类型为整型
        else if ((dataType == Types.INTEGER)
                || (dataType == Types.SMALLINT)
                || dataType == Types.TINYINT) {
            strValue = String.valueOf(sqlRowSet.getInt(i));
            strValue = CommonUtils.getInt(strValue);
        }
        // 数据类型为浮点
        else if (dataType == Types.NUMERIC) {
            if (dataScale == 0) {
                if (dataPrecision == 0) {
                    // 采用下面的方法使得数据输出的时候不会产生科学计数法样式
                    strValue = String.valueOf(sqlRowSet.getBigDecimal(i));
                } else {
                    strValue = String.valueOf(sqlRowSet.getLong(i));
                }
            } else {
                // 采用下面的方法使得数据输出的时候不会产生科学计数法样式
                strValue = String.valueOf(sqlRowSet.getBigDecimal(i));
            }
            strValue = CommonUtils.getInt(strValue);
        }
        return StringUtil.cTrim(strValue);
    }
}
