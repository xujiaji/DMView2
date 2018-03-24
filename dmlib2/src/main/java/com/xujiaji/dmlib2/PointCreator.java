package com.xujiaji.dmlib2;
import android.graphics.PointF;
import java.util.ArrayList;

/**
 * 创建PointF
 */

public class PointCreator extends ArrayList<PointF>
{
    private static PointCreator mInstance;

    private PointCreator() {}

    public static PointCreator getInstance()
    {
        if (mInstance == null)
        {
            synchronized (PointCreator.class)
            {
                mInstance = new PointCreator();
            }
        }
        return mInstance;
    }

    public PointF newPointF()
    {
        for (PointF p : this)
        {
            if (p.x == 0 && p.y == 0)
            {
                return p;
            }
        }
        System.out.println("PointCreator size: " + size());
        PointF pointF = new PointF();
        add(pointF);
        return pointF;
    }

    public void reset(PointF pointF)
    {
        pointF.x = 0;
        pointF.y = 0;
    }

}
