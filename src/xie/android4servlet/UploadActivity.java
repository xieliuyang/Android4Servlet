package xie.android4servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import xie.android4servlet.servlet.ServiceRulesException;
import xie.android4servlet.servlet.UserService;
import xie.android4servlet.servlet.UserServiceImpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class UploadActivity extends Activity {
 
	private static final int FLAG_LOAD_IMAGE=1;
	private Button selectButton;
	private String pathNane;
	UserService userservice=new UserServiceImpl();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uplosd_layout);
		this.selectButton=(Button)this.findViewById(R.id.btn_select);
		this.selectButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, FLAG_LOAD_IMAGE);				
			}
		});	
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==FLAG_LOAD_IMAGE) {
			if (data==null) {	
				Toast.makeText(this, "û��ѡ��ͼƬ1��", Toast.LENGTH_LONG).show();				
		}else {
			Uri uri=data.getData();
			if (uri==null) {
				Toast.makeText(this, "û��ѡ��ͼƬ2��", Toast.LENGTH_LONG).show();
			}else {
				String path =null;
				String[] pojo={MediaStore.Images.Media.DATA};
				Cursor cursor=getContentResolver().query(uri, pojo, null, null, null);
				if (cursor!=null) {
					int columnIdex=cursor.getColumnIndexOrThrow(pojo[0]);
					cursor.moveToFirst();
					path=cursor.getString(columnIdex);
					cursor.close();
				}
				if (path==null) {
					Toast.makeText(this, "û��ѡ��ͼƬ3��", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(this, "ͼƬ����·���ǣ�"+path, Toast.LENGTH_LONG).show();
					pathNane=path;
					new AlertDialog.Builder(this)
					               .setTitle("��ʾ")
					               .setMessage("Ҫ�ϴ�ѡ���ͼƬ��")
					               .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										doUpload();
										
									}
								}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
										
									}
								}).create().show();
					

				}
			}
		}
	}
	
	
	
	

	}
	protected void doUpload() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					InputStream in=new FileInputStream(new File(pathNane));
					Map<String, String> data=new HashMap<String, String>();
					data.put("name", "xie");
					data.put("gender", "��");
					final String result = userservice.userUpload(in, data);
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(UploadActivity.this, result, Toast.LENGTH_LONG).show();						
						}
					});
					
				} catch (final ServiceRulesException e) {
					

					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
						}
						});
				
				}catch (Exception e) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							Toast.makeText(UploadActivity.this, "�ϴ�����", Toast.LENGTH_SHORT).show();
						}
						});
				}
				
			}
		}).start();
		
	}}
