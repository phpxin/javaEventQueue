service ThriftTest
{

	bool sendMsg(1:i64 userId) ,
	bool requestRemoteApi(1:string url)
}