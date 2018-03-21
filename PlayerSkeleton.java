public class PlayerSkeleton {

	private static int[] weights;

	//implement this function to have a working system
	public int pickMove(State s, int[][] legalMoves) {
		int bestMove = 0;
		int maxSoFar = Integer.MIN_VALUE;
		for (int i = 0; i < legalMoves.length; i++)
		{
			NextState ns = new NextState(s.getField(), s.getTop(), s.getNextPiece());
			ns.makeMove(i);
			int currValue = getHeuristic(ns);
			//printGrid(ns);
			//System.out.println(currValue);
			if (currValue > maxSoFar)
			{
				maxSoFar = currValue;
				bestMove = i;
			}
		}
		return bestMove;
	}
	
	public static void main(String[] args) {
		//initialise weights here
		weights = new int[22];
		//weights 0 to 9 are column heights
		//weights 10 to 18 are differences in adjacent column heights
		//weight 19 is maximum column heights
		//weight 20 is number of holes
		//weight 21 is reward for clearing.
		//debug setting initialisations
		for (int i = 0; i < 10; i++)
			weights[i] = -51;
		for (int i = 10; i < 19; i++)
			weights[i] = -18;
		weights[19] = 0;
		weights[20] = -35;
		weights[21] = 76;
		State s = new State();
		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		while(!s.hasLost()) {
			s.makeMove(p.pickMove(s,s.legalMoves()));
			s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
	public static int getHeuristic(NextState ns)
	{
		int heuristic = 0;
		//get col height heuristic
		for (int i = 0; i < 10; i++)
		{
			heuristic += weights[i] * ns.getColumnHeight(i);
		}
		//get adjacent col height diff heuristic
		for (int i = 0; i < 9; i++)
		{
			heuristic += weights[i+10] * ns.getColumnHeightDiff(i);
		}
		//get max col height heuristic
		heuristic += weights[19] * ns.getMaxColumnHeight();
		//get holes heuristic
		heuristic += weights[20] * ns.getHoles();
		//get cleared rows heuristic
		heuristic += weights[21] * ns.getRowsCleared();
		return heuristic;
	}
	
	public static void printGrid(NextState ns)
	{
		int[][] grid = ns.getField();
		for (int i = grid.length-1; i >= 0; i--)
		{
			for (int j = 0; j < grid[i].length; j++)
			{
				System.out.print(grid[i][j] + " " );
			}
			System.out.println();
		}
	}
	
}
