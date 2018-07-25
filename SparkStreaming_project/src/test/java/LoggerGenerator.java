import org.apache.log4j.Logger;

/**
 * log4j集成Flume采集日志
 */
public class LoggerGenerator {

    public static Logger logger=Logger.getLogger(LoggerGenerator.class.getName());

    public static void main(String[] args) throws Exception{
        int index=0;
        while(true){
            Thread.sleep(1000);
            logger.info("value "+index);
            index++;
        }
    }
}
