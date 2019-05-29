package com.example.administrator.smarthome1.fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabFirstFragment extends Fragment {
    private static final String TAG = "TabFirstFragment";
    private String temperature,light,humidity;
    private String lamp,air,curtain;
    private String blood,bodyTemperature,pressure;


    protected View mView;
    protected Context mContext;
    Map<String,String> listItem = new HashMap<>();
    private TextView tv_temperatureShow,tv_lightShow,tv_humidityShow;
    private TextView tv_lampShow,tv_airShow,tv_curtainShow;
    private TextView tv_bloodShow,tv_bodyTemperatureShow,tv_pressureShow;
    private Button btn_temperature,btn_light,btn_humidity;
    private Button btn_lamp,btn_air,btn_curtain;
    private Button btn_blood,btn_bodyTemperature,btn_pressure;
    private Button btn_makesure,btn_clear;

    @Override
    //创建碎片视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //在Fragment中直接调用getActivity()方法，可以直接得到Fragment依附的Activity，而Activity是一个Context类型的对象
        mContext = getActivity();
        //inflate()方法一般接收两个参数，第一个参数就是要加载的布局id，第二个参数是指给该布局的外部再嵌套一层父布局，如果不需要就直接传null
        mView = inflater.inflate(R.layout.fragment_tab_first, container, false);
        tv_temperatureShow=(EditText) mView.findViewById(R.id.tv_temperatureShow);
        tv_lightShow=(EditText) mView.findViewById(R.id.tv_lightShow);
        tv_humidityShow=(EditText) mView.findViewById(R.id.tv_humidityShow);
        tv_lampShow=(EditText) mView.findViewById(R.id.tv_lampShow);
        tv_airShow=(EditText) mView.findViewById(R.id.tv_airShow);
        tv_curtainShow=(EditText) mView.findViewById(R.id.tv_curtainShow);
        tv_bloodShow=(EditText) mView.findViewById(R.id.tv_bloodShow);
        tv_bodyTemperatureShow=(EditText) mView.findViewById(R.id.tv_bodyTemperatureShow);
        tv_pressureShow=(EditText) mView.findViewById(R.id.tv_pressureShow);
        btn_temperature = (Button) mView.findViewById(R.id.btn_temperature);
        btn_light = (Button) mView.findViewById(R.id.btn_light);
        btn_humidity = (Button) mView.findViewById(R.id.btn_humidity);
        btn_lamp = (Button) mView.findViewById(R.id.btn_lamp);
        btn_air = (Button) mView.findViewById(R.id.btn_air);
        btn_curtain = (Button) mView.findViewById(R.id.btn_curtain);
        btn_blood = (Button) mView.findViewById(R.id.btn_blood);
        btn_bodyTemperature = (Button) mView.findViewById(R.id.btn_bodyTemperature);
        btn_pressure = (Button) mView.findViewById(R.id.btn_pressure);
        btn_makesure = (Button) mView.findViewById(R.id.btn_makesure);
        btn_clear = (Button) mView.findViewById(R.id.btn_clear);
        btn_makesure.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Connect conn = new Connect();
                conn.execute();//AsyncTask的execute方法要在主线程UI中执行！！
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_airShow.setText("");
                tv_bloodShow.setText("");
                tv_bodyTemperatureShow.setText("");
                tv_curtainShow.setText("");
                tv_humidityShow.setText("");
                tv_lampShow.setText("");
                tv_lightShow.setText("");
                tv_temperatureShow.setText("");
                tv_pressureShow.setText("");
            }
        });

        btn_temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        temperature = tv_temperatureShow.getText().toString();
                        Connection conn = ConnectMysql.getConn();
                        String sql_temperature = "update smart_home_message set temperature='"+temperature+"'";
                        try {
                            PreparedStatement pst = conn.prepareStatement(sql_temperature);
                            pst.execute();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }finally {
                            ConnectMysql.closeConn();
                        }
                    }
                }).start();
            }
        });
        btn_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        light = tv_lightShow.getText().toString();
                        Connection conn = ConnectMysql.getConn();
                        String sql_temperature = "update smart_home_message set light='"+light+"'";
                        try {
                            PreparedStatement pst = conn.prepareStatement(sql_temperature);
                            pst.execute();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }finally {
                            ConnectMysql.closeConn();
                        }
                    }
                }).start();
            }
        });
        btn_humidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        humidity = tv_humidityShow.getText().toString();
                        Connection conn = ConnectMysql.getConn();
                        String sql_temperature = "update smart_home_message set humidity='"+humidity+"'";
                        try {
                            PreparedStatement pst = conn.prepareStatement(sql_temperature);
                            pst.execute();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }finally {
                            ConnectMysql.closeConn();
                        }
                    }
                }).start();
            }
        });
        btn_lamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        lamp = tv_lampShow.getText().toString();
                        Connection conn = ConnectMysql.getConn();
                        String sql_temperature = "update smart_home_message set lamp='"+lamp+"'";
                        try {
                            PreparedStatement pst = conn.prepareStatement(sql_temperature);
                            pst.execute();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }finally {
                            ConnectMysql.closeConn();
                        }
                    }
                }).start();
            }
        });
        btn_air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        air = tv_airShow.getText().toString();
                        Connection conn = ConnectMysql.getConn();
                        String sql_temperature = "update smart_home_message set air='"+air+"'";
                        try {
                            PreparedStatement pst = conn.prepareStatement(sql_temperature);
                            pst.execute();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }finally {
                            ConnectMysql.closeConn();
                        }
                    }
                }).start();
            }
        });

        btn_curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        curtain = tv_curtainShow.getText().toString();
                        Connection conn = ConnectMysql.getConn();
                        String sql_temperature = "update smart_home_message set curtain='"+curtain+"'";
                        try {
                            PreparedStatement pst = conn.prepareStatement(sql_temperature);
                            pst.execute();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }finally {
                            ConnectMysql.closeConn();
                        }
                    }
                }).start();
            }
        });
        btn_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        blood = tv_bloodShow.getText().toString();
                        Connection conn = ConnectMysql.getConn();
                        String sql_temperature = "update smart_home_message set blood='"+blood+"'";
                        try {
                            PreparedStatement pst = conn.prepareStatement(sql_temperature);
                            pst.execute();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }finally {
                            ConnectMysql.closeConn();
                        }
                    }
                }).start();
            }
        });
        btn_bodyTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bodyTemperature = tv_bodyTemperatureShow.getText().toString();
                        Connection conn = ConnectMysql.getConn();
                        String sql_temperature = "update smart_home_message set body_temperature='"+bodyTemperature+"'";
                        try {
                            PreparedStatement pst = conn.prepareStatement(sql_temperature);
                            pst.execute();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }finally {
                            ConnectMysql.closeConn();
                        }
                    }
                }).start();
            }
        });
        btn_pressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        pressure = tv_pressureShow.getText().toString();
                        Connection conn = ConnectMysql.getConn();
                        String sql_temperature = "update smart_home_message set body_temperature='"+bodyTemperature+"'";
                        try {
                            PreparedStatement pst = conn.prepareStatement(sql_temperature);
                            pst.execute();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }finally {
                            ConnectMysql.closeConn();
                        }
                    }
                }).start();
            }
        });
        return mView;
    }


    private class Connect extends AsyncTask<Void,Void,Map<String,String>> {
        String user="root";
        String password = "mk143741";
        String url = "jdbc:mysql://10.20.4.164/mk1";
        String sql = "select * from smart_home_message";
        @Override
        protected Map<String,String> doInBackground(Void... voids){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn= DriverManager.getConnection(url,user,password);
                Statement stmt= conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                listItem = new HashMap<>();
                while(rs.next()){//按数据库表的行获取数据
                    listItem.put("temperature",rs.getString("temperature"));
                    listItem.put("light",rs.getString("light"));
                    listItem.put("humidity",rs.getString("humidity"));
                    listItem.put("lamp",rs.getString("lamp"));
                    listItem.put("air",rs.getString("air"));
                    listItem.put("curtain",rs.getString("curtain"));
                    listItem.put("blood",rs.getString("blood"));
                    listItem.put("body_temperature",rs.getString("body_temperature"));
                    listItem.put("pressure",rs.getString("pressure"));
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
                try{
                    conn.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
                //tv_text.setText(result);这句话是错误的，在子线程中不能操作主线程中的控件，即布局中的控件，切记切记！！！而且这种情况下安装apk的时候有可能会出现闪退
                return listItem;//return result是把子线程的结果传递给onPostExecute函数
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Map<String,String> result){
            tv_temperatureShow.setText(result.get("temperature"));
            tv_lightShow.setText(result.get("light"));
            tv_humidityShow.setText(result.get("humidity"));
            tv_lampShow.setText(result.get("lamp"));
            tv_airShow.setText(result.get("air"));
            tv_curtainShow.setText(result.get("curtain"));
            tv_bloodShow.setText(result.get("blood"));
            tv_bodyTemperatureShow.setText(result.get("body_temperature"));
            tv_pressureShow.setText(result.get("pressure"));
        }
    }
}
