package com.ric4.rmi;

import java.net.Socket;

/**
 * 
 * @author abh1sh3k47
 *
 */
public interface IConnectionResetCallback 
{
	public void connectionReset(Socket s);
}
