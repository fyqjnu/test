package cn.leyundong.message;

import java.util.concurrent.PriorityBlockingQueue;

import cn.leyundong.message.MyMessage.MessageType;

public class MessageQueue extends PriorityBlockingQueue<MyMessage> {
	private static final long serialVersionUID = 1L;
	
	private MessageManager mgr;
	
	@Override
	public boolean add(MyMessage e) {
		boolean result = super.add(e);
//		System.out.println("size -> " + size());
		if (size() == 1) {
			MyMessage top = peek();
			doSendMessage(top);
		}
		return result;
	}
	
	private synchronized void doSendMessage(MyMessage m) {
		if (mgr == null) {
			mgr = MessageManager.getInstance();
		}
		mgr.notifyNewMessage(m);
	}
	
	/**
	 * 回收一条消息
	 * @param m
	 */
	public void destroyMessage(MyMessage m) {
		System.out.println("回收消息=" + m);
		m.type = null;
		m.messageId = 0;
		m.arg1 = 0;
		m.arg2 = 0;
		m.obj1 = null;
		m.obj2 = null;
		m.obj3 = null;
		m.obj4 = null;
		m.needFeedbackResult = false;
		m.needHandle = true;
		m.callback = new NullMessageCallback();
	}
	
	private void checkNext() {
		if (size() > 0) {
			MyMessage msg = peek();
			doSendMessage(msg);
		}
	}
	
	public void removeMessageFromQueue(MyMessage m) {
		super.remove(m);
		System.out.println("移除消息=" + m);
//		System.out.println("remove msg -> " + m + ",current size -> " + size());
		checkNext();
	}
	
}
