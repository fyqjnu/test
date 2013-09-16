package cn.leyundong.interfaces;

import java.util.List;

import cn.quickdevelp.interfaces.IDataRequest;
import cn.quickdevelp.interfaces.IJson;

public interface ITaskGenerater {
	IDataRequest generateTask();
	IDataRequest generateTask(Object param);
}
