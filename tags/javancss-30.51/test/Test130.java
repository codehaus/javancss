import java.lang.reflect.ParameterizedType;

public class Bug<T> {

    private Class<T> clazz;

    /**
     * Demonstrate the javancss bug. This code is from
     * an article on the hibernate website:
     * http://www.hibernate.org/328.html
     */
    public Bug () {
        this.clazz= (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
