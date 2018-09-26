import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MapPanel extends JPanel {
	
	static Dimension start = new Dimension();
	static Vector<Dimension> end = new Vector<Dimension>();
	public JLabel mouse;
	private BufferedImage grass;
	private TexturePaint grasstp;
	private BufferedImage wall;
	private TexturePaint walltp;
	private BufferedImage house;
	private InputStream inputg = ClassLoader.getSystemResourceAsStream("grass.png");
	private InputStream inputw = ClassLoader.getSystemResourceAsStream("wall.png");
	private InputStream inputh = ClassLoader.getSystemResourceAsStream("house.png");
	private InputStream inputm1 = ClassLoader.getSystemResourceAsStream("mouse1.png");
	private InputStream inputm2 = ClassLoader.getSystemResourceAsStream("mouse2.png");
	private InputStream inputm3 = ClassLoader.getSystemResourceAsStream("mouse3.png");
	private InputStream inputm4 = ClassLoader.getSystemResourceAsStream("mouse4.png");
	private ImageIcon mouseRight, mouseDown, mouseLeft, mouseUp;
	
	MapPanel() {
		setLayout(null);
		try {
			mouseRight = new ImageIcon(ImageIO.read(inputm1));
			mouseDown = new ImageIcon(ImageIO.read(inputm2));
			mouseLeft = new ImageIcon(ImageIO.read(inputm3));
			mouseUp = new ImageIcon(ImageIO.read(inputm4));
			mouse = new JLabel(mouseRight);
			grass = ImageIO.read(inputg);
			wall = ImageIO.read(inputw);
			house = ImageIO.read(inputh);
		} catch (IOException e) {}
		
		grasstp = new TexturePaint(grass, new Rectangle(0,0,20,20));
		walltp = new TexturePaint(wall, new Rectangle(0,0,20,20));
		mouse.setBounds(start.height*20, start.width*20, 20, 20);
		add(mouse);
	}
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;	
		g2d.setPaint(grasstp);
		int x=0, y=0;
		for(int i=0 ; i<Movement.row ; i++) {
			
			for(int j=0 ; j<Movement.column ; j++) {
				
				if(Movement.map[i][j] == -1 )
					g2d.setPaint(walltp);
					
				else
					g2d.setPaint(grasstp);
					
				g2d.fillRect(x, y, 20, 20);
				x+=20;
				
				if(Movement.map[i][j] == -3 ) {
					g2d.setPaint(walltp);
					g2d.fillOval(j*20, i*20, 5, 5);
				}
				else if(Movement.map[i][j] == -2) {
					
					end.addElement(new Dimension(j,i));
					g2d.drawImage(house, null, j*20, i*20);
					}
				}
			x=0;
			y+=20;
			}
		}
	void setMouseOrientationTo(int o) {
		
		switch(o) {
		
		case 1: mouse.setIcon(mouseRight); break;
		case 2: mouse.setIcon(mouseDown); break;
		case 3: mouse.setIcon(mouseLeft); break;
		case 4: mouse.setIcon(mouseUp); break;
		}
	}
}
