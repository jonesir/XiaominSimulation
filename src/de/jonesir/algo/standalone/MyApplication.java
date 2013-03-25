package de.jonesir.algo.standalone;
import java.net.*;
//import java.util.StringTokenizer;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class MyApplication {
	static char CHAT='1', FILE='2'; 
	//defined by the constructor:
	String username;

	//defined internally:
	ReadThread readthread;

	//linking need:
	NetCod_Module netCod;
	
	Vector fileList;
	int fileLength;

	static Window console;
//--------------------------------------------------------------------
//	public MyApplication(String username, NetCod_Module netCod) {
	public MyApplication(String username) {
		readthread = new ReadThread(this);
		this.username = username;
		console=new Window(username);
		fileList = new Vector();
//		this.netCod = netCod;

	}


//--------------------------------------------------------------------
	public synchronized void readData(String str) {
		
		Payload p = new Payload();
		p.Buf.append((char) username.length());
		p.Buf.append(username);
		p.Buf.append((char) str.length());
		p.Buf.append(str);

		int l = p.Buf.length();
		int c = 0;
		for(int i = l ; i < 100; i++) {
			p.Buf.append((char) c);
		}

		send(p);
	}


//--------------------------------------------------------------------
	public synchronized void writeData(Payload p) {
		int j;
		char[] cid = new char[1];
		FileId fileId;
		FileCntr fileCntr;
		byte bb=(byte)p.Buf.charAt(0);
		
		if (p.Buf.charAt(0)==CHAT) {
			byte lname = (byte) p.Buf.charAt(1);

			String s=console.myConsole.getText();

//1			if(lname < 15) {
				int ldata = (int) p.Buf.charAt(lname+2);
				console.myConsole.setText(s+"\n"+p.Buf.substring(2, lname+2) + ": " + p.Buf.substring(lname+3, lname+3+ldata+2));
//				System.out.println(p.Buf.substring(1, lname+1) + ": " + p.Buf.substring(lname+2, lname+2+ldata+1));
//1			} else {
//1				console.myConsole.setText(s+"\n"+p.Buf+"\n");
				//System.out.println(p.Buf);
//1			}
			console.repaint();
			
		}
		if (p.Buf.charAt(0)==FILE) {
			byte lname = (byte) p.Buf.charAt(1);
//			fileId = new FileId(p.Buf.substring(3,3+lname), (int) p.Buf.charAt(2));
			cid[0] = p.Buf.charAt(2);
			fileId = new FileId(p.Buf.substring(3,3+lname), Integer.parseInt(new String(cid)));
			if(fileList.size() > 0) {
				boolean found = false;
				
				for(j = 0; j < fileList.size(); j++) {
					fileCntr = (FileCntr) fileList.elementAt(j);
					if(fileCntr.id.compare(fileId)) {
						found = true;
						if(++(fileCntr.cntr) == fileLength) {
							System.out.println("file No " + fileId.id + " coming from " + fileId.str + " is well received");
						}
						break;
					}
				}
				
				if(!found) {
					fileCntr = new FileCntr(fileId,1);
					fileList.add(fileCntr);
				}
			}else {
				fileCntr = new FileCntr(fileId,1);
				fileList.add(fileCntr);
			}
			
		}
	}


//--------------------------------------------------------------------
	public synchronized void send(Payload p) {

		netCod.sendToTransport(p);

	}


//--------------------------------------------------------------------	
	public synchronized void createFile(int fileLength, int id) {
		
		int i;
		File f = new File("data" + String.valueOf(id) + ".txt"); 
		try{
			PrintWriter out = new PrintWriter(new FileWriter(f));
			
			for( i = 0; i < fileLength; i++) {
				out.println(id + username + " : EPFL" + i);
			}
			out.close();
		}
		catch(IOException e) {
			System.err.println("CreateFile: " + e);
		}
	
	}
		
	
