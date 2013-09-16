package cn.leyundong.message;

public class NullMessageCallback implements IMessageCallback {

	@Override
	public void onMessageHandle(long messageId, Object result) {
		System.out.println("空消息回来-----------");
	}

}
