import java.util.*;

class Learner implements Comparable<Learner>, Runnable
{
	public static int NUM_WEIGHTS = 22;
	public static double MIN_WEIGHT = -200;
	public static double MAX_WEIGHT = 0;
	public static double MAX_REWARD_WEIGHT = 400;
	public static int NUM_TETRIS = 20;
	public double[] weights;
	public int fitness = -1;
	
	public Learner (double[] inWeights, int fitness)
	{
		weights = new double[NUM_WEIGHTS];
		for (int i = 0; i < NUM_WEIGHTS; i++)
			weights[i] = inWeights[i];
		this.fitness = fitness;
	}
	
	public Learner (double[] inWeights)
	{
		weights = new double[NUM_WEIGHTS];
		for (int i = 0; i < NUM_WEIGHTS; i++)
			weights[i] = inWeights[i];
	}
	
	public Learner ()
	{
		weights = new double[NUM_WEIGHTS];
		Random rnggod = new Random(System.nanoTime());
		//negative weights for evaluation function , range -200 to 0
		for (int i = 0; i < NUM_WEIGHTS-1; i++)
		{
			weights[i] = rnggod.nextDouble()*(MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT;
		}
		//set large positive weight for reward value
		weights[NUM_WEIGHTS-1] = rnggod.nextDouble()*MAX_REWARD_WEIGHT;
	}
	
	public void run()
	{
		if (fitness < 0)
		{
			fitness = 0;
			for (int i = 0; i < NUM_TETRIS; i++)
			{
				PlayerSkeleton ps = new PlayerSkeleton();
				ps.setWeights(weights);
				fitness += ps.run();
			}
			fitness = fitness/NUM_TETRIS;
		}
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