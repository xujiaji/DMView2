package com.xujiaji.dmlib2;
import android.view.View;
import com.xujiaji.dmlib2.entity.BaseDmEntity;
import java.util.PriorityQueue;

/**
 * 弹幕接口
 */
public interface DM
{

    /**
     * 添加一个弹幕
     * @param templateView 模板View
     */
    void add(View templateView);

    void add(BaseDmEntity entity);

}