//--------------------------------------------------------------------
	public synchronized void getFile(InetAddress source, int MDU, int id) {
		
		NCdatagram g;
		Coef_Elt coef_elt;
		int l, j, i = 0;
		
		try{
//			BufferedReader in = new BufferedReader(new FileReader("data.txt"));
			BufferedReader in = new BufferedReader(new FileReader("data" + String.valueOf(id) + ".txt"));
			String line;
			id = id*1000;
			while((line = in.readLine()) != null) {
				
//				id = (int) line.charAt(0);
				
				g = new NCdatagram();
				g.Buf.append(FILE);
				g.Buf.append((char) username.length());
				
				g.Buf.append(line);
				l = g.Buf.length();
				
				for(j = l; j < MDU; j++) {
					g.Buf.append((char) 0);
				}
				
				coef_elt = new Coef_Elt();
				coef_elt.coef_ = 1;
//				coef_elt.id_.time_ = System.currentTimeMillis();
				coef_elt.id_.time_ = ++i + id;
//				coef_elt.id_.saddr = source;
				coef_elt.id_.saddr = InetAddress.getLocalHost();
				g.coefs_list.add(coef_elt);
				netCod.ddedBuf.add(g);
			}
		}
		catch(IOException e) {
			System.err.println("CreateFile: " + e);
		}
	}
	
	public static InetAddress isIp4(String s) throws NumberFormatException, UnknownHostException {
		byte[] b = new byte[4];

		try
		{
			StringTokenizer st = new StringTokenizer(s,".");
			int count = 0;
            
			
			while (st.hasMoreTokens())
			{
				count++;
				if (count > 4) {
					throw new NumberFormatException();
		        }
				String quod = st.nextToken();
				int i = Integer.parseInt(quod);
				if (i<0 || i>255) {					
					throw new NumberFormatException();
		        }
				b[count-1]=new Integer(i).byteValue();
			}
		}
		catch (NumberFormatException ex)
		{
			b[0] = new Integer(224).byteValue();
			b[1] = new Integer(224).byteValue();
			b[2] = new Integer(224).byteValue();
			b[3] = new Integer(224).byteValue();
		}
		finally {
			InetAddress a = InetAddress.getByAddress(b);
			return(a);
		}
	}


//	 Tells the applet you will be using the ActionListener methods.

	public class Window extends Frame implements ActionListener 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Button myButton;
		TextArea myConsole;
		TextField myInput;
		
		public Window(String node_id)
		{
			setTitle("Console"+node_id);
			setLayout(new BorderLayout());
			setBackground(Color.orange);

			myConsole = new TextArea(40,80);
			myConsole.setEditable(false);
			add("North", myConsole);
			myInput= new TextField(40);
			add("South", myInput);
			myInput.addActionListener(this);
			pack();
//			show();
		}

		public void actionPerformed(ActionEvent evt) {
	        if (evt.getSource() == myInput){
	        	String str=myInput.getText();
	        	Payload p = new Payload();
	        	p.Buf.append(CHAT);
				p.Buf.append((char) username.length());
				p.Buf.append(username);
				p.Buf.append((char) str.length());
				p.Buf.append(str);

				int l = p.Buf.length();
				int c = 0;
				for(int i = l ; i < 100; i++) {
					p.Buf.append((char) c);
				}
				send(p);
				String s=myConsole.getText();

				myConsole.setText(s+"\n"+username+" : "+str);
				myInput.setText("");
				repaint();
	        }

			
		}

//		public boolean handleEvent(Event evt)
//		{
//			if("Hit This".equals(evt.arg))
//			{
//				System.out.println("He hit me");
//				return true;
//			}
//			return false;
//		}

	}


