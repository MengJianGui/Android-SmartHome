package com.example.administrator.smarthome1.fragment;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.smarthome1.R;
import com.example.administrator.smarthome1.TabFragmentActivity;
import com.example.administrator.smarthome1.util.ConnectMysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabSecondFragment extends Fragment {
	private static final String TAG = "TabSecondFragment";
	String order="";
	String modify="";
	private ArrayList<String> list;
	protected View mView;
	protected Context mContext;
	private TextView tv_service,tv_makesure;
	private EditText et_modifyservice;
	private Button btn_service;
	private Button btn_clear;
	private Button btn_list;
	private Button btn_makesure;
	private Button btn_delete;
	private RadioGroup rg_feedback;
	private RadioButton rb_unsatisfied;
	private RadioButton rb_satisfied;
	private RadioButton rb_justsoso;
	private LinearLayout ll_modifyservice;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0x001:
					et_modifyservice.setText(msg.obj.toString());
					modify=msg.obj.toString();
					break;
				case 0x002:
					et_modifyservice.setText(msg.obj.toString());
					modify=msg.obj.toString();
					break;
				case 0x003:
					et_modifyservice.setText(msg.obj.toString());
					modify=msg.obj.toString();
					break;
				case 0x004:
					et_modifyservice.setText(msg.obj.toString());
					modify=msg.obj.toString();
					break;
				case 0x005:
					et_modifyservice.setText(msg.obj.toString());
					modify=msg.obj.toString();
					break;
				case 0x006:
					et_modifyservice.setText(msg.obj.toString());
					modify=msg.obj.toString();
					break;
				default:
					break;


			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		//inflate()方法一般接收两个参数，第一个参数就是要加载的布局id，第二个参数是指给该布局的外部再嵌套一层父布局，如果不需要就直接传null
		mView = inflater.inflate(R.layout.fragment_tab_second, container, false);
		//在Fragment的onCreateView函数中调用getArguments方能获取请求数据
		String desc = String.format("我是%s页面，来自%s",
				"服务反馈", getArguments().getString("tag"));//bundle传递数据，接受来自activity传来的tag对应的的字符串
		Log.i(TAG,desc);
		setViews();
		return mView;
	}
	//对控件进行注册
	private void setViews(){
		tv_service=(TextView)mView.findViewById(R.id.tv_service);
		tv_service.setMovementMethod(ScrollingMovementMethod.getInstance());
		tv_makesure=(TextView)mView.findViewById(R.id.tv_makesure);
		btn_service=(Button)mView.findViewById(R.id.btn_service);
		btn_clear=(Button)mView.findViewById(R.id.btn_clear);
		btn_makesure=(Button)mView.findViewById(R.id.btn_makesure);
		btn_delete=(Button)mView.findViewById(R.id.btn_delete);
		btn_list=(Button)mView.findViewById(R.id.btn_list);
		rg_feedback = mView.findViewById(R.id.rg_feedback);
		rb_unsatisfied = mView.findViewById(R.id.rb_unsatisfied);
		rb_satisfied = mView.findViewById(R.id.rb_satisfied);
		rb_justsoso = mView.findViewById(R.id.rb_justsoso);
		et_modifyservice = mView.findViewById(R.id.et_modifyservice);
		ll_modifyservice = mView.findViewById(R.id.ll_modifyservice);
		registerForContextMenu(btn_list);//给btn_list设置上下文弹出菜单，要长按才可以
		// 给rg_login设置单选监听器
		rg_feedback.setOnCheckedChangeListener(new RadioListener());
		btn_service.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(v.getId()==R.id.btn_service){
					TabSecondFragment.Connect conn1 = new TabSecondFragment.Connect();
					conn1.execute();
				}
			}
		});
		btn_clear.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(v.getId()==R.id.btn_clear){
					String clear="请点击获取服务信息";
					tv_service.setText(clear);
				}
			}
		});
		btn_makesure.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(v.getId()==R.id.btn_makesure){
					TabSecondFragment.ModifyMySql conn2 = new TabSecondFragment.ModifyMySql();
					conn2.execute();
				}
			}
		});
		btn_delete.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(v.getId()==R.id.btn_delete){
					new Thread(new Runnable() {
						@Override
						public void run() {
							Connection conn = ConnectMysql.getConn();
							String sql4 ="delete * from service_add where id='"+order+"'";
							try{
								PreparedStatement pst = conn.prepareStatement(sql4);
								pst.executeUpdate();
							}catch(Exception e){
								e.printStackTrace();
								tv_service.setText(e.toString());
							}finally{
								ConnectMysql.closeConn();
							}
						}
					}).start();
				}
			}
		});
	}
	@Override
	// 重写上下文菜单的创建方法
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		MenuInflater inflator = new MenuInflater(mContext);
		inflator.inflate(R.menu.menu_service, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	// 上下文菜单被点击时触发该方法
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		String service1 = "服务1";
		String service2 = "服务2";
		String service3 = "服务3";
		String service4 = "服务4";
		String service5 = "服务5";
		String service6 = "服务6";
		Message message = new Message();
		switch(item.getItemId()){
			case R.id.one:
				message.what = 0x001;
				message.obj = service1;
				handler.sendMessage(message);
				break;
			case R.id.two:
				message.what = 0x002;
				message.obj = service2;
				handler.sendMessage(message);
				break;
			case R.id.three:
				message.what = 0x003;
				message.obj = service3;
				handler.sendMessage(message);
				break;
			case R.id.four:
				message.what = 0x004;
				message.obj = service4;
				handler.sendMessage(message);
				break;
			case R.id.five:
				message.what = 0x005;
				message.obj = service5;
				handler.sendMessage(message);
				break;
			case R.id.six:
				message.what = 0x006;
				message.obj = service6;
				handler.sendMessage(message);
				break;
		}
		return true;
	}


	// 定义登录方式的单选监听器
	private class RadioListener implements RadioGroup.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == R.id.rb_unsatisfied) { //选择了不满意
				ll_modifyservice.setVisibility(View.VISIBLE);
			} else if (checkedId == R.id.rb_justsoso) { //选择了一般
				ll_modifyservice.setVisibility(View.INVISIBLE);
			}else if (checkedId == R.id.rb_satisfied){ //选择了满意
				ll_modifyservice.setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * 获取正在进行的服务
	 * 从慕课网上看到的视频说，在SQL语句中select语句最好不要用*，否则会被老板骂，也就是把每一列写出来，还说了连接数据库的语句要是记不得就要去面壁了！！！
	 */
	private class Connect extends AsyncTask<Void,Void,Object> {
		String sql3="select * from on_service";
		@Override
		protected Object doInBackground(Void... voids){
			String response1="";
			Connection conn = ConnectMysql.getConn();
			list=new ArrayList<>();
			try{
				Statement stmt= conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql3);
				ResultSetMetaData rsmd = rs.getMetaData();
				while(rs.next()){//按数据库表的行获取数据
					list.add(rsmd.getColumnName(1)+":"+rs.getString(1)+"; ");
					list.add(rsmd.getColumnName(2)+":"+rs.getString(2)+"; ");
					list.add(rsmd.getColumnName(3)+":"+rs.getString(3)+"; ");
					list.add(rsmd.getColumnName(4)+":"+rs.getString(4)+"; ");
					list.add(rsmd.getColumnName(5)+":"+rs.getString(5)+"; ");
					list.add(rsmd.getColumnName(6)+":"+rs.getString(6)+"; ");
					list.add(rsmd.getColumnName(7)+":"+rs.getString(7)+"; ");
					list.add(rsmd.getColumnName(8)+":"+rs.getString(8));
				}
				try{
					rs.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
				try{
					stmt.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
				//System.out.println(result);
				//tv_text.setText(result);这句话是错误的，在子线程中不能操作主线程中的控件，即布局中的控件，切记切记！！！而且这种情况下安装apk的时候有可能会出现闪退
				return list;//return result是把子线程的结果传递给onPostExecute函数
			}catch(Exception e){
				e.printStackTrace();
				tv_service.setText(e.toString());
			}finally{
				ConnectMysql.closeConn();
			}
			return response1;
		}
		@Override
		protected void onPostExecute(Object result){
			String tvservice="";
			for(int i = 0 ; i<list.size();i++){
				tvservice+=list.get(i);
			}
			tv_service.setText(tvservice);
			order=list.get(0);
			System.out.println(order);
			order=order.substring(order.length()-3,order.length()-2);
			System.out.println(order);
			//service=list.get(3).substring(6);
		}
	}
	//用户更改服务需要开启的子线程
	private class ModifyMySql extends AsyncTask<Void,Void,String> {
		String sql1 = "update service_add set service='"+modify+"' where id='"+order+"'";
		String sql2="select * from service_add where id='"+order+"'";
		@Override
		protected String doInBackground(Void... voids){
			String response2="";
			Connection conn = ConnectMysql.getConn();
			try{
				String result="";
				PreparedStatement pst = conn.prepareStatement(sql1);
				pst.executeUpdate();
				Statement stmt= conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql2);
				ResultSetMetaData rsmd = rs.getMetaData();
				while(rs.next()){//按数据库表的行获取数据
					result+=rsmd.getColumnName(1)+":"+rs.getString(1)+"; ";
					result+=rsmd.getColumnName(2)+":"+rs.getString(2)+"; ";
					result+=rsmd.getColumnName(3)+":"+rs.getString(3)+"; ";
					result+=rsmd.getColumnName(4)+":"+rs.getString(4)+"; ";
					result+=rsmd.getColumnName(5)+":"+rs.getString(5)+"; ";
					result+=rsmd.getColumnName(6)+":"+rs.getString(6)+"; ";
					result+=rsmd.getColumnName(7)+":"+rs.getString(7)+"; ";
					result+=rsmd.getColumnName(8)+":"+rs.getString(8);
				}
				try{
					rs.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
				try{
					stmt.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
				//System.out.println(modify);
				System.out.println(order);
				//System.out.println(result);
				//tv_text.setText(result);这句话是错误的，在子线程中不能操作主线程中的控件，即布局中的控件，切记切记！！！而且这种情况下安装apk的时候有可能会出现闪退
				return result;//return result是把子线程的结果传递给onPostExecute函数
			}catch(Exception e){
				e.printStackTrace();
				tv_makesure.setText(e.toString());
			}finally{
				ConnectMysql.closeConn();
			}
			return response2;
		}
		@Override
		protected void onPostExecute(String result){
			tv_makesure.setText(result);
		}
	}
}