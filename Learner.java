import java.util.*;

class Learner implements Comparable<Learner>
{
	public static final int NUM_WEIGHTS = 22;
	public static final int MIN_WEIGHT = -200;
	public static final int MAX_WEIGHT = 100;
	public int[] weights;
	public int fitness = -1;
	public PlayerSkeleton ps;
	
	public Learner (int[] inWeights, int fitness)
	{
		weights = new int[NUM_WEIGHTS];
		for (int i = 0; i < NUM_WEIGHTS; i++)
			weights[i] = inWeights[i];
		ps = new PlayerSkeleton();
		this.fitness = fitness;
		ps.setWeights(weights);
	}
	
	public Learner (int[] inWeights)
	{
		weights = new int[NUM_WEIGHTS];
		for (int i = 0; i < NUM_WEIGHTS; i++)
			weights[i] = inWeights[i];
		ps = new PlayerSkeleton();
		ps.setWeights(weights);
	}
	
	public Learner ()
	{
		weights = new int[NUM_WEIGHTS];
		Random rnggod = new Random(System.nanoTime());
		//negative weights for evaluation function , range -200 to 100
		for (int i = 0; i < NUM_WEIGHTS-1; i++)
		{
			weights[i] = rnggod.nextInt(MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT;
		}
		//set large positive weight for reward value
		weights[NUM_WEIGHTS-1] = rnggod.nextInt(500);
		ps = new PlayerSkeleton();
		ps.setWeights(weights);
	}
	
	public void run()
	{
		if (fitness < 0)
			fitness = ps.run();
	}
	
	public static void main (String[] args)
	{
	}
	
	//compareTo is slightly different from usual
	//this definition is so that collections.sort sorts by descending order
	public int compareTo(Learner other)
	{
		return other.fitness - this.fitness;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(fitness);
		for (int i = 0; i < NUM_WEIGHTS; i++)
		{
			sb.append(" ");
			sb.append(weights[i]);
		}
		return sb.toString();
	}
}