package intersky.appbase;

import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * Author: wangjie  email:tiantian.china.2@gmail.com
 * Date: 13-10-10
 * Time: 涓婂崍9:25
 */
public class PadFragmentTabAdapter {
    private List<Fragment> fragments; // 涓€涓猼ab椤甸潰瀵瑰簲涓€涓狥ragment
    private PadFragmentBaseActivity fragmentActivity; // Fragment鎵€灞炵殑Activity
    private int fragmentContentId; // Activity涓墍瑕佽鏇挎崲鐨勫尯鍩熺殑id
    private int checkedid = 0;
    private int currentTab; // 褰撳墠Tab椤甸潰绱㈠紩

    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 鐢ㄤ簬璁╄皟鐢ㄨ€呭湪鍒囨崲tab鏃跺€欏鍔犳柊鐨勫姛鑳?

    public PadFragmentTabAdapter(PadFragmentBaseActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId) {
        this.fragments = fragments;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        // 榛樿鏄剧ず绗竴椤?
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
               fragment.onStart(); // 鍚姩鐩爣tab鐨刼nStart()
//               fragment.onResume(); // 鍚姩鐩爣tab鐨刼nResume()
           }else{
               ft.add(fragmentContentId, fragment);
           }
           showTab(this.checkedid); // 鏄剧ず鐩爣tab
           ft.commit();
           // 濡傛灉璁剧疆浜嗗垏鎹ab棰濆鍔熻兘鍔熻兘鎺ュ彛
    	}

    }

    /**
     * 鍒囨崲tab
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
        currentTab = idx; // 鏇存柊鐩爣tab涓哄綋鍓峵ab
    }

    /**
     * 鑾峰彇涓€涓甫鍔ㄧ敾鐨凢ragmentTransaction
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index){
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        // 璁剧疆鍒囨崲鍔ㄧ敾
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
     *  鍒囨崲tab棰濆鍔熻兘鍔熻兘鎺ュ彛
     */
    static class OnRgsExtraCheckedChangedListener{
        public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index){

        }
    }

}