package com.ruanyf.ffstg.scenes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

import com.ruanyf.ffstg.GameState;
import com.ruanyf.ffstg.GameView;
import com.ruanyf.ffstg.Sprite;
import com.ruanyf.ffstg.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 标题菜单场景
 * Created by Feng on 2017/12/25.
 */
public class TitleScene extends Scene {

	private Shader logoShineShader;
	private Paint logoPaint, logoShinePaint;
	private List<Paint> paints;

	private List<Sprite> meteors;
	private Bitmap light64Bmp, light128Bmp, light512Bmp, lightLongBmp;

	/**
	 * 构造
	 */
	public TitleScene() {

		logoPaint = new Paint();
		logoPaint.setAntiAlias(true);
		logoPaint.setTextAlign(Paint.Align.CENTER);
		logoPaint.setColor(Color.WHITE);
		logoPaint.setShadowLayer(4, 0, 0, Color.BLACK);

		logoShinePaint = new Paint();
		logoShinePaint.setAntiAlias(true);
		logoShinePaint.setTextAlign(Paint.Align.CENTER);
		logoShinePaint.setColor(Color.BLACK);
		logoShinePaint.setShadowLayer(8, 0, 0, Color.BLACK);

		paints = new ArrayList<>();
		paints.add(logoPaint);
		paints.add(logoShinePaint);
		paints.add(logoShinePaint); // 发光层绘制两遍

		light64Bmp = GameUtil.INSTANCE.getBitmap("lights/light_circle_64_a50.png");
		light128Bmp = GameUtil.INSTANCE.getBitmap("lights/light_circle_128_a50.png");
		light512Bmp = GameUtil.INSTANCE.getBitmap("lights/light_circle_512_a10.png");
		lightLongBmp = GameUtil.INSTANCE.getBitmap("lights/light_long45d_a50.png");

		// 流星
		meteors = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			Sprite sprite = null;
			if (i % 4 == 0) {
				sprite = new Sprite(light64Bmp);
				sprite.setSpeed(-5, 5);
			} else if ((i + 1) % 4 == 0) {
				sprite = new Sprite(light128Bmp);
				sprite.setSpeed(-6, 6);
			} else if ((i + 2) % 4 == 0) {
				sprite = new Sprite(light512Bmp);
				sprite.setSpeed(-8, 8);
			} else {
				sprite = new Sprite(lightLongBmp);
				sprite.setSpeed(-4, 4);
			}
			meteors.add(sprite);
		}
	}

	/**
	 * 逻辑操作
	 */
	@Override
	public void doLogic() {
		super.doLogic();

		// 流星
		if (getStep() % 20 == 0) {
			for (Sprite sprite : meteors) {
				if (!sprite.isVisible()) {
					sprite.setPosition(getScreenWidth(), -sprite.getHeight() / 2 - getScreenWidth()
							+ (getScreenWidth() + getScreenHeight()) * (float) Math.random());
					sprite.setVisible(true);
				}
			}
		}
		for (Sprite sprite : meteors) {
			sprite.doLogic();
		}

		// 游戏LOGO
		int shineOffset = (int) ((getStep() * 5) % getScreenWidth());
		logoShineShader = new LinearGradient(shineOffset - 100, shineOffset - 100,
				shineOffset, shineOffset,
				new int[]{Color.argb(0, 255, 255, 255),
						Color.argb(255, 255, 255, 255),
						Color.argb(0, 255, 255, 255)},
				new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);
		logoShinePaint.setShader(logoShineShader);

		// TODO test 5秒后跳转
		if (getStep() > GameView.FPS * 10) {
			setFading(true);
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
		canvas.drawColor(Color.BLACK);

		// 流星
		for (Sprite sprite : meteors) {
			sprite.doDraw(canvas);
		}

		// 绘制游戏LOGO
		for (Paint paint : paints) {
			paint.setTextSize(100);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(4);
			canvas.drawText("FFStG", getScreenWidth() / 2, 220, paint);
			paint.setStyle(Paint.Style.FILL);
			paint.setTextSize(20);
			canvas.drawText("Feng's Flight Shooting Game", getScreenWidth() / 2, 244, paint);
		}

		// 绘制淡入淡出覆盖层
		if (isFading()) {
			canvas.drawColor(Color.argb(getFadeOpacity(), 0, 0, 0));
		}

		// 开启调试的情况下绘制调试信息
		if (GameUtil.INSTANCE.isDebug()) {
			String debugLine1 = "- TitleScene -";
			String debugLine2 = "Step: " + getStep();
			canvas.drawText(debugLine1, 20, 30, getDebugPaint());
			canvas.drawText(debugLine2, 20, 50, getDebugPaint());
		}
	}

	/**
	 * 场景结束后的操作
	 */
	@Override
	public void onEnded() {
		changeScene(GameState.TITLE);
	}

}
