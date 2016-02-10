import javax.swing.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import javax.swing.UIManager;

class Router_Frame
{
	JFrame Ing_fra;
	Container cp1;
	JTextArea Ing_data;
	JTextField InTxt,OutTxt;
	JLabel InPac,OutPac;
	static String instring="";
	static int length=0;
	static int length1=0;
	static String s="";
	static String s1="";
	String fin="";
	static String dest="";
	static String sou="";
	static int inp=0,outp=0;
	JScrollPane jsp;
	ObjectInputStream in;
	OutputStream out;
	int readcnt=0;
	boolean header=false;
	static String text;
	static String text1;
	char chstr[]=new char[512];
	Socket soc;
	String inf="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

	Router_Frame()
	{

	}

	public void dis_ing_fra()
	{
		Ing_fra=new JFrame("Router");
		cp1=Ing_fra.getContentPane();
		cp1.setLayout(null);
		Ing_data=new JTextArea();
		InPac=new JLabel("INCOMING PACKETS :");
		OutPac=new JLabel("OUTGOING PACKETS :");
		InTxt=new JTextField();
		OutTxt=new JTextField();
		InTxt.setText("0");
		OutTxt.setText("0");
		InPac.setForeground(Color.blue);
		OutPac.setForeground(Color.blue);
		Ing_data.setEditable(false);
		InTxt.setEditable(false);
		OutTxt.setEditable(false);
		Ing_data.setBorder(BorderFactory.createEtchedBorder(Color.black,Color.black));
		InTxt.setBorder(BorderFactory.createEtchedBorder(Color.black,Color.black));
		OutTxt.setBorder(BorderFactory.createEtchedBorder(Color.black,Color.black));
		jsp=new JScrollPane(Ing_data);
		cp1.add(jsp);
		cp1.add(InPac);
		cp1.add(InTxt);
		cp1.add(OutPac);
		cp1.add(OutTxt);
		jsp.setBounds(5,5,470,400);
		InPac.setBounds(5,425,130,25);
		InTxt.setBounds(125,425,75,25);
		OutPac.setBounds(250,425,150,25);
		OutTxt.setBounds(375,425,75,25);
		Ing_fra.setBounds(5,5,500,500);
		Ing_fra.setVisible(true);
		Ing_fra.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}

	public void dis_ing_data()
	{
		try
		{
			System.out.println("Waiting...");
			ServerSocket ss=new ServerSocket(7799);
			while(true)
			{
				soc=ss.accept();
				System.out.println("connected...");
				display();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void add()
	{
		text+="Source :"+sou+"\nMessage :"+s.trim()+"\nDestination :"+dest+"\n";
    	Ing_data.setText(text);
    }

	public void display()
	{
		try
		{
			ObjectInputStream ois=new ObjectInputStream(soc.getInputStream());
		    instring=(String) ois.readObject();
		    length=instring.length();
		    int fin=instring.indexOf('|');
		    if(fin>0)
		    {
				int last=instring.lastIndexOf('|');
				header=true;
				Ing_data.append("---------------------------------------------------------------\n");
				Ing_data.append(instring.substring(0,last)+"\n");
				Ing_data.append("---------------------------------------------------------------"+"\n");
				Socket soc1=new Socket("localhost",7700);
			ObjectOutputStream oos=new ObjectOutputStream(soc1.getOutputStream());				oos.writeObject(instring);

			}
			else
			{
				header=false;
			}
		    if(length>0 && (!header))
		    {
				Socket soc1=new Socket("192.168.1.67",7700);
		   	ObjectOutputStream oos=new ObjectOutputStream(soc1.getOutputStream());
			   	oos.writeObject(instring);
			   	oos.flush();
			   	outp++;
				OutTxt.setText(outp+"");
		       	for(int l=0;l<length;l++)
			   	{
			    	if((instring.charAt(l))=='`')
			 	    {
			 	    	dest=instring.substring(0,l);
			 	    	s1=instring.substring(l+1,length);
			 	    	l=length+1;
			 	    	length1=s1.length();
		 	   	}
			    }
			   	for(int l=0;l<length1;l++)
			    {
			    	if((s1.charAt(l))=='`')
			        {
			        	sou=s1.substring(0,l);
		        	if((s1.substring((s1.length()-4),s1.length())).equals("null"))
			        	{
			       			s=s1.substring(l+1,length1-4);
						}
						else
						{
							s=s1.substring(l+1,length1);
						}
			       		l=length1+1;
			       	}
			    }
			   	add();
 		   	inp++;
				InTxt.setText(inp+"");
		      } 
	}
		
		catch(Exception e1)
		{
		e1.printStackTrace();
		}
	}
}

class CFRRouter
{
	public static void main(String args[])
	{
		Router_Frame obj=new Router_Frame();
		obj.dis_ing_fra();
		Back b=new Back(obj);
		obj.dis_ing_data();
	}
}

