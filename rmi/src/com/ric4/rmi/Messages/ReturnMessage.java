package com.ric4.rmi.Messages;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class ReturnMessage extends Message
{
	private Object o;
	private Throwable throwable;
	private Class service;
	private long callerId;
	
	public ReturnMessage(Object o, Class service,long callerId)
	{
		this.o=o;
		this.service = service;
		this.callerId = callerId;
	}

	public Object getO() {
		return o;
	}

	public void setO(Object o) {
		this.o = o;
	}

	public Class getService() {
		return service;
	}

	public void setService(Class service) {
		this.service = service;
	}

	public long getCallerId() {
		return callerId;
	}

	public void setCallerId(long callerId) {
		this.callerId = callerId;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
	
}
