package com.ruanyf.ffstg;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * 图片背景类
 * Created by Feng on 2017/12/23.
 */
public class BitmapBackground {

	private Bitmap bitmap;
	float y1, y2; // 两张图的y坐标
	private float moveSpeed; // 移动速度

	public BitmapBackground(Bitmap bitmap) {
		this.bitmap = bitmap;
		y1 = 0 - bitmap.getHeight();
		y2 = 0;
	}

	public BitmapBackground(Bitmap bitmap, float moveSpeed) {
		this(bitmap);
		this.moveSpeed = moveSpeed;
	}

	public float getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	/**
	 * 逻辑操作
	 */
	public void doLogic() {
		y1 += moveSpeed;
		y2 += moveSpeed;
		if (y1 >= 0) {
			y1 = 0 - bitmap.getHeight();
			y2 = 0;
		}
	}

	/**
	 * 绘制操作
	 *
	 * @param canvas 画布对象
	 */
	public void doDraw(Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, y1, null);
		canvas.drawBitmap(bitmap, 0, y2, null);
	}
}
