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
	
   public static void test3(List<Object[]> param)
   {
      for (Object[] objeto : lista) {
         // nothing
      }
   }
   
   public static void test4(List<Object[]> param)
   {
      for (Object objeto[] : lista) {
         // nothing
      }
   }

}
