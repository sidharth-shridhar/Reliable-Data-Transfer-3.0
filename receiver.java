import java.net.*;
import java.io.*;

public class receiver {
	static String ip;
	static int port;

	public static void main(String[] a) throws IOException {
		if (a.length != 2) 
        {
            System.out.println("Incomplete arguments <missing host add or port");
            System.exit(0);
        }

        String host = a[0];
        int port = Integer.parseInt(a[1]);
 		Socket socket=null;
 		PrintWriter pw=null;
 		BufferedReader bin=null;
			try {
			 socket = new Socket(host, port);
			 pw = new PrintWriter(socket.getOutputStream(), true);
			 bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					
			String inp, out;
			out = "";
			int count = 0;
			String msg = "";
			System.out.println("Waiting to connect sender...");

			// Create packet object
			packet pkt = new packet();
			while ((inp = bin.readLine()) != null) {
				// Terminate receiver
				if (inp.equals("-1")) {
					pw.println(-1);
					break;
				}

				pkt.parseMsg(inp);
				msg = msg + pkt.cont + " ";
				count++;
				out = "Waiting " + pkt.sno + ", " + count + ", " + inp + ", " + pkt.VM();
				System.out.println(out);
				pw.println(pkt.VM());
				if (pkt.flag == true) {
					System.out.println("Message: " + msg);
				}
			}
			
		} catch (IOException e) {
			System.out.println("Something went wrong! ERR:  " + e.getMessage());
			System.exit(0);
		}
		finally {
			pw.close();
			bin.close();
			socket.close();
		}
		
	}
    
  
}