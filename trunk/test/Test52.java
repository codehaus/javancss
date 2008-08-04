public class Test52
{
  int var = 0;

  public void methodTest52()
  {
    this.var = 2;
  }
    
  public Test52()
  {
    super();

    this.var = 1;
  }

  public Test52(int i)
  {
    this.var = i;
  }

  class Inner
  {
    public Inner()
    {
    Test52.this.super();
    }
  }
}
