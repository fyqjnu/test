package cn.leyundong.activity.clubpage;

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
public class ClubCaiWuDetailActivity extends Activity {
	
	private Header mHeader;
	private ListView mLvClubCaiWuDetail;
	private ClubCaiWuDetailAdapter mAdapter;
	private List<JuLeBuBean> mClubCaiWu;
	private ProgressDialog mDialog;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.club_caiwu_detail);
		initViews();
	}
	
	private void initViews(){
		mContext = ClubCaiWuDetailActivity.this;
		mClubCaiWu = new ArrayList<JuLeBuBean>();
		
		mHeader = (Header)findViewById(R.id.header);
		mHeader.setTitle("俱乐部会员财务明细");
//		mLvClubCaiWuDetail = (ListView)findViewById(R.id.lvClubCaiWuDetail);
		
		JuLeBuBean objClub = (JuLeBuBean)getIntent().getSerializableExtra("ClubDetail");
		System.out.println("俱乐部= " + objClub);
		
		ChaXunBean objInfo = new ChaXunBean();
		objInfo.yhid = MyApplication.mUser.yhid;
		objInfo.jlbid = objClub.jlbid;
		objInfo.dlryid = objClub.yhid;
		
		mDialog =ProgressDialog.show(this, "", "Loading...");
		new GetClubCaiWuDetailTask().execute(objInfo);
		
	}
	
	
	class GetClubCaiWuDetailTask extends AsyncTask<ChaXunBean, Integer, JuLeBuBean[]>{

		@Override
		protected JuLeBuBean[] doInBackground(ChaXunBean... params) {
			String url = Urls.getUrl(Urls.TYPE_CLUB, RequestId.CLUB_CAIWU_DETAIL);
			List<IJson> list = new ArrayList<IJson>();
			list.add(new BeanProxy<ChaXunBean>(params[0]));
			System.out.println("url=" + url);
			IHttpDuty duty = new NetCheckHttpDuty(mContext, new HttpPostDuty(mContext, url, list));
			String post = duty.post();
			System.out.println("chaxun=" + post);
			Result result = JsonParser.getResultFromJson(post);
			if (result.success) {
				return doWithJson(post);
			}
			return null;
		}

		
		@Override
		protected void onPostExecute(JuLeBuBean[] result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mAdapter = new ClubCaiWuDetailAdapter(mContext, Arrays.asList(result));
			mLvClubCaiWuDetail.setAdapter(mAdapter);
			mDialog.dismiss();
		}



		private JuLeBuBean[] doWithJson(String json) {
			try {
				JSONObject obj = new JSONObject(json);
				JSONObject page = obj.getJSONObject("page");
				JSONArray ary = page.getJSONArray("list");
				JuLeBuBean[] infos = JsonUtil.parseJsonArray(ary.toString(), JuLeBuBean.class);
				System.out.println("---------------------");
				System.out.println(Arrays.asList(infos));
				return infos;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	class ClubCaiWuDetailAdapter extends BaseAdapter{
		
		private Context ctx;
		private List<JuLeBuBean> mClubCaiWus;
		
		public ClubCaiWuDetailAdapter(Context paramCtx, List<JuLeBuBean> paramCaiWus){
			ctx = paramCtx;
			mClubCaiWus = paramCaiWus;
		}

		@Override
		public int getCount() {
			return mClubCaiWus.size();
		}

		@Override
		public Object getItem(int position) {
			return mClubCaiWus.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ClubCaiWuDetailViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(ctx).inflate(R.layout.item_club_caiwu_detail, null);
				holder = new ClubCaiWuDetailViewHolder();
				holder.tvName = (TextView)convertView.findViewById(R.id.tvName);
				holder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);
				holder.tvDate = (TextView)convertView.findViewById(R.id.tvType);
				holder.tvMoney1 = (TextView)convertView.findViewById(R.id.tvMoney1);
				holder.tvMoney2 = (TextView)convertView.findViewById(R.id.tvMoney2);
				convertView.setTag(holder);
			}else{
				holder = (ClubCaiWuDetailViewHolder)convertView.getTag();
			}
			
			holder.tvName.setText("");
			holder.tvDate.setText("");
			holder.tvDate.setText("");
			holder.tvMoney1.setText("");
			holder.tvMoney2.setText("");
			
			return convertView;
		}
		
		
	}
	
	class ClubCaiWuDetailViewHolder{
		TextView tvName;
		TextView tvDate;
		TextView tvType;
		TextView tvMoney1;
		TextView tvMoney2;
	}

}
