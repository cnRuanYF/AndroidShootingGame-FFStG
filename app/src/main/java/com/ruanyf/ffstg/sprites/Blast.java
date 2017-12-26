package com.ruanyf.ffstg.sprites;

import android.graphics.Bitmap;

import java.util.List;

/**
 * 爆炸精灵类
 * Created by Feng on 2017/12/24.
 */
public class Blast extends Sprite {

	/**
	 * 使用单张图片构建多帧Sprite
	 *
	 * @param bitmap 位图对象
	 * @param width  帧宽度
	 * @param height 帧高度
	 */
	public Blast(Bitmap bitmap, int width, int height) {
		super(bitmap, width, height);

		// 初始化为动态
		setAnimateInterval(5);
		setPlaying(true);
	}

	/**
	 * 使用多张图片构建多帧Sprite
	 *
	 * @param bitmapList
	 * @param width
	 * @param height
	 */
	public Blast(List<Bitmap> bitmapList, int width, int height) {
		super(bitmapList, width, height);

		// 初始化为动态
		setAnimateInterval(5);
		setPlaying(true);
	}

	/**
	 * 重写下一帧方法
	 */
	@Override
	public void nextFrame() {
		super.nextFrame();

		// 爆炸只执行一遍，执行完消失
		if (getFrameSequanceIndex() == 0) {
			setVisible(false);
		}
	}
}
