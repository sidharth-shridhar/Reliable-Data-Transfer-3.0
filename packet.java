import java.net.*;
import java.io.*;

public class packet {
	Integer sno;
	Integer pid;
	Integer cs;
	public String cont;
	public boolean flag = false;
	
	public packet() {
		sno = 1;
		pid = 0;
	}
	
	public void createPacket(String pcont) {
		cont = pcont;
		getSequenceNum();
		cs = genCS(cont);
		pid++;
	}
	
	public String genMsg() {
		return sno + " " + pid + " " + cs + " " + cont;
	}

	public Integer genCS(String s) {
		int num;
		int sum = 0;
		for (int i = 0; i < s.length(); i++) {
			num = (int) s.charAt(i);
			sum = sum + num;
			if ((int) s.charAt(i) == 46) {
				flag = true;
			}
		}
		return sum;
    }
    
    // Parse the message and break up by spaces
    public void parseMsg(String pcont) {
    	String[] splited = pcont.split("\\s+");
    	for (int i = 0; i < splited.length; i++) { 
    		sno = Integer.parseInt(splited[0]);
    		pid = Integer.parseInt(splited[1]);
    		cs = Integer.parseInt(splited[2]);
    		cont = splited[3];
    	}
    }
    
    // Corrupt the checksum by adding 1
    public void corruptCS() {
    	cs = cs + 1;
    }
    
    // Check the checksum to see whether or not it is corrupt
    public String VM() {
    	Integer newCS = genCS(cont);
    	if (newCS.equals(cs)) {
    		return "ACK" + sno.toString();
    	}
    	else {
	    		if (sno == 0) {
					sno = 1;
				}
				else {
					sno = 0;
				}
    		return "ACK" + sno.toString();
    	}
    }
    
    // Alternates the 0 and 1
    public void getSequenceNum() {
    	if (sno == 0) {
			sno = 1;
		}
		else {
			sno = 0;
		}
    }
	
}