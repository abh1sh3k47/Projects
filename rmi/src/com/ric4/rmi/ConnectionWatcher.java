package com.ric4.rmi;

/**
 * 
 * @author abh1sh3k47
 *
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ric4.rmi.Messages.LoginMessage;
import com.ric4.rmi.Messages.Message;
import com.ric4.rmi.Messages.MethodCallMessage;
import com.ric4.rmi.Messages.ReturnMessage;
import com.ric4.rmi.validation.ClientValidator;
import com.ric4.rmi.validation.ICredentials;

class ConnectionWatcher extends Thread
{
	private final ObjectInputStream is;
	private final ObjectOutputStream os;
	private final Socket clientSocket;
	private final Map<Class,Object> serviceContainer;
	private final Map<Class,RmiInvocationHandler> invocationHandlerMap;
	private final IConnectionResetCallback connResetCallback;
	private final ClientValidator clientValidator;
	private final ICredentials clientCredentials;

	private static final Message message = new Message();

	ConnectionWatcher(Socket clientSocket,Map<Class,Object> serviceContainer, ClientValidator clientValidator, ICredentials clientCredentials, IConnectionResetCallback callback) throws IOException
	{
		this.clientSocket = clientSocket;
		this.os= new ObjectOutputStream(clientSocket.getOutputStream());
		this.is=new ObjectInputStream(clientSocket.getInputStream());
		this.serviceContainer = serviceContainer;
		this.invocationHandlerMap = new ConcurrentHashMap<Class,RmiInvocationHandler>(); //invocationHandlerMap;
		this.clientValidator = clientValidator;
		this.connResetCallback=callback;
		this.clientCredentials = clientCredentials;
		setDaemon(true);
	}

	ConnectionWatcher(Socket clientSocket,Map<Class,Object> serviceContainer, ClientValidator clientValidator, IConnectionResetCallback callback) throws IOException
	{
		this(clientSocket,serviceContainer,clientValidator,null,callback);
	}
	
	@Override
	public void run() 
	{
		try 
		{
			//If login is not required this will just act as a handshake.
			LoginMessage loginMessage = new LoginMessage(clientCredentials);
			synchronized(os)
			{
				os.writeObject(loginMessage);
				os.flush();
			}
			
			Object o = is.readObject();
			//this should be a LoginMessage
			if(!(o instanceof LoginMessage))
			{
				clientSocket.close();
				System.err.println("Client Didnt login.");
				connResetCallback.connectionReset(clientSocket);
				return;
			}
			
			LoginMessage loginMsg = (LoginMessage) o;
			if(!clientValidator.validate(loginMsg.getCredentials()))
			{
				clientSocket.close();
				System.err.println("Client login FAILED!!");
				connResetCallback.connectionReset(clientSocket);
				return;
			}
			
			while(true)
			{
				o = is.readObject();
				MessageHandler msgHandler = new MessageHandler(o);
				msgHandler.start();
			}
		} 
		catch(IOException ioe)
		{
			connResetCallback.connectionReset(clientSocket);
			System.err.println(ioe);
		}
		catch (Exception e) 
		{
			connResetCallback.connectionReset(clientSocket);
			System.err.println(e);
		}
	}

	public <K> K getService(final Class<K> c) throws IOException
	{
		RmiInvocationHandler inv;
		if(invocationHandlerMap.containsKey(c))
		{
			inv = invocationHandlerMap.get(c);
		}
		else
		{
			inv = new RmiInvocationHandler(os,c);
			registerInvocationHandler(c, inv);
		}
		Object o = Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c},inv);
		return (K)o;
	}

	void registerInvocationHandler(Class c,RmiInvocationHandler inv)
	{
		invocationHandlerMap.put(c, inv);
	}

	class MessageHandler extends Thread
	{
		private Object message;
		MessageHandler(Object message)
		{
			this.message = message;
		}

		@Override
		public void run() 
		{
			try
			{
				if(message instanceof MethodCallMessage)
				{
					MethodCallMessage mcm = (MethodCallMessage)message;
					Object service = serviceContainer.get(mcm.getC());
					Object result = null;
					Throwable throwable = null;
					for(Method m:service.getClass().getMethods())
					{
						if(m.getName().equals(mcm.getMethod()))
						{
							try
							{
								result = m.invoke(service, mcm.getArgs());
							}
							catch(Throwable e)
							{
								// To Propagate back
								throwable = e;
							}
						}
					}
					ReturnMessage rm = new ReturnMessage(result,mcm.getC(),mcm.getCallId());
					rm.setThrowable(throwable);
					synchronized(os)
					{
						os.writeObject(rm);
						os.flush();
						//os.reset();
					}
				}
				else if(message instanceof ReturnMessage)
				{
					ReturnMessage rm = (ReturnMessage)message;
					RmiInvocationHandler ih = invocationHandlerMap.get(rm.getService());
					MethodCallMessage callerMcm = ih.getAndRemoveSentMethodCallMessage(rm.getCallerId());
					synchronized(callerMcm)
					{
						callerMcm.setResult(rm.getO());
						callerMcm.setThrowable(rm.getThrowable());
						callerMcm.notifyAll();
					}
				}

				//catch(InvocationTargetException e)
				//{
				//System.err.println(e);
				//break;
				//}
			}
			catch(IOException ioe)
			{
				connResetCallback.connectionReset(clientSocket);
				System.err.println(ioe);
			}
		}
	}
}