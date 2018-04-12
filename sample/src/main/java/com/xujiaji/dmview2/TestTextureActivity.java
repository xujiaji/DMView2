package com.xujiaji.dmview2;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xujiaji.dmlib2.widget.DMTextureView;

public class TestTextureActivity extends AppCompatActivity {

    private DMTextureView dmLeftRight;
    private DMTextureView dmRightLeft;
    private DMTextureView dmUpDown;
    private DMTextureView dmDownUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_texture);
        dmDownUp = findViewById(R.id.dmDownUp);
        dmUpDown = findViewById(R.id.dmUpDown);
        dmRightLeft = findViewById(R.id.dmRightLeft);
        dmLeftRight = findViewById(R.id.dmLeftRight);
    }

    public void onClickAddDM(View view)
    {
        addDM("小明1", "一条消息1", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3512331237,2033775251&fm=27&gp=0.jpg");
        addDM("小明2", "消息2 sdaf", "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1881776517,987084327&fm=27&gp=0.jpg");
        addDM("小明3", "消息3dsf", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=16550438,2220103346&fm=27&gp=0.jpg");
        addDM("小明4", "消息4ds", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2237097328,2363045038&fm=27&gp=0.jpg");
        addDM("小明5", "消息5", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=586574978,3261086036&fm=27&gp=0.jpg");
//
//        addDM("小明6", "消息  6", "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3185942390,3143936596&fm=27&gp=0.jpg");
//        addDM("小明7", "消  息7", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1137298932,366992998&fm=27&gp=0.jpg");
//        addDM("小明8", "消息8", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1727532235,3210820490&fm=27&gp=0.jpg");
//        addDM("小明9", "消 息9", "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=782046930,1105099424&fm=27&gp=0.jpg");
//        addDM("小明10", "这是最后到消息 10", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=73760354,2094376027&fm=27&gp=0.jpg");
    }

    private void addDM(String name, String msg, String imgUrl)
    {
        final View templateViewDownUp = LayoutInflater.from(this).inflate(R.layout.barrage_down_up, null);
        final ViewHolder mViewHolderDownUp = new ViewHolder(templateViewDownUp);
        mViewHolderDownUp.tvBarrageName.setText(name);
        mViewHolderDownUp.tvBarrageMsg.setText(msg);

        final View templateViewUpDown = LayoutInflater.from(this).inflate(R.layout.barrage_up_down, null);
        final ViewHolder mViewHolderUpDown = new ViewHolder(templateViewUpDown);
        mViewHolderUpDown.tvBarrageName.setText(name);
        mViewHolderUpDown.tvBarrageMsg.setText(msg);

        final View templateViewRightLeft = LayoutInflater.from(this).inflate(R.layout.barrage, null);
        final ViewHolder mViewHolderRightLeft = new ViewHolder(templateViewRightLeft);
        mViewHolderRightLeft.tvBarrageName.setText(name);
        mViewHolderRightLeft.tvBarrageMsg.setText(msg);

        final View templateViewLeftRight = LayoutInflater.from(this).inflate(R.layout.barrage_left_right, null);
        final ViewHolder mViewHolderLeftRight = new ViewHolder(templateViewLeftRight);
        mViewHolderLeftRight.tvBarrageName.setText(name);
        mViewHolderLeftRight.tvBarrageMsg.setText(msg);

        Glide.with(this)
                .load(imgUrl)
                .into(new SimpleTarget<Drawable>()
                {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition)
                    {
                        mViewHolderDownUp.imgBarrageHead.setImageDrawable(resource);
                        dmDownUp.getController().add(templateViewDownUp);

                        mViewHolderUpDown.imgBarrageHead.setImageDrawable(resource);
                        dmUpDown.getController().add(templateViewUpDown);

                        mViewHolderRightLeft.imgBarrageHead.setImageDrawable(resource);
                        dmRightLeft.getController().add(templateViewRightLeft);

                        mViewHolderLeftRight.imgBarrageHead.setImageDrawable(resource);
                        dmLeftRight.getController().add(templateViewLeftRight);
                    }
                });
    }

    private static class ViewHolder
    {
        TextView tvBarrageName;
        TextView tvBarrageMsg;
        ImageView imgBarrageHead;

        ViewHolder(View view)
        {
            tvBarrageName = view.findViewById(R.id.tvBarrageName);
            tvBarrageMsg = view.findViewById(R.id.tvBarrageMsg);
            imgBarrageHead = view.findViewById(R.id.imgBarrageHead);
        }
    }
}
