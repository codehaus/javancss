
 package mypackage;
 
 public class FooMain {
 public class Outer {
   private Inner inner;
   public Outer() {
     inner = this.new Inner() {
       @Override
       public int getI() {
         return 15;
       }
     };
   }
   private class Inner {
     public int getI() {
       return 1;
     }
   }
 }
 }