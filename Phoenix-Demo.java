package hbase_demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

public class PhoenixReadDemo {
	public static void main(String[] args) throws SQLException {

		//phoenix connection properties
		Statement phoenixStmt = null;
		ResultSet phoenixRs = null;
		Connection phoenixConn = null;

		try {
		  //hbase-site.xml must be in classpath
			System.setProperty("java.security.krb5.realm", "GS.COM");
			System.setProperty("java.security.krb5.kdc", "kdcmaster.is.gs.com");
			UserGroupInformation.setConfiguration(new Configuration());
			UserGroupInformation.loginUserFromKeytab("zhenba@GS.COM", "H:/workspaces/sandbox_workspace/hadoop_demo/src/main/resources/zhenba.keytab");

			Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
			phoenixConn = DriverManager.getConnection("jdbc:phoenix:d179663-001.dc.gs.com,d179663-002.dc.gs.com,d179663-003.dc.gs.com:2181");
			System.out.println("Connected");
			
			phoenixStmt = phoenixConn.createStatement();
			phoenixRs = phoenixStmt.executeQuery("select * from load_history");
			
			StringBuilder row;
			ResultSetMetaData meta = phoenixRs.getMetaData();
			while(phoenixRs.next()){
				row = new StringBuilder();
				for(int i = 1; i<= meta.getColumnCount(); ++i){
					row.append(phoenixRs.getString(i)).append(",");
				}
				System.out.println(row.toString());
			}
			
			//drop table
			phoenixStmt.execute("drop table load_history2");
			phoenixConn.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			phoenixRs.close();
			phoenixStmt.close();
			phoenixConn.close();
		}
	}
}
