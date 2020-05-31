package displayComponents;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import driver.Driver;
import fileIO.ImageLoader;
import gameObjects.Player;

/**
 *	
 * Created May 30, 2020
 * @author t4canty
 * @author TJ178
 * Class for an animated startup screen that also spawns the ImageLoader Thread
 * 
 */

public class Startup extends JPanel implements ActionListener{
	//========Varibles========//
	private boolean debug;
	private boolean maxed = false;
	private boolean animationFinished = false;
	private boolean isJar;
	private int id = Player.MARINE;
	private int alpha = 255;
	private float a2 = 0.0f;
	private Dimension d;
	private String t;
	private JFrame f;
	private ImageLoader i;
	private JButton start;
	private JLabel pictureLabel;
	private JPanel selectPanel;
	private JRadioButton marine;
	private JRadioButton wsb;
	private JRadioButton s;
	private Image logo;
	
	
	public Startup(Dimension bounds, String title, boolean debug, boolean isJar) {
		//====Pre-Setup====//
		//Load images before ImageLoader
		ImageIcon Placeholder = null;
		try {
			Placeholder = new ImageIcon(ImageIO.read((Startup.class.getResourceAsStream("/img/noimage.png"))));
			logo = ImageIO.read((Startup.class.getResourceAsStream("/img/gameLogo.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		i = new ImageLoader();															//Start imageLoader  thread
		i.start(isJar); 
		
		//====Setup====//
		f = new JFrame("Startup");
		this.debug = debug;
		d = bounds;
		t = title;
		selectPanel = new JPanel();
		marine = new JRadioButton("Marine");
		wsb = new JRadioButton("WSB");
		s = new JRadioButton("Secret");
		start = new JButton("Start");
		JPanel anotherFuckingPanelJustForButtons = new JPanel();
		pictureLabel = new JLabel(Placeholder);
		ButtonGroup bg = new ButtonGroup();
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} 		 //Replace later with custom buttons - but for now better than the ugly default
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException| UnsupportedLookAndFeelException e1) {e1.printStackTrace();}
		
		//====Component setup====//
		f.setResizable(false);
		f.setBackground(Color.white);
		f.setSize(800, 800);
		
		start.setActionCommand("l");
		start.addActionListener(this);
		start.setEnabled(false);
		marine.setActionCommand("m");
		marine.addActionListener(this);
		wsb.setActionCommand("w");
		wsb.addActionListener(this);
		s.setActionCommand("s");
		s.addActionListener(this);
		s.setEnabled(false);
		wsb.setEnabled(false);
		marine.setEnabled(false);
		
		bg.add(marine);
		bg.add(wsb);
		bg.add(s);
		
		anotherFuckingPanelJustForButtons.setLayout(new GridLayout(1, 3));
		anotherFuckingPanelJustForButtons.add(marine);
		anotherFuckingPanelJustForButtons.add(wsb);
		anotherFuckingPanelJustForButtons.add(s);
		
		selectPanel.setLayout(new BorderLayout());
		selectPanel.add(pictureLabel);
		selectPanel.add(anotherFuckingPanelJustForButtons, BorderLayout.PAGE_END);
		
		this.setLayout(new BorderLayout());
		this.add(selectPanel);
		this.add(start, BorderLayout.PAGE_END);
		
		f.add(this);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Timer t = new Timer(30, this);
		t.start();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paintComponents(g);
		g.setColor(new Color(0, 0, 0, alpha));
		g.fillRect(0, 0, f.getWidth(), f.getHeight()); 								//Animated black screen
		
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a2);//Animated logo
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(ac);
		g2d.drawImage(logo, f.getWidth()/2 - logo.getWidth(null)/2, f.getHeight()/2 - logo.getHeight(null)/2, null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!animationFinished) {													//Function to fade out logo
			if(a2 < 1 && !maxed)
				a2 += 0.01f;
			else if(a2 >= 1) {
				maxed = true;
				a2 -= 0.01;
			}else if(a2 > 0.1 && maxed) {
				a2 -= 0.01;
			}
			if(a2 < 0.1 && maxed) {
				animationFinished = true;
				a2 = 0f;
			}
		}
		
		if(animationFinished && alpha == 0) {										//Re-enable buttons after the animation completes
			s.setEnabled(true);
			wsb.setEnabled(true);
			marine.setEnabled(true);
		}
		
		if(a2 > 1) { a2 = (float) 1; }												//Error correction
		if(alpha > 0 && animationFinished) alpha /= 1.2;
		
		repaint();
		
		if(!i.isAlive()) start.setEnabled(true);		
		if(e.getActionCommand() != null) System.out.println(e.getActionCommand());
		if(e.getActionCommand() != null) {
			switch(e.getActionCommand()) {
			case "l":
				System.out.println("Creating Driver");
				new Driver(d, t, debug, id, isJar);
				f.dispose();
				break;
			case "m":
				if(debug) System.out.println("Selected marine");
				id = Player.MARINE;
				//pictureLabel = new ImageIcon(marineSplash)  set this later
				pictureLabel.setIcon(null);
				selectPanel.revalidate();
				break;
			case "w":
				if(debug) System.out.println("Selected WSB");
				pictureLabel.setIcon(null);
				selectPanel.revalidate();
				id = Player.WSB;
				break;
			case "s":
				if(debug) System.out.println("Selected Secret");
				pictureLabel.setIcon(null);
				selectPanel.revalidate();
				id = Player.SECRET;
			}
		}
	}
}
