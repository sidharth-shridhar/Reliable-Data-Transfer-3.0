

import java.io.*;
import java.net.*;
import java.util.*;


public class network {
	// list of thread
	static List<Object> Msgs = new ArrayList<Object>();
	static ServerSocket serverSocket;
	//main
	public static void main(String[] args) throws IOException {
		int port;
    
    	if (args.length != 1)
        {
            System.err.println("Incomplete arguments!");
            System.exit(1);
        }
        
        port = Integer.parseInt(args[0]);
        
        try {
			serverSocket = new ServerSocket(port);
			System.out.println("Waiting... connect receiver");
			
				new msgThread(serverSocket.accept()).start();
				new msgThread(serverSocket.accept()).start();
			
		} catch (Exception e) {
			System.out.println("ERR: " + e.getMessage());
			
		}
    }	


public static class msgThread extends Thread {
	private Socket socket = null;
	msgThread mt = null;
	int id = 0;
	
	public msgThread(Socket socket) {
		this.socket = socket;
		Msgs.add(this);
		id = Msgs.size() - 1;
	}
	
	public void run() {
		String inp = "";
		String ACK0 = "ACK0";
		String ACK1 = "ACK1";
		String ACK2 = "ACK2";
		
		try {
			PrintWriter pw = null;
			BufferedReader bin = null;
			pw = new PrintWriter(socket.getOutputStream(), true);
			bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	
			System.out.println("Get connection from: " + socket.getRemoteSocketAddress().toString());
			
			while ((inp = bin.readLine()) != null) {
				if (inp.equals("-1")) {
				
					if (id == 1) {
						pass("-1");
					}
					break;
				}

				String[] splitMsg = inp.split("\\s+");
				// Randomizer
				double x = Math.random();
			
				if (x < 0.5 || splitMsg.length == 1) // PASS
				{
					
					if (splitMsg[0].contains("ACK")) {
						System.out.println("Received: " + splitMsg[0] + ", PASS");
					}
					else {
						System.out.println("Received: ACK" + splitMsg[0] + ", PASS");
					}
					pass(inp);
				}
				// for packet corruption
				else if (x >= 0.5 && x <= 0.75) // CORRUPT
				{
					
					packet p = new packet();
					p.parseMsg(inp);
					p.corruptCS();
					System.out.println("Received: Packet" + splitMsg[0] + ", " + splitMsg[1] + ", CORRUPT");
					pass(p.genMsg());
				}
				
				else // DROP
				{
					System.out.println("Received: Packet" + splitMsg[0] + ", " + splitMsg[1] + ", DROP");
					pw.println(ACK2);
				}
		   }
;
		   socket.close();
		}
		catch (IOException e) {
				System.out.println("ERR: " + e.getMessage());
		}
	}
	
	// Send message
	public void send(String pMessage) {
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter(socket.getOutputStream(), true);
			
			pw.println(pMessage);	
		}
		catch (IOException e) {
				e.printStackTrace();
		}
	}
	public void pass(String pMessage) {
		if (id == 0) {
			mt = (msgThread)Msgs.get(1);
		}
		else {
			mt = (msgThread)Msgs.get(0);
		}
		// Send opposite thread
		mt.send(pMessage);
	}
}//eo msgThread class

}//eo network class