package com.ric4.rmi;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.ric4.rmi.Messages.MethodCallMessage;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class RmiInvocationHandler implements InvocationHandler
{
	public static final int SENT_METHOD_CALLS_MAX_SIZE = 100;
	private ObjectOutputStream out;
	private Class c;
	private Object lastResult;
	
	private Map<Long,MethodCallMessage> sentMethodCalls = new ConcurrentHashMap<Long,MethodCallMessage>();
	
	private BlockingQueue<MethodCallMessage> queue;

	public RmiInvocationHandler(ObjectOutputStream out,Class c) throws IOException
	{
		this.out=out;
		this.c=c;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
	{
		MethodCallMessage m = new MethodCallMessage(c,method,args);
		synchronized(m)
		{
			synchronized(sentMethodCalls)
			{
				while(sentMethodCalls.size()>SENT_METHOD_CALLS_MAX_SIZE)
				{
					sentMethodCalls.wait();
				}
				sentMethodCalls.put(m.getCallId(), m);
			}
			synchronized(out)
			{
				out.writeObject(m);
				out.flush();
			}
			m.wait();
			if(m.getThrowable()!=null)
			{
				throw m.getThrowable();
			}
			return m.getResult();
		}
	}

	public MethodCallMessage getAndRemoveSentMethodCallMessage(Long id)
	{
		synchronized(sentMethodCalls)
		{
			try
			{
				return sentMethodCalls.remove(id);
			}
			finally
			{
				sentMethodCalls.notify();
			}
		}
	}
	
	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public Class getC() {
		return c;
	}

	public void setC(Class c) {
		this.c = c;
	}

	public Object getLastResult() {
		return lastResult;
	}

	public void setLastResult(Object lastResult) {
		this.lastResult = lastResult;
	}
}
