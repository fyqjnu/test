package cn.leyundong.activity.page;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import cn.leyundong.dialog.MyDialog;
import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.util.ThreadManager;

public abstract class AbstractAsyncPage implements IPage {
	
	private ConcurrentHashMap<Integer, IDataRequest> mRequestMap = new ConcurrentHashMap<Integer, IDataRequest>();
	
	protected MyDialog dlg;
	
	protected void showDialog(Context ctx, String msg) {
		if (dlg == null) {
			dlg = new MyDialog(ctx);
		}
		dlg.setMessage(msg);
		dlg.show();
	}
	
	protected void dismissDialog() {
		dlg.dismiss();
		dlg.obj1 = null;
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			onTaskExecuted(msg.arg1, msg.obj);
			mRequestMap.remove(msg.arg1);
		};
	};
	
	public void onDestroy() {
	};
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	protected void executeTask(final int what, final IDataRequest request) {
		if (mRequestMap.contains(what)) {
			return ;
		}
		Callable<Object> call = new Callable<Object>() {
			
			@Override
			public Object call() throws Exception {
				return request.doRequest();
			}
		};
		FutureTask<Object> task = new FutureTask<Object>(call){
			@Override
			protected void done() {
				super.done();
				
				Message m = handler.obtainMessage();
				m.arg1 = what;
				try {
					Object result = get();
					m.obj = result;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					m.sendToTarget();
				}
			}
		};
		ThreadManager.getInstance().execute(task);
	}
	
	protected void onTaskExecuted(int what, Object result){};
	protected void toast(Context ctx, String text) {
		Toast.makeText(ctx, text, Toast.LENGTH_LONG).show();
	}
}
