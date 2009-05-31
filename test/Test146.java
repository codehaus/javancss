public class Class1 {
  public <T> T foo() {
    return null;
  }
}

class Class2 extends Class1 {
  public <T> T bar() {
    return this.<T>foo();
  }
  public <T> T foo() {
    return super.<T>foo();
  }
}
