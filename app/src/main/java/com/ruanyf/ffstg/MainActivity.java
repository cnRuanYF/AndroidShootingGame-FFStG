package com.ruanyf.ffstg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

		// 初始化
		GameUtil.INSTANCE.setDebug(true);
		GameUtil.INSTANCE.setFrameSkipEnable(false);
		GameUtil.INSTANCE.setInvincibleMode(false);

		// 启动选项
		String[] optionTexts = {"显示FPS信息", "开启跳帧", "玩家无敌"};
		boolean[] optionValues = {true, false, false};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Debug选项");
		builder.setCancelable(false);
		builder.setMultiChoiceItems(optionTexts, optionValues, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				switch (which) {
					case 0:
						GameUtil.INSTANCE.setDebug(isChecked);
						break;
					case 1:
						GameUtil.INSTANCE.setFrameSkipEnable(isChecked);
						break;
					case 2:
						GameUtil.INSTANCE.setInvincibleMode(isChecked);
						break;
				}
			}
		});
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setContentView(new GameView(MainActivity.this));
			}
		});
		builder.show();
	}
}
