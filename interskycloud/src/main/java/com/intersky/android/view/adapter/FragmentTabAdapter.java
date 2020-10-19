package com.intersky.android.view.adapter;

import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.intersky.R;

import java.util.List;



/**
 * Created with IntelliJ IDEA.
 * Author: wangjie  email:tiantian.china.2@gmail.com
 * Date: 13-10-10
 * Time: 上午9:25
 */
public class FragmentTabAdapter {
    private List<Fragment> fragments; // 一个tab页面对应一个Fragment
    private AppCompatActivity fragmentActivity; // Fragment所属的Activity
    private int fragmentContentId; // Activity中所要被替换的区域的id
    private int checkedid = 0;
    private int currentTab; // 当前Tab页面索引

    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 用于让调用者在切换tab时候增加新的功能

    public FragmentTabAdapter(AppCompatActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId) {
        this.fragments = fragments;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        // 默认显示第一页
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(0));
        ft.commit();

    }

    public void onCheckedChanged(int checkedId) {
    	if(this.checkedid != checkedId)
    	{
    		this.checkedid = checkedId;
    		 Fragment fragment = fragments.get(this.checkedid);
    		 FragmentTransaction ft = obtainFragmentTransaction(this.checkedid);
    		 getCurrentFragment().onPause();
    		 if(fragment.isAdded()){
               fragment.onStart(); // 启动目标tab的onStart()
//               fragment.onResume(); // 启动目标tab的onResume()
           }else{
               ft.add(fragmentContentId, fragment);
           }
           showTab(this.checkedid); // 显示目标tab
           ft.commit();
           // 如果设置了切换tab额外功能功能接口
    	}

    }

    /**
     * 切换tab
     * @param idx
     */
    private void showTab(int idx){
        for(int i = 0; i < fragments.size(); i++){
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);

            if(idx == i){
                ft.show(fragment);
            }else{
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx; // 更新目标tab为当前tab
    }

    /**
     * 获取一个带动画的FragmentTransaction
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index){
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        // 设置切换动画
        if(index > currentTab){
            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        }else{
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        return ft;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment(){
        return fragments.get(currentTab);
    }

    public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
        return onRgsExtraCheckedChangedListener;
    }

    public void setOnRgsExtraCheckedChangedListener(OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
        this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
    }

    /**
     *  切换tab额外功能功能接口
     */
    static class OnRgsExtraCheckedChangedListener{
        public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index){

        }
    }

}