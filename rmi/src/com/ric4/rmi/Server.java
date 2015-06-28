package com.ric4.rmi;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.ric4.rmi.Messages.MethodCallMessage;
import com.ric4.rmi.Messages.ReturnMessage;


public class Server {

	ServerSocket serverSocket = null;
	final Map<Class,Object> serviceContainer = new HashMap<Class,Object>();
	final Map<Class,RmiInvocationHandler> invocationHandlerMap = new HashMap<Class,RmiInvocationHandler>();
	ExecutorService executor;
	Map<Socket,ConnectionWatcher> activeClients;

	public Server(int port) throws IOException
	{
		activeClients = Collections.synchronizedMap(new HashMap<Socket,ConnectionWatcher>());
		serverSocket = new ServerSocket(port);
		executor = new ScheduledThreadPoolExecutor(5);
		ClientConnectionHandler c = new ClientConnectionHandler(serverSocket);
		c.start();
	}

	public void registerService(Class serviceClass, Object service)
	{
		serviceContainer.put(serviceClass, service);
	}

	public Object getClientService(Socket s,Class service) throws IOException
	{
		return activeClients.get(s).getService(service);
	}
	
	public Set<Socket> getActiveClients()
	{
		return activeClients.keySet();
	}
	
	class ClientConnectionHandler extends Thread
	{
		ServerSocket serverSocket;

		ClientConnectionHandler(ServerSocket serverSocket)
		{
			this.serverSocket=serverSocket;
		}

		@Override
		public void run() 
		{
			IConnectionResetCallback callback = new IConnectionResetCallback() {
				@Override
				public void connectionReset(Socket s) {
					System.out.println("Client lost - "+s.getInetAddress());
					activeClients.remove(s);
				}
			};
			
			while(true)
			{
				try
				{
					Socket clientSocket = serverSocket.accept();
					ConnectionWatcher watcher = new ConnectionWatcher(clientSocket,serviceContainer,callback);
					activeClients.put(clientSocket, watcher);
					executor.execute(watcher);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
