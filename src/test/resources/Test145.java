public class Test {
    /**
     * Takes an Iterable of Iterables (e.g. a list of lists), of T and flattens
     * it down to a Collection of T.
     */
    public static <T, Source extends Iterable<? extends Iterable<T>>, Store extends Collection<T>> Store flatten(Source source, Store store) {
        for (Iterable<T> subSource : source) {
            for (T t : subSource) {
                store.add(t);
            }
        }
        return store;
    }
}