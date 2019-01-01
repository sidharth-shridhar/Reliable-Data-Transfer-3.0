import java.net.*;
import java.io.*;

public class sender {
	static String host;
	static int port;
	static String fn;
	
	public static void main(String[] args) throws IOException {
		if (args.length != 3) 
        {
            System.out.println("Incomplete arguments");
            System.exit(0);
        }

        host = args[0];
        port = Integer.parseInt(args[1]);
        fn = args[2];
        Socket socket = null;
		PrintWriter pw = null;
		BufferedReader bin = null;
		 
		try {
			socket = new Socket(host, port);
			pw = new PrintWriter(socket.getOutputStream(), true);
			bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String ack;
			try {
					FileInputStream fs = new FileInputStream(fn);
					DataInputStream din = new DataInputStream(fs);
					BufferedReader read = new BufferedReader(new InputStreamReader(din));
		
					String inp;
		
					int count = 0;
					String msg = "";
					
					// create  packet object
					packet pkt = new packet();
					while ((inp = read.readLine()) != null) {
						String[] split = inp.split("\\s+");
						int i = 0;
						while (i < split.length) {
							// Create the message
							pkt.createPacket(split[i]);
							// Generate the message 
							msg = pkt.genMsg();
							// Send message to network
							pw.println(msg);
							ack = bin.readLine();
							
							count++;
							
							if (ack.equals("ACK2")) { // DROP
								System.out.println("Waiting: " + ack+ ", " + count + ", DROP, resend Packet" + pkt.sno);
							}
							else if (checker(ack, pkt.sno) == true) { // PASS
								i++;
								System.out.println("Waiting: " + ack + ", " + count + ", " + ack + ", no more packets to send");
							}
							else { // CORRUPT
								System.out.println("Waiting: " + ack + ", " + count + ", " + ack + ", send Packet" + pkt.sno);
							}
						}
						pw.println(-1);
				}

				din.close();
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
				}
		 }catch (Exception e) {
			System.out.println("Something went wrong! ERR: " + e.getMessage());
			
		}
		finally {
			pw.close();
			bin.close();
			socket.close();
		}
	
}
public static boolean  checker(String ackNet, Integer sno) {
    	String ackn = "ACK" + sno.toString();
    	return ackn.equals(ackNet);
    }
}
    