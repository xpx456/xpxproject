package intersky.appbase;




import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {


	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	public void measureStatubar(BaseActivity context, RelativeLayout statubar) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) statubar.getLayoutParams();
		params.height = context.mToolBarHelper.statusBarHeight;
		statubar.setLayoutParams(params);
	}
	
}
