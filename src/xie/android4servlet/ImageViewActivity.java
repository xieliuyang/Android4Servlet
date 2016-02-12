package xie.android4servlet;

import xie.android4servlet.servlet.ServiceRulesException;
import xie.android4servlet.servlet.UserService;
import xie.android4servlet.servlet.UserServiceImpl;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageViewActivity extends Activity {
	
	private ImageView imageViewSample;
	
	UserService userservice=new UserServiceImpl();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageview_activity);
		this.imageViewSample=(ImageView)this.findViewById(R.id.image_view_sample);
//		this.imageViewSample.setImageResource(R.drawable.sample);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					final Bitmap bitmap=userservice.getImage();
					if (bitmap!=null) {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								
								imageViewSample.setImageBitmap(bitmap);
							}
							
						});
					}		
					
				} catch (final ServiceRulesException e) {
					

					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							Toast.makeText(ImageViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
						}
						});
				
				}
				catch (Exception e) {
					

					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							Toast.makeText(ImageViewActivity.this, "º”‘ÿ‘∂≥ÃÕº∆¨", Toast.LENGTH_SHORT).show();
						}
						});
				
				}
			}
			
		}).start();
	}
	
	

}
