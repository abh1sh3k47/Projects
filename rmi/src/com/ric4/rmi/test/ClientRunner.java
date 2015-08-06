package com.ric4.rmi.test;

import java.util.Random;

import com.ric4.rmi.Client;
import com.ric4.rmi.validation.ICredentials;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class ClientRunner {

	public static void main(String... args) throws Exception
	{
		Client c = new Client("localhost",27015,new ICredentials() {});
		IFactorialService ifs = c.getService(IFactorialService.class);
		IDuplicateService ids = c.getService(IDuplicateService.class);

		//System.out.println(ifs.getFactorial(10));

		for(int i=0;i<10;i++)
		{
			Thread t1 = new FactPrinter(ifs);
			t1.start();
		}

		for(int i=0;i<10;i++)
		{
			Thread t1 = new DupPrinter(ids,i);
			//t1.start();
		}
	}

	static class FactPrinter extends Thread
	{
		IFactorialService ifs;
		Random r = new Random(System.currentTimeMillis());
		FactPrinter(IFactorialService ifs)
		{
			this.ifs=ifs;
		}
		@Override
		public void run() {

			for(int i=0;i<1000000;i++)
			{
				int n = r.nextInt(20);
				int fact = ifs.getFactorial(n);
				int myFact = factorial(n);
				if(myFact!=fact)
				{
					System.err.println("Factorial Mismatch for "+n+" "+fact+" should be -"+myFact);
				}
				//System.out.println("Factorial of "+n+" is "+fact);
			}
		}
	}

	static class DupPrinter extends Thread
	{
		IDuplicateService ids;
		Random r;
		int seed;
		DupPrinter(IDuplicateService ids,int i)
		{
			this.ids=ids;
			seed =i;
			r = new Random(seed);
		}
		@Override
		public void run() {
			while(true)
			{
				byte [] bytes = new byte[r.nextInt(50)];
				r.nextBytes(bytes);
				String s = "";
				for(byte b:bytes)
				{
					s += b;
				}

				String dup = ids.duplicate(s);
				if(!dup.contains(s)){
					System.err.println(dup+" MISMATCHED "+s); 
				}
				//System.out.println("Duplicate of '"+s+"' = "+ ids.duplicate(s));
			}
		}
	}

	static int factorial(int i)
	{
		if(i<0) throw new NumberFormatException();

		Integer fact = 1;
		for(int k=1;k<=i;k++)
		{
			fact = fact*k;
		}
		return fact;
	}

}
