package com.ric4.chatter;

import java.io.Serializable;

/**
 * 
 * @author abh1sh3k47
 *
 */
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
