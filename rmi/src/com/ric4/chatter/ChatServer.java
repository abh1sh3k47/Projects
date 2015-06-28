package com.ric4.chatter;

import java.io.IOException;
import java.net.Socket;

import com.ric4.rmi.Server;

public class ChatServer 
{
	public static void main(String... args) throws IOException
	{
		Server s = new Server(27015);
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
