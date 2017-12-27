package com.ruanyf.ffstg.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ruanyf.ffstg.GameState;
import com.ruanyf.ffstg.stages.Stage;

import java.io.IOException;

/**
 * 游戏工具
 * Created by Feng on 2017/12/23.
 */
public enum GameUtil {
	INSTANCE;

	private Context context;
	private GameState gameState;
	private Stage currentStage;

	private boolean isShowFPS, isDebug, isFrameSkipEnable, isInvincibleMode;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public Stage getCurrentStage() {
		return currentStage;
	}

	public void setCurrentStage(Stage currentStage) {
		this.currentStage = currentStage;
	}

	/**
	 * 从assets中按文件名获取位图对象
	 *
	 * @param filePath 图片文件路径
	 * @return 位图对象
	 */
	public Bitmap getBitmap(String filePath) {
		try {
			return BitmapFactory.decodeStream(getContext().getAssets().open(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isShowFPS() {
		return isShowFPS;
	}

	public void setShowFPS(boolean showFPS) {
		isShowFPS = showFPS;
	}

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean debug) {
		isDebug = debug;
	}

	public boolean isFrameSkipEnable() {
		return isFrameSkipEnable;
	}

	public void setFrameSkipEnable(boolean frameSkipEnable) {
		isFrameSkipEnable = frameSkipEnable;
	}

	public boolean isInvincibleMode() {
		return isInvincibleMode;
	}

	public void setInvincibleMode(boolean invincibleMode) {
		isInvincibleMode = invincibleMode;
	}

	public void showGameSetting() {
		String[] optionTexts = {"显示FPS", "显示调试信息", "开启跳帧", "玩家无敌"};
		boolean[] optionValues = {isShowFPS, isDebug, isFrameSkipEnable, isInvincibleMode};
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Debug选项");
		builder.setMultiChoiceItems(optionTexts, optionValues, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				switch (which) {
					case 0:
						GameUtil.INSTANCE.setShowFPS(isChecked);
						break;
					case 1:
						GameUtil.INSTANCE.setDebug(isChecked);
						break;
					case 2:
						GameUtil.INSTANCE.setFrameSkipEnable(isChecked);
						break;
					case 3:
						GameUtil.INSTANCE.setInvincibleMode(isChecked);
						break;
				}
			}
		});
		builder.setPositiveButton("OK", null);
		builder.show();
	}
}
