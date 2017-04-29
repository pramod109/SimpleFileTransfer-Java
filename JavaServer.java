
import java.net.*;
import java.io.*;
import java.util.*;

public class JavaServer{
	
	public static void main(String args[]) throws Exception
	{
		ServerSocket serversock = new ServerSocket(1025);
		while(true)
		{
			System.out.println("Waiting for Connection...");
			Socket sock = serversock.accept();
			System.out.println("\nAccepted connection : " + sock);
			FileTransfer ftp = new FileTransfer(sock);
		}
	}
}

class FileTransfer extends Thread {
                                                          
	public final static int FILE_SIZE1 = 6022386;
	DataInputStream in;
	DataOutputStream out;
	Socket socket;
	OutputStream os;
	InputStream is1;
	/*OutputStream os;
    ServerSocket servsock;
    Socket sock;*/
	FileTransfer(Socket sock)
	{
		try
		{	socket=sock;
			in = new DataInputStream(sock.getInputStream());
			out = new DataOutputStream(sock.getOutputStream());
			start();		
		}catch(Exception ex)
		{
		
		}
	}
	void send() throws Exception
	{
		
		String File_Name = in.readUTF();
		System.out.println("Searching file name" + File_Name);
		File myFile = new File(File_Name);
		byte [] mybytearray  = new byte [(int)myFile.length()];
		FileInputStream fis = new FileInputStream(myFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		bis.read(mybytearray,0,mybytearray.length);
		os = socket.getOutputStream();
		
		System.out.println("\nSending " + File_Name + "(" + mybytearray.length + " bytes)");
		//out.writeInt((int)myFile.length());
		os.write(mybytearray,0,mybytearray.length);
		os.flush();
		System.out.println("\nSending file to client is complete!");
			  
		bis.close();
		fis.close();
		os.close();
		}

			 
	void receive() throws Exception
	{	   
	
		//System.out.println("Entered receive function");
		int bytesRead;
		int current = 0;
		String File_Name = in.readUTF();
		byte [] mybytearray  = new byte [FILE_SIZE1];
		is1 = socket.getInputStream();
		FileOutputStream fos = new FileOutputStream(File_Name);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		bytesRead = is1.read(mybytearray,0,mybytearray.length);
		current = bytesRead;
	
	do {
			bytesRead = is1.read(mybytearray, current, (mybytearray.length-current));
			if(bytesRead >= 0) current += bytesRead;
		}   while(bytesRead > -1);

	bos.write(mybytearray, 0 , current);
	bos.flush();
	System.out.println("\nFile " + File_Name + " downloaded (" + current + " bytes read)");
		  
	fos.close();
	bos.close();
		}  
	public void run()
	{
		while(true)
		{
			try
			{
				System.out.println("Awaiting request......");
				String Request = in.readUTF();
				switch(Request){
					case "send": 	System.out.println("Download request received");
									send();
									break;
					case "receive":	receive();
									break;
					case "delete":	delete();
									break;
					case "rename":	rename();
									break;
					default:
									break;
				}
			}catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}
		}
	}
	void delete() throws Exception{
		String File_Name = in.readUTF();
		File myFile = new File(File_Name);
		myFile.delete();
		out.writeUTF("File deleted");
	}
	void rename()throws Exception{
		String File_Name = in.readUTF();
		File myFile = new File(File_Name);
		out.writeUTF("Please give a new name: ");
		String name = in.readUTF();
		File file2= new File(name);
		myFile.renameTo(file2);
		out.writeUTF("File renamed");
	}
}
