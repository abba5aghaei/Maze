import java.awt.Dimension;
import java.util.Stack;
import java.util.Vector;
import javax.swing.JOptionPane;

public class Movement {
	
	static int[][] map;
	static int row;
	static int column;
	private static Dimension check;
	static boolean existWay = false;
	static Vector<Dimension> path = new Vector<Dimension>();
	
	static void initializeMap(int[][] selectedMap) {
		
		row = selectedMap.length;
		column = selectedMap[0].length;
		map = new int[row][column];
		for(int i=0 ; i<row ; i++)
			for(int j=0 ; j<column ; j++)
				map[i][j] = selectedMap[i][j];
	}
	static void getOptimalRoute(Dimension p){
		
		Stack stack = new Stack();
		stack.push(p);
		path.addElement(new Dimension(50,50));
		
		while(!stack.isEmpty()){
			check = (Dimension)stack.pop();
			if(check.width >0){
				if (map[check.width-1][check.height] == 0 || map[check.width-1][check.height] == -2)
				{
					if (map[check.width-1][check.height] == -2)
					{
						existWay = true;
						break;
					}
					map[check.width-1][check.height] = map[check.width][check.height] + 1;  
					Dimension pos = new Dimension(check.width-1,check.height);
					stack.push(pos);
				}
			}
			if (check.width <row)
			{
				if (map[check.width+1][check.height] == 0 || map[check.width+1][check.height] == -2)
				{
					if ( map[check.width+1][check.height] == -2)
					{
						existWay = true;
						break;
					}
					map[check.width+1][check.height] = map[check.width][check.height] + 1;
					Dimension pos = new Dimension(check.width+1,check.height);
					stack.push(pos);
				}
			}
			if (check.height > 0)
			{
				if (map[check.width][check.height-1] == 0 || map[check.width][check.height-1] == -2)
				{
					if (map[check.width][check.height-1] == -2)
					{
						existWay = true;
						break;
					}
					map[check.width][check.height-1] = map[check.width][check.height] + 1;
					Dimension pos = new Dimension(check.width,check.height-1);
					stack.push(pos);
				}
			}
			if (check.height <column )
			{
				if (map[check.width][check.height+1] == 0 || map[check.width][check.height+1] == -2)
				{
					if (map[check.width][check.height+1] == -2)
					{
						existWay = true;
						break;
					}
					map[check.width][check.height+1] = map[check.width][check.height] + 1;
					Dimension pos = new Dimension(check.width,check.height+1);
					stack.push(pos);
				}
			}
		}
		if (existWay == false){
			JOptionPane.showMessageDialog(null, "There are no way to house!","map",JOptionPane.INFORMATION_MESSAGE);
		}
		else{
			int l;
			int q = map[check.width][check.height];
	 
			for (int i=0; i<q; i++)
			{
				l = map[check.width][check.height]-1;
				path.addElement(check);
				map[check.width][check.height] = -3;
	 
				if (check.width > 0)
				{
					if (map[check.width-1][check.height] == l)
					{
						Dimension check1 =new  Dimension(check.width-1,check.height-1);
						check = check1;
					}
				}
				if (check.width <row)
				{
					if (map[check.width+1][check.height] == l)
					{	
						Dimension check1 = new Dimension(check.width+1,check.height);
						check = check1;								
					}
				}
				if (check.height > 0)
				{
					if (map[check.width][check.height-1] == l)
					{
						Dimension check1 = new Dimension(check.width,check.height-1);
						check = check1;								
					}
				}
				if (check.height <column)
				{
					if (map[check.width][check.height+1] == l)
					{
						Dimension check1 = new Dimension(check.width,check.height+1);
						check = check1;								
					}
				}
			}
		}
	}
}
