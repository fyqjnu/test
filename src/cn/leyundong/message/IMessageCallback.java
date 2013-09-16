package cn.leyundong.message;

public interface IMessageCallback {
	void onMessageHandle(long messageId, Object result);
}
