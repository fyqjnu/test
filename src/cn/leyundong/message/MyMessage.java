package cn.leyundong.message;

import java.io.Serializable;

/**
 * 全局消息通信
 */
public class MyMessage implements Serializable, Comparable<MyMessage> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum MessageType {
		/**
		 * 切换到某个页面
		 */
		PAGE_TURN_TO,
		/**
		 * 有可用网络
		 */
		NETWORK_AVAILABLE,
		/**
		 * 订单项改变
		 */
		DINGDANGXIANG_CHANGED,
	}
	
	
	public static enum Priority {
		LOW(1),
		MIDDLE(2),
		HIGH(3);
		int p;
		private Priority(int p) {
			this.p = p;
		}
		public int getPriority() {
			return p;
		}
	}
	
	public long messageId;
	public MessageType type;
	public int arg1;
	public int arg2;
	public Object obj1;
	public Object obj2;
	public Object obj3;
	public Object obj4;
	public Priority priority = Priority.MIDDLE;
	/**
	 * 默认消息至少需要被一个接收者处理
	 */
	public boolean needHandle = true;
	
	//是否需要反馈结果
	public boolean needFeedbackResult;
	
	public IMessageCallback callback = new NullMessageCallback();
	
	@Override
	public int compareTo(MyMessage another) {
		if (another != null) {
			return ((MyMessage)another).priority.getPriority() - priority.getPriority();
		}
		return 0;
	}

	@Override
	public String toString() {
		return "MyMessage [messageId=" + messageId + ", type=" + type
				+ ", arg1=" + arg1 + ", arg2=" + arg2 + ", obj1=" + obj1
				+ ", obj2=" + obj2 + ", obj3=" + obj3 + ", obj4=" + obj4
				+ ", priority=" + priority + ", needHandle=" + needHandle
				+ ", needFeedbackResult=" + needFeedbackResult + ", callback="
				+ callback + "]";
	}
	
}
