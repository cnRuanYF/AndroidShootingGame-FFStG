package com.ruanyf.ffstg.scenes.backgrounds;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ruanyf.ffstg.utils.ScreenUtil;

/**
 * 制作人员名单背景类
 * Created by Feng on 2017/12/27.
 */
public class StaffBackground extends Background {

	private float screenW, screenH;
	private float canvasOffset;
	private Paint linePaint,textPaint;

	public StaffBackground() {
		screenW = ScreenUtil.INSTANCE.getScreenWidth();
		screenH = ScreenUtil.INSTANCE.getScreenHeight();

		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setColor(Color.WHITE);
		linePaint.setStrokeWidth(1.5f);
		linePaint.setAlpha(48);

		textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(64);
		textPaint.setStyle(Paint.Style.STROKE);
	}

	@Override
	public void doLogic() {
		super.doLogic();
		canvasOffset = getStep() % 200;
	}

	/**
	 * 绘制操作
	 *
	 * @param canvas 画布对象
	 */
	@Override
	public void doDraw(Canvas canvas) {
		canvas.save();
		canvas.rotate(30);
		canvas.translate(0, canvasOffset);

		canvas.drawColor(Color.rgb(20, 40, 60));

		// 画网格线
		for (int i = (int) -screenW / 20; i < screenH * 2 / 20; i++) { // 横线
			canvas.drawLine(0, 20 * i,
					screenW * 2, 20 * i, linePaint);
		}
		for (int i = 0; i < screenW * 2 / 20; i++) { // 竖线
			canvas.drawLine(20 * i, -screenH,
					20 * i, screenH, linePaint);
		}

		// 画Logo
		for (int row = (int) -screenW / 100; row < screenH / 100; row++) {
			for (int col = 0; col < screenW * 2 / 200; col++) {
				textPaint.setColor(Color.rgb(20, 40, 60));
				textPaint.setStrokeWidth(8);
				canvas.drawText("FFStG", 220 * col + (row % 2 == 0 ? 110 : 0), 100 * row, textPaint);
				textPaint.setColor(Color.WHITE);
				textPaint.setAlpha(48);
				textPaint.setStrokeWidth(3);
				canvas.drawText("FFStG", 220 * col + (row % 2 == 0 ? 110 : 0), 100 * row, textPaint);
			}
		}

		canvas.restore();
	}
}
