package cn.leyundong.activity.clubpage;

import android.app.Activity;
import android.view.View;
import cn.leyundong.R;
import cn.leyundong.activity.page.AbstractAsyncPage;
import cn.quickdevelp.interfaces.IFindViewById;

/**
 * 发起活动
 * @author chenjunjun
 *
 */
public class ClubHuoDong extends AbstractAsyncPage implements IFindViewById{

	private Activity act;
	
	private View view;
	
	public ClubHuoDong(Activity act){
		this.act = act;
		
		view = act.getLayoutInflater().inflate(R.layout.clubhuodong, null);
	}
	
	@Override
	public View getView() {
		return view;
	}

	@Override
	public View getViewById(int id) {
		return view.findViewById(id);
	}

}
