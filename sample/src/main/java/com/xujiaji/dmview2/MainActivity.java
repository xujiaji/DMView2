package com.xujiaji.dmview2;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xujiaji.dmlib2.LogUtil;
import com.xujiaji.dmlib2.callback.OnDMAddListener;
import com.xujiaji.dmlib2.callback.ViewCreator;
import com.xujiaji.dmlib2.entity.BaseDmEntity;
import com.xujiaji.dmlib2.widget.DMSurfaceView;
import com.xujiaji.dmlib2.widget.DMTextureView;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private DMSurfaceView dmSurfaceView;
    private DMSurfaceView dmAnnouncement;
    private DMSurfaceView dmAnnouncement2;
    private DMSurfaceView dmAnnouncement3;
    private DMSurfaceView dmAnnouncement4;
    private CheckBox checkBox;
    private final Handler handler = new Handler();

    private Runnable runnable;

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

        dmSurfaceView.getController().setOnDMAddListener(new OnDMAddListener() {
            @Override
            public void added(BaseDmEntity dmEntity) {

            }

            @Override
            public void addedAll() {
                Log.e("MainActivity", "thread: " + Thread.currentThread().getName());
                Toast.makeText(MainActivity.this, "弹幕该轮显示完毕", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("MainActivity onPause()");
        dmSurfaceView.getController().pause();
        dmAnnouncement.getController().pause();
        dmAnnouncement2.getController().pause();
        dmAnnouncement3.getController().pause();
        dmAnnouncement4.getController().pause();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            runnable = null;
        }
    }

    public void onClickAddDM(View view) {
        dmAnnouncement.getController().add(LayoutInflater.from(this).inflate(R.layout.announcement_text, null));

        dmAnnouncement.getController().setOnDMAddListener(new OnDMAddListener() {
            @Override
            public void added(BaseDmEntity dmEntity) {
                dmAnnouncement.getController().add(LayoutInflater.from(MainActivity.this).inflate(R.layout.announcement_text, null));
            }

            @Override
            public void addedAll() {

            }
        });


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

//    CustomTimer mCustomTimer;
    public void onClickAdd5NoOver(View view) {
//        mCustomTimer = new CustomTimer(20000) {
//            @Override
//            public void onTick() {
//                addDM("小明11", "1111111111111111111111111111111111 11111111111 ", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3512331237,2033775251&fm=27&gp=0.jpg");
//                addDM("小明21", "21212121212121212121212121212121212121消息2 sdaf", "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1881776517,987084327&fm=27&gp=0.jpg");
//                addDM("小明31", "31313131313131313131313131313131313131313消息3dsfabcdefghi", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=16550438,2220103346&fm=27&gp=0.jpg");
//                addDM("小明41", "4141414141414141414141414141414141414141414141414141414141414消息4ds", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2237097328,2363045038&fm=27&gp=0.jpg");
//                addDM("小明51", "51515151515151515151515151515151515151515151515151消息5", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=586574978,3261086036&fm=27&gp=0.jpg");
//            }
//        };
//        mCustomTimer.start();
        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    addDM("小明11", "1111111111111111111111111111111111 11111111111 ", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3512331237,2033775251&fm=27&gp=0.jpg");
                    addDM("小明21", "21212121212121212121212121212121212121消息2 sdaf", "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1881776517,987084327&fm=27&gp=0.jpg");
                    addDM("小明31", "31313131313131313131313131313131313131313消息3dsfabcdefghi", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=16550438,2220103346&fm=27&gp=0.jpg");
                    addDM("小明41", "4141414141414141414141414141414141414141414141414141414141414消息4ds", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2237097328,2363045038&fm=27&gp=0.jpg");
                    addDM("小明51", "51515151515151515151515151515151515151515151515151消息5", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=586574978,3261086036&fm=27&gp=0.jpg");

                    if (runnable != null) {
                        handler.postDelayed(runnable, 10000);
                    }
                }
            };
            handler.post(runnable);
        }
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
                            dmSurfaceView.getController().add(new ViewCreator() {
                                @Override
                                public View build() {
                                    return LayoutInflater.from(MainActivity.this).inflate(R.layout.barrage_other, null);
                                }
                            });
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
