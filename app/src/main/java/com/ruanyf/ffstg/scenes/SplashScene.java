package com.ruanyf.ffstg.scenes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ruanyf.ffstg.GameState;
import com.ruanyf.ffstg.GameView;
import com.ruanyf.ffstg.utils.GameUtil;

/**
 * 闪屏场景
 * Created by Feng on 2017/12/25.
 */
public class SplashScene extends Scene {

	private int fadeOpacity, fadeColor, bgColor;

	private Bitmap logoSchoolBmp, logoFengBmp, logoWebsiteBmp;
	private Paint authorPaint;

	public SplashScene() {
		fadeOpacity = 255;

		logoSchoolBmp = GameUtil.INSTANCE.getBitmap("logo_school.png");
		logoFengBmp = GameUtil.INSTANCE.getBitmap("logo_feng.png");
		logoWebsiteBmp = GameUtil.INSTANCE.getBitmap("logo_website.png");

		authorPaint = new Paint();
		authorPaint.setAntiAlias(true);
		authorPaint.setTextSize(24);
		authorPaint.setTextAlign(Paint.Align.CENTER);
		authorPaint.setColor(Color.GRAY);
	}

	/**
	 * 渐变结束的操作
	 */
	@Override
	public void onEnded() {
		// TODO 改用公共方法处理场景切换
	}

	/**
	 * 逻辑操作
	 */
	@Override
	public void doLogic() {
		super.doLogic();

		if (getStep() < GameView.FPS * 1) { // 校徽淡入
			bgColor = Color.WHITE;
			fadeOpacity -= 5;
			if (fadeOpacity < 0) {
				fadeOpacity = 0;
			}
			fadeColor = Color.argb(fadeOpacity, 0, 0, 0);
		} else if (getStep() > GameView.FPS * 2 && getStep() < GameView.FPS * 3) { // 校徽淡出
			fadeOpacity += 5;
			if (fadeOpacity > 255) {
				fadeOpacity = 255;
			}
			fadeColor = Color.argb(fadeOpacity, 255, 255, 255);
		} else if (getStep() > GameView.FPS * 3 && getStep() < GameView.FPS * 4) { // 头像淡入
			fadeOpacity -= 5;
			if (fadeOpacity < 0) {
				fadeOpacity = 0;
			}
			fadeColor = Color.argb(fadeOpacity, 255, 255, 255);
		} else if (getStep() > GameView.FPS * 5 && getStep() < GameView.FPS * 6) { // 头像淡出
			fadeOpacity += 5;
			if (fadeOpacity > 255) {
				fadeOpacity = 255;
			}
			fadeColor = Color.argb(fadeOpacity, 0, 0, 0);
		} else if (getStep() > GameView.FPS * 6 && getStep() < GameView.FPS * 7) { // 网站淡入
			bgColor = Color.BLACK;
			fadeOpacity -= 5;
			if (fadeOpacity < 0) {
				fadeOpacity = 0;
			}
			fadeColor = Color.argb(fadeOpacity, 0, 0, 0);
		} else if (getStep() > GameView.FPS * 8 && getStep() < GameView.FPS * 9) { // 网站淡出
			fadeOpacity += 5;
			if (fadeOpacity > 255) {
				fadeOpacity = 255;
			}
			fadeColor = Color.argb(fadeOpacity, 0, 0, 0);
		} else if (getStep() > GameView.FPS * 9) {
			// 场景跳转
			changeScene(GameState.TITLE);
		}
	}

	/**
	 * 绘制操作
	 *
	 * @param canvas 画布对象
	 */
	@Override
	public void doDraw(Canvas canvas) {

		// 绘制背景
		canvas.drawColor(bgColor);

		// 绘制Logo
		if (getStep() < GameView.FPS * 3) {
			// 校徽
			canvas.drawBitmap(logoSchoolBmp, getScreenWidth() / 2 - logoSchoolBmp.getWidth() / 2,
					getScreenHeight() / 2 - logoSchoolBmp.getHeight() / 2, null);
		} else if (getStep() > GameView.FPS * 3 && getStep() < GameView.FPS * 6) {
			// 头像
			canvas.drawBitmap(logoFengBmp, getScreenWidth() / 2 - logoFengBmp.getWidth() / 2,
					getScreenHeight() / 2 - logoFengBmp.getHeight() / 2 - 32, null);
			canvas.drawText("阮耀锋作品", getScreenWidth() / 2, getScreenHeight() / 2 + 72, authorPaint);
		} else if (getStep() > GameView.FPS * 6 && getStep() < GameView.FPS * 9) {
			// 网站
			canvas.drawBitmap(logoWebsiteBmp, getScreenWidth() / 2 - logoWebsiteBmp.getWidth() / 2,
					getScreenHeight() / 2 - logoWebsiteBmp.getHeight() / 2, null);
		}

		// 绘制淡入淡出覆盖层
		canvas.drawColor(fadeColor);

		// 开启调试的情况下绘制调试信息
		if (GameUtil.INSTANCE.isDebug()) {
			String debugLine1 = "- SplashScene -";
			String debugLine2 = "Step: " + getStep();
			String debugLine3 = "fadeOpacity: " + fadeOpacity;
			canvas.drawText(debugLine1, 20, 30, getDebugPaint());
			canvas.drawText(debugLine2, 20, 50, getDebugPaint());
			canvas.drawText(debugLine3, 20, 70, getDebugPaint());
		}
	}
}
