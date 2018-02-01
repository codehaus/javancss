import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;

class MyList<E> extends ArrayList<E> {

    public MyList replaceAll2(UnaryOperator<E> operator) {
        return this;
    }
}

class T {

    public void tvarMember() {
    }
}

class Foo {

    public void bar() {
    }

    static class Bar {
    }

}

class R<A> {

    R() {
    }

    R(Integer a) {
    }

    R(String a) {
    }
}

public class LamdasAndMethodRefs {

    public LamdasAndMethodRefs() {
        //Method references
        Runnable a = super::toString;
        Runnable b = LamdasAndMethodRefs.super::toString;
    }

    public static void main(String[] args) {
        //Method references
        LongSupplier a = System::currentTimeMillis; // static method
        ToIntFunction<String> b = String::length;             // instance method
        ToIntFunction<List> c = List::size;
        ToIntFunction<List<String>> d = List<String>::size;  // explicit type arguments for generic type
        UnaryOperator<int[]> e = int[]::clone;
        Consumer<T> f = T::tvarMember;

        Runnable g = System.out::println;
        Consumer<Integer> h = String::valueOf; // overload resolution needed
        IntSupplier i = "abc"::length;
        Consumer<int[]> j = Arrays::sort;          // type arguments inferred from context
        Consumer<String[]> k = Arrays::<String>sort;          // explicit type arguments
        Supplier<ArrayList<String>> l = ArrayList<String>::new;     // constructor for parameterized type
        Supplier<ArrayList> m = ArrayList::new;             // inferred type arguments
        IntFunction<int[]> n = int[]::new;                 // array creation
        Supplier<Foo> o = Foo::<Integer>new; // explicit type arguments
        Supplier<Foo.Bar> p = Foo.Bar::new;           // inner class constructor
        Supplier<R<String>> q = R<String>::<Integer>new;  // generic class, generic constructor

        Foo[] foo = new Foo[2];
        int r = 1;
        foo[r] = new Foo();
        Runnable s = foo[r]::bar;
        boolean test = false;
        MyList<String> list = new MyList<>();
        Supplier<Iterator<String>> fun = (test ? list.replaceAll2(String::trim) : list)::iterator;

        // Lamdas
        Runnable t = () -> {
        }; // No parameters; result is void
        IntSupplier u = () -> 42; // No parameters, expression body
        Supplier<Object> v = () -> null; // No parameters, expression body
        v = () -> {
            return 42;
        }; // No parameters, block body with return
        t = () -> {
            System.gc();
        }; // No parameters, void block body
        v = () -> {                 // Complex block body with returns
            if (true) {
                return 12;
            }
            else {
                int result = 15;
                for (int i2 = 1; i2 < 10; i2++) {
                    result *= i2;
                }
                return result;
            }
        };
        IntFunction<Integer> w = (int x) -> x + 1; // Single declared-type parameter
        w = (int x) -> {
            return x + 1;
        }; // Single declared-type parameter
        w = (x) -> x + 1; // Single inferred-type parameter
        w = x -> x + 1; // Parentheses optional for
                // single inferred-type parameter
        Function<String, Integer> z = (String s2) -> s2.length(); // Single declared-type parameter
        Consumer<Thread> a2 = (Thread t2) -> {
            t2.start();
        }; // Single declared-type parameter
        z = s3 -> s3.length(); // Single inferred-type parameter
        a2 = t3 -> {
            t3.start();
        }; // Single inferred-type parameter
        IntBinaryOperator b2 = (int x, int y) -> x + y; // Multiple declared-type parameters
        b2 = (x, y) -> x + y; // Multiple inferred-type parameters

        List<String> myList
                = Arrays.asList("a1", "a2", "b1", "c2", "c1");

        myList.stream().filter((s4) -> {
            System.out.println("filter " + s4);
            return s4.startsWith("c");
        }).map(String::toUpperCase).sorted().forEach(
                 System.out::println);

    }

}