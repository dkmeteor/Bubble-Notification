package com.dk.view.drop.sample;

import com.dk.view.drop.CoverManager;

import android.app.Activity;
import android.os.Bundle;

public class BigDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big);

        getActionBar().hide();
        CoverManager.getInstance().init(this);

        CoverManager.getInstance().setMaxDragDistance(350);
        CoverManager.getInstance().setExplosionTime(150);
    }

}
