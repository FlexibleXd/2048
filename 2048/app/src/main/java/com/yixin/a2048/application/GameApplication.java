package com.yixin.a2048.application;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by flexible on 2016/9/1.
 */
public class GameApplication extends Application {
    public static SharedPreferences mSp;
    public static int mGameLines;
    public static int mGameGoal;
    public static int mItemSize;
//    public static int[][] mHisory;
    public static int Score = 0;
    public static String SP_HIGH_SCROE = "SP_HIGH_SCROE";
    public static String KEY_HIGH_SCROE = "KEY_HIGH_SCROE";
    public static String KEY_GAME_LINES = "KEY_GAME_LINES";
    public static String KEY_GAME_GOAL = "KEY_GAME_GOAL";
//    public static String KEY_HISTORY = "KEY_HISTORY";

    @Override
    public void onCreate() {
        super.onCreate();
        mSp = getSharedPreferences(SP_HIGH_SCROE, 0);
        mGameLines = mSp.getInt(KEY_HIGH_SCROE, 4);
        mGameGoal = mSp.getInt(KEY_GAME_GOAL, 2048);
        mItemSize = 0;
    }
}
