package com.ruanyf.ffstg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.ruanyf.ffstg.utils.ScreenUtil;

import java.util.List;

/**
 * 游戏精灵类
 * Created by Feng on 2017/12/23.
 */
public class Sprite {

	private boolean isVisible;
	private boolean isPlaying;

	private long step; // Sprite自身进度(帧数)
	private int animateInterval; // 动画速率

	private float x, y, width, height, speedX, speedY;

	private Bitmap bitmap; // 使用单张图片构建Sprite
	private List<Bitmap> bitmapList; // 使用多张图片构建Sprite
	private int[] frameX, frameY; // 存放(多帧Sprite)每帧坐标
	private int frameTotal; // 存放(多帧Sprite)总帧数

	private int[] frameSequance; // 帧序列
	private int frameSequanceIndex; // 帧序列索引

	private Rect src, dst; // 用于多帧裁剪的矩形区

	/**
	 * 使用单张图片构建静态Sprite
	 *
	 * @param bitmap 位图对象
	 */
	public Sprite(Bitmap bitmap) {
		this(bitmap, bitmap.getWidth(), bitmap.getHeight());
	}

	/**
	 * 使用单张图片构建多帧Sprite
	 *
	 * @param bitmap 位图对象
	 * @param width  帧宽度
	 * @param height 帧高度
	 */
	public Sprite(Bitmap bitmap, int width, int height) {
		this.width = width;
		this.height = height;
		this.bitmap = bitmap;

		// 帧位的获取
		int totalColumns = bitmap.getWidth() / width; // 横向帧数 = 图片宽度 / 帧宽度
		int totalRows = bitmap.getHeight() / height; // 同上
		frameTotal = totalColumns * totalRows;
		frameX = new int[frameTotal];
		frameY = new int[frameTotal];
		for (int row = 0; row < totalRows; row++) {
			for (int col = 0; col < totalColumns; col++) {
				frameX[totalColumns * row + col] = col * width;
				frameY[totalColumns * row + col] = row * height;
			}
		}
		src = new Rect();
		dst = new Rect();
		frameSequance = new int[frameTotal]; // 默认帧序列初始化
		for (int i = 0; i < frameSequance.length; i++) {
			frameSequance[i] = i; // 按顺序给帧序列赋值
		}
	}

	/**
	 * 使用多张图片构建多帧Sprite
	 *
	 * @param bitmapList
	 * @param width
	 * @param height
	 */
	public Sprite(List<Bitmap> bitmapList, int width, int height) {
		this.width = width;
		this.height = height;
		this.bitmapList = bitmapList;
	}

	public long getStep() {
		return step;
	}

	public void setStep(long step) {
		this.step = step;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean visible) {
		isVisible = visible;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean playing) {
		isPlaying = playing;
	}

	public int getAnimateInterval() {
		return animateInterval;
	}

	public void setAnimateInterval(int animateInterval) {
		this.animateInterval = animateInterval;
	}

	public float getX() {
		return x;
	}

	public float getCenterX() {
		return x + width / 2;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setCenterX(float centerX) {
		this.x = x - width / 2;
	}

	public float getY() {
		return y;
	}

	public float getCenterY() {
		return y + height / 2;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setCenterY(float centerY) {
		this.y = y - height / 2;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setCenterPosition(float x, float y) {
		this.x = x - width / 2;
		this.y = y - height / 2;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}

	public void setSpeed(float speedX, float speedY) {
		this.speedX = speedX;
		this.speedY = speedY;
	}

	public int[] getFrameSequance() {
		return frameSequance;
	}

	public void setFrameSequance(int[] frameSequance) {
		this.frameSequance = frameSequance;
	}

	public int getFrameSequanceIndex() {
		return frameSequanceIndex;
	}

	public void setFrameSequanceIndex(int frameSequanceIndex) {
		this.frameSequanceIndex = frameSequanceIndex;
	}

	/**
	 * 移动Sprite
	 *
	 * @param distanceX 相对横坐标
	 * @param distanceY 相对纵坐标
	 */
	public void move(float distanceX, float distanceY) {
		x += distanceX;
		y += distanceY;
		outOfBounds();
	}

	/**
	 * 越界处理
	 */
	public void outOfBounds() {
		/**
		 * 后续扩展可能有从各种方向进入的敌机/子弹，在子类中一一重写不现实，
		 * 所以改为：根据移动速度判断方向，如从右往左移动，只在左侧越界，以此类推
		 */
		if ((speedX < 0 && getX() + getWidth() < 0)
				|| (speedX > 0 && getX() > ScreenUtil.INSTANCE.getScreenWidth())
				|| (speedY < 0 && getY() + getHeight() < 0)
				|| (speedY > 0 && getY() > ScreenUtil.INSTANCE.getScreenHeight())) {
			setVisible(false);
		}
	}

	/**
	 * 下一帧
	 */
	public void nextFrame() {
		frameSequanceIndex = (frameSequanceIndex + 1) % frameSequance.length; // 超过总帧数回到第1帧
	}

	/**
	 * 逻辑操作
	 */
	public void doLogic() {
		step++;
		// 动画开启时每到达间隔时间执行下一帧
		if (isVisible && isPlaying && step % animateInterval == 0) {
			nextFrame();
		}
		if (isVisible) {
			move(speedX, speedY);
		}
	}

	/**
	 * 绘制操作
	 *
	 * @param canvas
	 */
	public void doDraw(Canvas canvas) {
		if (isVisible) { // 可见时才绘制
			// 若存在单张图片，按照单张图片方式绘制
			if (bitmap != null) {
				src.set((int) frameX[frameSequance[frameSequanceIndex]],
						(int) frameY[frameSequance[frameSequanceIndex]],
						(int) (frameX[frameSequance[frameSequanceIndex]] + width),
						(int) (frameY[frameSequance[frameSequanceIndex]] + height));
				dst.set((int) x, (int) y, (int) (x + width), (int) (y + height));
				canvas.drawBitmap(bitmap, src, dst, null);
			} else if (bitmapList != null) { // 图片列表不为空则按照多张图片方式绘制
				canvas.drawBitmap(bitmapList.get(frameSequanceIndex), x, y, null);
			}
		}
	}
}
