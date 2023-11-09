import java.io.*;
import java.net.*;
import java.util.*;

public class CalcClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Config config=new Config();
		String serverIP=config.getServerIP(); //Server IP address settings
		int serverPort=config.getServerPort(); //Server port number settings
		
		try {
			Socket socket=new Socket(serverIP, serverPort); //Request to connect to server
			
			//Create input/output stream
			BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			Scanner scanner=new Scanner(System.in);
			
			while(true) {
				System.out.println("Please enter the calculation formula (enter with spaces, ex) 10 + 20 ): ");
				String inputMessage=scanner.nextLine(); //Receive formula from the user
				
				if(inputMessage.equalsIgnoreCase("bye")) {
					out.write(inputMessage); //Send data to server
					out.flush();
					break;
				}
				
				String[] parts1=inputMessage.split(" ");
				
				//Converting an expression to protocol message
				if(parts1.length!=3) {
					out.write("ARG"+"\n"); //Send data to server
					out.flush();
				}
				else {
					String operand1=parts1[0];
					String operator=parts1[1];
					String operand2=parts1[2];
					
					String outputMessage;
					
					switch(operator) {
					    case "+":
					    	outputMessage="ADD "+operand1+" "+operand2;
					    	break;
					    case "-":
					    	outputMessage="SUB "+operand1+" "+operand2;
					    	break;
					    case "*":
					    	outputMessage="MUL "+operand1+" "+operand2;
					    	break;
					    case "/":
					    	outputMessage="DIV "+operand1+" "+operand2;
					    	break;
					    default:
					    	outputMessage="OPER "+operand1+" "+operand2;
					}
					
					out.write(outputMessage+"\n"); //Send data to server
					out.flush();
				}
				
				String resultMessage=in.readLine(); //Receive data from server
				String[] parts2=resultMessage.split(" ");
				
				//Converting protocol messages to calculated results
				String para1=parts2[0];
				String para2=parts2[1];
				
				String result;
				
				switch(para1) {
				    case "ANS":
				    	result="Answer: "+para2;
				    	break;
				    case "ERRARG":
				    	result="Error message: too many arguments";
				    	break;
				    case "ERRDIV":
				    	result="Error message: divided by zero";
				    	break;
				    case "ERROPER":
				    	result="Error message: invalid operation";
				    	break;
				    default:
				    	result="Error message";
				}
				
				System.out.println(result); //Outputting Calculation Results
			}
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
