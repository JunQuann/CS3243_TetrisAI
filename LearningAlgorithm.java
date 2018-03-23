import java.util.*;
import java.io.*;

public class LearningAlgorithm
{
	public static final int POP_SIZE = 150;
	public static final int NUM_GEN = 40; //number of new pop introduced in each generation
	public static final int MUTATION_RATE = 100; //mutation rate out of MAX_MUTATION_RATE
	public static final int MAX_MUTATION_RATE = 1000; //value for 100% chance of mutation occuring
	public static final int NUM_RUNS = 20; //number of runs to learn each time this algo is run
	public static final int TOURNAMENT_SIZE = 20; //size of tournament for tournament mating algorithm
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
				double[] readWeights = new double[Learner.NUM_WEIGHTS];
				int fitness = sc.nextInt();
				for (int j = 0; j < Learner.NUM_WEIGHTS; j++)
					readWeights[j] = sc.nextDouble();
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
			Thread[] threads = new Thread[POP_SIZE];
			for (int i = 0; i < POP_SIZE; i++)
			{
				threads[i] = new Thread(learners.get(i));
				threads[i].start();
			}
			for (int i = 0; i < POP_SIZE; i++)
			{
				try
				{
					threads[i].join();
				}
				catch (InterruptedException ie)
				{
					System.out.println("Exception when joining thread. " + ie.getMessage());
				}
			}
			/*for (int i = 0; i < POP_SIZE; i++)
			{
				learners.get(i).run();
				totalFitness += learners.get(i).fitness;
			}*/
			Collections.sort(learners);
			System.out.println(learners.get(0).fitness);
			Learner[] newGeneration = new Learner[NUM_GEN];
			for (int k = 0; k < NUM_GEN; k++)
			{
				newGeneration[k] = tournamentMating();
			}
			//kill off last NUM_GEN of the old generation, replace with the new generation
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
		ArrayList<Integer> fittestTwo = tournament();
		return weightedReproduce(learners.get(fittestTwo.get(0)), learners.get(fittestTwo.get(1)));
	}
	
	//returns 2 integers - the position of the 2 winners of the tournament
	public ArrayList<Integer> tournament()
	{
		ArrayList<Integer> tList = new ArrayList<Integer>();
		//randomly select TOURNAMENT_SIZE people
		for (int i = 0; i < TOURNAMENT_SIZE; i++)
		{
			tList.add(rnggod.nextInt(POP_SIZE));
		}
		Collections.sort(tList);
		//since Learners is sorted by fittest, the 2 smallest integers picked are the 2 fittest
		ArrayList<Integer> result = new ArrayList<Integer>();
		result.add(tList.get(0));
		result.add(tList.get(1));
		return result;
	}
	
	public Learner consecutiveMating(int k)
	{
		Learner firstParent = learners.get(k*2);
		Learner secondParent = learners.get(k*2+1);
		return weightedReproduce(firstParent, secondParent);
	}
	
	public Learner weightedReproduce(Learner first, Learner second)
	{
		double[] newW = new double[Learner.NUM_WEIGHTS];
		//add 1 to adjust for edge case of fitness = 0
		int weightFirst = first.fitness+1;
		int weightSecond = second.fitness+1;
		for (int i = 0; i < Learner.NUM_WEIGHTS; i++)
		{
			double currW = first.weights[i] * weightFirst + second.weights[i] * weightSecond;
			currW = currW/(double)(weightFirst + weightSecond);
			newW[i] = currW;
		}
		mutate(newW);
		return new Learner(newW);
	}
	
	public Learner reproduce(Learner first, Learner second)
	{
		int crossoverPoint = rnggod.nextInt(Learner.NUM_WEIGHTS);
		double[] newW = new double[Learner.NUM_WEIGHTS];
		for (int i = 0; i < crossoverPoint; i++)
			newW[i] = first.weights[i];
		for (int i = crossoverPoint; i < Learner.NUM_WEIGHTS; i++)
			newW[i] = second.weights[i];
		//perform mutation here
		mutate(newW);
		return new Learner(newW);
	}
	
	public void mutate(double[] weights)
	{
		for (int i = 0; i < Learner.NUM_WEIGHTS; i++)
		{
			int mutationChance = rnggod.nextInt(MAX_MUTATION_RATE);
			if (mutationChance < MUTATION_RATE)
			{
				weights[i] += rnggod.nextDouble()*100-50; //randomly mutate the value by [-50, 50)
			}
		}
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
