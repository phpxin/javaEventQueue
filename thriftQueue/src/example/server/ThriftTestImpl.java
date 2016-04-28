package example.server;

import org.apache.thrift.TException;

public class ThriftTestImpl implements ThriftTest.Iface {

	@Override
	public boolean sendMsg(long userId) throws TException {
		// TODO Auto-generated method stub
		System.out.println("hello world");
		return false;
	}
	
	@Override
	public boolean requestRemoteApi(String url) throws TException {
		// TODO Auto-generated method stub
		QueueThread.addUrl(url);
		return false;
	}
	
}
