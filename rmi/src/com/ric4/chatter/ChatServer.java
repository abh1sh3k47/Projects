package com.ric4.chatter;

import java.io.IOException;
import java.net.Socket;

import com.ric4.rmi.Server;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class ChatServer 
{
	public static void main(String... args) throws IOException
	{
		if(args.length < 1) 
		{
			System.out.println("Invalid Usage!!");
			return;
		}
		
		String portS = args[0];
		int port = Integer.parseInt(portS);
		
		Server s = new Server(port);
		s.registerService(IChatMaster.class, new ServerChatMaster(s));
	}
	
	public static class ServerChatMaster implements IChatMaster
	{

		private Server server;
		ServerChatMaster(Server server)
		{
			this.server=server;
		}
		
		@Override
		public void sendMsgToAll(ChatMessage msg) throws IOException {
			
			for(Socket socket:server.getActiveClients())
			{
				IChatMaster clientChatMaster = (IChatMaster) server.getClientService(socket, IChatMaster.class);
				clientChatMaster.recieveMsg(msg);
			}
		}

		@Override
		public void recieveMsg(ChatMessage msg) { 
			
		}
		
	}
	
	
}
