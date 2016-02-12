package xie.android4servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xie.android4servlet.servlet.ServiceRulesException;
import xie.android4servlet.servlet.UserService;
import xie.android4servlet.servlet.UserServiceImpl;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DownloadActivity extends Activity{
	
	private Button btDownload;
	
	UserService userservice=new UserServiceImpl();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_activity);
		this.btDownload=(Button)this.findViewById(R.id.btn_down);
		this.btDownload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						try {
							userservice.userDownload();
							runOnUiThread(new Runnable() {
                               
								@Override
								public void run() {

									Toast.makeText(DownloadActivity.this, "下载完毕", Toast.LENGTH_SHORT).show();
								}
								});
							
						} catch (final ServiceRulesException e) {
							

							runOnUiThread(new Runnable() {

								@Override
								public void run() {

									Toast.makeText(DownloadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
								}
								});
						
						}catch (Exception e) {
							e.printStackTrace();
							runOnUiThread(new Runnable() {

								@Override
								public void run() {

									Toast.makeText(DownloadActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
								}
								});
						
						}
				
						
					}
				}).start();
			}
		});
		
	}
	


}
