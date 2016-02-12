package xie.android4servlet.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import xie.android4servlet.MainActivity;
import xie.android4servlet.RegisterActivity;
import xie.android4servlet.entity.Student;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class UserServiceImpl implements UserService{
	
	

	@Override
	public void UserLogin(String LoginName, String LoginPassword)
			throws Exception {
		HttpClient client=new DefaultHttpClient();
		String uri="http://192.168.1.101:8080/maizi/login.do?LoginName="+LoginName+"&LoginPassword="+LoginPassword;
		HttpGet get=new HttpGet(uri);
		HttpResponse response=client.execute(get);
		int statuCode=response.getStatusLine().getStatusCode();
		if (statuCode!=200) {
			throw new ServiceRulesException(MainActivity.MSG_SEVICE_ERROR);
			
		}
		
		String reuslt=EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
		if (reuslt.equals("niubi")) {
			
		}else {
			throw new ServiceRulesException(MainActivity.MSG_LOGIN_FAILED);
		}
	}

	@Override
	public void UserRegister(String RegisterName, List<String> interestList)
			throws Exception {
		HttpClient client=new DefaultHttpClient();
		String uri="http://192.168.1.101:8080/maizi/regester.do?";
		HttpPost post=new HttpPost(uri);
		
		/*
		 * 要封装的数据格式可以写成
		 * {"RegisterName":"","interestList":["game","music","sport"]}
		 */
		JSONObject object=new JSONObject();//相当于花括号有了{}
		object.put("RegisterName", RegisterName);
		JSONArray array=new JSONArray();//相当于有了【】
		if (interestList !=null) {
			for (String string : interestList) {
				array.put(string);
			}
		}
		object.put("interestList", array);
		
		NameValuePair parameter=new BasicNameValuePair("Data", object.toString());
		List<NameValuePair> params= new ArrayList<NameValuePair>();
		params.add(parameter);
		
		post.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		
		HttpResponse response=client.execute(post);
		int statuCode=response.getStatusLine().getStatusCode();
		if (statuCode!=200) {
			throw new ServiceRulesException(RegisterActivity.MSG_REGISTER_ERROR);
			
		}
		
		/***
		 * 从响应中取出结果
		 */
		String results=EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
		/***
		 * 解析结果
		 */
		JSONObject jsonResults=new JSONObject(results);
		
		String result=jsonResults.getString("result");
		if (result.equals("success")) {
			
		}else {
			String errorMsg=jsonResults.getString("errorMsg");
			throw new ServiceRulesException(errorMsg);
		}
//		
	}

	@Override
	public List<Student> getStudents() throws Exception {
		List<Student> students =new ArrayList<Student>();
		HttpClient client= new DefaultHttpClient();
		String uri="http://192.168.1.101:8080/maizi/getStudents.do";
		HttpGet get= new HttpGet(uri);
		
		HttpResponse response=client.execute(get);
		
		int statuCode=response.getStatusLine().getStatusCode();
		if (statuCode!=200) {
			throw new ServiceRulesException(MainActivity.MSG_SEVICE_ERROR);	
		}
		
		String results=EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
		
		JSONArray array=new JSONArray(results);
		for (int i = 0; i < array.length(); i++) {
			JSONObject jsonStudent=array.getJSONObject(i);
			Long id =Long.parseLong(jsonStudent.getString("id"));
			String name = jsonStudent.getString("name");
			int age =jsonStudent.getInt("age"); 
			
			students.add(new Student(id, name, age));
			
		}
		
		return students;
	}

	@Override
	public Bitmap getImage() throws Exception {
	    Bitmap bitmap=null;
	    HttpURLConnection urlConnection=null;
	    URL url=null;
	    InputStream in=null;
	    try {
	    	url=new URL("http://192.168.1.101:8080/maizi/getImage.jpeg?id=102");
	    	urlConnection=(HttpURLConnection)url.openConnection();
	    	urlConnection.setDoInput(true);
	    	urlConnection.setRequestMethod("GET");
	    	urlConnection.connect();
	    	int reponseCode=urlConnection.getResponseCode();
	    	if (reponseCode!=200) {
				throw new ServiceRulesException("请求服务器出错。");
			}
	    	in=urlConnection.getInputStream();
	    	if (in!=null) {
	    		bitmap=BitmapFactory.decodeStream(in);
				
			}
			
		} finally {
			if (in!=null) {
				in.close();
			}
			if (urlConnection!=null) {
				urlConnection.disconnect();
			}
			
		}
		return bitmap;
	}

	@Override
	public String userUpload(InputStream in, Map<String, String> data)
			throws Exception {
		HttpClient client=new DefaultHttpClient();
		String uri="http://192.168.1.101:8080/maizi/upload.do";
		HttpPost post=new HttpPost(uri);
		
		MultipartEntity entity=new MultipartEntity();
		for (Map.Entry<String, String> entry : data.entrySet()) {
			String key=entry.getKey();
			String value=entry.getValue();
			entity.addPart(key,new StringBody(value,Charset.forName("UTF-8")));
			
			
			
		}
		entity.addPart("file", new InputStreamBody(in, "multipart/form-data", "test.jpg"));
		post.setEntity(entity);
		
		
		HttpResponse response=client.execute(post);
		int statuCode=response.getStatusLine().getStatusCode();
		if (statuCode!=200) {
			throw new ServiceRulesException("上传服务器出错。");
			
		}
		
		/***
		 * 从响应中取出结果
		 */
		String result=EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
		
		return result;
	}

	@Override
	public void userDownload() throws Exception {
		InputStream in = null;
		OutputStream out =null;
		HttpURLConnection urlConnection = null;
		URL url = null;
		try {
			url=new URL("http://192.168.1.101:8080/maizi/download.do");
	    	urlConnection=(HttpURLConnection)url.openConnection();
	    	urlConnection.setDoInput(true);
	    	urlConnection.setRequestMethod("GET");
	    	urlConnection.connect();
	    	int reponseCode=urlConnection.getResponseCode();
	    	if (reponseCode!=200) {
				throw new ServiceRulesException("请求服务器出错。");
			}
	    	in=new BufferedInputStream(urlConnection.getInputStream());
	    	out=new BufferedOutputStream(new FileOutputStream(new File(Environment.getExternalStorageDirectory()+"/hh.pdf")));
	    	
	    	byte[] buffer=new byte[20480];
	    	int read=0;
	    	while ((read=in.read(buffer))!=-1) {
	    		out.write(buffer, 0, read);
				
				
			}
	    	
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if (in!=null) {			
					in.close();			
			}
			if (out!=null) {	
					out.close();	
			}
		}
		
		
	}



}
