package com.zhbwang.bigdata.es.es4sql;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ElasticSearchDruidDataSourceFactory;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by allwefantasy on 8/26/16.
 */
public class JDBCTests {
    @Test
    public void testJDBC() throws Exception {
        Properties properties = new Properties();
        properties.put("url", "jdbc:elasticsearch://127.0.0.1:9300/" + TestsConstants.TEST_INDEX);
        DruidDataSource dds = (DruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
        Connection connection = dds.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT  gender,lastname,age from  " + TestsConstants.TEST_INDEX + " where lastname='Heath'");
        ResultSet resultSet = ps.executeQuery();
        List<String> result = new ArrayList<String>();
        while (resultSet.next()) {
            result.add(resultSet.getString("lastname") + "," + resultSet.getInt("age") + "," + resultSet.getString("gender"));
        }

        ps.close();
        connection.close();
        dds.close();

        Assert.assertTrue(result.size()==2);
        Assert.assertTrue(result.get(0).equals("Heath,39,F"));
        Assert.assertTrue(result.get(1).equals("Heath,39,F"));
    }

}


