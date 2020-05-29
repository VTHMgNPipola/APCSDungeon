package gameObjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * Created May 26, 2020
 * @author t4canty
 * @author TJ178
 *
 */
public class Player extends GameObject{
	//========Variables========//
	private Gun activeGun;																		//Currently held gun.
	private int minX = 0;	//bounds of room
	private int minY = 0;
	private int maxX;													
	private int maxY;
	private int graphicsDir;		//direction that a player is holding their gun
	private long lastWalk = 0; 		//last time player moved - used for idle vs moving animation
	private long lastDamageTaken = 0;//last time the player took damage - used for hurt animation
	private double gunAngle;
	public boolean isShooting;
	private long lastBulletShot = 0; 															//system time when last bullet was shot, used for cooldown
	private int ammo = 20;
	ArrayList<Gun> inventory = new ArrayList<Gun>();											//List of guns currently in the player's inventory
	
	//========Constructors========//
	/**
	 * Player constructor with x and y inputs;
	 * @param x
	 * Starting X position for the player on the jframe
	 * @param y
	 * Starting Y position for the player on the jframe
	 * @param size
	 * Size of the player object
	 * @param Sprite1
	 * Full path to the idle sprite
	 * @param Sprite2
	 * Full path to the move sprite
	 * @param Sprite3
	 * Full path to the hurt sprite
	 * @param Sprite4
	 * Full path to Attack Sprite
	 * 
	 */
	public Player(int x, int y, Dimension size, BufferedImage Sprite1, BufferedImage Sprite2, BufferedImage Sprite3, BufferedImage Sprite4) throws IOException {
		this.x = x;
		this.y = y;
		this.hp = 100;
		this.rBox = new Rectangle(size);
		idleSprite = new AnimatedImage(Sprite1);
		moveSprite = new AnimatedImage(Sprite2);
		attackSprite = new AnimatedImage(Sprite3);
		hurtSprite = new AnimatedImage(Sprite4);
		
		activeGun = new Gun(10, 300, 10, 10, 10, 0, "Bad Gun", super.isJar);
		inventory.add(activeGun);
	}
	/**
	 * Player constructor
	 * @param x
	 * Starting X position for the player on the jframe
	 * @param y
	 * Starting Y position for the player on the jframe
	 * @param size
	 * Size of the player object
	 * @param x
	 * Starting X position for the player on the jframe
	 * @param y
	 * Starting Y position for the player on the jframe
	 * @param size
	 * Size of the player object
	 * @param Sprite1
	 * Full path to the idle sprite
	 * @param Sprite2
	 * Full path to the move sprite
	 * @param Sprite3
	 * Full path to the hurt sprite
	 * @param Sprite4 full path to Attack Sprite
	 */
	public Player(Dimension size, BufferedImage Sprite1, BufferedImage Sprite2, BufferedImage Sprite3, BufferedImage Sprite4) throws IOException {
		this.x = 0;
		this.y = 0;
		this.hp = 100;
		this.rBox = new Rectangle(size);
		idleSprite = new AnimatedImage(Sprite1);
		moveSprite = new AnimatedImage(Sprite2);
		attackSprite = new AnimatedImage(Sprite3);
		hurtSprite = new AnimatedImage(Sprite4);
		activeGun = new Gun(10, 300, 10, 10, 10, 0, "Bad Gun", super.isJar);
		inventory.add(activeGun);
	}
		/**
		 * 
	 * Player constructor with x and y inputs;
	 * @param x
	 * Starting X position for the player on the jframe
	 * @param y
	 * Starting Y position for the player on the jframe
	 * @param size
	 * Size of the player object
	 * @param Sprite1
	 * Full path to the idle sprite
	 * @param Sprite2
	 * Full path to the move sprite
	 * @param Sprite3
	 * Full path to the hurt sprite
	 * @param Sprite
	 * Full path to Attack Sprite
	 * @param debug
	 * Sets debug flag
	 * 
	 */
	public Player(int x, int y, Dimension size, BufferedImage Sprite1, BufferedImage Sprite2, BufferedImage Sprite3, BufferedImage Sprite4, boolean debug) throws IOException {
		this(x, y, size, Sprite1, Sprite2, Sprite3, Sprite4);
		this.debug = debug;
	}
	/**
	 * Player constructor
	 * @param x
	 * Starting X position for the player on the jframe
	 * @param y
	 * Starting Y position for the player on the jframe
	 * @param size
	 * Size of the player object
	 * @param x
	 * Starting X position for the player on the jframe
	 * @param y
	 * Starting Y position for the player on the jframe
	 * @param size
	 * Size of the player object
	 * @param Sprite1
	 * Full path to the idle sprite
	 * @param Sprite2
	 * Full path to the move sprite
	 * @param Sprite3
	 * Full path to the hurt sprite
	 * @param Sprite4 full path to Attack Sprite
	 * @param debug
	 * Sets debug flag
	 */
	public Player(Dimension size, BufferedImage Sprite1, BufferedImage Sprite2, BufferedImage Sprite3, BufferedImage Sprite4, boolean debug) throws IOException {
		this(size, Sprite1, Sprite2, Sprite3, Sprite4);
		this.debug = debug;
	}
	
