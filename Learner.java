import java.util.*;

class Learner implements Comparable<Learner>, Runnable
{
	public static int NUM_WEIGHTS = 22;
	public static double MIN_WEIGHT = -200;
	public static double MAX_WEIGHT = 100;
	public static double MAX_REWARD_WEIGHT = 500;
	public double[] weights;
	public int fitness = -1;
	public PlayerSkeleton ps;
	
	public Learner (double[] inWeights, int fitness)
	{
		weights = new double[NUM_WEIGHTS];
		for (int i = 0; i < NUM_WEIGHTS; i++)
			weights[i] = inWeights[i];
		ps = new PlayerSkeleton();
		this.fitness = fitness;
		ps.setWeights(weights);
	}
	
	public Learner (double[] inWeights)
	{
		weights = new double[NUM_WEIGHTS];
		for (int i = 0; i < NUM_WEIGHTS; i++)
			weights[i] = inWeights[i];
		ps = new PlayerSkeleton();
		ps.setWeights(weights);
	}
	
	public Learner ()
	{
		weights = new double[NUM_WEIGHTS];
		Random rnggod = new Random(System.nanoTime());
		//negative weights for evaluation function , range -200 to 100
		for (int i = 0; i < NUM_WEIGHTS-1; i++)
		{
			weights[i] = rnggod.nextDouble()*(MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT;
		}
		//set large positive weight for reward value
		weights[NUM_WEIGHTS-1] = rnggod.nextDouble()*MAX_REWARD_WEIGHT;
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