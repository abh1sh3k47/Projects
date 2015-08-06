package com.ric4.chatter;

import java.io.IOException;

/**
 * 
 * @author abh1sh3k47
 *
 */
public interface IChatMaster 
{
	public void sendMsgToAll(ChatMessage msg) throws IOException;
	public void recieveMsg(ChatMessage msg) throws IOException;
}
