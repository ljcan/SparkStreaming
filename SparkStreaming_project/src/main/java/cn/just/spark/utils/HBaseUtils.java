package cn.just.spark.utils;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * JAVA操作HBase工具类，使用单例模式编写
 */
public class HBaseUtils {
    private HBaseAdmin hBaseAdmin=null;
    private Configuration configuration=null;

    private static HBaseUtils instance=null;

    private HBaseUtils(){
        configuration=new Configuration();
        configuration.set("hbase.rootdir","hdfs://hadoop-senior04.shinelon.com:8020/user/shinelon/hbase");
        configuration.set("hbase.zookeeper.quorum","hadoop-senior04.shinelon.com");
        try {
            hBaseAdmin = new HBaseAdmin(configuration);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static synchronized HBaseUtils getInstance(){
        if(instance==null){
            instance=new HBaseUtils();
        }
        return instance;
    }

    /**
     * 获取表名
     * @param tableName
     * @return
     */
    public HTable getTable(String tableName){
        HTable table=null;
        try {
        table=new HTable(configuration,tableName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return table;
    }

    /**
     * 向表中写入数据
     * @param tableName     表名
     * @param rowKey        rowkey
     * @param cf            列簇
     * @param column        列名
     * @param value         列的值
     */
    public void put(String tableName,String rowKey,String cf,String column,String value){
        HTable table=getTable(tableName);
        Put put=new Put(Bytes.toBytes(rowKey));

        put.add(Bytes.toBytes(cf),Bytes.toBytes(column),Bytes.toBytes(value));
        try{
        table.put(put);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
       HTable table= HBaseUtils.getInstance().getTable("course_clickcount");

        System.out.println(table.getName().getNameAsString());
    }


}
