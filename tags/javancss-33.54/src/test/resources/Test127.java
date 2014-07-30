/**
 * Useless class with a single genericized constructor to illustrate MagicDraw 11.5 reverse engineering bug.
 */
public class ClassWithGenericizedConstructor {

    //public ClassWithGenericizedConstructor(T genericArg) {
    public <T> ClassWithGenericizedConstructor(T genericArg) {
        super();
  }
}
