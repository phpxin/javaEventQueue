package example.server;

import java.net.InetSocketAddress;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

public class ThriftTestServer {
	public static void main(String[] args) { 
		
		try { 
			// 设置服务端口为 7911 
			//TServerTransport serverTransport = new TServerSocket(7911); 
			//serverTransport.
			TServerTransport serverTransport = new TServerSocket(new InetSocketAddress("127.0.0.1", 7911)); 
			
			// 设置协议工厂为 TBinaryProtocol.Factory 
			//Factory proFactory = new TBinaryProtocol.Factory(); 
			
			// 关联处理器与 Hello 服务的实现
			TProcessor processor = new ThriftTest.Processor(new ThriftTestImpl()); 
			
			//TServer server = new TThreadPoolServer(processor, serverTransport, proFactory); 
			TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
			
			System.out.println("Start server on port 7911..."); 
			
			(new Thread(new QueueThread()) ).start();  //  start queue thread
			
			server.serve(); 
		} catch (TTransportException e) { 
			e.printStackTrace(); 
		} 
		

	}
}
