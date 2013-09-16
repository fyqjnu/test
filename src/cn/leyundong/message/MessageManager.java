package cn.leyundong.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import android.os.Handler;
import android.os.Message;

public class MessageManager extends Handler {
	private static MessageManager mInstance;
	
	private MessageQueue queue;
	
	private List<MyMessage> mMessageCache = new ArrayList<MyMessage>();
	
	private CopyOnWriteArrayList<MessageObserver> mMessgaeObservers = new CopyOnWriteArrayList<MessageObserver>();
	
	private Map<MyMessage, Integer> reSendMessageMap = new HashMap<MyMessage, Integer>();
	
	private MessageManager() {
		queue = new MessageQueue();
		for (int i = 0; i < 10; i++) {
			mMessageCache.add(new MyMessage());
		}
	}
	
	public static synchronized MessageManager getInstance() {
		if (mInstance == null) {
			mInstance = new MessageManager();
		}
		return mInstance;
	}
	
	@Override
	public void handleMessage(android.os.Message msg) {
		boolean isHandled = false;
		MyMessage mm = (MyMessage) msg.obj;
		for (MessageObserver o : mMessgaeObservers) {
			if (o.onReceiverMessage(mm)) {
				isHandled = true;
			}
		}
		//是否需要重新发送消息
		boolean needResendMessage = false;
		if ((!isHandled) && mm.needHandle) {
			//消息未被处理
			Integer count = reSendMessageMap.get(msg.obj);
			if (count == null || count < 5) {
				int i = 0;
				if (count != null) {
					i = count;
				}
				i++;
				reSendMessageMap.put((MyMessage) msg.obj, i);
				System.out.println("消息=" + msg.obj + " 未被处理，200毫秒后再尝试第 " + i + " 次处理");
				Message msgNew = obtainMessage();
				msgNew.obj = msg.obj;
				sendMessageDelayed(msgNew, 200);
				needResendMessage = true;
			} else {
				reSendMessageMap.remove(msg.obj);
			}
		}
		
		if (!needResendMessage) {
			queue.destroyMessage(mm);
		}
		//队列中包含该条消息则清掉
		if (queue.contains(mm)) {
			queue.removeMessageFromQueue(mm);
		}
	}
	
	public synchronized void sendMyMessage(MyMessage mm) {
		if (mm == null) {
			return ;
		}
		queue.add(mm);
	}
	
	public synchronized void addObserver(MessageObserver observer) {
		if (observer == null || mMessgaeObservers.contains(observer)) {
			return ;
		}
		mMessgaeObservers.add(observer);
	}
	public synchronized void removeObserver(MessageObserver observer) {
		if (observer == null || !mMessgaeObservers.contains(observer)) {
			return ;
		}
		mMessgaeObservers.remove(observer);
	}
	
	public synchronized MyMessage obtainMyMessage() {
		MyMessage target = null;
		for (MyMessage m : mMessageCache) {
			if (m.type == null) {
				target = m;
				break;
			}
		}
		if (target == null) {
			target = new MyMessage();
			mMessageCache.add(target);
		}
		return target;
	}
	
	public synchronized void notifyNewMessage(MyMessage mm) {
		android.os.Message m = new android.os.Message();
		m.obj = mm;
		sendMessage(m);
	}
}
