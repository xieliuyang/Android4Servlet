package xie.android4servlet.servlet;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

import xie.android4servlet.entity.Student;

public interface UserService {
	
	public void UserLogin(String LoginName,String LoginPassword)throws Exception;
	public void UserRegister(String RegisterName,List<String> interestList)throws Exception;
	public List<Student> getStudents() throws Exception;
	public Bitmap getImage() throws Exception;
	public String userUpload(InputStream in,Map<String, String> data) throws Exception; 
	public void userDownload() throws Exception;
}
