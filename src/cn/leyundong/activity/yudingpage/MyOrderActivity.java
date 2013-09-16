package cn.leyundong.activity.yudingpage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.leyundong.R;
import cn.leyundong.activity.BaseActivity;
import cn.leyundong.adapter.OrderAdapter;
import cn.leyundong.constant.Urls;
import cn.leyundong.dialog.MyDialog;
import cn.leyundong.dialog.TimeoutDialog.OnTaskTimeout;
import cn.leyundong.entity.BeanListProxy;
import cn.leyundong.entity.BeanProxy;
import cn.leyundong.entity.ChaXunBean;
import cn.leyundong.entity.DingDanXiangBean;
import cn.leyundong.entity.Result;
import cn.leyundong.interfaces.ITaskGenerater;
import cn.leyundong.json.JsonParser;
import cn.leyundong.message.MessageManager;
import cn.leyundong.message.MessageObserver;
import cn.leyundong.message.MyMessage;
import cn.leyundong.message.MyMessage.MessageType;
import cn.leyundong.view.Header;
import cn.quickdevelp.anno.ViewRef;
import cn.quickdevelp.http.HttpPostDuty;
import cn.quickdevelp.http.IHttpDuty;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IJson;

/**
 * 对话框形式
 * @author MSSOFT
 *
 */
public class MyOrderActivity extends BaseActivity implements ITaskGenerater, OnTaskTimeout, MessageObserver {

	private static final int base_id = 1000;
	
	private int taskCounter = 0;
	
	private Set<Integer> timeoutTask = new HashSet<Integer>();
	
	@ViewRef(id=R.id.tvMoney)TextView tvMoney;
	@ViewRef(id=R.id.header)Header header;
	@ViewRef(id=R.id.btnClear, onClick="clear") Button btnClear;
	@ViewRef(id=R.id.btnXuDing, onClick="xuding") Button btnXuDing;
	@ViewRef(id=R.id.btnPay, onClick="pay") Button btnPay;
	@ViewRef(id=R.id.lv) ListView lv;
	
	//传递过来
	private ChaXunBean extraChaXunBean;
	//传过来的订单项
	private List<DingDanXiangBean> infos = new ArrayList<DingDanXiangBean>();
	
//	private TimeoutDialog td;
	private MyDialog dlg;
	
	private OrderAdapter adapter;

	private double mCurrentMoney;
	
	//刚开始订单项个数
	
	private void clear(View v) {
		System.out.println("clear-------------");
		//清空数据
		adapter.clearAllDingDang();
	}
	
	private void xuding(View v) {
		System.out.println("续订----------");
		finish();
	}
	
	private void pay(View v) {
		System.out.println("付款---------------" + adapter.getCount());
		//向服务器提交订单
		if (adapter.getCount() == 0) {
			//用户已经完全删掉订单项
			toast("当前已没有订单！");
			return ;
		}
		int id = taskCounter++;
//		td.show(id);
		dlg.show();
		
		executeTask(id, generateTask());
	}
	
	@Override
	protected void onTaskExecuted(int what, Object result) {
		if (dlg.isShowing()) {
			dlg.dismiss();
		}
		System.out.println("result=" + result);
		Result ret = JsonParser.getResultFromJson((String) result);
		if (ret.success) {
			toast("下单成功");
			setResult(RESULT_OK);
		} else {
			toast("下单失败：" + ret.error);
		}
		clear(btnClear);
		finish();
	}
	
	private void showMoney(double m) {
		mCurrentMoney = m;
		tvMoney.setText("您订单费用" + m + "元");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		infos = (List<DingDanXiangBean>) getIntent().getSerializableExtra(DingDanXiangBean[].class.getSimpleName());
		extraChaXunBean = (ChaXunBean) getIntent().getSerializableExtra(ChaXunBean.class.getSimpleName());
		System.out.println("传递过来的订单项=" + extraChaXunBean);
		List<DingDanXiangBean> ddx = (List<DingDanXiangBean>) getIntent().getSerializableExtra(DingDanXiangBean.class.getSimpleName());
		if (extraChaXunBean == null || ddx == null) {
			finish();
			return ;
		}
		infos.addAll(ddx);
		MessageManager.getInstance().addObserver(this);
		
		setContentView(R.layout.myorder);
		header.setTitle("我的预订");
		
		adapter = new OrderAdapter(act);
		for (DingDanXiangBean b : infos) {
			System.out.println("订单项=" + b.hashCode());
		}
		adapter.addData(infos);
		lv.setAdapter(adapter);
		
//		td = new TimeoutDialog(act, "正在申请下单,请稍候...	");
//		td.setTimeoutListener(this);
		
		dlg = new MyDialog(act);
		dlg.setMessage("正在申请下单,请稍候...");
		
		double money = 0;
		for (DingDanXiangBean b : infos) {
			money += b.ydjg;
		}
		showMoney(money);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MessageManager.getInstance().removeObserver(this);
	}

	@Override
	public IDataRequest generateTask() {
		String url = Urls.getUrl(Urls.TYPE_YUDINGZHUANGQU, 4);
		List<IJson> list = new ArrayList<IJson>();
		list.add(new BeanProxy<ChaXunBean>(extraChaXunBean));
		list.add(new BeanListProxy<DingDanXiangBean>(infos));
		return new AsyncTask(url, list);
	}
	
	private class AsyncTask implements IDataRequest {

		private String url;
		private List<IJson> list;
		public AsyncTask(String url, List<IJson> d) {
			this.url = url;
			this.list = d;
		}
		
		@Override
		public Object doRequest() {
			IHttpDuty t = new HttpPostDuty(act, url, list);
			return t.post();
		}
	}

	@Override
	public IDataRequest generateTask(Object param) {
		return null;
	}

	@Override
	public void onTimeout(long id) {
		timeoutTask.add((int) id);
	}

	@Override
	public boolean onReceiverMessage(MyMessage mm) {
		if (mm.type == MessageType.DINGDANGXIANG_CHANGED) {
			DingDanXiangBean[] bs = (DingDanXiangBean[]) mm.obj1;
			double tmp = mCurrentMoney;
			for (DingDanXiangBean b : bs) {
				tmp -= b.ydjg;
			}
			showMoney(tmp);
			return true;
		}
		return false;
	}
	
}
