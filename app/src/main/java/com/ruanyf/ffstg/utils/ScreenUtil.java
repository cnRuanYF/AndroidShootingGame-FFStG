package com.ruanyf.ffstg.utils;

import android.util.Log;

/**
 * 屏幕适配工具类
 * Created by Feng on 2017/12/23.
 */
public enum ScreenUtil {
	INSTANCE; // 单例模式，确保随时可访问

	private static final int DESIGN_SCREEN_REF = 480;
	private float screenScale;
	private int screenWidth, screenHeight;

	/**
	 * 设置屏幕实际尺寸，自动计算缩放及设计尺寸
	 *
	 * @param width  实际宽度（px）
	 * @param height 实际高度 (px)
	 */
	public void setActualScreenSize(int width, int height) {
		/*
		 * 由于现在的设备屏幕比例从4:3~18:9不等，
		 * 以固定的设计尺寸进行拉伸会造成比例不正确，
		 * 我的解决方案是参考较短的边长获取缩放比例，
		 * 根据缩放比例确定较长的边长，以适应实际比例
		 */
		if (width > height) {
			// 横屏的情况以高度为参照
			screenScale = (float) height / DESIGN_SCREEN_REF;
			screenWidth = (int) (width / screenScale);
			screenHeight = DESIGN_SCREEN_REF;
		} else {
			// 竖屏的情况
			screenScale = (float) width / DESIGN_SCREEN_REF;
			screenWidth = DESIGN_SCREEN_REF;
			screenHeight = (int) (height / screenScale);
		}

		Log.d("ScreenUtil", "actualScreenSize: " + width + " * " + height
				+ "\ndesignScreenSize: " + screenWidth + " * " + screenHeight
				+ "\nscreenScale: " + screenScale);
	}

	public float getScreenScale() {
		return screenScale;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
}
