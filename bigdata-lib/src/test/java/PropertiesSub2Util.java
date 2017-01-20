import com.zhbwang.bigdata.lib.PropertiesTool;

/**
 * Created by wangzb on 2017-1-18.
 */
public class PropertiesSub2Util{

    private static PropertiesTool propertiesTool = new PropertiesTool("conf/conf2.properties");

    public static String getValue(String key){
        return propertiesTool.getValue(key);
    }
    public static int getInt(String key){
        return propertiesTool.getInt(key);
    }


    public static void main(String[] args){
        System.out.println(PropertiesSubUtil.getValue("user.dir"));
        System.out.println(PropertiesSub2Util.getValue("user.dir2"));
    }
}
