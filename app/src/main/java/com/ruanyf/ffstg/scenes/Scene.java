package com.ruanyf.ffstg.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ruanyf.ffstg.GameState;
import com.ruanyf.ffstg.utils.GameUtil;
import com.ruanyf.ffstg.utils.ScreenUtil;

/**
 * 游戏场景类
 * Created by Feng on 2017/12/23.
 */
public abstract class Scene {

	private boolean isFading, isFadingIn; // 正在渐变
	private int fadeOpacity; // 渐变透明度
	private int fadeSpeed; // 渐变速度

	private long step;
	private float screenWidth, screenHeight;
	private Paint debugTextPaint, debugShapePaint;

	private Callback callback;

	/**
	 * 场景事件回调监听
	 */
	public interface Callback {
		public void onSceneChanged();
	}

	public void addCallback(Callback callback) {
		this.callback = callback;
	}

	public Scene() {
		isFading = true;
		isFadingIn = true;
		fadeOpacity = 255;
		fadeSpeed = 2;

		screenWidth = ScreenUtil.INSTANCE.getScreenWidth();
		screenHeight = ScreenUtil.INSTANCE.getScreenHeight();

		debugTextPaint = new Paint();
		debugTextPaint.setAntiAlias(true);
		debugTextPaint.setTextSize(16);
		debugTextPaint.setShadowLayer(2, 0, 0, Color.BLACK);
		debugTextPaint.setColor(Color.YELLOW);

		debugShapePaint = new Paint();
		debugShapePaint.setAntiAlias(true);
		debugShapePaint.setStyle(Paint.Style.STROKE);
		debugShapePaint.setColor(Color.YELLOW);
	}

	public long getStep() {
		return step;
	}

	public void setStep(long step) {
		this.step = step;
	}

	public float getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(float screenWidth) {
		this.screenWidth = screenWidth;
	}

	public float getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(float screenHeight) {
		this.screenHeight = screenHeight;
	}

	public boolean isFading() {
		return isFading;
	}

	public void setFading(boolean fading) {
		isFading = fading;
	}

	public int getFadeOpacity() {
		return fadeOpacity;
	}

	public void setFadeOpacity(int fadeOpacity) {
		this.fadeOpacity = fadeOpacity;
	}

	public int getFadeSpeed() {
		return fadeSpeed;
	}

	public void setFadeSpeed(int fadeSpeed) {
		this.fadeSpeed = fadeSpeed;
	}

	public Paint getDebugTextPaint() {
		return debugTextPaint;
	}

	public Paint getDebugShapePaint() {
		return debugShapePaint;
	}

	/**
	 * 切换场景
	 *
	 * @param gameState 游戏状态枚举
	 */
	public void changeScene(GameState gameState) {
		GameUtil.INSTANCE.setGameState(gameState);
		callback.onSceneChanged();
	}

	/**
	 * 单击被确认的操作
	 *
	 * @param x 触摸点x坐标
	 * @param y 触摸点y坐标
	 */
	public void detectSingleTap(float x, float y) {
	}

	/**
	 * 渐变结束的操作
	 */
	public abstract void onEnded();

	/**
	 * 逻辑操作
	 */
	public void doLogic() {
		step++;

		// 场景切换渐变效果
		if (isFading) {
			if (isFadingIn) { // 淡入
				fadeOpacity -= fadeSpeed;
				if (fadeOpacity <= 0) {
					fadeOpacity = 0;
					isFading = false;
					isFadingIn = false;
				}
			} else { // 淡出
				fadeOpacity += fadeSpeed;
				if (fadeOpacity >= 255) {
					fadeOpacity = 255;
					onEnded(); // 淡出完毕执行的方法
				}
			}
		}
	}

	/**
	 * 绘制操作
	 *
	 * @param canvas 画布对象
	 */
	public abstract void doDraw(Canvas canvas);

}
