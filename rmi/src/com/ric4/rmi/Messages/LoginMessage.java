package com.ric4.rmi.Messages;

import com.ric4.rmi.validation.ICredentials;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class LoginMessage extends Message
{
	private ICredentials credentials;
	
	public LoginMessage(ICredentials credentials)
	{
		this.credentials = credentials;
	}

	public ICredentials getCredentials() {
		return credentials;
	}

	public void setCredentials(ICredentials credentials) {
		this.credentials = credentials;
	}
	
}
