import java.util.*;

public class PieceGenerator
{
	public static void main (String[] args)
	{
		for (int i = 0; i < 100000; i++)
		{
			System.out.println((int)(Math.random()*State.N_PIECES));
		}
	}
}