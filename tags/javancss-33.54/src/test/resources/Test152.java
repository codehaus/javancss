package issues;

import java.util.ArrayList;
import java.util.List;

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

    void postCopyOnDestination(String str) throws InstantiationException, IllegalAccessException {
       List<AllowedMMProduct> listToReset = new ArrayList<AllowedMMProduct>();
       
       List<AllowedMMProductAudit> auditProducts;
       auditProducts = this.<AllowedMMProduct,AllowedMMProductAudit>copyListFromParent(AllowedMMProductAudit.class, getMmAuthorisedProducts_());
    }
    
    List<AllowedMMProduct> getMmAuthorisedProducts_() {
       return null;
    }

}
