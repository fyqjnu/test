package cn.leyundong.activity.clubpage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import cn.leyundong.MyApplication;
import cn.leyundong.R;
import cn.leyundong.activity.BaseActivity;
import cn.leyundong.constant.RequestId;
import cn.leyundong.constant.Urls;
import cn.leyundong.entity.BeanProxy;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.JuLeBuBean;
import cn.leyundong.entity.Result;
import cn.leyundong.json.JsonParser;
import cn.leyundong.view.Header;
import cn.quickdevelp.http.HttpPostDuty;
import cn.quickdevelp.http.IHttpDuty;
import cn.quickdevelp.http.NetCheckHttpDuty;
import cn.quickdevelp.interfaces.IJson;
import cn.quickdevelp.json.JsonUtil;

/**
 * 俱乐部会员财务详细
 * @author chenjunjun
 *
 */
public class ClubCaiWuDetailActivityNew extends BaseActivity {

	private ClubCaiWuDetailPage page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		JuLeBuBean extraJuLeBu = (JuLeBuBean) getIntent().getSerializableExtra(JuLeBuBean.class.getSimpleName());
		if (extraJuLeBu == null) {
			finish();
			return ;
		}
		
		page = new ClubCaiWuDetailPage(act, extraJuLeBu);
		setContentView(page.getView());
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		page.onDestroy();
	}
	
	
}
