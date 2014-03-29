import java.util.*;

public class Test48 {
    public Test48() {
        boolean a     = true;
        boolean b     = (true);
        boolean test  = Object[].class.isAssignableFrom(this.getClass());
        boolean test2 = Object[][].class.isAssignableFrom(this.getClass());
        boolean fuck  = (!Object[].class.isAssignableFrom(this.getClass()));
        boolean fuck2 = (Object[].class.isAssignableFrom(this.getClass()));
    }

    public void foo() {
    Object o = new Test48();
    if (!((o instanceof Collection) ||
          //(Object[].class.isAssignableFrom(o.getClass())))) {
              Object[].class.isAssignableFrom(this.getClass()))) {
        System.out.println("true");
    } else {
        System.out.println("false");
    }
    }
}
