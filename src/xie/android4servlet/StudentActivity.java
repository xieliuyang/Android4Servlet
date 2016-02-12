package xie.android4servlet;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import xie.android4servlet.adapter.StudentAdapter;
import xie.android4servlet.entity.Student;
import xie.android4servlet.servlet.ServiceRulesException;
import xie.android4servlet.servlet.UserService;
import xie.android4servlet.servlet.UserServiceImpl;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

public class StudentActivity extends Activity {
	
	private ListView listViewStudent;
	
	private List<Student> studentList;
	private StudentAdapter adapter;
	
	UserService userservice=new UserServiceImpl();
	
	private static ProgressDialog dialog;
	
	private final static int FLAG_STUDENT_SUCESS=1;
	public static final String MSG_STUDENT_ERROR="加载数据错误。";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_activity);
		/**
		 * 构建本地数据源
		 */
		this.listViewStudent=(ListView)this.findViewById(R.id.listview_student);
		this.studentList=new ArrayList<Student>();
//		this.studentList.add(new Student(100L,"Tom",20));
//		this.studentList.add(new Student(101L,"Jack",21));
//		this.studentList.add(new Student(102L,"Lisa",22));
		
		
	    

		if (dialog==null) {
			dialog= new ProgressDialog(StudentActivity.this);
		}
		dialog.setTitle("waitting...");
		dialog.setMessage("请等待。。。");
		dialog.setCancelable(false);
		dialog.show();
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					studentList=userservice.getStudents();
					handler.sendEmptyMessage(FLAG_STUDENT_SUCESS);
					
				}
				catch(ServiceRulesException e) {
					e.printStackTrace();
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putSerializable("ErrorMsg", e.getMessage());
					msg.setData(data);
					handler.sendMessage(msg);
				}					
				catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putSerializable("ErrorMsg", MSG_STUDENT_ERROR);
					msg.setData(data);
					handler.sendMessage(msg);
				}
			}
			
		});
		thread.start();
		
	    
	    
	}
	 private void showTip(String str) {
	    	Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
			
		}
	
	    private void loadDataListView(){
	    	this.adapter =new StudentAdapter(this, R.layout.student_item, this.studentList);
		    this.listViewStudent.setAdapter(this.adapter);
	    }
	    
	    private static class Ihandler extends Handler {
	    	
	    	private final WeakReference<Activity>mActivity;
	    	
	    	public Ihandler(StudentActivity activity){
	    		
	    		mActivity=new WeakReference<Activity>(activity);
	    	}
			
			@Override
			public void handleMessage(Message msg) {
				
				if (dialog !=null) {		
					dialog.dismiss();
				}
				
				int flag=msg.what;
				switch (flag) {
				case FLAG_STUDENT_SUCESS:
					((StudentActivity)mActivity.get()).loadDataListView();
					break;
	            case 0:
	            	String ErroMsg=(String)msg.getData().getSerializable("ErrorMsg");
	            	((StudentActivity)mActivity.get()).showTip(ErroMsg);
	            	break;
				default:
					break;
				}
			}
		}
	    private Ihandler handler=new Ihandler(this);
	
	
	

}
