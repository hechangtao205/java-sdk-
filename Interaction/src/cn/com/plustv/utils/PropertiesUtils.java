package cn.com.plustv.utils;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import cn.com.plustv.InteractionCode;

public class PropertiesUtils {  
      
    private static Properties properties=new Properties();  
    private static Logger logger = Logger.getLogger(PropertiesUtils.class);  
    static{  
        try {  
        	InputStream fis = new FileInputStream(System.getProperty("user.dir") +"/conf/env.properties");
            //注意属性配置文件所在的路径  
            properties.load(fis);  
        } catch (Exception e) {  
        	logger.error(e.getMessage());  
        }  
    }  
      
    //读取属性配置文件中的某个属性对应的值  
    public static String readProperty(String property){  
        return (String) properties.get(property);  
    }  
          
}  