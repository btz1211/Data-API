package hbase_demo;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.security.UserGroupInformation;

public class HBaseDemo {
	public static void main(String[] arg) throws IOException {
		System.setProperty("java.security.krb5.realm", "GS.COM");
		System.setProperty("java.security.krb5.kdc", "kdcmaster.is.gs.com");

    //must have hbase-site.xml in classpath
    Configuration config = HBaseConfiguration.create();
    config.set("hadoop.security.authentication", "Kerberos");
		UserGroupInformation.setConfiguration(config);
		UserGroupInformation.loginUserFromKeytab("zhenba@GS.COM", "H:/workspaces/sandbox_workspace/hbase_demo/src/main/resources/zhenba.keytab");
		System.out.println("connection established");
		
        HTable testTable = new HTable(config, "zhenba_test");
        Scan scan = new Scan();
        ResultScanner scanner = testTable.getScanner(scan);
        for (Result result = scanner.next(); result != null; result = scanner.next())
            System.out.println("Found row : " + result);
       
        scanner.close();
        testTable.close();
        System.out.println("connection closed");
    }
}
