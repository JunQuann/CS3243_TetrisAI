import java.util.*;
import java.io.*;

public class LearningAlgorithm
{
	public static final int POP_SIZE = 50; //total population size
	public static final int NUM_GEN = 20; //number of new pop introduced in each generation
	public static final int MUTATION_RATE = 40; //mutation rate out of MAX_MUTATION_RATE
	public static final int MAX_MUTATION_RATE = 1000; //value for 100% chance of mutation occuring
	public static final int NUM_RUNS = 100; //number of runs to learn each time this algo is run
	public static final int TOURNAMENT_SIZE = 8; //size of tournament for tournament mating algorithm
	public static final boolean newFile = false;
	public ArrayList<Learner> learners;
	public static Random rnggod;
	
	public LearningAlgorithm ()
	{
		rnggod = new Random(System.nanoTime());
		learners = new ArrayList<Learner>();
	}
	
	public void run () throws IOException
	{
		int totalRuns = 0;
		if (!newFile)
		{
			//read data from file
			Scanner sc = new Scanner(new File("weights.txt"));
			totalRuns = sc.nextInt();
			for (int i = 0; i < POP_SIZE; i++)
			{
				int[] readWeights = new int[Learner.NUM_WEIGHTS];
				int fitness = sc.nextInt();
				for (int j = 0; j < Learner.NUM_WEIGHTS; j++)
					readWeights[j] = sc.nextInt();
				learners.add(new Learner(readWeights, fitness));
			}
		}
		else
		{
			//start over with new random population
			for (int i = 0; i < POP_SIZE; i++)
				learners.add(new Learner());
		}
		for (int run = 0; run < NUM_RUNS; run++)
		{
			int totalFitness = 0;
			for (int i = 0; i < POP_SIZE; i++)
			{
				learners.get(i).run();
				totalFitness += learners.get(i).fitness;
			}
			Collections.sort(learners);
			System.out.println(learners.get(0).fitness);
			Learner[] newGeneration = new Learner[NUM_GEN];
			for (int k = 0; k < NUM_GEN; k++)
			{
				//newGeneration[k] = consecutiveMating(k);
				newGeneration[k] = tournamentMating();
			}
			//kill off last NEW_GEN of the old generation, replace with the new generation
			int i = POP_SIZE-1;
			for (int j = 0; j < newGeneration.length; j++) 
			{
				learners.set(i, newGeneration[j]);
				i--;
			}
		}
		//save data to file
		PrintWriter out = new PrintWriter("weights.txt");
		out.println(NUM_RUNS+totalRuns);
		for (int i = 0; i < POP_SIZE; i++)
		{
			out.println(learners.get(i).toString());
		}
		out.close();
	}
	
	public Learner tournamentMating()
	{
		Learner firstParent = tournament();
		Learner secondParent = tournament();
		return reproduce(firstParent, secondParent);
	}
	
	public Learner tournament()
	{
		ArrayList<Integer> tList = new ArrayList<Integer>();
		//randomly select TOURNAMENT_SIZE people
		//since Learners is sorted by fittest, we can just find the smallest integer picked
		for (int i = 0; i < TOURNAMENT_SIZE; i++)
		{
			tList.add(rnggod.nextInt(POP_SIZE));
		}
		Collections.sort(tList);
		return learners.get(tList.get(0)); //return the fittest in the tournament
	}
	
	public Learner consecutiveMating(int k)
	{
		Learner firstParent = learners.get(k*2);
		Learner secondParent = learners.get(k*2+1);
		return reproduce(firstParent, secondParent);
	}
	
	public Learner reproduce(Learner first, Learner second)
	{
		int crossoverPoint = rnggod.nextInt(Learner.NUM_WEIGHTS);
		int[] newW = new int[Learner.NUM_WEIGHTS];
		for (int i = 0; i < crossoverPoint; i++)
			newW[i] = first.weights[i];
		for (int i = crossoverPoint; i < Learner.NUM_WEIGHTS; i++)
			newW[i] = second.weights[i];
		//perform mutation here
		for (int i = 0; i < Learner.NUM_WEIGHTS; i++)
		{
			int mutationChance = rnggod.nextInt(MAX_MUTATION_RATE);
			if (mutationChance < MUTATION_RATE)
			{
				newW[i] += rnggod.nextInt(101)-50; //randomly mutate the value by -50 to 50.
			}
		}
		Learner newPerson = new Learner(newW);
		return newPerson;
	}
	
	public static void main(String[] args)
	{
		LearningAlgorithm la = new LearningAlgorithm();
		try
		{
			la.run();
		}
		catch (IOException ioe)
		{
			System.out.println(ioe.getMessage());
		}
	}
}
