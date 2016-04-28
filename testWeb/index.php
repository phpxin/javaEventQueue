<?php
date_default_timezone_set('Asia/ChongQing');

//  模拟一个接口提供端，假设提供端效率较低，这里延迟10m

sleep(3);

$data = array(
	'title' => 'a.360 response' ,
	'content' => 'response at '.date('Y-m-d H:i:s') 
) ;


echo json_encode($data) ;
exit();
