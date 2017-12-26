package com.ruanyf.ffstg.scenes.backgrounds;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.ruanyf.ffstg.GameView;
import com.ruanyf.ffstg.sprites.Sprite;
import com.ruanyf.ffstg.utils.GameUtil;
import com.ruanyf.ffstg.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 光斑下落背景
 * Created by Feng on 2017/12/26.
 */

public class lightfallBackground extends Background {

	private float screenW, screenH;
	private List<Sprite> meteors;
	private Bitmap light64Bmp, light128Bmp, light512Bmp, lightLongBmp;

	public lightfallBackground() {
		screenW = ScreenUtil.INSTANCE.getScreenWidth();
		screenH = ScreenUtil.INSTANCE.getScreenHeight();

		light64Bmp = GameUtil.INSTANCE.getBitmap("lights/light_circle_64_a50.png");
		light128Bmp = GameUtil.INSTANCE.getBitmap("lights/light_circle_128_a50.png");
		light512Bmp = GameUtil.INSTANCE.getBitmap("lights/light_circle_512_a10.png");
		lightLongBmp = GameUtil.INSTANCE.getBitmap("lights/light_long45d_a50.png");

		// 流星
		meteors = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
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

	@Override
	public void doLogic() {
		super.doLogic();

		if (getStep() % GameView.FPS / 4 == 0) {
			for (Sprite sprite : meteors) {
				if (!sprite.isVisible()) {
					sprite.setPosition(screenW, -sprite.getHeight() / 2 - screenW
							+ (screenW + screenH) * (float) Math.random());
					sprite.setVisible(true);
					break;
				}
			}
		}
		for (Sprite sprite : meteors) {
			sprite.doLogic();
		}

	}

	/**
	 * 绘制操作
	 *
	 * @param canvas 画布对象
	 */
	@Override
	public void doDraw(Canvas canvas) {

		for (Sprite sprite : meteors) {
			sprite.doDraw(canvas);
		}

	}
}
