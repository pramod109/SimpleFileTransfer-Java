
import java.io.*;
import java.net.*;
import java.util.*;

public class JavaClient1 {
                            
	public final static String SERVER = "127.0.0.1";              
	public final static int FILE_SIZE = 6022386;   

	DataOutputStream out;
	DataInputStream in;
	OutputStream os;
	InputStream is1;
	Socket sock;
	public Scanner is = new Scanner(System.in);
												
													
	public static void main (String [] args ) throws IOException {
	JavaClient1 b = new JavaClient1();
	b.connect();
	}
	void connect()
	{
	try
	{
			
		System.out.println("Please enter the address of the server");
		String address = is.nextLine();
		System.out.println("Connecting to Server....");
		sock = new Socket(address,1025);
		System.out.println("Socket Created");
		in = new DataInputStream(sock.getInputStream());
        out = new DataOutputStream(sock.getOutputStream());
		while(true)
		{			
			System.out.println("Please select a file operation");
			System.out.println("1. Upload");
			System.out.println("2. Download");
			System.out.println("3. Delete");
			System.out.println("4. Rename");
			System.out.println("Enter your choice: ");
			int option = is.nextInt();
			String temp = is.nextLine();
			switch(option)
			{
				case 1:		
					System.out.println("Option 1 selected");
					out.writeUTF("receive");
					send();			
					break;
				case 2:
					out.writeUTF("send");
					receive();
					break;
				case 3:
					out.writeUTF("delete");
					delete();
					break;
				case 4:
					out.writeUTF("rename");
					rename();
					break;
				case 5:
					out.writeUTF("disconnect");
					sock.close();
				default:
					break;
			}
		}	
	}catch (Exception ex){System.out.println(ex.getMessage());}

	}	  
				
void receive()throws Exception
{
	Scanner temp = new Scanner(System.in);
	String File_Name;				
	int bytesRead;
	int current = 0;
	System.out.println("Enter the file to be downloaded: ");
	File_Name = is.nextLine();
	System.out.println("Downloading.....");
	out.writeUTF(File_Name); 
	byte [] mybytearray  = new byte [FILE_SIZE];
	is1 = sock.getInputStream();
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
		  		  
void send()throws Exception
	{  
		Scanner temp = new Scanner(System.in);
		int bytesRead;
		int current = 0;
		System.out.println("Please enter the name of the file to be uploaded: ");
		String File_Name = is.nextLine();
		System.out.println("Uploading.....");
		out.writeUTF(File_Name);
		File myFile = new File (File_Name);
		byte [] mybytearray  = new byte [(int)myFile.length()];
		FileInputStream fis = new FileInputStream(myFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		bis.read(mybytearray,0,mybytearray.length);
		os= sock.getOutputStream();
		System.out.println("\nSending " + File_Name + "(" + mybytearray.length + " bytes) to server");
		os.write(mybytearray,0,mybytearray.length);
		System.out.println("File Upload complete");
		os.flush();
				  
		fis.close();
		bis.close();
		os.close();
		}
	void delete() throws Exception
	{	
		System.out.println("Enter the name of the file you want to delete: ");
		Scanner temp = new Scanner(System.in);
		String File_Name = is.nextLine();
		out.writeUTF(File_Name);
		System.out.println(in.readUTF());
		
	}	
	void rename() throws Exception
	{
		System.out.println("Enter the name of the file you want to rename: ");
		Scanner temp = new Scanner(System.in);
		String File_Name = is.nextLine();
		out.writeUTF(File_Name);
		System.out.println(in.readUTF());
		String name = is.nextLine();
		out.writeUTF(name);
		System.out.println(in.readUTF());
		
	}
}
