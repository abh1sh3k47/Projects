package com.ric4.rmi;

import java.net.Socket;

public interface IConnectionResetCallback 
{
	public void connectionReset(Socket s);
}
