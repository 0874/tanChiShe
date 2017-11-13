package main;
//解压jar到指定目录【http://blog.csdn.net/qiyueqinglian/article/details/46638043】unzip *.jar -d path
import init.EnvironmentInit;
import model.MyPoint;
import org.apache.log4j.Logger;
import org.junit.Test;
import view.MyWindow;
//项目打包规则【https://www.cnblogs.com/Anders888/p/5757412.html】
/*资源文件，配置文件不与class文件一起打包
*而是在打包成jar后，在同级目录放置其他文件，这个时候这一级目录就等同于开发时的项目根目录
* */
import java.util.ArrayList;
import java.util.Date;
//import java.util.logging.Logger;

public class Main {
    public static final Logger log = Logger.getLogger(Main.class.getName());
    @Test
    public void t(){
        System.out.println("hello world :"+new Date());
    }
    public static void main(String []args){
        //初始化日志
        new EnvironmentInit().t2();
        log.info("log4j init finished");
        //加载数据文件
        log.info("加载数据文件");
        //加载资源文件
        log.info("加载资源文件");
        //启动窗口
        log.info("start window");
        try{
            log.info("启动window");
            MyWindow.getWindow();
        }catch (ExceptionInInitializerError exceptionInInitializerError){
            log.info("sorry,the system cannot support gui.:"+exceptionInInitializerError.getLocalizedMessage());
        }finally {
            log.info("main方法语句完毕");
        }
    }
    //测试以下arraylist操作
    @Test
    public void testArray(){
        ArrayList<MyPoint> arrayList = new ArrayList<MyPoint>();
        arrayList.add(0,new MyPoint());
        System.out.println(arrayList.toString());
    }
}
