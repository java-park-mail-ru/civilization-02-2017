import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import java.io.InputStream;
import java.util.Properties;

public abstract class AbstractTestCase {
    protected static Properties properties;
    protected static String localAddress;
    //init properties
    static {
        initProperties();
    }
    private static void initProperties(){
        properties = new Properties();
        try(InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties")) {
            properties.load(resourceStream);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        localAddress = "localhost" + properties.getProperty("server.port");
    }
}