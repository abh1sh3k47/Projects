package com.ric4.rmi.validation;

/**
 * 
 * @author abh1sh3k47
 *
 */
public abstract class ClientValidator 
{
	public final boolean validate(ICredentials credentials)
	{
		return isValid(credentials);
	}
	
	public abstract boolean isValid(ICredentials credentials);
}
