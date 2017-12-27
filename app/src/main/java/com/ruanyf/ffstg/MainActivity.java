package com.ruanyf.ffstg;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.ruanyf.ffstg.utils.GameUtil;
import com.ruanyf.ffstg.utils.ScreenUtil;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// AppCompat主题无默认全屏主题，在此设置
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 传入Context
		GameUtil.INSTANCE.setContext(this);

		// 屏幕适配
		DisplayMetrics dm = getResources().getDisplayMetrics();
		ScreenUtil.INSTANCE.setActualScreenSize(dm.widthPixels, dm.heightPixels);

		// 初始化设置
		GameUtil.INSTANCE.setShowFPS(true);
		GameUtil.INSTANCE.setDebug(false);
		GameUtil.INSTANCE.setFrameSkipEnable(true);
		GameUtil.INSTANCE.setInvincibleMode(false);

		setContentView(new GameView(MainActivity.this));
	}
}
