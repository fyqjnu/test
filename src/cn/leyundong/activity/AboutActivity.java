package cn.leyundong.activity;

import android.os.Bundle;
import cn.leyundong.R;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;

/**
 * 关于页面
 * @author chenjunjun
 *
 */
public class AboutActivity extends BaseActivity {
	
	@ViewRef(id=R.id.header) Header header;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.about);
		
		header.setTitle("更多");
	}
	
}
