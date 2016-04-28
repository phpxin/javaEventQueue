<?php
namespace demo ;

include 'HttpCurl.class.php' ;

try{
	$hc = new \HttpCurl('http://a.360lt.com/');
	$hc->exec() ;
	$result = $hc->getResponse();
	echo htmlspecialchars($result);  //   这里应该有数据库操作，将获取的接口数据插入数据库
}catch (Exception $e){
	echo '请求失败 : ' , $e->getMessage();
}