//--------------------------------------------------------------------
//--------------------------------------------------------------------
	public static void main(String[] args) throws GaloisException, IOException, NumberFormatException{
		String name="Default",dest_address="224.224.224.224",dest_port="60000",role="S"; 
		
		int arg_num=args.length;
		int fileNumber = 1;
		
		
		switch(arg_num)	{
		case 1:
			role=args[0];
			break;
		case 2:
			role=args[0];
			name=args[1];
			break;
		case 3:
			role=args[0];
			name=args[1];
			fileNumber = Integer.parseInt(args[2]);
			break;
		case 4:
			role=args[0];
			name=args[1];
			fileNumber = Integer.parseInt(args[2]);
			dest_address=args[3];
			break;
		case 5:
			role=args[0];
			name=args[1];
			fileNumber = Integer.parseInt(args[2]);
			dest_address=args[3];
			dest_port=args[4];
			break;
		}
		
		MyApplication application = new MyApplication(name);

			
		console.myConsole.setText("Role:"+role+"\n"+"User Name:"+name+"\n"
				+"Destination Adress:"+dest_address+"\n"+"Destination Port:"
				+dest_port);
//		System.out.println();
//		System.out.println();
//		System.out.println("Destination Adress:"+dest_address);
//		System.out.println("Destination Port:"+dest_port);
	

//----------Constant Value :------------------------------------------	
		int port = Integer.parseInt(dest_port); 
		
		int MDU  = 100; //bytes
		int MCLU = 10;  //NCdatagram in a combination
		int HCL = 2*MCLU; // Hard Coefficient Limit
		//int maxPktLength = MDU + 10*MCLU*Coef_Elt.coef_size + 1;
		int maxPktLength = MDU + HCL*Coef_Elt.coef_size + 1;
		int fileLength=50;
		//the 1 indicates the byte that carries the size of the coefs_list
		short fresh_fwd_factor = 1;
//		byte[] b=new byte[4];
//	    b[0]=new Integer(192).byteValue();
//	    b[1]=new Integer(168).byteValue();
//	    b[2]=new Integer(1).byteValue();
//	    b[3]=new Integer(5).byteValue();
//	    InetAddress source1=InetAddress.getByAddress(b);
	    InetAddress source=InetAddress.getLocalHost();
		InetAddress destination=isIp4(dest_address);
//		InetAddress source = InetAddress.getByName("192.168.1.111");
//	    b[0]=new Integer(10).byteValue();
//	    b[1]=new Integer(0).byteValue();
//	    b[2]=new Integer(1).byteValue();
//	    b[3]=new Integer(111).byteValue();
//	    InetAddress destination=InetAddress.getByAddress(b);
//      InetAddress destination = InetAddress.getByName("192.168.1.255");
		long rate = 50; //ms: the time between sending 2 pkts
//		System.out.println(args[0]);

		console.setVisible(true);

//----------defining all objects:-------------------------------------
		NetCod_Module netCod = new NetCod_Module(MDU, MCLU, HCL, port);
		NetCodAdapter adapter = new NetCodAdapter(MDU);
		SocketInterface sInt;
		sInt = new SocketInterface(port, maxPktLength,destination);


//----------defining object parameters:-------------------------------
		//MyApplication:------------------------------------------
		application.fileLength = fileLength;

		//NetCod_Module:------------------------------------------
		netCod.source = source;
		netCod.destination = destination;
		netCod.fresh_fwd_factor = fresh_fwd_factor;
		netCod.fileLength = fileLength;
		

		//NetCodAdapter:------------------------------------------
		adapter.rate = rate;

		//SocketInterface:----------------------------------------


//----------linking all objects:--------------------------------------
		//MyApplication:------------------------------------------
		application.netCod = netCod;


		//NetCod_Module:------------------------------------------
		netCod.application = application;
		netCod.readthread = application.readthread;


		//NetCodAdapter:------------------------------------------
		adapter.netCod = netCod;
		adapter.sInt = sInt;


		//SocketInterface:----------------------------------------
		sInt.adapter = adapter;


//----------activate the application:---------------------------------
		//MyApplication:------------------------------------------
//		application.readthread.start();
		console.setVisible(true);
		if(role.charAt(0) == 'S') {
			for(int id = 0; id < fileNumber; id++) {
				application.createFile(fileLength,id);
				application.getFile(source, MDU,id);
			}
			
		}
		

		//SocketInterface:----------------------------------------
		sInt.recvFromPhy();		
		

		//NetCodAdapter:------------------------------------------
		adapter.run();

		
		//NetCod_Module:------------------------------------------
		netCod.time = System.currentTimeMillis();
		
		
//		MyApplication:------------------------------------------
//		application.readthread.start();

	}

}


//--------------------------------------------------------------------	
class FileId {
	int id;
	String str;
	
	public FileId(String str, int id) {
		this.str = str;
		this.id = id;
	}
	
	public boolean compare(FileId fileId) {
		if((id == fileId.id) & str.equals(fileId.str) ) {
			return true;
		}
		return false;
	}
}


//--------------------------------------------------------------------
class FileCntr{
	FileId id;
	int cntr;
	
	public FileCntr(FileId id, int cntr) {
		this.id = id;
		this.cntr = cntr;
	}
}
