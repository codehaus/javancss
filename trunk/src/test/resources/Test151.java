import java.util.List;

public class FooBar
{

	public static void test(List<int[]> param)
	{
		for (int[] val : param)
			;
	}
	
	public static void test2(List<int[]> param)
	{
		for (int val[] : param)
			;
	}
	
}
