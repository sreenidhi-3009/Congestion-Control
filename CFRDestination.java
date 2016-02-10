import java.net.*;
import java.io.*;

class CFRDestination
    {
    //this class acts as the destination end of the network
    
     public static void main(String args[]) throws IOException
          {
	       System.out.println("******************************DESTINATION***************************************");
	       String instring=" ";
           String s=" ";
           int length=0;
           String dest=" ";
           try
             {
	          ServerSocket sock1 = new ServerSocket(7711);

	          while(true)
	              {
	               System.out.println("waiting..........");
                   Socket insocket1 = sock1.accept();
                   System.out.println("connected sucessfully.............");
                   try
                     {
                      ObjectInputStream ois=new ObjectInputStream(insocket1.getInputStream());
                      instring=(String) ois.readObject();
		   	         }
			       catch(ClassNotFoundException e)
			         {
					  e.printStackTrace();
					 }
			       length=instring.length();
			       int st=instring.indexOf('`');
			       int end=instring.lastIndexOf('`');
			       dest=instring.substring(st+1,end);
				   if((instring.substring((instring.length()-4),instring.length())).equals("null"))
				   {
			       	s=instring.substring(end+1,length-4);
			   	   }
			   	   else
			   	   {
					s=instring.substring(end+1,length);
				   }
			       byte data[]=s.getBytes();
          	       FileOutputStream fos=new FileOutputStream(dest+".txt",true);
                   fos.write(data);
                   Runtime r = Runtime.getRuntime();
		           Process p = null;
                   p = r.exec("notepad"+" "+dest+".txt");
                  }
             }

           catch(UnknownHostException e)
             {
			  e.printStackTrace();
			 }
          }
    }
