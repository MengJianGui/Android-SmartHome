package com.example.administrator.smarthome1.fragment;

import com.example.administrator.smarthome1.R;
import com.example.administrator.smarthome1.util.ConnectMysql;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TabThirdFragment extends Fragment {
	private static final String TAG = "TabThirdFragment";
	protected View mView;
	protected Context mContext;
	private int mType=0;
	private TextView tv_add;
	private EditText et_person,et_time,et_location,et_behavior,et_duration,et_pressure,et_service;
	private Button btn_add,btn_generate,btn_query,btn_clear;
	private Spinner sp_person,sp_time,sp_location,sp_behavior,sp_duration,sp_pressure,sp_service;

	private String[] person = {"Elder", "Adult","Child","Zero"};
	private String[] time = {"Morning", "Noon","Afternoon", "Night","Latenight","Zero"};
	private String[] location = {"Livingroom", "Bedroom","Kitchen", "Studyingroom","Zero"};
	private String[] behavior = {"Standing", "Walking","Sitting", "Lying_bed","Lying_floor","Zero"};
	private String[] pressure = {"Normal", "Light","Heavy","Zero"};
	private String[] duration = {"0", "30","60", "90","Zero"};
	private String[] service = {"服务1", "服务2","服务3", "服务4","服务5", "服务6"};
	StringBuffer result=new StringBuffer();
	StringBuilder sql_repeat = new StringBuilder();
	private StringBuilder sql_person,sql_time,sql_location,sql_behavior,sql_pressure,sql_duration,sql_service;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		mView = inflater.inflate(R.layout.fragment_tab_third, container, false);
		String desc = String.format("我是%s页面，来自%s",
				"服务添加", getArguments().getString("tag"));
		Log.i(TAG,desc);
		setViews();
		initTypeSpinner();
        //setResult();

		btn_query.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult();
				TabThirdFragment.Connect connect = new TabThirdFragment.Connect();
				connect.execute();
			}
		});

		btn_generate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult();
				tv_add.setText(result);
			}
		});
		btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sql_repeat.toString().equals("服务规则库中有相同的规则属性，请勿重复添加")){
                    Toast.makeText(mContext,"请勿重复添加",Toast.LENGTH_SHORT).show();
                }
                if (sql_repeat.toString().equals("可以添加此服务规则")){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Connection conn = ConnectMysql.getConn();
                            String sql_repeat ="INSERT INTO service_add (person,time,location,behavior,state_duration,user_pressure,service) VALUES ('"+sql_person.toString()+"' , " +
                                    " '"+sql_time.toString()+"' , '"+sql_location.toString()+"' , '"+sql_behavior.toString()+"' , '"+sql_duration.toString()+"' , '"+sql_pressure.toString()+"' , '"+sql_service.toString()+"') ";
                            try{
                                PreparedStatement pst = conn.prepareStatement(sql_repeat);
                                pst.executeUpdate();
                            }catch(Exception e){
                                e.printStackTrace();
                            }finally{
                                ConnectMysql.closeConn();
                            }
                        }
                    }).start();
                }
            }
        });
		btn_clear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tv_add.setText("");
			}
		});
		return mView;
	}
	private void setViews(){
		et_person = (EditText) mView.findViewById(R.id.et_person);
		et_time=(EditText)mView.findViewById(R.id.et_time);
		et_location=(EditText)mView.findViewById(R.id.et_location);
		et_behavior=(EditText)mView.findViewById(R.id.et_behavior);
		et_duration=(EditText)mView.findViewById(R.id.et_duration);
		et_pressure=(EditText)mView.findViewById(R.id.et_pressure);
		et_service=(EditText)mView.findViewById(R.id.et_service);
		tv_add=(TextView)mView.findViewById(R.id.tv_add);
		tv_add.setMovementMethod(ScrollingMovementMethod.getInstance());
		btn_add=(Button) mView.findViewById(R.id.btn_add);
		btn_query=(Button)mView.findViewById(R.id.btn_query);
		btn_generate=(Button) mView.findViewById(R.id.btn_generate);
		btn_clear=(Button) mView.findViewById(R.id.btn_clear);
		sp_person = (Spinner) mView.findViewById(R.id.sp_person);
		sp_time=(Spinner)mView.findViewById(R.id.sp_time);
		sp_location=(Spinner)mView.findViewById(R.id.sp_location);
		sp_behavior=(Spinner)mView.findViewById(R.id.sp_behavior);
		sp_duration=(Spinner)mView.findViewById(R.id.sp_duration);
		sp_pressure=(Spinner)mView.findViewById(R.id.sp_pressure);
		sp_service=(Spinner)mView.findViewById(R.id.sp_service);
	}

	private void setResult(){
		result=new StringBuffer();

		StringBuilder mk1 = new StringBuilder();
		sql_person = new StringBuilder();
		sql_time = new StringBuilder();
		sql_location = new StringBuilder();
		sql_behavior = new StringBuilder();
		sql_duration = new StringBuilder();
		sql_pressure = new StringBuilder();
		sql_service = new StringBuilder();
		mk1.append(et_person.getText().toString().trim());
		if(mk1.length()!=0){
			sql_person.append(et_person.getText().toString().trim());
			result.append("person:").append(et_person.getText().toString().trim()).append("; ");
		}else{
			sql_person.append(sp_person.getSelectedItem().toString().trim());
			result.append("person:").append(sp_person.getSelectedItem().toString().trim()).append("; ");
		}

		StringBuilder mk2=new StringBuilder();
		mk2.append(et_time.getText().toString().trim());
		if(mk2.length()!=0){
			sql_time.append(et_time.getText().toString().trim());
			result.append("time:").append(et_time.getText().toString().trim()).append("; ");
		}else{
			sql_time.append(sp_time.getSelectedItem().toString().trim());
			result.append("time:").append(sp_time.getSelectedItem().toString().trim()).append("; ");
		}

		StringBuilder mk3=new StringBuilder();
		mk3.append(et_location.getText().toString().trim());
		if(mk3.length()!=0){
			sql_location.append(et_location.getText().toString().trim());
			result.append("location:").append(et_location.getText().toString().trim()).append("; ");
		}else{
			sql_location.append(sp_location.getSelectedItem().toString().trim());
			result.append("location:").append(sp_location.getSelectedItem().toString().trim()).append("; ");
		}

		StringBuilder mk4=new StringBuilder();
		mk4.append(et_behavior.getText().toString().trim());
		if(mk4.length()!=0){
			sql_behavior.append(et_behavior.getText().toString().trim());
			result.append("behavior:").append(et_behavior.getText().toString().trim()).append("; ");
		}else{
			sql_behavior.append(sp_behavior.getSelectedItem().toString().trim());
			result.append("behavior:").append(sp_behavior.getSelectedItem().toString().trim()).append("; ");
		}

		StringBuilder mk5=new StringBuilder();
		mk5.append(et_duration.getText().toString().trim());
		if(mk5.length()!=0){
			sql_duration.append(et_duration.getText().toString().trim());
			result.append("duration:").append(et_duration.getText().toString().trim()).append("; ");
		}else{
			sql_duration.append(sp_duration.getSelectedItem().toString().trim());
			result.append("duration:").append(sp_duration.getSelectedItem().toString().trim()).append("; ");
		}

		StringBuilder mk6=new StringBuilder();
		mk6.append(et_pressure.getText().toString().trim());
		if(mk6.length()!=0){
			sql_pressure.append(et_pressure.getText().toString().trim());
			result.append("pressure:").append(et_pressure.getText().toString().trim()).append("; ");
		}else{
			sql_pressure.append(sp_pressure.getSelectedItem().toString().trim());
			result.append("pressure:").append(sp_pressure.getSelectedItem().toString().trim()).append("; ");
		}
		StringBuilder mk7=new StringBuilder();
		mk7.append(et_service.getText().toString().trim());
		if(mk7.length()!=0){
			sql_service.append(et_service.getText().toString().trim());
			result.append("service:").append(et_service.getText().toString().trim());
		}else{
			sql_service.append(sp_service.getSelectedItem().toString().trim());
			result.append("service:").append(sp_service.getSelectedItem().toString().trim());
		}
	}

	// 初始化用户类型的下拉框
	private void initTypeSpinner() {
		// 声明一个下拉列表的数组适配器
		ArrayAdapter<String> typeAdapter0 = new ArrayAdapter<String>(mContext,
				R.layout.item_select, person);
		// 设置数组适配器的布局样式
		typeAdapter0.setDropDownViewResource(R.layout.item_dropdown);
		// 从布局文件中获取名叫sp_type的下拉框
		//Spinner sp_person = mView.findViewById(R.id.sp_person);
		// 设置下拉框的标题
		sp_person.setPrompt("请选择用户类型");
		// 设置下拉框的数组适配器
		sp_person.setAdapter(typeAdapter0);
		// 设置下拉框默认显示第几项
		sp_person.setSelection(mType);
		// 给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
		sp_person.setOnItemSelectedListener(new TypeSelectedListener());

		ArrayAdapter<String> typeAdapter1 = new ArrayAdapter<String>(mContext,
				R.layout.item_select, time);
		// 设置数组适配器的布局样式
		typeAdapter1.setDropDownViewResource(R.layout.item_dropdown);
		// 从布局文件中获取名叫sp_type的下拉框
		//Spinner sp_time = mView.findViewById(R.id.sp_time);
		// 设置下拉框的标题
		sp_time.setPrompt("请选择时间类型");
		// 设置下拉框的数组适配器
		sp_time.setAdapter(typeAdapter1);
		// 设置下拉框默认显示第几项
		sp_time.setSelection(mType);
		// 给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
		sp_time.setOnItemSelectedListener(new TypeSelectedListener());

		ArrayAdapter<String> typeAdapter2 = new ArrayAdapter<String>(mContext,
				R.layout.item_select, location);
		typeAdapter2.setDropDownViewResource(R.layout.item_dropdown);
		//Spinner sp_location = mView.findViewById(R.id.sp_location);
		sp_location.setPrompt("请选择位置类型");
		sp_location.setAdapter(typeAdapter2);
		sp_location.setSelection(mType);
		sp_location.setOnItemSelectedListener(new TypeSelectedListener());

		ArrayAdapter<String> typeAdapter3 = new ArrayAdapter<String>(mContext,
				R.layout.item_select, behavior);
		typeAdapter3.setDropDownViewResource(R.layout.item_dropdown);
		//Spinner sp_behavior = mView.findViewById(R.id.sp_behavior);
		sp_behavior.setPrompt("请选择行为类型");
		sp_behavior.setAdapter(typeAdapter3);
		sp_behavior.setSelection(mType);
		sp_behavior.setOnItemSelectedListener(new TypeSelectedListener());

		ArrayAdapter<String> typeAdapter4 = new ArrayAdapter<String>(mContext,
				R.layout.item_select, duration);
		typeAdapter4.setDropDownViewResource(R.layout.item_dropdown);
		//Spinner sp_duration = mView.findViewById(R.id.sp_duration);
		sp_duration.setPrompt("请选择状态持续时间");
		sp_duration.setAdapter(typeAdapter4);
		sp_duration.setSelection(mType);
		sp_duration.setOnItemSelectedListener(new TypeSelectedListener());

		ArrayAdapter<String> typeAdapter5 = new ArrayAdapter<String>(mContext,
				R.layout.item_select, pressure);
		typeAdapter5.setDropDownViewResource(R.layout.item_dropdown);
		//Spinner sp_pressure = mView.findViewById(R.id.sp_pressure);
		sp_pressure.setPrompt("请选择用户压力类型");
		sp_pressure.setAdapter(typeAdapter5);
		sp_pressure.setSelection(mType);
		sp_pressure.setOnItemSelectedListener(new TypeSelectedListener());

		ArrayAdapter<String> typeAdapter6 = new ArrayAdapter<String>(mContext,
				R.layout.item_select, service);
		typeAdapter6.setDropDownViewResource(R.layout.item_dropdown);
		//Spinner sp_service = mView.findViewById(R.id.sp_service);
		sp_service.setPrompt("请选择用户服务类型");
		sp_service.setAdapter(typeAdapter6);
		sp_service.setSelection(mType);
		sp_service.setOnItemSelectedListener(new TypeSelectedListener());
	}

	// 定义用户类型的选择监听器
	class TypeSelectedListener implements AdapterView.OnItemSelectedListener {
		// 选择事件的处理方法，其中arg2代表选择项的序号
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			mType = arg2;
		}
		// 未选择时的处理方法，通常无需关注
		public void onNothingSelected(AdapterView<?> arg0) {}
	}

	private class Connect extends AsyncTask<Void,Void,String> {
		@Override
		protected String doInBackground(Void... voids) {
			String response1 = "";
			String sql_repeat;
			//and与双引号之间要有空格，不然还会出错，但是这是为什么呢？
			String sql_search = "select * from service_add where person='"+sql_person.toString()+"' and " +
                    "time='"+sql_time.toString()+"' and location='"+sql_location.toString()+"' and " +
                    "behavior='"+sql_behavior.toString()+"' and state_duration='"+sql_duration.toString()+"' and " +
                    "user_pressure='"+sql_pressure.toString()+"' and service='"+sql_service.toString()+"'";
			Connection conn = ConnectMysql.getConn();
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql_search);
				if (rs.next()) {//按数据库表的行获取数据
					sql_repeat = "服务规则库中有相同的规则属性，请勿重复添加";
				}else {
					sql_repeat = "可以添加此服务规则";
				}
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				//System.out.println(result);
				//tv_text.setText(result);这句话是错误的，在子线程中不能操作主线程中的控件，即布局中的控件，切记切记！！！而且这种情况下安装apk的时候有可能会出现闪退
				return sql_repeat;//return result是把子线程的结果传递给onPostExecute函数
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectMysql.closeConn();
			}
			return response1;
		}

		@Override
		protected void onPostExecute(String result) {
		    sql_repeat = new StringBuilder();
		    sql_repeat.append(result);
			tv_add.setText(result);
		}
	}
}
