package com.example.administrator.smarthome1.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.administrator.smarthome1.R;
import com.example.administrator.smarthome1.adapter.MyAdapter;
import com.example.administrator.smarthome1.util.ConnectMysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class TabFourthFragment extends Fragment {
    private static final String TAG = "TabFourthFragment";
    protected View mView;
    protected Context mContext;
    private Spinner sp_service;
    private TextView tv_time_select;
    private TimePicker timePicker;
    private Calendar calendar;
    private Calendar calendar1;
    private Button btn_add_service;
    private Button btn_clear;
    private ListView lv_service_list;
    private TextView tv_dialog;
    private Spinner sp_dialog;
    private static Bundle outState;
    private boolean saveListview=false;

    private int hour;
    private int hour1;
    private int minute;
    private int minute1;
    private int mType=0;
    private int flag=1;
    private boolean serviceRepeat=true;
    private String[] service = {"服务1","服务2","服务3","服务4","服务5","服务6"};
    public StringBuilder serviceItem;
    private StringBuilder adjust;
    private ArrayList<String> list_service=new ArrayList<String>();
    private MyAdapter myAdapter;
    private Set<String> set = new HashSet<>();

    @Override
    //创建碎片视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //在Fragment中直接调用getActivity()方法，可以直接得到Fragment依附的Activity，而Activity是一个Context类型的对象
        mContext = getActivity();
        //inflate()方法一般接收两个参数，第一个参数就是要加载的布局id，第二个参数是指给该布局的外部再嵌套一层父布局，如果不需要就直接传null
        mView = inflater.inflate(R.layout.fragment_tab_fourth, container, false);
        //在Fragment的onCreateView函数中调用getArguments方能获取请求数据

        SharedPreferences sharedPreferences1 = mContext.getSharedPreferences("myData1",Context.MODE_PRIVATE);
        sharedPreferences1.getStringSet("list_service",set);
        list_service = new ArrayList<>(new HashSet<>(sharedPreferences1.getStringSet("list_service",set)));
        sp_service = (Spinner) mView.findViewById(R.id.sp_service);
        tv_time_select = (TextView) mView.findViewById(R.id.tv_time_select);
        lv_service_list = (ListView) mView.findViewById(R.id.lv_service_list);
        btn_add_service = (Button) mView.findViewById(R.id.btn_add_service);
        btn_clear = (Button) mView.findViewById(R.id.btn_clear);
        if (savedInstanceState!=null){
            list_service=savedInstanceState.getStringArrayList("list_service");
        }
        if (outState!=null){
            list_service=outState.getStringArrayList("list_service");
        }
        if (list_service==null){
            list_service=new ArrayList<String>();
        }
        myAdapter = new MyAdapter(list_service,mContext);
        myAdapter.adjust();
        initTypeSpinner();
        setTime();//设定时间
        setListView();

        btn_add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceItem = new StringBuilder("项目"+flag+":"+"时间*"+tv_time_select.getText().toString()
                        +" + " +sp_service.getSelectedItem().toString());
                String[] strArray1 = serviceItem.toString().split("\\*");
                for (int i=0;i<lv_service_list.getCount();i++){
                    if (list_service.size()>0){
                        String[] strArray2 = list_service.get(i).split("\\*");
                        if ((strArray1[1].trim()).equals(strArray2[1].trim())){
                            serviceRepeat=false;
                        }else {
                            serviceRepeat=true;
                        }
                    }
                }

                if (tv_time_select.getText().toString().equals("")){
                    Toast.makeText(mContext,"请选择时间",Toast.LENGTH_SHORT).show();
                }else {
                    if(serviceRepeat){
                        flag++;
                        myAdapter.add(serviceItem.toString());
                        myAdapter.adjust();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Connection conn = ConnectMysql.getConn();
                                String sql_add = "insert into alarm_service (time,service) values ('"+tv_time_select.getText().toString()+"','"+sp_service.getSelectedItem().toString()+"')";
                                try {
                                    PreparedStatement pst = conn.prepareStatement(sql_add);
                                    pst.execute();
                                }catch (SQLException e){
                                    e.printStackTrace();
                                }finally {
                                    ConnectMysql.closeConn();
                                }
                            }
                        }).start();
                    }else{
                        Toast.makeText(mContext,"服务添加重复，请重新选择",Toast.LENGTH_SHORT).show();
                    }
                }
                //重新进入时可以载入数据
                savedListService();
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow(v);
            }
        });

        return mView;
    }

    //初始化用户下拉框
    private void initTypeSpinner() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(mContext,
                R.layout.item_select, service);
        typeAdapter.setDropDownViewResource(R.layout.item_dropdown);

        sp_service.setPrompt("请选择服务类型");
        sp_service.setAdapter(typeAdapter);
        sp_service.setSelection(mType);
    }

    //TextView获取设定的时间
    public void setTime(){
        timePicker = (TimePicker)mView.findViewById(R.id.tp_add);
        //设置点击事件不弹键盘
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        calendar = Calendar.getInstance();
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        //使timepicker显示当前时间
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int min) {
                String desc = String.format("%d : %d", hourOfDay, min);
                tv_time_select.setText(desc);
            }
        });
    }
    //ListView点击item时触发事件
    public void setListView(){

        lv_service_list.setAdapter(myAdapter);
        lv_service_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //parent：哪个AdapterView;view：你点击的Listview的某一项的内容，来源于adapter。如用((TextView)view).getText().toString()，可以取出点击的这一项的内容
            //position：是adapter的某一项的位置，如点击了listview第2项，而第2项对应的是adapter的第2个数值，那此时position的值就为1了。
            //id：值为点击了Listview的哪一项对应的数值，点击了listview第2项，那id就等于1。
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final int mk=position;
                String[] service1 = {"服务1","服务2","服务3","服务4","服务5","服务6"};
                ArrayAdapter<String> typeAdapter1 = new ArrayAdapter<String>(mContext,
                        R.layout.item_select, service1);
                typeAdapter1.setDropDownViewResource(R.layout.item_dropdown);
                View view1 = (LinearLayout)getLayoutInflater().inflate(R.layout.listview_dialog,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("请修改服务项目");
                builder.setView(view1);
                TimePicker tp_dialog=(TimePicker)view1.findViewById(R.id.tp_dialog);

                tv_dialog = (TextView)view1.findViewById(R.id.tv_dialog);
                sp_dialog = (Spinner)view1.findViewById(R.id.sp_dialog);
                sp_dialog.setPrompt("请选择服务类型");
                sp_dialog.setAdapter(typeAdapter1);
                sp_dialog.setSelection(mType);
                tp_dialog.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
                calendar1 = Calendar.getInstance();
                hour1=calendar1.get(Calendar.HOUR_OF_DAY);
                minute1 = calendar1.get(Calendar.MINUTE);
                tp_dialog.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int min) {
                        String desc = String.format("%d : %d", hourOfDay, min);
                        tv_dialog.setText(desc);
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (tv_dialog.getText().toString().equals("")){
                            Toast.makeText(mContext,"请选择时间",Toast.LENGTH_SHORT).show();
                        }else {
                            adjust = new StringBuilder("项目"+mk+":"+"时间*"+tv_dialog.getText().toString()
                                    +" + " +sp_dialog.getSelectedItem().toString());
                            myAdapter.replace(mk,adjust.toString());
                            myAdapter.adjust();
                            savedListService();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int mk_mysql = position+1;
                                    Connection conn = ConnectMysql.getConn();
                                    String sql_add = "update alarm_service set time = '"+tv_dialog.getText().toString()+"',service = '"+sp_dialog.getSelectedItem().toString()+"' where id ='"+mk_mysql+"'";
                                    try {
                                        PreparedStatement pst = conn.prepareStatement(sql_add);
                                        pst.execute();
                                    }catch (SQLException e){
                                        e.printStackTrace();
                                    }finally {
                                        ConnectMysql.closeConn();
                                    }
                                }
                            }).start();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        //ListView item中的删除按钮点击事件
        myAdapter.setOnItemDeleteClickListener(new MyAdapter.onItemDeleteListener() {
            @Override
            public void onDeleteClick(final int i) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int mk_mysql = i+1;
                        Connection conn = ConnectMysql.getConn();
                        String sql_add = "DELETE FROM alarm_service WHERE id = '"+mk_mysql+"'";
                        String sql_order1 = "ALTER TABLE alarm_service DROP id";
                        String sql_order2 = "ALTER TABLE alarm_service ADD id MEDIUMINT(8)";
                        String sql_order3 = "ALTER TABLE alarm_service MODIFY COLUMN id MEDIUMINT(8) AUTO_INCREMENT,ADD PRIMARY KEY(id)";
                        try {
                            PreparedStatement pst = conn.prepareStatement(sql_add);
                            PreparedStatement pst1 = conn.prepareStatement(sql_order1);
                            PreparedStatement pst2 = conn.prepareStatement(sql_order2);
                            PreparedStatement pst3 = conn.prepareStatement(sql_order3);
                            pst.execute();
                            pst1.execute();
                            pst2.execute();
                            pst3.execute();

                        }catch (SQLException e){
                            e.printStackTrace();
                        }finally {
                            ConnectMysql.closeConn();
                        }
                    }
                }).start();
                myAdapter.remove(i);
                myAdapter.adjust();
                myAdapter.notifyDataSetChanged();
                savedListService();

            }
        });

    }
    //悬浮窗设置,按下清除按钮时触发
    private void initPopWindow(View v) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popupwindow, null, false);
        Button btn_makesure = (Button) view.findViewById(R.id.btn_makesure);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setAnimationStyle(R.xml.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效

        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 50, 0);

        //设置popupWindow里的按钮的事件
        btn_makesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Connection conn = ConnectMysql.getConn();
                        String sql_add = "TRUNCATE TABLE alarm_service";
                        try {
                            PreparedStatement pst = conn.prepareStatement(sql_add);
                            pst.execute();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }finally {
                            ConnectMysql.closeConn();
                        }
                    }
                }).start();
                myAdapter.clear();
                savedListService();
                flag=1;
                serviceRepeat=true;
                popWindow.dismiss();


            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });
    }
    @Override
    public void onPause(){
        super.onPause();
        if(saveListview){
            if (outState!=null){
                outState.clear();
                outState=null;
            }
            outState = new Bundle();
            outState.putStringArrayList("list_service",list_service);
        }else{
            if (outState!=null){
                outState.clear();
                outState=null;
            }

        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        Log.i(TAG,"onSaveInstanceState is saved");
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("list_service",list_service);
    }
    //重新进入时可以载入数据，这里为保存数据
    public void savedListService(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myData1",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = new HashSet<>();
        set.addAll(list_service);
        editor.putStringSet("list_service",set);
        editor.apply();
    }
}