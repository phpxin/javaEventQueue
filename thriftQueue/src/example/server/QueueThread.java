package example.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import tools.db;

public class QueueThread implements Runnable {

	public static ArrayDeque<String> urls ;
	
	{
		QueueThread.urls = new ArrayDeque<String>() ;
	}
	
	public static void addUrl(String _url) {
		// 1.
		// ����ʹ�ö����п��ܻ�����������������¼���ʧ�������ṩһ��˼·����Redis������
		// 2.
		// ���ڴ������¼������Զ�������Э�飬��ʽ�磺
		// {"type":"sendmsg","values":{"par1":"val1" ...}}
		// Ȼ������һ������״̬����ͨ����ͬ���¼�ִ�в�ͬ�Ĳ������緢���š����ʼ�������ͼƬ���ϴ�ͼƬ��ͬ�����ݵ�
		// 3.
		// �����Ҫ�������Щ���ϵ�����¼�������ʹ��java�ķ������������װһ���¼������࣬ÿ����Ա����Ϊ����һ���¼���Э���а��������¼��ĺ�����
		QueueThread.urls.add(_url) ;
	}
	
	@Test
	public void unitTest(){
		try {
			doRequest("http://a.360lt.com/");
			//saveData("{\"title\":\"a.360 response\",\"content\":\"response at 2016-04-28 14:14:48\"}");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean doRequest(String url){
		System.out.println("try to connect " + url);
		try {
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection() ;
			BufferedInputStream bis = new BufferedInputStream( conn.getInputStream() );
			
			int len = 1024 ;
			int off = 0 ;
			byte b[] = new byte[len] ;
			int readLen = 0;
			int realReadLen = 0 ;
			ByteArrayOutputStream contentBytes = new ByteArrayOutputStream();
			while((realReadLen = bis.read(b, off, len)) != -1) {
				contentBytes.write(b);
				contentBytes.flush();
				readLen += realReadLen ;
			}
			
			String jsonStr = new String(contentBytes.toByteArray(), 0, readLen, "utf-8") ;
			System.out.println(jsonStr);
			
			saveData(jsonStr) ;
			
		} catch (Exception e) {
			//����Ӧ�ü�¼��־
			System.out.println("catch " + e.getClass().toString() + " msg is " +  e.getMessage());
		}
		
		return true;
	}
	
	private boolean saveData(String json){
		
		JSONTokener jsonParser = new JSONTokener(json);
		JSONObject jsonObj = (JSONObject)jsonParser.nextValue() ;
		String title = jsonObj.getString("title") ;
		String content = jsonObj.getString("content") ;
		
		Connection gdbcConn = null ;
		PreparedStatement ps = null ;
		ResultSet res = null ;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");

			String gdbcUrl = "jdbc:mysql://" +db.HOST+ ":" +db.PORT+ "/" +db.DBNAME+ "?characterEncoding=" +db.CHARSET;
			String user = db.USER;
			String pwd = db.PWD;
			gdbcConn = DriverManager.getConnection(gdbcUrl, user, pwd);

			String sql = "insert into demo(title,content,sort) values(?,?,?)";
			ps =  gdbcConn.prepareStatement(sql);
			ps.setString(1, title); 
			ps.setString(2, content);
			ps.setInt(3, 1);
			boolean isok = ps.execute();
			
			System.out.println("save data ok");
		}catch(Exception e){
			System.out.println("catch " + e.getClass().toString() + " msg is " +  e.getMessage());
			System.out.println(e.getLocalizedMessage());
		}finally{
			try{
			
				if(ps != null)
					ps.close();
				if(res != null)
					res.close();
				if(gdbcConn != null)
					gdbcConn.close();
				
			}catch(Exception e){
				//��¼��־
				System.out.println("catch " + e.getClass().toString() + " msg is " +  e.getMessage());
			}
			
		}
		
		
		return true;
		
	}
	
	@Override
	public void run() {
		try{
			while(true){
				//int cs = QueueThread.urls.size();
				while(!QueueThread.urls.isEmpty()){
					String _url = QueueThread.urls.pop() ;
					doRequest(_url);
				}
				Thread.sleep(1000);
			}
			
		}catch(NoSuchElementException e){
			//����쳣Ӧ�õ����������������з���
			System.out.println("catch " + e.getClass().toString() + " msg is " +  e.getMessage());
		}catch (Exception e) {
			//����Ӧ�ü�¼��־
			System.out.println("catch " + e.getClass().toString() + " msg is " +  e.getMessage());
		}
		
		
	}

}
