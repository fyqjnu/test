package cn.leyundong.activity.bridge;

/**
 *
 * 两个对象间进行通信的桥梁
 * @param <V>
 */
public interface ICommunicationBridge<V> {
	V getValue();
}
