import java.io.*;
import java.util.*;

public class Config {
	private String serverIP;
	private int serverPort;
	
	public Config() {
		//Default Settings Information
		serverIP="localhost";
		serverPort=1234;
		
		try {
			//Read server IP address and server port number from file
			Properties properties=new Properties();
			properties.load(new FileInputStream("src/server_info.dat"));
			
			if(properties.containsKey("serverIP")) {
				serverIP=properties.getProperty("serverIP");
			}
			
			if(properties.containsKey("serverPort")) {
				serverPort=Integer.parseInt(properties.getProperty("serverPort"));
			}
			
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public String getServerIP() { //Returns server IP address
		return serverIP;
	}
	
	public int getServerPort() { //Returns server port number
		return serverPort;
	}
}
