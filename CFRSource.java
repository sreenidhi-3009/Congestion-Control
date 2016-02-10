import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

class SouFrame implements ActionListener
    {
    //source end of the network
     static JButton jb1;
     static JButton jb2;
     static JLabel jl1,jl2;
     static JTextField jtf;
     static JTextArea jtf1;
     static JScrollPane jsp;
     static JFrame jf;
     String dest_addr=" ";
     String Msg=" ";
     String conc=" ";
     String fin=" ";
     int st=0;
     int end=48;
     int len1=0;
     Socket soc;
     int split=0;
     InetAddress in1=InetAddress.getLocalHost();

     SouFrame() throws IOException
	 {
	 	CreateFrame();
 	 }

 	 public void CreateFrame()
	 {
		jf=new JFrame("Source");
	 	Container cp=jf.getContentPane();
		jl1=new JLabel("Destination Machine Name :");
		jtf=new JTextField();
		cp.setBackground(Color.pink);
		jl2=new JLabel("Type   the  Message              :");
		jtf1=new JTextArea(10,10);
		jb1=new JButton("SEND");
		jb2=new JButton("CLEAR");
		jsp=new JScrollPane(jtf1);
		cp.setLayout(null);
		cp.add(jl1);
		cp.add(jtf);
		cp.add(jl2);
		cp.add(jsp);
		cp.add(jb1);
		jl1.setForeground(Color.blue);
		jl2.setForeground(Color.blue);
		jb1.setBorder(BorderFactory.createEtchedBorder(Color.yellow,Color.red));
		jtf.setBorder(BorderFactory.createEtchedBorder(Color.black,Color.black));
		jsp.setBorder(BorderFactory.createEtchedBorder(Color.black,Color.black));
		jb1.addActionListener(this);
		cp.add(jb2);
		jb2.setBorder(BorderFactory.createEtchedBorder(Color.yellow,Color.red));
		jb2.addActionListener(this);
		jl1.setBounds(5,50,195,25);
		jtf.setBounds(165,50,200,25);
		jl2.setBounds(5,100,195,25);
		jsp.setBounds(165,100,200,200);
		jb1.setBounds(150,325,75,25);
		jb2.setBounds(250,325,75,25);
		jf.setVisible(true);
		jf.setBounds(100,100,400,400);
		jf.validate();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==jb1)
	    {
			try
			{
				SendPacket();
		    }
		    catch(IOException e1)
		    {
				JOptionPane.showMessageDialog((Component) null,"InRouter Machine is Not Ready To Data Transfer","Click OK",JOptionPane.ERROR_MESSAGE);
			}
		}
		else
	    {
			jtf.setText("");
			jtf1.setText("");
       	}
    }

	public void  SendPacket() throws IOException
	{
		try
	 	{
	    	dest_addr=jtf.getText();
		    Msg=jtf1.getText();
		    if(((dest_addr.trim()).length())>0)
		    {
			  	if(((Msg.trim()).length())>0)
			  	{
	 	      		System.out.println("**********************"+jtf.getText()+"****************");
	 	      		soc=new Socket("localhost",7788);
	  	      		OutputStream out  = soc.getOutputStream();
	 	      		st=0;
	 	      		end=48;
	 	      		conc=dest_addr+"`"+in1.getHostName()+"`";
	 	      		byte buffer[]=Msg.getBytes();
	 	      		int len=buffer.length;
	 	      		len1=len;
	 	      		if(len<=48)
	 	        	{
	 		       		 fin=conc+Msg+"\n"+"null";
	 		       		 byte buffer1[]=fin.getBytes();
	 		      		 out.write(buffer1);
	 		      	}
	 		  		else
	 		  		{
						fin=conc+Msg.substring(st,end)+"\n";
	 		  			byte buffer2[]=fin.getBytes();
	 		  		 	out.write(buffer2);
	 		  		 	Thread.sleep(1000);
	 		  		 	while(len1>48)
	 		    	  	{
	    			  		len1-=48;
	 					    if(len1<=48)
	 			    	    {
						  													fin=conc+Msg.substring(end,len)+"\n"+"null";
	 			    	   	byte buffer3[]=fin.getBytes();
	 			    	   	out.write(buffer3);
	 			    	   	Thread.sleep(1000);
	 			    	    }
	 						else
	 			    	    {
						    	split=end+48;
	 			    	        fin=conc+Msg.substring(end,split)+"\n";
	 			    	        end=split;
	 			    	        byte buffer5[]=fin.getBytes();
	 			    	        out.write(buffer5);
	 			    	        Thread.sleep(1000);
	 			    	    }
	 			    	}
	 			   	}
				}
				else
				{
					 JOptionPane.showMessageDialog((Component) null,"There Is No Message To Send","Click OK",JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else
			{
				JOptionPane.showMessageDialog((Component) null,"Destination Machine Name is Missing","Click OK",JOptionPane.INFORMATION_MESSAGE);
			}
	    }
	 	catch(UnknownHostException e)
	 	{
			JOptionPane.showMessageDialog((Component) null,"Check the InRouter Machine Name","Click OK",JOptionPane.INFORMATION_MESSAGE);
		}
	    catch(InterruptedException e1)
	    {
		}
	}
}

class CFRSource
    {
     public static void main(String args[])throws IOException
          {
           SouFrame sf=new SouFrame();
          }
     }
