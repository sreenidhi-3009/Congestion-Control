import java.io.*;
import java.net.*;

class Back extends Thread
{	Router_Frame rf;
	ServerSocket ss; 
	Back(Router_Frame obj1)
	{
		rf=obj1;
		start();
	}

	public void run()
	{
		try
		{
			ss=new ServerSocket(888);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		while(true)
		{
			try
		    {
			Socket soc=ss.accept();
			ObjectInputStream ois=new ObjectInputStream(soc.getInputStream());
		    	String instring=(String) ois.readObject();
		    	rf.Ing_data.append("---------------------------------------------------------------\n");
		    	rf.Ing_data.append(instring+"\n");
		    	rf.Ing_data.append("---------------------------------------------------------------"+"\n");
		    	Socket soc1=new Socket("localhost",777);
			ObjectOutputStream oos=new ObjectOutputStream(soc1.getOutputStream());
			oos.writeObject(instring);
		    	Thread.sleep(1000);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
