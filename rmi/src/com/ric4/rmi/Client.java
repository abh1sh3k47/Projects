package com.ric4.rmi;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.ric4.rmi.validation.ICredentials;
import com.ric4.rmi.validation.ValidateAllClientValidator;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class Client 
{
	private final Map<Class,Object> serviceContainer = new HashMap<Class,Object>();
	private final Map<Class,RmiInvocationHandler> invocationHandlerMap = new HashMap<Class,RmiInvocationHandler>();
	private Socket serverSocket;
	private ConnectionWatcher watcher;	

	public Client(String hostname,int port, ICredentials credentials)
	{
		try
		{
			serverSocket = new Socket(hostname,port);
			IConnectionResetCallback callback = new IConnectionResetCallback() {
				@Override
				public void connectionReset(Socket s) {	
					System.err.println("Server "+s.getLocalAddress()+" Went Away!!");
				}
			};
			watcher = new ConnectionWatcher(serverSocket,serviceContainer,new ValidateAllClientValidator(),credentials,callback);
			watcher.start();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public Client(String hostname,int port)
	{
		this(hostname,port,null);
	}

	public <K> K getService(final Class<K> c) throws IOException
	{
		return watcher.getService(c);
	}
	
	public void registerService(Class serviceClass, Object service)
	{
		serviceContainer.put(serviceClass, service);
	}
}
