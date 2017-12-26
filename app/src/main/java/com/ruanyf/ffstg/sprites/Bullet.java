package com.ruanyf.ffstg.sprites;

import android.graphics.Bitmap;

import java.util.List;

/**
 * 子弹类
 * Created by Feng on 2017/12/23.
 */
public class Bullet extends Sprite {

	/**
	 * 使用单张图片构建静态Sprite
	 *
	 * @param bitmap 位图对象
	 */
	public Bullet(Bitmap bitmap) {
		super(bitmap);
	}

	/**
	 * 使用单张图片构建多帧Sprite
	 *
	 * @param bitmap 位图对象
	 * @param width  帧宽度
	 * @param height 帧高度
	 */
	public Bullet(Bitmap bitmap, int width, int height) {
		super(bitmap, width, height);
	}

	/**
	 * 使用多张图片构建多帧Sprite
	 *
	 * @param bitmapList
	 * @param width
	 * @param height
	 */
	public Bullet(List<Bitmap> bitmapList, int width, int height) {
		super(bitmapList, width, height);
	}

}
