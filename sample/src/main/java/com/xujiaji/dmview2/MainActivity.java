package com.xujiaji.dmview2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xujiaji.dmlib2.DMSurfaceView;

public class MainActivity extends AppCompatActivity {

    private DMSurfaceView dmSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dmSurfaceView = findViewById(R.id.dmView);
    }

    public void onClickAddDM(View view)
    {
        dmSurfaceView.addElem(this);
    }
}
