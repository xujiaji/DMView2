package com.xujiaji.dmlib2;

import android.util.Log;

import com.xujiaji.dmlib2.entity.BaseDmEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来存储需要更新到画面上的弹幕
 */

public class RefreshDMList extends ArrayList<BaseDmEntity> implements Runnable
{
    /**
     * 刷新间隔
     */
    private static int SPACE_TIME = 0;
    private boolean mRun = false;
    private OnRefreshListener mListener;

    public synchronized void addDM(BaseDmEntity dmEntity)
    {
        add(dmEntity);
        if (mRun)
        {
            return;
        }
        mRun = true;
        new Thread(this).start();
    }

    public void setOnRefreshListener(OnRefreshListener l)
    {
        mListener = l;
    }

    @Override
    public void run()
    {
        while (mRun)
        {
            Log.e("RefreshDMList", " run()");
            List<BaseDmEntity> data = new ArrayList<>(this);
            for (BaseDmEntity dmEntity : data)
            {
                if (dmEntity.getPositions().isEmpty())
                {
                    data.remove(dmEntity);
                    if (mListener != null)
                    {
                        mListener.overDM(dmEntity);
                    }
                }
            }

//            try
//            {
//                Thread.sleep(100);
//            } catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }

            if (mListener != null)
            {
                Log.e("RefreshDMList", " refreshDM");
                mListener.refreshDM(data);
            }

            if (data.size() == 0)
            {
                mRun = false;
            }
        }

    }

    public interface OnRefreshListener
    {
        void refreshDM(List<BaseDmEntity> list);
        void overDM(BaseDmEntity dmEntity);
    }
}
