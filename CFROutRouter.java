import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.lang.String;
import javax.swing.UIManager;

class Get implements Runnable
	{
	 Tim1 ti=new Tim1();
	 Thread t1;
	 static String instring=" ";
	 static int length=0;
 	 static int length1=0;
	 static String s=" ";
	 static String s1=" ";
	 static String dest=" ";
	 static String sou=" ";
   	/* String*/
	 int intfirst,first;
	 boolean header=false;
 	 long tim;  
	 long  avg_rate;
	 Socket soc;
	 Vector sour=new Vector();
	 Vector des=new Vector();
	 Vector leng=new Vector();
	 Vector virtual_time=new Vector();
	 String instring1="";
	 static TSW ts;
	 static JFrame jf1;
	 static JTextArea jta;
	 static JButton jb1;
	 static JLabel jl1;
	 static String text;
	 static Container cp1;
	 String find,sub1;
	 int fir,sec;
	 static long CT;
	 int last,check;

     Get()
       {

	   }

	 Get(Socket insocket1)
    {
		t1=new Thread(this,"Get");
		soc=insocket1;
		t1.start();
       }

	 public void dis_Sec_Frame()
          {
	       jf1 =new JFrame("Details");
	       jb1=new JButton("Close");
	       jta=new JTextArea();
	       jta.setEnabled(false);
	       JScrollPane jsp=new JScrollPane(jta);
	       cp1=jf1.getContentPane();
	       cp1.setLayout(null);
	       cp1.add(jb1);
	       jb1.addActionListener(new AL());
	       cp1.add(jsp);
	       jsp.setBounds(20,5,450,375);
	       jb1.setBounds(150,400,150,50);
	       jf1.setBounds(25,25,500,500);
          }
     class AL implements ActionListener
         {
          public void actionPerformed(ActionEvent e)
               {
	   	        if(e.getSource()==jb1)
                 {
			      jf1.setVisible(false);
			     }
               }
	      }

     public void show()
          {
		   jf1.setVisible(true);
		  }

     public void add()
          {
		   text+="Source :"+sou+"\nMessage :"+s.trim()+"\nDestination :"+dest+"\n";
		   jta.setText(text);
		  }

	 public void run()
	     {
          try
		    {
			 ObjectInputStream ois=new ObjectInputStream(soc.getInputStream());
		     instring=(String) ois.readObject();
		     length=instring.length();
		     header=true;
		     CT=ti.calculateTime();
		     int fin=instring.indexOf('|');
			 if(fin>0)
			 {
				int last=instring.lastIndexOf('|');
			 	fir=instring.indexOf('|');
			 	sec=instring.indexOf('|',fir+1);
			 	int thi=instring.indexOf('|',sec+1);
			 	if(thi<0)
			 	{
					Thread.sleep(10000);
					jta.append("---------------------------------------------------------------\n");
				 	jta.append(instring+"\n");
			 		jta.append("---------------------------------------------------------------"+"\n");
			 		Back b=new Back(instring,fir,sec,this);
				}
				if(thi>0)
			 	{
					Thread.sleep(10000);
					jta.append("---------------------------------------------------------------\n");
				jta.append(instring);
					jta.append("\n---------------------------------------------------------------"+"\n");
					instring1=instring.substring(0,sec)+"|";
					long LT=Long.parseLong(instring.substring(last+1,instring.length()));
					tim=(CT-LT);
					ts=new TSW(tim);
					avg_rate=length/tim;
					find=instring.substring(sec,thi+1);
			 		if(find.equals("|f|"))
		 		{
					String sub=instring.substring(thi+1,instring.length());
						do
						{
							int beg=sub.indexOf('~');
							if(beg>0)
							{
								intfirst=sub.indexOf('~',beg+1);
								check=sub.indexOf('|',first+1);
								if(check<0)
								{
									last=sub.length();
								}
								else
								{
									last=check;
								}
								sour.add(sub.substring(0,beg));
								des.add(sub.substring(beg+1,first));												leng.add(sub.substring(first+1,last));
											            instring1+=sub.substring(0,beg)+"~"+sub.substring(beg+1,first)+"~"+sub.substring(first+1,last)+"|";
								if(check>0)
								{
									sub=sub.substring(check+1,sub.length());
								}
								if(!leng.isEmpty())
								{
				long tim1=(Long.parseLong((leng.get(leng.size()-1)).toString()))*tim;
							virtual_time.add(tim1+"");									instring1+=virtual_time.get(virtual_time.size()-1)+"|";								}
							}
							else
							{
								check=-1;
							}
						}while(check>0);
						Back b=new Back(instring1,fir,sec,this);
			 		}
					else
					{
					}
			 	}

		 	 }
			 else
			 {
			 	header=false;
			 }
 		 if(length>0 && (!header))
		     {
				try
				{
					ts.TimeSlide(length,CT);
				}
				catch(Exception e){e.printStackTrace();}
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
			   Socket soc1=new Socket(dest,7711);
			   ObjectOutputStream oos=new ObjectOutputStream(soc1.getOutputStream());
			   oos.writeObject(instring);
			   oos.flush();
		      }
		  	}
		  catch(ClassNotFoundException e)
		      {	  }
		  catch(IOException e1)
		      {	  }
		  catch(Exception e)
		  	  {	  }
 	     }
  }




