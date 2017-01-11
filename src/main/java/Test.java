import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/8.
 */
public class Test {

    public static void main(String[] args){
        String[] strs = {"123123SSDFDS","SSS","DDD","EEE","DDSD"};
        String[] strsLowercase = getLowerCaseArray(strs);
        System.out.println("原始数组数据为：");
        for (String str : strs){
            System.out.println(str);
        }
        System.out.println("转化后数组数据为：");
        for (Object str : strsLowercase){
            System.out.println(str);
        }
    }
    public static String[] getLowerCaseArray(String[] strs){
        List<String> list = new ArrayList<String>();
        for (String str : strs){
            list.add(str.toLowerCase());
        }
        String[] strings=new String[list.size()];
        for(int i=0,j=list.size();i<j;i++){
            strings[i]=list.get(i);
        }
        return strings;
    }
}
