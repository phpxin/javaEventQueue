<?php
namespace demo ;

require_once 'Thrift/ClassLoader/ThriftClassLoader.php';

use Thrift\ClassLoader\ThriftClassLoader;
use Thrift\Protocol\TBinaryProtocol;
use Thrift\Transport\TSocket;
use Thrift\Transport\THttpClient;
use Thrift\Transport\TBufferedTransport;
use Thrift\Exception\TException;

$loader = new ThriftClassLoader();
$loader->registerNamespace('Thrift', __DIR__);
$loader->register();

include 'Types.php' ;
include 'ThriftTest.php' ;

$url = 'http://a.360lt.com/' ;



try{

	$socket = new TSocket('127.0.0.1', 7911);
	$transport = new TBufferedTransport($socket, 1024, 1024);
	$protocol = new TBinaryProtocol($transport);
	
	$cli = new \ThriftTestClient($protocol);
	
	$transport->open();
	
	//$cli->ping();
	//print "ping()<br />\n";
	
	$isok = $cli->requestRemoteApi($url);
	
	
	$transport->close();

}catch (\Exception $e){
	echo "catch exception " . get_class($e) . ", message is " . $e->getMessage() ;
}