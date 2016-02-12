package xie.android4servlet;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import xie.android4servlet.servlet.ServiceRulesException;
import xie.android4servlet.servlet.UserService;
import xie.android4servlet.servlet.UserServiceImpl;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	private final static int FLAG_REGISTER_SUCESS=1;
	
	public final static String MSG_REGISTER_ERROR="×¢²á´íÎó¡£";
	private final static String MSG_REGISTER_SUCESS="×¢²á³É¹¦¡£";
	public final static String MSG_SEVICE_ERROR="·þÎñÆ÷Á¬½Ó´íÎó¡£";
	public final static String 	MSG_LOGIN_FAILED="µÇÂ¼Ãû»òÃÜÂë´íÎó¡£";
	
	private EditText registerName;
	private CheckBox cheGame;
	private CheckBox cheMusic;
	private CheckBox cheSport;
	private Button registerButton;
	
	UserService userservice=new UserServiceImpl();
	
	private static ProgressDialog dialog;
	
	private void init() {
		registerName=(EditText)this.findViewById(R.id.reg_name);
		registerButton=(Button)this.findViewById(R.id.bt_register);
		cheGame=(CheckBox)this.findViewById(R.id.che_game);
		cheMusic=(CheckBox)this.findViewById(R.id.che_music);
		cheSport=(CheckBox)this.findViewById(R.id.che_sport);
		
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		this.init();
		
		 this.registerButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					final String RegisterName=registerName.getText().toString();
					final List<String> interestList=new ArrayList<String>();
					if (cheGame.isChecked()) {
						interestList.add(cheGame.getText().toString());
					}
					if (cheMusic.isChecked()) {
						interestList.add(cheMusic.getText().toString());
					}
					if (cheSport.isChecked()) {
						interestList.add(cheSport.getText().toString());
					}
					
					
					if (dialog==null) {
						dialog= new ProgressDialog(RegisterActivity.this);
					}
					dialog.setTitle("waitting...");
					dialog.setMessage("ÇëµÈ´ý¡£¡£¡£");
					dialog.setCancelable(false);
					dialog.show();
					
					
				     
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								userservice.UserRegister(RegisterName, interestList);
								handler.sendEmptyMessage(FLAG_REGISTER_SUCESS);
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
								data.putSerializable("ErrorMsg", MSG_REGISTER_ERROR);
								msg.setData(data);
								handler.sendMessage(msg);
							}
						}
						
					});
					thread.start();
					
					
					
					
				}
			});
	}
	
	 public void showTip(String str) {
	    	Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
			
		}
	    
	    private static class Ihandler extends Handler {
	    	
	    	private final WeakReference<Activity>mActivity;
	    	
	    	public Ihandler(RegisterActivity activity){
	    		
	    		mActivity=new WeakReference<Activity>(activity);
	    	}
			
			@Override
			public void handleMessage(Message msg) {
				
				if (dialog !=null) {		
					dialog.dismiss();
				}
				
				int flag=msg.what;
				switch (flag) {
				case FLAG_REGISTER_SUCESS:
					((RegisterActivity)mActivity.get()).showTip(MSG_REGISTER_SUCESS);
					break;
	            case 0:
	            	String ErroMsg=(String)msg.getData().getSerializable("ErrorMsg");
	            	((RegisterActivity)mActivity.get()).showTip(ErroMsg);
	            	break;
				default:
					break;
				}
			}
		}
	    private Ihandler handler=new Ihandler(this);

	

}
