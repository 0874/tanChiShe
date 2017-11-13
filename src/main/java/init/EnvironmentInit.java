package init;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

public class EnvironmentInit {
//    private static final String projectRootPath = EnvironmentInit.class.getResource("/").getPath();
    static {
        System.out.println("启动log4j."+new Date());
        Properties properties = new Properties();
//        System.out.println(projectRootPath);
        try {
            properties.load(new InputStreamReader(new FileInputStream("config/log4j.properties"),
                    "UTF-8"));
        } catch (IOException e) {
            System.out.println("找不到log4j配置："+e.getMessage());
        }
        PropertyConfigurator.configure(properties);
    }
    @Test
    public  void t2(){
        Logger.getLogger(this.getClass()).info("hello log4j");
    }
}
