import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

public class HadoopDemo {
  public static void main(String[] args){
  		Configuration conf = new Configuration();
  		try {
      		System.setProperty("java.security.krb5.realm", "realm i.e 'your.firm.com'"); //set kerberos ream
    			System.setProperty("java.security.krb5.kdc", "your key-distribution-cetner (or TGS)"); //set kdc 
    			UserGroupInformation.setConfiguration(conf);
    			UserGroupInformation.loginUserFromKeytab("[principal]@[realm]", "/path/to/your/keytab/keytab.kt");
    			FileSystem fs = FileSystem.get(conf);
    			Path file = new Path("test.txt");
    
    			if(fs.exists(file)){
    				fs.delete(file);
    			}
    			FSDataOutputStream out = fs.create(file);
    			out.writeUTF("hello world");
    			out.close();
    
    			FSDataInputStream in = fs.open(file);
    			String messageIn = in.readUTF();
    			System.out.print(messageIn);
    			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
