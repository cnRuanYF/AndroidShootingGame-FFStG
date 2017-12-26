package com.ruanyf.ffstg.sprites.enemies;

import android.graphics.Bitmap;

import com.ruanyf.ffstg.sprites.Blast;
import com.ruanyf.ffstg.sprites.Bullet;
import com.ruanyf.ffstg.GameView;
import com.ruanyf.ffstg.WeaponType;
import com.ruanyf.ffstg.utils.GameUtil;
import com.ruanyf.ffstg.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 第一个BOSS
 * Created by Feng on 2017/12/24.
 */
public class Boss1 extends Enemy {

	/**
	 * 构建Boss1
	 */
	public Boss1() {
		super(GameUtil.INSTANCE.getBitmap("enemy_pink_large.png"));
		setCenterX(ScreenUtil.INSTANCE.getScreenWidth() / 2);
		setY(0 - getHeight());
		setSpeedY(3);
		initLife(30); // TODO 血量待考量

		Bitmap blastBmp = GameUtil.INSTANCE.getBitmap("blast_pink_large.png");
		setBlast(new Blast(blastBmp, blastBmp.getWidth() / 15, blastBmp.getHeight()));

		List<Bullet> bullets = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			bullets.add(new Bullet(GameUtil.INSTANCE.getBitmap("bullet2.png")));
		}
		setBullets(bullets);
		setFireInterval((int) (0.5 * GameView.FPS));

		setVisible(true);
	}

	/**
	 * 逻辑操作
	 */
	@Override
	public void doLogic() {
		super.doLogic();

		// 切换武器
		if (getStep() % 300 == 0) {
			setWeaponType(WeaponType.NORMAL);
		} else if ((getStep() + 100) % 300 == 0) {
			setWeaponType(WeaponType.SHOTGUN3);
		} else if ((getStep() + 200) % 300 == 0) {
			setWeaponType(WeaponType.SHOTGUN5);
		}

		// 进场后不再下移,开始横向移动
		if (getCenterY() > ScreenUtil.INSTANCE.getScreenHeight() / 5) {
			setCenterY(ScreenUtil.INSTANCE.getScreenHeight() / 5); // 归位
			setSpeed(2,0);
		}

		// 左右移动(横坐标>屏幕宽度3/4时改为向左移动，反之亦然)
		if (getCenterX() >= ScreenUtil.INSTANCE.getScreenWidth() * 3 / 4) {
			setSpeedX(-2);
		} else if (getCenterX() <= ScreenUtil.INSTANCE.getScreenWidth() * 1 / 4) {
			setSpeedX(2);
		}

	}
}