class Fir_Frame extends Get implements Runnable,ActionListener
    {
 String Monlab="Monitoring";
     int count=0;
     Thread t3;
     JFrame jf;
     Container cp;
     JPanel jp;
     JLabel jl;
     JButton jb;
     String inf="com.sun.java.swing.plaf.motif.MotifLookAndFeel";
     Fir_Frame()
       {
		dis_Fir_Frame();
		dis_Sec_Frame();
		t3=new Thread(this,"Mon");
	    t3.start();
	   }
    public void dis_Fir_Frame()
         {
          jf =new JFrame("Monitoring");
	      jl=new JLabel(Monlab);
          jb=new JButton("View Details");
          cp=jf.getContentPane();
          jl.setFont(new Font("Times-Roman",Font.BOLD,50));
          cp.setLayout(null);
	      cp.add(jl);
		  cp.add(jb);
		  jb.addActionListener(this);
		  jl.setBounds(60,5,500,500);
          jb.setBounds(150,350,150,50);
          jf.setVisible(true);
          jf.setBounds(25,25,500,500);
          try
		  {
		  	UIManager.setLookAndFeel(inf);
		  	SwingUtilities.updateComponentTreeUI(jf);
		  }
		  catch(Exception e)
		  {
		  	e.printStackTrace();
		  }
	     }
     public void actionPerformed(ActionEvent e)
          {
           if(e.getSource()==jb)
            { 
		 show();
		    }
          }
     public void run()
          {
           while(true)
               {
		Monlab=Monlab+".";
                jl.setText(Monlab);
                count++;
                if(count==11)
                 {
                  Monlab="Monitoring";
                  count=0;
                 }
                else
                 {
                 try
                    {
				     t3.sleep(500);
                    }
                  catch(InterruptedException e)
                    {
						e.printStackTrace();
                    }
                 }
              }
         }
    }


class OutRouter
    {
     public static void main(String args[]) throws IOException,InterruptedException
	  {
	   System.out.println("*********************Out Router********************************************");
	   try
	     {
	      ServerSocket socket = new ServerSocket(7700);
	      Fir_Frame ff=new Fir_Frame();
	      while(true)
	          {
			   System.out.println("waiting");
		   Socket insocket1 = socket.accept();
	           System.out.println("connected");
	           Get g1=new Get(insocket1);
	          }
	     }
	   catch(UnknownHostException e)
	     {
     System.out.println("UHE");
	     }
   }
    }
