package com.xujiaji.dmview2;


import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xujiaji.dmlib2.DMSurfaceView;

public class MainActivity extends AppCompatActivity {

    private DMSurfaceView dmSurfaceView;
    private ViewHolder mViewHolder;
    private View templateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dmSurfaceView = findViewById(R.id.dmView);
        templateView = LayoutInflater.from(this).inflate(R.layout.barrage, null);
        mViewHolder = new ViewHolder(templateView);
    }

    public void onClickAddDM(View view)
    {
        addDM("name1", "name1消息1", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3512331237,2033775251&fm=27&gp=0.jpg");
        addDM("name2", "消息2 sdaf", "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1881776517,987084327&fm=27&gp=0.jpg");
        addDM("name3", "消息3dsf", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=16550438,2220103346&fm=27&gp=0.jpg");
        addDM("name4", "消息4ds", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2237097328,2363045038&fm=27&gp=0.jpg");
        addDM("name5", "消息5", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=586574978,3261086036&fm=27&gp=0.jpg");

        addDM("name6", "消息  6", "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3185942390,3143936596&fm=27&gp=0.jpg");
        addDM("name7", "消  息7", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1137298932,366992998&fm=27&gp=0.jpg");
        addDM("name8", "消息8", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1727532235,3210820490&fm=27&gp=0.jpg");
        addDM("name9", "消 息9", "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=782046930,1105099424&fm=27&gp=0.jpg");
        addDM("name10", "消息 10", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=73760354,2094376027&fm=27&gp=0.jpg");
    }

    private void addDM(String name, String msg, String imgUrl)
    {
        mViewHolder.tvBarrageName.setText(name);
        mViewHolder.tvBarrageMsg.setText(msg);
//        mViewHolder.imgBarrageHead.setImageBitmap(bitmap);
        Glide.with(this)
                .load(imgUrl)
                .into(new SimpleTarget<Drawable>()
                {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition)
                    {
                        mViewHolder.imgBarrageHead.setImageDrawable(resource);
                        dmSurfaceView.add(templateView);
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
