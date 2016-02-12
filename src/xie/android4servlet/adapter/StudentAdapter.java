package xie.android4servlet.adapter;

import java.util.List;

import xie.android4servlet.R;
import xie.android4servlet.entity.Student;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StudentAdapter extends ArrayAdapter<Student>{
	
	private LayoutInflater mInflater;
	private int mResource;

	/**
	 * 
	 * @param context ������
	 * @param resource student_item layout
	 * @param objects  List<Student>����Դ
	 */
	public StudentAdapter(Context context, int resource, List<Student> objects) {
		super(context, resource, objects);
		this.mInflater = LayoutInflater.from(context);
		this.mResource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout view=null;
		
		if (convertView==null) {
			view = (LinearLayout)mInflater.inflate(mResource, null);//inflate��������Ҫ���þ��ǽ�xmlת����һ��View�������ڶ�̬�Ĵ������֡�
		}else {
			view = (LinearLayout)convertView;
		}
		
		//��ȡ���ݰ󶨵�һ��Item�ϵĶ���
		Student student = getItem(position);
		
		/**
		 * ��ȡItem�е�TextView�ؼ�
		 */
		TextView txtID=(TextView)view.findViewById(R.id.txt_id);
		TextView txtName=(TextView)view.findViewById(R.id.txt_name);
		TextView txtAge=(TextView)view.findViewById(R.id.txt_age);
		
		/**
		 *  ����ֵ
		 */
	    txtID.setText(student.getId().toString());
	    txtName.setText(student.getName());
	    txtAge.setText(String.valueOf(student.getAge()));
		
		return view;
		
	} 
	

}
