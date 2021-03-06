package com.ric4.rmi.test;

import com.ric4.rmi.Server;
import com.ric4.rmi.validation.ClientValidator;
import com.ric4.rmi.validation.ICredentials;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class ServerRunner 
{

	public static void main(String[] args) throws Exception 
	{
		Server s = new Server(27015,new ClientValidator() {
			@Override
			public boolean isValid(ICredentials credentials) {
				System.out.println("Credentials "+credentials);
				if(credentials != null)
					return true;
				return false;
			}
		});
		
		s.registerService(IFactorialService.class, new FactorialService());
		s.registerService(IDuplicateService.class, new DuplucateService());
	}


	public static class FactorialService implements IFactorialService
	{
		@Override
		public Integer getFactorial(Integer i)
		{
			if(i<0) throw new NumberFormatException();

			Integer fact = 1;
			for(int k=1;k<=i;k++)
			{
				fact = fact*k;
			}
			
			//int z = 0;
			//int p = 100/z;

			/*	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
			return fact;
		}
	}
	
	public static class DuplucateService implements IDuplicateService
	{
		@Override
		public String duplicate(String s) 
		{
			return "DPLICATE["+s+"]";
		}
		
	}
}
