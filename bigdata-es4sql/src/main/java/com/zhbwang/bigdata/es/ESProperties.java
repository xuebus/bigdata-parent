package com.zhbwang.bigdata.es;

import com.zhbwang.bigdata.lib.PropertiesTool;

/**
 * Created by wangzb on 2017-1-18.
 */
public class ESProperties {

    private static PropertiesTool propertiesTool = new PropertiesTool("conf.properties");

    public static String getValue(String key){
        return propertiesTool.getValue(key);
    }
    public static int getInt(String key){
        return propertiesTool.getInt(key);
    }


    public static void main(String[] args){
        System.out.println(ESProperties.getValue("es.cluster.hosts"));
    }
}
