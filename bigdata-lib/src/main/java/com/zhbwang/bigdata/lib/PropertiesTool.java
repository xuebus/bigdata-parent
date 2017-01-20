package com.zhbwang.bigdata.lib;


import com.zhbwang.bigdata.lib.tools.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class PropertiesTool {

	private Properties properties = null; //初始化属性  采用单例模式
	private InputStream is = null;

	public PropertiesTool(String filePath){
		properties = new Properties();
		try {
			LogUtil.info("开始加载配置文件: {0}", filePath);
			URL fileURL = PropertiesTool.class.getClassLoader().getResource(filePath);
			if (fileURL == null) {
				LogUtil.error("Not Found Properties File: {0}", filePath);
			} else {
				String fileURLStr = fileURL.getPath();
				if (fileURLStr.contains("jar")) {
					is = new FileInputStream(System.getProperty("user.dir") + File.separator + filePath);
				} else {
					is = PropertiesTool.class.getClassLoader().getResourceAsStream(filePath);
				}
			}
			properties.load(is);
			LogUtil.info("加载配置文件完成: {0}", filePath);
		} catch(Exception e){
			LogUtil.error("加载配置文件失败", e);

		}
	}

	/**
	 * 单例模式，初始化属性文件加载
	 *
	 * @return
	 */
	private  Properties getProperties() {
		return properties;
	}

	/**
	 * 根据key获取属性值
	 *
	 * @param key
	 * @return
	 */
	public  String getValue(String key) {
		String value = getProperties().getProperty(key);
		if (value!=null && !"".equalsIgnoreCase(value)) {
			return value;
		}
		return ""; //若属性无值或者无相关属性key，则返回空串
	}

	/**
	 * 根据key获取Int属性值
	 *
	 * @param key
	 * @return
	 */
	public  int getInt(String key) {
		try {
			String value = getProperties().getProperty(key);
			if (value!=null && !"".equalsIgnoreCase(value)) {
				return Integer.parseInt(value);
			}
		} catch (Exception e) {
			return -1;
		}
		return -1; //若属性无值或者无相关属性key，则返回空串
	}


	public static void main(String[] args) {
		LogUtil.info(new PropertiesTool("conf/conf.properties").getValue("user.dir"));
	}
}