	//========Methods========//
	@Override
	public void paint(Graphics g) {
		rBox.x = x;																					//set hitbox to curremnt y
		rBox.y = y;																					//Set hitbox to current x
		Graphics2D g2d = (Graphics2D) g; 															//neccessary for drawing gifs
		g2d.setColor(Color.BLACK);
		
		if(Math.abs(gunAngle) > 2.35) {
			graphicsDir = LEFT;
		}else if(Math.abs(gunAngle) < .79) {
			graphicsDir = RIGHT;
		}else if(gunAngle > 0) {
			graphicsDir = UP;
		}else {
			graphicsDir = DOWN;
		}
		
		
		///All of these states are the same right now as we don't have sprites for them yet
		if(System.currentTimeMillis() - lastWalk < 75 && !(System.currentTimeMillis() - lastDamageTaken < 20)) {
			
			//moving sprites
			switch(graphicsDir) {
			case LEFT:
				g2d.drawImage(moveSprite.getCurrentFrame(), x, y, rBox.width, rBox.height, null);
				break;
			case RIGHT:
				g2d.drawImage(moveSprite.getCurrentFrame(), x + rBox.width, y, -rBox.width, rBox.height, null);
				break;
			case UP:
				//include the other thing later
				//break;
			case DOWN:
				g2d.drawImage(idleSprite.getCurrentFrame(), x, y, rBox.width, rBox.height, null);
			}
			
			///hurt sprites
		} else if(System.currentTimeMillis() - lastDamageTaken < 20){
			switch(graphicsDir) {
			case LEFT:
				g2d.drawImage(moveSprite.getCurrentFrame(), x, y, rBox.width, rBox.height, null);
				break;
			case RIGHT:
				g2d.drawImage(moveSprite.getCurrentFrame(), x + rBox.width, y, -rBox.width, rBox.height, null);
				break;
			case UP:
				//include the other thing later
				//break;
			case DOWN:
				g2d.drawImage(idleSprite.getCurrentFrame(), x, y, rBox.width, rBox.height, null);
			}
			
			//idle sprites
		} else {
			switch(graphicsDir) {
			case LEFT:
				g2d.drawImage(moveSprite.getCurrentFrame(), x, y, rBox.width, rBox.height, null);
				break;
			case RIGHT:
				g2d.drawImage(moveSprite.getCurrentFrame(), x + rBox.width, y, -rBox.width, rBox.height, null);
				break;
			case UP:
				//include the other thing later
				//break;
			case DOWN:
				g2d.drawImage(idleSprite.getCurrentFrame(), x, y, rBox.width, rBox.height, null);
			}
		}
		
		
		
		if(debug) g2d.draw(rBox);
		g2d.rotate(gunAngle, rBox.getCenterX(), rBox.getCenterY());
		if(Math.abs(gunAngle) > 1.07) {
			g2d.drawImage(activeGun.getSprite(), (int)(rBox.getCenterX()) + 13, (int)(rBox.getCenterY()) + 20, 50, -50, null);
		}else {
			g2d.drawImage(activeGun.getSprite(), (int)(rBox.getCenterX()) + 13, (int)(rBox.getCenterY()) - 20, 50, 50, null);
		}
		
		//if(debug) g2d.drawLine((int)(rBox.getCenterX()), (int)(rBox.getCenterY()), (int)(rBox.getCenterX() + 100), (int)(rBox.getCenterY()));
		g2d.rotate(-gunAngle, rBox.getCenterX(), rBox.getCenterY());
	}
	/**
	 * Method to determine if cooldown is over
	 * @return
	 * returns true or false if enough time has passed. 
	 */
	public boolean canShootBullet() {
		return activeGun.canShoot();
	}
	/**
	 * Movement method.
	 * @param dir
	 * Determines the direction of the movement.
	 */
	public void move(int dir) {
		lastWalk = System.currentTimeMillis();
		switch(dir) {
		case UP:
			y -= 10;
			break;
		case RIGHT:
			x += 10;
			break;
		case DOWN:
			y += 10;
			break;
		case LEFT:
			x -= 10;
			break;
		case UPRIGHT:
			y -= 5;
			x += 5;
			break;
		case UPLEFT:
			y -= 5;
			x -= 5;
			break;
		case DOWNRIGHT:
			y += 5;
			x += 5;
			break;
		case DOWNLEFT:
			y += 5;
			x -= 5;
			break;
			
		}
		if(y < minY) y = minY;																			//Collision on the bounds of the room
		if(y > maxY) y = maxY;
		if(x < minX) x = minX;
		if(x > maxX) x = maxX;
	}
	/**
	 * Teleport player to given x/y coordinates. 
	 * @param x
	 * @param y
	 */
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Update method to determine the angle of the gun
	 * @param mouseX
	 * @param mouseY
	 */
	public void updateGunAngle(int mouseX, int mouseY) {
		gunAngle = Math.atan2(mouseY - rBox.getCenterY(), mouseX - rBox.getCenterX());
	}
	/**
	 * Method to check all currently colliding entities.
	 * @param entities
	 */
	public void checkCollision(ArrayList<GameObject> entities) {
		for(int i = 0; i < entities.size(); i++) {
			if(entities.get(i).getHitbox().intersects(rBox)) {
				if(entities.get(i) instanceof Projectile) {
					if(((Projectile) entities.get(i)).isEnemyFire()) {
						hp -= ((Projectile) entities.get(i)).getDamage();
						entities.remove(i);
						i--;
					}
				}else if(entities.get(i) instanceof DroppedItem) {
					((DroppedItem)entities.get(i)).getItem().use(this);
					entities.remove(i);
					i--;
				}
			}
		}
	}
	
	
	/**
	 * Return projectile entity shot from gun
	 */
	//TODO: make this responsive to different types of guns
	public Projectile getNewBullet() {
		return activeGun.getGunshot(getCenterX(), getCenterY(), gunAngle, false);
	}
	
	public void reload() {
		ammo = activeGun.reload(ammo);
	}
	
	
	/**
	 * Sets collision bounds for the JFrame.
	 * @param bounds
	 * Bounds read from a room object. 
	 */
	public void updateBounds(int[] bounds) {
		minY = bounds[0];
		maxX = bounds[1] - rBox.width;
		maxY = bounds[2] - rBox.height;
		minX = bounds[3];
	}
	
	//========Getters/Setters========//
	public void add(Gun l) {inventory.add(l);}
	public Gun get(int i) {return inventory.get(i);}
	public ArrayList<Gun> getInventory(){return inventory;}
	public double getGunAngle() { return gunAngle; }
	public Gun getActiveGun() { return activeGun; }
	public void setActiveGun(Gun g) {activeGun = g; }
	public int getTotalAmmo() { return ammo; }
	public int getAmmoInMag() { return activeGun.getAmmoInMag(); }
	public void addAmmo(int amt) { ammo += amt; }
}
