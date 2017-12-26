package com.ruanyf.ffstg.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ruanyf.ffstg.GameState;

import java.io.IOException;

/**
 * 游戏工具
 * Created by Feng on 2017/12/23.
 */
public enum GameUtil {
	INSTANCE;

	private Context context;
	private GameState gameState;

	private boolean isDebug, isFrameSkipEnable, isInvincibleMode;

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
}
