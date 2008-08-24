public interface Test111<T> {
    public boolean isValid1(Bar.Info info);
    public boolean isValid2(Bar<T> info);
    public boolean isValid(Bar<T>.Info info);
}
