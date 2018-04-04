import java.util.*;
import java.io.*;

public class HillClimbing
{
	public static final double START_CLIMB = 4.0;
	public static final double epsilon = 0.01;
	
	public void run (int weightToTrain) throws FileNotFoundException
	{
		Scanner sc = new Scanner(new File("hillclimbing.txt"));
		double[] weights = new double[Learner.NUM_WEIGHTS];
		for (int i = 0; i < Learner.NUM_WEIGHTS; i++)
			weights[i] = sc.nextDouble();
		int runs = 0;
		double movement = START_CLIMB;
		while (movement > epsilon)
		{
			runs++;
			System.out.println(runs + " " + movement);
			//hill climbing starts here
			//move the weights and set as new array
			double[] increaseWeight = Arrays.copyOf(weights, weights.length);
			double[] decreaseWeight = Arrays.copyOf(weights, weights.length);
			increaseWeight[weightToTrain] += movement;
			decreaseWeight[weightToTrain] -= movement;
			//get fitness for current weights
			Learner current = new Learner(weights);
			Learner increased = new Learner(increaseWeight);
			Learner decreased = new Learner(decreaseWeight);
			int fitness = getFitness(current);
			int fitnessIncreased = getFitness(increased);
			int fitnessDecreased = getFitness(decreased);
			System.out.println(fitness + " " + fitnessIncreased + " " + fitnessDecreased);	
			//if original fitness is better, then don't move weight
			if (fitness > fitnessIncreased && fitness > fitnessDecreased) 
			{
				movement = movement/2;
			}
			//if increasing weight improves fitness, and decreasing reduces fitness
			else if (fitness <= fitnessIncreased && fitness > fitnessDecreased) 
			{
				weights = increaseWeight;
			}
			//if decreasing weight improves fitness, and increasing reduces fitness
			else if (fitness > fitnessIncreased && fitness <= fitnessDecreased)
			{
				weights = decreaseWeight;
			}
			//if both going up and down are better, go in direction of steepest ascent
			else if (fitness <= fitnessIncreased && fitness <= fitnessDecreased)
			{
				if (fitnessIncreased >= fitnessDecreased)
					weights = increaseWeight;
				else
					weights = decreaseWeight;
			}
			saveToFile(weights);
		}
	}
	
	public void saveToFile(double[] weights) throws FileNotFoundException
	{
		PrintWriter out = new PrintWriter("hillclimbing.txt");
		for (int j = 0; j < weights.length; j++)
		{
			out.println(weights[j]);
		}
		out.close();
	}
	
	public static void main (String[] args)
	{
		int weightToTrain = Integer.parseInt(args[0]);
		try
		{
			HillClimbing hc = new HillClimbing();
			hc.run(weightToTrain);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public int getFitness(Learner current)
	{
		current.run();
		return current.fitness;
	}
	
	public void printArray(double[] arr)
	{
		for (int i = 0; i < arr.length; i++)
		{
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}
}