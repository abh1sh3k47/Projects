package com.ric4.chatter;

import java.io.Serializable;

public class ChatMessage implements Serializable
{
	String message;
	long senderId;
	
	public ChatMessage(String message, long senderId)
	{
		this.message = message;
		this.senderId = senderId;
	}
}
