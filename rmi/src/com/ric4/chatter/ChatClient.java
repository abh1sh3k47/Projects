package com.ric4.chatter;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import com.ric4.rmi.Client;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class ChatClient
{
	static long test;
	static long clientId;
	public static void main(String[] args) throws IOException 
	{
		if(args.length < 2) 
		{
			System.out.println("Invalid Usage!!");
			return;
		}
		
		String hostname = args[0];
		String portS = args[1];
		int port = Integer.parseInt(portS);
		
		Client c = new Client(hostname,port);
		c.registerService(IChatMaster.class, new ClientChatMaster(c));
		IChatMaster serverChatMaster = c.getService(IChatMaster.class);
		
		Random r = new Random(System.currentTimeMillis()%100);
		clientId = r.nextInt();
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Name!");
		String name = sc.nextLine();
		while(true)
		{
			String line = sc.nextLine();
			serverChatMaster.sendMsgToAll(new ChatMessage(name+":"+line,clientId));
		}
		
	}

	public static class ClientChatMaster implements IChatMaster
	{
		private Client client;
		ClientChatMaster(Client client)
		{
			this.client = client;
		}
		@Override
		public void sendMsgToAll(ChatMessage msg) throws IOException {
			
		}

		@Override
		public void recieveMsg(ChatMessage msg) throws IOException {
			if(msg.senderId!=clientId)
			System.out.println(msg.message);
		}
		
	}
}
