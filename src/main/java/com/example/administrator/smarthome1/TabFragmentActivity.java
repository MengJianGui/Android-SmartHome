package com.example.administrator.smarthome1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


import com.example.administrator.smarthome1.fragment.TabFirstFragment;
import com.example.administrator.smarthome1.fragment.TabSecondFragment;
import com.example.administrator.smarthome1.fragment.TabThirdFragment;
import com.example.administrator.smarthome1.fragment.TabFourthFragment;

public class TabFragmentActivity extends FragmentActivity {
    private static final String TAG = "TabFragmentActivity";
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_fragment);

        Bundle bundle = new Bundle();
        //bundle设置数据的方式
        bundle.putString("tag", TAG);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        /////////addTab(标题，跳转的Fragment，传递参数的Bundle)//////////
        mTabHost.addTab(getTabView(R.string.menu_first, R.drawable.tab_first_selector1),
                TabFirstFragment.class, bundle);
        mTabHost.addTab(getTabView(R.string.menu_second, R.drawable.tab_second_selector),
                TabSecondFragment.class, bundle);
        mTabHost.addTab(getTabView(R.string.menu_third, R.drawable.tab_third_selector),
                TabThirdFragment.class, bundle);
        mTabHost.addTab(getTabView(R.string.menu_fourth, R.drawable.tab_fourth_selector),
                TabFourthFragment.class, bundle);

        //设置tabs之间的分隔线不显示
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }
    //一个TabSpec通常包含了indicator(指示器)、content（tab对应展示的页面view，可以为这个view的id或者是一个intent）、tag
    private TabSpec getTabView(int textId, int imgId) {

        //getResources().getString获取strings.xml中的字符串的值。尽量将字符串定义在strings.xml是一种非常好的习惯
        String text = getResources().getString(textId);
        Drawable drawable = ContextCompat.getDrawable(this,imgId);

        //必须设置图片大小，否则不显示
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        //LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化；
        View item_tabbar = getLayoutInflater().inflate(R.layout.item_tabbar, null);

        //而findViewById()是找xml布局文件下的具体widget控件(如Button、TextView等)
        TextView tv_item = (TextView) item_tabbar.findViewById(R.id.tv_item_tabbar);

        tv_item.setText(text);//这个text是底部标签栏的名称

        //底部标签的图片在标签名称的上面
        tv_item.setCompoundDrawables(null, drawable, null, null);
        TabSpec spec = mTabHost.newTabSpec(text).setIndicator(item_tabbar);
        return spec;
    }
    /**
     * 点击空白位置 隐藏软键盘
     */
    public boolean onTouchEvent(MotionEvent event) {
        if(null != TabFragmentActivity.this.getCurrentFocus()){
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {


                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
// 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
// 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
// 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}

