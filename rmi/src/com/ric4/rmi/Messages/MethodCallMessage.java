package com.ric4.rmi.Messages;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class MethodCallMessage extends Message 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1492382272776946155L;
	
	private static AtomicLong atomicLong = new AtomicLong(Long.MIN_VALUE);
	
	private Long callId = atomicLong.addAndGet(1);
	private String method;
	private Object [] args;
	private Class c;
	
	private Object result;
	private Throwable throwable;
	
	public MethodCallMessage(Class c,Method method, Object [] args)
	{
		this.method = method.getName();
		this.args = args;
		this.c=c;
		
		if(callId % 100000 == 0) System.out.println("MCM - "+callId);
	}

	@Override
	public int hashCode() {
		return callId.intValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof MethodCallMessage))
			return false;
		MethodCallMessage other = (MethodCallMessage)obj;
		return callId.equals(other.callId);
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Class getC() {
		return c;
	}

	public void setC(Class c) {
		this.c = c;
	}

	public long getCallId() {
		return callId;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
}
