package com.example.administrator.smarthome1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.administrator.smarthome1.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mList = new ArrayList<>();

    public MyAdapter(){}

    public MyAdapter(List<String> list,Context context){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount(){
        return  mList.size();
    }

    @Override
    public Object getItem(int i){
        return mList.get(i);
    }

    @Override
    public long getItemId(int i){
        return  i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_style, viewGroup,false);
            viewHolder.mTextView = (TextView) view.findViewById(R.id.tv_test);
            viewHolder.mButton = (Button) view.findViewById(R.id.btn_test);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTextView.setText(mList.get(i));
        viewHolder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemDeleteListener.onDeleteClick(i);
            }
        });
        return view;
    }

    //添加一个元素
    public void add(String data){
        if (mList == null){
            mList = new ArrayList<>();
        }
        mList.add(data);
        notifyDataSetChanged();
    }

    //往特定位置添加元素
    public void add(int position, String data){
        if (mList==null){
            mList = new ArrayList<>();
        }
        mList.add(position,data);
        notifyDataSetChanged();
    }
    //替换某个项目
    public void replace(int position,String data){
        if (mList==null){
            mList=new ArrayList<>();
        }
        mList.remove(position);
        mList.add(position,data);
        notifyDataSetChanged();
    }

    //移除所有记录
    public void clear(){
        if (mList==null){
            mList=new ArrayList<>();
        }
        mList.clear();
        notifyDataSetChanged();
    }
    //删除某一项
    public void remove(int position){
        if(mList==null){
            mList=new ArrayList<>();
        }
        mList.remove(position);
        notifyDataSetChanged();
    }

    //使字符串中的项目数与ListView中的对应
    public void adjust(){
        int mk;
        if (mList==null){
            mList=new ArrayList<>();
        }
        if (mList.size()>0){
            for(int i=0;i<mList.size();i++){
                mk=i+1;
                String[] listArray = mList.get(i).split(":");
                if (!listArray[0].equals("项目"+mk)){
                    mList.remove(i);
                    mList.add(i,"项目"+mk+":"+listArray[1]+":"+listArray[2]);
                }
            }
        }else{
            mList=new ArrayList<>();
        }
    }

    /**
     * 删除按钮的监听接口
     */
    public interface onItemDeleteListener {
        void onDeleteClick(int i);
    }

    private onItemDeleteListener mOnItemDeleteListener;

    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;

    }

    class ViewHolder {
        TextView mTextView;
        Button mButton;
    }
}
