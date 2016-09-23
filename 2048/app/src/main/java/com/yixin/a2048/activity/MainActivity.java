package com.yixin.a2048.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yixin.a2048.R;
import com.yixin.a2048.View.GameView;
import com.yixin.a2048.application.GameApplication;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GameView view;
    private TextView highScore;
    private static TextView nowScore;
    private static MainActivity activity;
    private RelativeLayout main;
    private TextView goal;

    public MainActivity() {
        activity = this;
    }

    public static MainActivity getActivity() {
        return activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        main = (RelativeLayout) findViewById(R.id.rl_main);
        RelativeLayout game = (RelativeLayout) findViewById(R.id.game);
        view = new GameView(this);
        game.addView(view);
        Button btnCz = (Button) findViewById(R.id.btn_cz);
        Button btnSz = (Button) findViewById(R.id.btn_sz);
        Button btnCx = (Button) findViewById(R.id.btn_cx);
        btnCz.setOnClickListener(this);
        btnSz.setOnClickListener(this);
        btnCx.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolsbar);
        setSupportActionBar(toolbar);
        goal = (TextView) findViewById(R.id.goal);
        highScore = (TextView) findViewById(R.id.high_scroe);
        nowScore = (TextView) findViewById(R.id.now_score);
        highScore.setText(GameApplication.mSp.getInt(GameApplication.KEY_HIGH_SCROE, 0) + "");
        nowScore.setText(GameApplication.Score + "");
        goal.setText(GameApplication.mSp.getInt(GameApplication.KEY_GAME_GOAL, 2046) + "");
    }

    public static void setScore() {
        nowScore.setText(GameApplication.Score + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cz:
                view.initView();
                nowScore.setText("0");
                GameApplication.Score = 0;
                break;
            case R.id.btn_sz:
                Intent intent = new Intent(this, SzActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_cx:
                view.recall();
                break;

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        if (TextUtils.equals(key, "1")) {
            goal.setText(GameApplication.mSp.getInt(GameApplication.KEY_GAME_GOAL, 2048) + "");
            view.initView();
        }
    }

    private void blur(Bitmap bkg, View view, float radius) {
        Bitmap overlay = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.drawBitmap(bkg, -view.getLeft(), -view.getTop(), null);
        RenderScript rs = RenderScript.create(this);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
        rs.destroy();
    }
}
