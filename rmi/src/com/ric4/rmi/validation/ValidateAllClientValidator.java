package com.ric4.rmi.validation;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class ValidateAllClientValidator extends ClientValidator 
{
	@Override
	public boolean isValid(ICredentials credentials) {
		return true;
	}

}
