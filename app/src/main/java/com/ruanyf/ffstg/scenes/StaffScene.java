package com.ruanyf.ffstg.scenes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

import com.ruanyf.ffstg.BitmapBackground;
import com.ruanyf.ffstg.GameState;
import com.ruanyf.ffstg.GameView;
import com.ruanyf.ffstg.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 制作人名单场景
 * Created by Feng on 2017/12/25.
 */
public class StaffScene extends Scene {

	private Bitmap logoSchoolBmp, logoFengBmp, logoWebsiteBmp;

	private Shader mShader, shadowShader;
	private Paint mPaint, shadowPaint;
	private List<Paint> paints;

	private BitmapBackground bitmapBackground;

	/**
	 * 构造
	 */
	public StaffScene() {
		logoSchoolBmp = GameUtil.INSTANCE.getBitmap("logo_school.png");
		logoFengBmp = GameUtil.INSTANCE.getBitmap("logo_feng.png");
		logoWebsiteBmp = GameUtil.INSTANCE.getBitmap("logo_website.png");
		mShader = new LinearGradient(0, 0, 0, getScreenHeight(),
				new int[]{Color.argb(0, 255, 255, 255),
						Color.argb(255, 255, 255, 255),
						Color.argb(255, 255, 255, 255),
						Color.argb(0, 255, 255, 255)},
				new float[]{0, 0.25f, 0.75f, 1}, Shader.TileMode.CLAMP);
		shadowShader = new LinearGradient(0, 0, 0, getScreenHeight(),
				new int[]{Color.argb(0, 0, 0, 0),
						Color.argb(255, 0, 0, 0),
						Color.argb(0, 0, 0, 0)},
				new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextAlign(Paint.Align.CENTER);
		mPaint.setColor(Color.WHITE);
		mPaint.setShader(mShader);

		shadowPaint = new Paint();
		shadowPaint.setAntiAlias(true);
		shadowPaint.setTextAlign(Paint.Align.CENTER);
		shadowPaint.setColor(Color.BLACK);
		shadowPaint.setShadowLayer(4, 0, 0, Color.BLACK);
		shadowPaint.setShader(shadowShader);

		paints = new ArrayList<>();
		paints.add(shadowPaint);
		paints.add(mPaint);

		bitmapBackground = new BitmapBackground(GameUtil.INSTANCE.getBitmap("background1.png"), 1);
	}

	/**
	 * 逻辑操作
	 */
	@Override
	public void doLogic() {
		super.doLogic();
		bitmapBackground.doLogic();

		// 15秒后跳转
		if (getStep() > GameView.FPS * 14) {
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

		// 画背景
		bitmapBackground.doDraw(canvas);

		// 画字幕
		for (Paint paint : paints) {
			float x = getScreenWidth() / 2;
			float y = getScreenHeight() - getStep() * 2 + 20;

			paint.setTextSize(24);
			canvas.drawText("—— STAFF ——", x, y, paint);

			paint.setTextSize(100);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(4);
			canvas.drawText("FFStG", x, y += 192, paint);
			paint.setStyle(Paint.Style.FILL);

			paint.setTextSize(20);
			canvas.drawText("Feng's Flight Shooting Game", x, y += 24, paint);

			canvas.drawBitmap(logoSchoolBmp, x - logoSchoolBmp.getWidth() / 2, y += 128, null);

			paint.setTextSize(20);
			canvas.drawText("指导老师：谢奇峰", x, y += 108, paint);

			canvas.drawBitmap(logoFengBmp, x - logoFengBmp.getWidth() / 2, y += 128, null);

			canvas.drawText("制作：阮耀锋", x, y += 172, paint);
			canvas.drawText("编程：阮耀锋", x, y += 24, paint);
			canvas.drawText("美工：阮耀锋", x, y += 24, paint);
			paint.setTextSize(16);
			canvas.drawText("(部分素材来源于网络)", x, y += 20, paint);

			canvas.drawBitmap(logoWebsiteBmp, x - logoWebsiteBmp.getWidth() / 2, y += 128, null);

			paint.setTextSize(20);
			canvas.drawText("欢迎访问我的个人网站", x, y += 128, paint);
		}

		// 绘制淡入淡出覆盖层
		if (isFading()) {
			canvas.drawColor(Color.argb(getFadeOpacity(), 0, 0, 0));
		}

		// 开启调试的情况下绘制调试信息
		if (GameUtil.INSTANCE.isDebug()) {
			String debugLine1 = "- StaffScene -";
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
