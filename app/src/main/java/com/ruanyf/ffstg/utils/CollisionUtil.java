package com.ruanyf.ffstg.utils;

import com.ruanyf.ffstg.sprites.Sprite;

/**
 * 碰撞检测工具类
 * Created by Feng on 2017/12/24.
 */
public class CollisionUtil {

	/**
	 * 基于位图对象的碰撞检测
	 *
	 * @param sprite1 碰撞判定区域为位图的Sprite
	 * @param sprite2 碰撞判定区域为位图的Sprite
	 * @return 若发生碰撞返回true
	 */
	public static boolean bitmapCollision(Sprite sprite1, Sprite sprite2) {
		// 使用排除法排除不可能发生碰撞的情形
		if (!sprite1.isVisible() || !sprite2.isVisible()                 // 排除不可见情形
				|| sprite1.getX() + sprite1.getWidth() < sprite2.getX()  // 1在2的左边
				|| sprite1.getY() + sprite1.getHeight() < sprite2.getY() // 1在2的上方
				|| sprite1.getX() > sprite2.getX() + sprite2.getWidth()  // 1在2的右边
				|| sprite1.getY() > sprite2.getY() + sprite2.getHeight() // 1在2的下方
				) {
			return false;
		}
		return true;
	}

	/**
	 * 基于位图对象与点的碰撞检测
	 *
	 * @param bmpSprite 碰撞判定区域为位图的Sprite
	 * @param dotSprite 碰撞判定区域为中心点的Sprite
	 * @return 若发生碰撞返回true
	 */
	public static boolean bitmapDotCollision(Sprite bmpSprite, Sprite dotSprite) {
		if (bmpSprite.isVisible() && dotSprite.isVisible()
				&& dotSprite.getCenterX() >= bmpSprite.getX()
				&& dotSprite.getCenterX() <= bmpSprite.getX() + bmpSprite.getWidth()
				&& dotSprite.getCenterY() >= bmpSprite.getY()
				&& dotSprite.getCenterY() <= bmpSprite.getY() + bmpSprite.getHeight()
				) {
			return true;
		}
		return false;
	}

	/**
	 * 基于圆形判定区域的碰撞检测
	 *
	 * @param sprite1 碰撞判定区域为圆形的Sprite
	 * @param sprite2 碰撞判定区域为圆形的Sprite
	 * @return 若发生碰撞返回true
	 */
	public static boolean circleCollision(Sprite sprite1, Sprite sprite2) {
		if (sprite1.isVisible() && sprite2.isVisible()) {

			// 计算两个Sprite中点坐标在x/y轴的差
			float diffX = sprite1.getCenterX() - sprite2.getCenterX();
			float diffY = sprite1.getCenterY() - sprite2.getCenterY();

			// 取长宽中较小的边作为半径
			float radius1 = (sprite1.getWidth() <= sprite1.getHeight() ? sprite1.getWidth() : sprite1.getHeight()) / 2;
			float radius2 = (sprite2.getWidth() <= sprite2.getHeight() ? sprite2.getWidth() : sprite2.getHeight()) / 2;

			// 使用三角函数判断，若两个Sprite半径之和大于中心点距离，则碰撞
			if (radius1 + radius2 > Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 基于圆形区域与点的碰撞检测
	 *
	 * @param circleSprite 碰撞判定区域为圆形的Sprite
	 * @param dotSprite    碰撞判定区域为中心点的Sprite
	 * @return 若发生碰撞返回true
	 */
	public static boolean circleDotCollision(Sprite circleSprite, Sprite dotSprite) {
		if (circleSprite.isVisible() && dotSprite.isVisible()) {

			// 计算两个Sprite中点坐标在x/y轴的差
			float diffX = circleSprite.getCenterX() - dotSprite.getCenterX();
			float diffY = circleSprite.getCenterY() - dotSprite.getCenterY();

			// 取长宽中较小的边作为半径
			float radius = (circleSprite.getWidth() <= circleSprite.getHeight() ? circleSprite.getWidth() : circleSprite.getHeight()) / 2;

			// 使用三角函数判断，若circleSprite半径大于中心点距离，则碰撞
			if (radius > Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2))) {
				return true;
			}
		}
		return false;
	}
}
