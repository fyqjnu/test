package cn.leyundong.message;

public interface MessageObserver {
	/**
	 * 如果处理了消息返回true 否则返回false 如果消息没有被处理 则会重发5次
	 * @param mm
	 * @return
	 */
	boolean onReceiverMessage(MyMessage mm);
}
