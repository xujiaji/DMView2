package com.xujiaji.dmview2;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xujiaji.dmlib2.widget.DMSurfaceView;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private DMSurfaceView dmSurfaceView;
    private DMSurfaceView dmAnnouncement;
    private DMSurfaceView dmAnnouncement2;
    private DMSurfaceView dmAnnouncement3;
    private DMSurfaceView dmAnnouncement4;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dmSurfaceView = findViewById(R.id.dmView);
        dmAnnouncement = findViewById(R.id.dmAnnouncement);
        dmAnnouncement2 = findViewById(R.id.dmAnnouncement2);
        dmAnnouncement3 = findViewById(R.id.dmAnnouncement3);
        dmAnnouncement4 = findViewById(R.id.dmAnnouncement4);

        checkBox = findViewById(R.id.checkbox);
    }

    public void onClickAddDM(View view) {
        dmAnnouncement.getController().add(LayoutInflater.from(this).inflate(R.layout.announcement_text, null));

        dmAnnouncement2.getController().add(LayoutInflater.from(this).inflate(R.layout.announcement_text, null));

        dmAnnouncement3.getController().add(LayoutInflater.from(this).inflate(R.layout.announcement_image_text, null));

        dmAnnouncement4.getController().add(LayoutInflater.from(this).inflate(R.layout.announcement_image_text, null));

        addDM("小明1", "一条消息abcdefghijklmnopqrstuvwxyz123456789 一条消息abcdefghijklmnopqrstuvwxyz123456789 ", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3512331237,2033775251&fm=27&gp=0.jpg");
        addDM("小明2", "消息2 sdaf", "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1881776517,987084327&fm=27&gp=0.jpg");
        addDM("小明3", "消息3dsfabcdefghijklmnopqrstuvwxyz123456789", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=16550438,2220103346&fm=27&gp=0.jpg");
        addDM("小明4", "消息4ds", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2237097328,2363045038&fm=27&gp=0.jpg");
        addDM("小明5", "消息5", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=586574978,3261086036&fm=27&gp=0.jpg");

        addDM("小明6", "消息  6", "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3185942390,3143936596&fm=27&gp=0.jpg");
        addDM("小明7", "消  息7abcdefghijklmnopqrstuvwxyz123456789", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1137298932,366992998&fm=27&gp=0.jpg");
        addDM("小明8", "消息8", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1727532235,3210820490&fm=27&gp=0.jpg");
        addDM("小明9", "消 息9", "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=782046930,1105099424&fm=27&gp=0.jpg");
        addDM("小明10", "这是最后到消息 10", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=73760354,2094376027&fm=27&gp=0.jpg");
    }

    private void addDM(String name, String msg, String imgUrl) {
        final View templateView = LayoutInflater.from(this).inflate(R.layout.barrage, null);
        final ViewHolder mViewHolder = new ViewHolder(templateView);
        mViewHolder.tvBarrageName.setText(name);
        mViewHolder.tvBarrageMsg.setText(msg);
//        mViewHolder.imgBarrageHead.setImageBitmap(bitmap);
        final AtomicInteger atomicInteger = new AtomicInteger();
        final Random random = new Random();
        Glide.with(this)
                .load(imgUrl)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mViewHolder.imgBarrageHead.setImageDrawable(resource);
                        dmSurfaceView.getController().add(templateView);

                        if (!checkBox.isChecked()) {
                            return;
                        }
                        int num = atomicInteger.get() + 1;
                        if (num < random.nextInt(10)) {
                            dmSurfaceView.getController().add(LayoutInflater.from(MainActivity.this).inflate(R.layout.barrage_other, null));
                        }
                        atomicInteger.set(num);
                    }
                });
    }

    public void onClickGoTestDMTextureView(View view) {
        startActivity(new Intent(this, TestTextureActivity.class));
    }

    public void onClickClearScreen(View view) {
        dmSurfaceView.getController().destroy();
    }

    private static class ViewHolder {
        TextView tvBarrageName;
        TextView tvBarrageMsg;
        ImageView imgBarrageHead;

        ViewHolder(View view) {
            tvBarrageName = view.findViewById(R.id.tvBarrageName);
            tvBarrageMsg = view.findViewById(R.id.tvBarrageMsg);
            imgBarrageHead = view.findViewById(R.id.imgBarrageHead);
        }
    }
}
