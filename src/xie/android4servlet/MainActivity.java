package xie.android4servlet;

import java.lang.ref.WeakReference;

import xie.android4servlet.servlet.ServiceRulesException;
import xie.android4servlet.servlet.UserService;
import xie.android4servlet.servlet.UserServiceImpl;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	
	private final static int FLAG_LOGIN_SUCESS=1;
	private final static String MSG_LOGIN_ERROR="µÇÂ¼´íÎó¡£";
	private final static String MSG_LOGIN_SUCESS="µÇÂ¼³É¹¦¡£";
	public final static String MSG_SEVICE_ERROR="·þÎñÆ÷Á¬½Ó´íÎó¡£";
	public final static String 	MSG_LOGIN_FAILED="µÇÂ¼Ãû»òÃÜÂë´íÎó¡£";
	
	

	
	
	private EditText editName;
	private EditText editPassword;
	private Button btLogin;
	private Button btReset;
	
	UserService userservice=new UserServiceImpl();
	
	private static ProgressDialog dialog;
	
	private void  inti() {
		this.editName=(EditText)this.findViewById(R.id.edit_name);
		this.editPassword=(EditText)this.findViewById(R.id.edit_password);
		this.btLogin=(Button)this.findViewById(R.id.bt_login);
		this.btReset=(Button)this.findViewById(R.id.bt_reset);		
	}
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.inti();
        this.btLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				final String LoginName=editName.getText().toString();
				final String LoginPassword=editPassword.getText().toString();
				
				if (dialog==null) {
					dialog= new ProgressDialog(MainActivity.this);
				}
				dialog.setTitle("waitting...");
				dialog.setMessage("ÇëµÈ´ý¡£¡£¡£");
				dialog.setCancelable(false);
				dialog.show();
				
				
			     
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							userservice.UserLogin(LoginName, LoginPassword);
							handler.sendEmptyMessage(FLAG_LOGIN_SUCESS);
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
							data.putSerializable("ErrorMsg", MSG_LOGIN_ERROR);
							msg.setData(data);
							handler.sendMessage(msg);
						}
					}
					
				});
				thread.start();
				
				
				
				
			}
		});
        this.btReset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				editName.setText("");
				editPassword.setText("");
				
			}
		});
        
    }
    public void showTip(String str) {
    	Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		
	}
    
    private static class Ihandler extends Handler {
    	
    	private final WeakReference<Activity>mActivity;
    	
    	public Ihandler(MainActivity activity){
    		
    		mActivity=new WeakReference<Activity>(activity);
    	}
		
		@Override
		public void handleMessage(Message msg) {
			
			if (dialog !=null) {
				
				dialog.dismiss();
			}
			
			int flag=msg.what;
			switch (flag) {
			case FLAG_LOGIN_SUCESS:
				((MainActivity)mActivity.get()).showTip(MSG_LOGIN_SUCESS);
				break;
            case 0:
            	String ErroMsg=(String)msg.getData().getSerializable("ErrorMsg");
            	((MainActivity)mActivity.get()).showTip(ErroMsg);
            	break;
			default:
				break;
			}
		}
	}
    private Ihandler handler=new Ihandler(this);


  
}
