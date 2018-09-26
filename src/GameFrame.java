import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

public class GameFrame extends JFrame {

	static MapPanel mapPanel;
	public Movement movement;
	private int row = 20;
	private int column = 20;
	private int[][] selectedMap;
	private Go go;
	private static int counter;
	private int speed = 90;
	private int nx, ny, cx, cy;
	private int RIGHT = 1, DOWN = 2, LEFT = 3, UP = 4;
	private InputStream inputh = ClassLoader.getSystemResourceAsStream("house.png");
	private String s = "11111111111111111111100000000000000000p1"
			        + "1000000000000000000110000000000000000001"
			        + "1000000000000000000110000000000000000001"
			        + "1000000000000000000110000000000000000001"
			        + "1000000000000000000110000000000000000001"
			        + "1000000000000000000110000000000000000001"
			        + "1000000000000000000110000000000000000001"
			        + "1000000000000000000110000000000000000001"
			        + "1000000000000000000110000000000000000001"
			        + "1.00000000000000000111111111111111111111";

	GameFrame() {
		counter = 0;
		go = new Go();
		setMap();
		Movement.initializeMap(selectedMap);
		setTitle("Maze");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		mapPanel = new MapPanel();
		mapPanel.setBackground(Color.WHITE);
		setContentPane(mapPanel);
		setBounds(400, 130, Movement.column*20+6, Movement.row*20+29);
		addOptions();
		try {
			setIconImage(ImageIO.read(inputh));
		}
			catch(IOException e) {}
	}

	private void move() {
		try {
			counter = Movement.path.size()-1;
			go = new Go();
			go.start();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void toDigit() {

		String p;
		p= s.replaceAll("%", "1");
		p = p.replaceAll(" ", "0");
		s = p;
	}

	private void setMap() {

		selectedMap = new int[row][column];

		for(int i=0, k=0 ; i<row ; i++)
			for(int j=0 ; j<column ; j++, k++)
				if(s.charAt(k) == '1')
					selectedMap[i][j] = -1;
				else if(s.charAt(k) == '.')
					selectedMap[i][j] = -2;
				else if(s.charAt(k) == 'p' || s.charAt(k) == 'P') {
					selectedMap[i][j] = 0;
					MapPanel.start = new Dimension(i,j);
				}
				else
					selectedMap[i][j] = 0;
	}
	private void resetGame() {
		try {
			Movement.initializeMap(selectedMap);
			Movement.path.clear();
			mapPanel.mouse.setBounds(MapPanel.start.height*20, MapPanel.start.width*20, 20, 20);
			mapPanel.repaint();
			if(go!=null) {
				try {
					go.interrupt();
				}
				catch (Exception e) {
					e.getMessage();
				}
				go =null;
			}
		}
		catch (Exception e) {
			e.getMessage();
		}
	}
	class Go extends Thread {

		public void run() {
			while(true) {
				cx = Movement.path.elementAt(counter).height;
				cy = Movement.path.elementAt(counter).width;
				nx = Movement.path.elementAt(counter-1).height;
				ny = Movement.path.elementAt(counter-1).width;
	
				if(cx>nx)
					mapPanel.setMouseOrientationTo(LEFT);
				else if(nx>cx)
					mapPanel.setMouseOrientationTo(RIGHT);
				if(cy>ny)
					mapPanel.setMouseOrientationTo(UP);
				if(ny>cy)
					mapPanel.setMouseOrientationTo(DOWN);
	
				mapPanel.mouse.setBounds(cx*20, cy*20, 20, 20);
	
				counter--;
	
				if(counter < 1)
					go.stop();
				try {
					sleep(speed);
				}
				catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	private void addOptions() {

		JFileChooser explorer = new JFileChooser();
		explorer.setDialogTitle("Open a map");
		explorer.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		explorer.setMultiSelectionEnabled(false);
		explorer.setCurrentDirectory(new File("./"));
		JPopupMenu bar = new JPopupMenu();
		JMenuItem open = new JMenuItem("Open Map");
		open.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				int result = explorer.showOpenDialog(null);
				if(result == JFileChooser.CANCEL_OPTION)
					explorer.cancelSelection();
				else if(result == JFileChooser.APPROVE_OPTION) {
					File file = explorer.getSelectedFile();
					s = "";
					row = 0;
					try {
						Scanner input = new Scanner(new FileReader(file));
						String columner =  input.nextLine();
						column = columner.length();
						input.close();
						input = new Scanner(new FileReader(file));
						while(input.hasNext()) {
							s += input.nextLine();
							row++;
						}
						if(s.startsWith("%"))
							toDigit();
						setMap();
						setBounds(430, 130, column*20+6, row*20+29);
						input.close();
					}
					catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(null, "Invalid File!", "Eror", JOptionPane.ERROR_MESSAGE);
					}}
				resetGame();
				}});
		JMenuItem find = new JMenuItem("Find route");
		find.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				Movement.getOptimalRoute(MapPanel.start);
				mapPanel.repaint();
			}});
		JMenuItem reset = new JMenuItem("Reset");
		reset.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				resetGame();
			}});
		JMenuItem start = new JMenuItem("Start");
		start.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				Movement.getOptimalRoute(MapPanel.start);
				mapPanel.repaint();
				move();
				}});
		JMenu speedmeter = new JMenu("Speed");
		JMenuItem slow = new JMenuItem("slow");
		slow.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				speed =140;
			}});
		JMenuItem normal = new JMenuItem("normal");
		normal.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				speed =90;
			}});
		JMenuItem fast = new JMenuItem("fast");
		fast.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				speed =40;
			}});
		JMenuItem veryfast = new JMenuItem("very fast");
		veryfast.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				speed =10;
			}});
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				System.exit(0);
			}});
		speedmeter.add(slow);
		speedmeter.add(normal);
		speedmeter.add(fast);
		speedmeter.add(veryfast);
		bar.add(open);
		bar.add(find);
		bar.add(start);
		bar.add(reset);
		bar.add(speedmeter);
		bar.add(exit);
		addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent me) {

				if(me.isPopupTrigger())
					showMenu(me);
			}
			public void mousePressed(MouseEvent me) {

				if(me.isPopupTrigger())
					showMenu(me);
			}
			private void showMenu(MouseEvent me) {

				bar.show(me.getComponent(), me.getX(), me.getY());
			}
			public void mouseClicked(MouseEvent me) { }
			public void mouseEntered(MouseEvent me) { }
			public void mouseExited(MouseEvent me) { }});
	}
}
