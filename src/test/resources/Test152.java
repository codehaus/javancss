package issues;

public class GenericsInMethodInvocations 
{
    public static void main( String[] args )
    {
        App baz = new App();
        baz.foo();
        
        System.out.println("worked well");
    }
    
    public void foo()
    {
    	this.<Boolean> bar();
    }
    
    private <Boolean> void bar() {}
}
