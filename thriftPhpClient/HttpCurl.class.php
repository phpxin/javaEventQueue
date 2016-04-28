<?php 



class HttpCurl{
	
	public static $_METHOD_POST = 1 ;
	public static $_METHOD_GET = 2 ;
	
	private $url ;
	private $analyze = array();
	private $cookie = '';
	private $fields = array();
	private $method ;
	private $response = null ;
	
	public function __construct( $url, $method='' ){
		$this->url = $url ;
		$this->method = self::$_METHOD_GET ;
		$this->setMethod($method);
	}
	
	public function test()
	{
		
	}
	
	public function setCookie()
	{
		
	}
	
	public function setPostField()
	{
		
	}
	
	public function getResponse()
	{
		return $this->response ;
	}
	
	public function setMethod($method)
	{
		if ($method) {
			$this->method = $method ;
		}
	}
	
	public function exec( $ch=null )
	{
		if (empty($ch)){
			$res_flag = true;
			$ch = curl_init(); //初始化curl
		}
		
		if (false===$ch) {
			throw new HttpsCurlException('init failed', HttpsCurlException::$_E_CURL_INIT) ;
		}
		
		try{
		
			curl_setopt($ch, CURLOPT_URL, $this->url);
			curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);	//要求结果为字符串且输出到屏幕上
			curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1'); //okhttp/2.5.0
			
			if(preg_match('/^https\:/i', $this->url)){
				curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
			}
			
			if ($this->cookie){
				//设置cookie
				curl_setopt($ch, CURLOPT_COOKIESESSION, 1); 
				curl_setopt($ch, CURLOPT_COOKIE, $cookieStr );
			}
			
			if ($this->method == self::$_METHOD_POST){
				//post提交方式
				curl_setopt($ch, CURLOPT_POST, 1); 
				if ( $this->fields ){
					curl_setopt($ch, CURLOPT_POSTFIELDS, $this->fields);
				}
			}
			
			if (curl_errno($ch)){
				throw new HttpsCurlException('setopt failed '.curl_error($ch), HttpsCurlException::$_E_CURL_SETOPT) ;
			}
			
			$this->analyze['start'] = microtime(true);
			$response = curl_exec($ch);
			$this->analyze['end'] = microtime(true);
			
			
			if (false == $response){
				throw new HttpsCurlException('exec failed '.curl_error($ch), HttpsCurlException::$_E_CURL_EXEC) ;
			}
			
			$this->response = $response ;
		
		}catch (HttpsCurlException $e){
			
			throw new HttpsCurlException($e->getMessage(), $e->getCode()) ;
			
		}finally {
			
			if (isset($res_flag) && $res_flag){
				curl_close($ch);   //   本函数创建的资源将被销毁
			}
		}
		
		return true;
	}
}


class HttpsCurlException extends Exception{
	public static $_E_CURL_INIT = 1001;
	public static $_E_CURL_SETOPT = 1002;
	public static $_E_CURL_EXEC = 1003;
}

?>