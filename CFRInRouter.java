import javax.swing.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.Vector;
import javax.swing.UIManager;

class In_Frame //implements Runnable
{
	JFrame Ing_fra;
	Container cp1;
	int tot;
	static JTextArea Ing_data=new JTextArea();
	JTextField InTxt;
	static JTextField OutTxt=new JTextField();
	JLabel InPac,OutPac;
	String instring="";
	static int length=0;
	static int length1=0;
	static String s="";
	static String s1="";
	static int I=0;
	String fin="";
	static String dest="";
	static String sou="";
	boolean sta=true;
	static int inp=0,outp=0;
	JScrollPane jsp;
	BufferedReader in1;
	OutputStream out;
	int readcnt=0;
	static String text;
	String egg="";
	static String text1;
	static Vector msg=new Vector();
	static Vector len=new Vector();	
	static Vector des=new Vector();	
	static Vector sour=new Vector();
	char chstr[]=new char[512];
	long l;
	Send sen=new Send(this);
	String inf="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

	In_Frame()
	{
	}

	public void dis_ing_fra()
	{
		Ing_fra=new JFrame("In Router");
		cp1=Ing_fra.getContentPane();
		cp1.setLayout(null);
		InPac=new JLabel("INCOMING PACKETS :");
		OutPac=new JLabel("OUTGOING PACKETS :");
		InTxt=new JTextField();
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
		Ing_fra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try
		{
			UIManager.setLookAndFeel(inf);
			SwingUtilities.updateComponentTreeUI(Ing_fra);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		Ing_fra.setVisible(true);
	}

	public void dis_ing_data()
	{
		try
		{
			ServerSocket ss=new ServerSocket(7788);
			while(true)
			{
				System.out.println("waiting");
				Socket soc=ss.accept();
				System.out.println("Connected");
				sta=true;
				while(sta)
				{
			in1= new BufferedReader(new InputStreamReader(soc.getInputStream()));					out=soc.getOutputStream();
					display();
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void add()
	{
		text+="Source :"+sou+"\nMessage :"+s.trim()+"\nDestination :"+dest+"\n";
    	Ing_data.setText(text);
    }

    public void incoming(int in)
    {
		InTxt.setText(in+"");
	}

	public void outgoing(int out)
	{
		OutTxt.setText(out+"");
	}
	public void display()
	{
		try
		{
			while(true)
			{
				readcnt=in1.read(chstr);
				if(readcnt <=0)
			    {
			    	continue;
				}
			    else
			    {
				   	break;
				}
			}
			instring =new String(chstr, 0, readcnt);
	        msg.add(instring);
	        I++;
			if(!instring.endsWith("null"))
			{
				length=instring.length();
			    length1=0;
			    len.add(length+"");

			    for(int l=0;l<length;l++)
			    {
			    	if((instring.charAt(l))=='`')
			        {
			        	dest=instring.substring(0,l);
			        	des.add(dest);
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
				    	sour.add(sou);
				        s=s1.substring(l+1,length1);
				        l=length1+1;
				    }
				}
				add();
				inp++;
				incoming(inp);
		   	}
	      	else
	      	{
				sta=false;
				length=instring.length()-4;
				length1=0;
				len.add(length+"");
				for(int l=0;l<length;l++)
				{
					if((instring.charAt(l))=='`')
			        {
						dest=instring.substring(0,l);
						des.add(dest);
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
						sour.add(sou);
					    s=s1.substring(l+1,length1);
					    l=length1+1;
					}

				}
				add();
				inp++;
				incoming(inp);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

class A extends Thread
{
	In_Frame i;
	A(In_Frame obj1)
	{
		i=obj1;
		java.util.Timer t=new java.util.Timer();
		if((i.msg.size())>0)
		{
			t.schedule(i.sen,10000);
		}
		else
		{
			t.schedule(i.sen,10000,30000);
		}
		try
		{
			Thread.sleep(1000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}

class B extends Thread
{
		In_Frame i1;
		B(In_Frame obj1)
		{
			i1=obj1;
			try
			{
		Leaky l=new Leaky(i1);
				java.util.Timer t1=new java.util.Timer();
				t1.schedule(l,10000,1000);
			}
			catch(Exception e){}
		}
}

class InRouter
{
	public static void main(String args[])
	{
		try
		{
			In_Frame obj=new In_Frame();
			obj.dis_ing_fra();
			//Fin f=new Fin();
			A a=new A(obj);
			Back b=new Back(obj);
			RateControl rc=new RateControl(obj);
			obj.dis_ing_data();
		}
		catch(Exception e){}
	}
}
