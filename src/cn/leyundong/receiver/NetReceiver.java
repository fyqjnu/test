package cn.leyundong.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import cn.leyundong.message.MessageManager;
import cn.leyundong.message.MyMessage;
import cn.leyundong.message.MyMessage.MessageType;

/**
 * 监听网络变化  
 * @author Administrator
 *
 */
public class NetReceiver extends BroadcastReceiver {
	private static final String TAG = NetReceiver.class.getSimpleName() + ":";
	
	/**
	 * 网络是否可用
	 */
	public static boolean isNetworkAvailable;
	public static NetworkInfo mNetworkInfo;
	
	public static void init(Context ctx) {
		mNetworkInfo = checkNetworkState(ctx);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		System.out.println(TAG + "action=" + action);
		NetworkInfo netInfo = checkNetworkState(context);
		if (isNetworkAvailable) {
			//发送消息 网络可用
			MessageManager mMgr = MessageManager.getInstance();
			MyMessage mm = mMgr.obtainMyMessage();
			mm.type = MessageType.NETWORK_AVAILABLE;
			mm.obj1 = netInfo;
			//该消息可不被处理
			mm.needHandle = false;
			mMgr.sendMyMessage(mm);
		}
	}
	
	private static NetworkInfo checkNetworkState(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		boolean available = false;
		if (netInfo != null) {
			if (netInfo.isAvailable() || netInfo.isConnectedOrConnecting()) {
				available = true;
			}
		}
		isNetworkAvailable = available;
		return netInfo;
	}

}
