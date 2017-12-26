package com.ruanyf.ffstg.stages;

import android.graphics.Bitmap;

import com.ruanyf.ffstg.scenes.backgrounds.BitmapBackground;
import com.ruanyf.ffstg.sprites.Blast;
import com.ruanyf.ffstg.sprites.Bullet;
import com.ruanyf.ffstg.sprites.enemies.Boss1;
import com.ruanyf.ffstg.sprites.enemies.Enemy;
import com.ruanyf.ffstg.GameView;
import com.ruanyf.ffstg.WeaponType;
import com.ruanyf.ffstg.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 第一关
 * Created by Feng on 2017/12/23.
 */
public class Stage1 extends Stage {

	private Bitmap enemyBitmap1;
	private Bitmap bulletBitmap2;
	private Bitmap blastBmp1;

	public Stage1() {
		super();
		setBossStep(GameView.FPS * 30); // Boss在30秒出现
		setBackground(new BitmapBackground(GameUtil.INSTANCE.getBitmap("background1.png")));
		getBackground().setSpeed(1.5f);
		enemyBitmap1 = GameUtil.INSTANCE.getBitmap("enemy_black_small.png");
		bulletBitmap2 = GameUtil.INSTANCE.getBitmap("bullet2.png");
		blastBmp1 = GameUtil.INSTANCE.getBitmap("blast_yellow_small.png");
	}

	/**
	 * 进度控制
	 */
	@Override
	public void onProgress() {
		/*
		 * 第3秒出现第一波敌人(作为敌机初始化)
		 *  E E E E E E E E E
		 * i  E E E E E E E
		 * ↑    E E E E E
		 * |      E E E
		 * +--→j    E
		 */
		if (getStep() == 3 * GameView.FPS) {
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < i * 2 + 1; j++) {
					Enemy enemy = new Enemy(enemyBitmap1, WeaponType.NORMAL);
					enemy.setCenterPosition(getScreenWidth() / 2 - 50 * (i - j), -50 * i - enemy.getHeight());
					enemy.setSpeed(0, 3);
					enemy.setBlast(new Blast(blastBmp1, blastBmp1.getWidth() / 15, blastBmp1.getHeight()));
					enemy.setVisible(true);
					enemy.setLifeCurrent(1);
					getEnemies().add(enemy);
				}
			}
		}

		// 10~15秒后每1秒随机产生敌人
		if (getStep() > 10 * GameView.FPS
				&& getStep() < 15 * GameView.FPS
				&& getStep() % (1 * GameView.FPS) == 0) {
			// 复用已经隐藏的敌机
			for (Enemy enemy : getEnemies()) {
				if (enemy.isReusable()) {
					enemy.setPosition((float) ((getScreenWidth() - enemy.getWidth()) * Math.random()), -enemy.getHeight());
					enemy.setVisible(true);
					enemy.setLifeCurrent(1);
					break;
				}
			}
		}

		// 15~25秒后有几率产生发射子弹的敌人
		if (getStep() > 15 * GameView.FPS
				&& getStep() < 25 * GameView.FPS
				&& getStep() % (0.5f * GameView.FPS) == 0) {
			// 复用已经隐藏的敌机
			for (Enemy enemy : getEnemies()) {
				if (enemy.isReusable()) {
					enemy.setPosition((float) ((getScreenWidth() - enemy.getWidth()) * Math.random()), -enemy.getHeight());
					int weaponRate = (int) (Math.random() * 100);
					if (weaponRate > 100 - 10) { // 10%的几率携带散弹枪
						List<Bullet> bullets = new ArrayList<>();
						for (int i = 0; i < 15; i++) {
							bullets.add(new Bullet(bulletBitmap2));
						}
						enemy.setBullets(bullets);
						enemy.setFireInterval(GameView.FPS);
						enemy.setWeaponType(WeaponType.SHOTGUN3);
					} else if (weaponRate > 100 - 50) { // 50%的几率携带子弹
						List<Bullet> bullets = new ArrayList<>();
						for (int i = 0; i < 15; i++) {
							bullets.add(new Bullet(bulletBitmap2));
						}
						enemy.setBullets(bullets);
						enemy.setFireInterval(GameView.FPS);
						enemy.setWeaponType(WeaponType.NORMAL);
					}
					enemy.setLifeCurrent(1); // TODO 不同敌人血量不同
					enemy.setVisible(true);
					break;
				}
			}
		}

		// 出现Boss
		if (getStep() == getBossStep()) {
			setBoss(new Boss1());
		}

	}

}
