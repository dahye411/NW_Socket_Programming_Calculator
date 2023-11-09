import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class CalcServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Config config=new Config();
		int port=config.getServerPort(); //Port number setting
		
		try {
			ServerSocket listener=new ServerSocket(port); //Create server socket
			ExecutorService pool=Executors.newFixedThreadPool(20); //Create a thread pool that can process up to 20 tasks simultaneously => multi thread
			
			while(true) {
				Socket sock=listener.accept(); //Waiting for connection request from client
				pool.execute(new Calc(sock)); //Executes run() of Calc class in thread
			}
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static class Calc implements Runnable{
		private Socket socket;
		
		Calc(Socket socket){
			this.socket=socket;
		}
		
		public void run() {
			try {
				//Create input/output stream
				BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				
				while(true) {
					String inputMessage=in.readLine(); //Receive data from client
					
					if(inputMessage.equalsIgnoreCase("bye")) {
						break;
					}
					
					String[] parts=inputMessage.split(" ");
					
					//Calculate with data
					if(parts.length!=3) {
						out.write("ERRARG Argument error"+"\n"); //Send data to client
						out.flush();
					}
					else {
						String operation=parts[0];
						int operand1=Integer.parseInt(parts[1]);
						int operand2=Integer.parseInt(parts[2]);
						
						int result;
						String outputMessage;
						
						switch(operation) { 
						    case "ADD":
						    	result=operand1+operand2;
						    	outputMessage="ANS "+result;
						    	break;
						    case "SUB":
						    	result=operand1-operand2;
						    	outputMessage="ANS "+result;
						    	break;
						    case "MUL":
						    	result=operand1*operand2;
						    	outputMessage="ANS "+result;
						    	break;
						    case "DIV":
						    	if(operand2==0) {
						    		outputMessage="ERRDIV Division error";
						    	}
						    	else {
						    		result=operand1/operand2;
						    		outputMessage="ANS "+result;
						    	}
						    	break;
						    default:
						    	outputMessage="ERROPER Operation error";
						}
						
						out.write(outputMessage+"\n"); //Send data to client
						out.flush();
					}
				}
			}catch(IOException e) {
				System.out.println(e.getMessage());
			}finally {
				try {
					if(socket!=null) {
						socket.close(); //Close socket
					}
				}catch(IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
}
