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
 * 第1关 - 河道战役
 * Created by Feng on 2017/12/23.
 */
public class Stage1 extends Stage {

	private Bitmap enemyBitmap1;
	private Bitmap bulletBitmap2;
	private Bitmap blastBmp1;

	public Stage1() {
		super();
		setBossStep(GameView.FPS * 30); // Boss在30秒出现
		setBackground(new BitmapBackground(GameUtil.INSTANCE.getBitmap("background_river_480.png")));
		getBackground().setSpeed(1.5f);
		enemyBitmap1 = GameUtil.INSTANCE.getBitmap("enemy_black_small.png");
		bulletBitmap2 = GameUtil.INSTANCE.getBitmap("bullet_round_small.png");
		blastBmp1 = GameUtil.INSTANCE.getBitmap("blast_yellow_small.png");

		// 20个敌机初始化
		for (int i = 0; i < 20; i++) {
				Enemy enemy = new Enemy(enemyBitmap1, WeaponType.NORMAL);
				enemy.setSpeed(0, 3);
				enemy.setBlast(new Blast(blastBmp1, blastBmp1.getWidth() / 15, blastBmp1.getHeight()));
				enemy.setLifeCurrent(1);
				getEnemies().add(enemy);
		}

	}

	/**
	 * 进度控制
	 */
	@Override
	public void onProgress() {

		// 3~10秒后每1秒随机产生敌人
		if (getStep() > 3 * GameView.FPS
				&& getStep() < 10 * GameView.FPS
				&& getStep() % (0.5f * GameView.FPS) == 0) {
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

		// 10~20秒后有几率产生发射子弹的敌人
		if (getStep() > 10 * GameView.FPS
				&& getStep() < 20 * GameView.FPS
				&& getStep() % (0.5f * GameView.FPS) == 0) {
			// 复用已经隐藏的敌机
			for (Enemy enemy : getEnemies()) {
				if (enemy.isReusable()) {
					enemy.setPosition((float) ((getScreenWidth() - enemy.getWidth()) * Math.random()), -enemy.getHeight());
					int weaponRate = (int) (Math.random() * 100);
					if (weaponRate > 50) { // 50%的几率携带子弹
						List<Bullet> bullets = new ArrayList<>();
						for (int i = 0; i < 5; i++) {
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

		// 20~25秒后产生的敌人都有子弹
		if (getStep() > 20 * GameView.FPS
				&& getStep() < 25 * GameView.FPS
				&& getStep() % (0.5f * GameView.FPS) == 0) {
			// 复用已经隐藏的敌机
			for (Enemy enemy : getEnemies()) {
				if (enemy.isReusable()) {
					enemy.setPosition((float) ((getScreenWidth() - enemy.getWidth()) * Math.random()), -enemy.getHeight());
					int weaponRate = (int) (Math.random() * 100);
					if (weaponRate > 100 - 50) { // 50%的几率携带子弹
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
