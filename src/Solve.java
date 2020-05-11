import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Solve {

	final static String BADLINE = "A line in the file did not have the specified width";
	
	final static char MARIO = 'M'; // indicates Mario's starting position
	final static char PEACH = 'P'; // indicates Peach's location
	final static char VISIT = '~'; // indicates that a visible space has been visited
	final static char OPEN = ' ';  // indicates an visible, unvisited space
	final static char PATH = '@';  // used to indicate the path from Mario to Peach
	
	//static boolean hpo, hmo, wpo, wmo = false;
	
	public static void main(String[] args) {
		test();
		char[][] maze = loadMaze();
		int[] start = findMario(maze);
		maze[start[0]][start[1]] = OPEN;
		// your code below here
		pathify(maze, start[0], start[1]);
		solve(maze, start[0], start[1]);
		// your code above here
		maze[start[0]][start[1]] = MARIO;
		print(maze);
	}
	
	private static void test() {
		
	}
		
	
	private static boolean invalid(char[][] maze, int h, int w) {
		if(h > maze.length) {
			return true;
		} else {
			if(w > maze[h].length) {
				return true;
			}
		}
		return false;
	}

	private static boolean blocked(char[][] maze, int h, int w) {
		if(!invalid(maze, h, w)) {
			if(maze[h][w] == '-' || maze[h][w] == '+') {
				return true;
			}
			return false;
		}
		return invalid(maze, h, w);
	}

	private static boolean visited(char[][] maze, int h, int w) {
		if(maze[h][w] == VISIT) {
			return true;
		}
		return false;
	}

	private static boolean rescued(char[][] maze, int h, int w) {
		if(maze[h][w] == PEACH) {
			return true;
		}
		return false;
	}	

	private static boolean visible(char[][] maze, int h, int w) {
		if(maze[h][w] == OPEN) {
			return true;
		}
		return false;
	}
	
	private static void marvisit(char[][] maze, int h, int w) {
		if(!invalid(maze, h, w)) {
			maze[h][w] = VISIT;
		}
	}
		
	private static void pathify(char[][] maze, int h, int w) {
		if(!invalid(maze, h, w)) {
			maze[h][w] = PATH;
		}
	}
		
	private static int[] findMario(char[][] maze) {
		for (int h = 0; h < maze.length; h++) {
			for (int w = 0; w < maze[h].length; w++) {
				if (maze[h][w] == MARIO) {
					int[] mario = {h, w};
					return mario;
				}
			}
		}
		
		System.err.println("Could not find Mario in the maze");
		System.exit(0);
		return null; // can't get here but Java doesn't know that
	}
	
	private static void print(char[][] maze) {
		for (char[] c : maze) {
			System.out.println(new String(c));
		}
	}
	
	private static void clean(char[][] maze) {
		for (int h = 0; h < maze.length; h++) {
			for (int w = 0; w < maze[h].length; w++) {
				if (maze[h][w] == VISIT) {
					maze[h][w] = OPEN;
				}
			}
		}
	}

	public static char[][] loadMaze() {
		System.out.print("Maze file name: ");
		Scanner in = new Scanner(System.in);
		String fileName = in.nextLine();
		try {
			return getMaze(fileName);
		}
		catch (FileNotFoundException e) {
			System.err.println("Could not find file " + fileName);			
		}
		catch (NumberFormatException e) {
			System.err.println("File " + fileName + " does not have width and height on first 2 lines.");
		}
		catch (NoSuchElementException e) {
			if (e.getMessage().equals(BADLINE)) {
				System.err.println(BADLINE);
			}
			else {
				System.err.println("The height specified in the file is too large for the number of lines that follow.");
			}
		}

		System.exit(0);
		return null; // can't get here, but Java doesn't know that
	}
	
	public static char[][] getMaze(String fileName) throws FileNotFoundException, 
	                                                       NumberFormatException,
	                                                       NoSuchElementException {
		Scanner in = new Scanner(new File(fileName));
		int width = Integer.parseInt(in.nextLine());
		int height = Integer.parseInt(in.nextLine());
		char[][] maze = new char[height][];
		for (int h = 0; h < height; h++) {
			maze[h] = in.nextLine().toCharArray();
			if (maze[h].length != width) {
				throw new NoSuchElementException(BADLINE);
			}
		}
		return maze; 
	}
	
	/** SOLVE CODE BELOW HERE **/
	
	public static boolean solve(char[][] maze, int h, int w) {
		if(rescued(maze, h, w)) {
			return true;
		}
		
		if(!invalid(maze, h+1, w)) {
			if(visible(maze, h+1, w) && !blocked(maze, h+1, w)) {
				pathify(maze, h, w);
				marvisit(maze, h, w);
				//maze[h+1][w] = MARIO;
				return solve(maze, h+1, w);
				//hpo = true;
			}
		}
		if(!invalid(maze, h, w+1)) {
			if(visible(maze, h, w+1) && !blocked(maze, h, w+1)) {
				pathify(maze, h, w);
				marvisit(maze, h, w);
				//maze[h][w+1] = MARIO;
				return solve(maze, h, w+1);
				//wpo = true;
			}
		}
		
		if(!invalid(maze, h, w-1)) {
			if(visible(maze, h, w-1) && !blocked(maze, h, w-1)) {
				pathify(maze, h, w);
				marvisit(maze, h, w);
				//maze[h][w-1] = MARIO;
				return solve(maze, h, w-1);
				//wmo = true;
			}
			
		}
		if(!invalid(maze, h-1, w)) {
			if(visible(maze, h-1, w) && !blocked(maze, h-1, w)) {
				pathify(maze, h, w);
				marvisit(maze, h, w);
				//maze[h-1][w] = MARIO;
				return solve(maze, h-1, w);
				//hmo = true;
			}
		}
		
//		if(hpo) { solve(maze, h+1, w); }
//		if(wpo) { solve(maze, h, w+1); }
//		if(hmo) { solve(maze, h-1, w); }
//		if(wmo) { solve(maze, h, w-1); }
		
		return false;
	}
}
