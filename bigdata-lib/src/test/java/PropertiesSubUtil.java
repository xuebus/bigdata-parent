import com.zhbwang.bigdata.lib.PropertiesTool;

/**
 * Created by wangzb on 2017-1-18.
 */
public class PropertiesSubUtil{

    private static final String filePath = "conf/conf.properties";

    private static PropertiesTool propertiesTool =null;
    static{
        propertiesTool = new PropertiesTool(filePath);
    }
    public static String getValue(String key){
        return propertiesTool.getValue(key);
    }
    public static int getInt(String key){
        return propertiesTool.getInt(key);
    }




    public static void main(String[] args){
        System.out.println(PropertiesSubUtil.getValue("user.dir"));
    }
}
