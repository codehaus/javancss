package oki.doki;


import java.util.ArrayList;
import java.util.Collection;

import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.Modifier;

import static com.sun.mirror.declaration.Modifier.*;

public class Test89 {

    // Predefined filters for convenience.

    /**
     * A filter that selects only <tt>public</tt> declarations.
     */
    public static final DeclarationFilter FILTER_PUBLIC =
        new AccessFilter(PUBLIC);

    /**
     * A filter that selects only <tt>protected</tt> declarations.
     */
    public static final DeclarationFilter FILTER_PROTECTED =
        new AccessFilter(PROTECTED);

    /**
     * A filter that selects only <tt>public</tt> or <tt>protected</tt>
     * declarations.
     */
    public static final DeclarationFilter FILTER_PUBLIC_OR_PROTECTED =
        new AccessFilter(PUBLIC, PROTECTED);

    /**
     * A filter that selects only package-private (<i>default</i>)
     * declarations.
     */
    public static final DeclarationFilter FILTER_PACKAGE =
        new AccessFilter();

    /**
     * A filter that selects only <tt>private</tt> declarations.
     */
    public static final DeclarationFilter FILTER_PRIVATE =
        new AccessFilter(PRIVATE);


    /**
     * Constructs an identity filter:  one that selects all declarations.
     */
    public DeclarationFilter() {
    }



    // Methods to create a filter.

    /**
     * Returns a filter that selects declarations containing all of a
     * collection of modifiers.
     *
     * @param mods  the modifiers to match (non-null)
     * @return a filter that matches declarations containing <tt>mods</tt>
     */
    public static DeclarationFilter getFilter(
                         final Collection<Modifier> mods) {
    return new DeclarationFilter() {
        public boolean matches(Declaration d) {
        return d.getModifiers().containsAll(mods);
        }
    };
    }

    /**
     * Returns a filter that selects declarations of a particular kind.
     * For example, there may be a filter that selects only class
     * declarations, or only fields.
     * The filter will select declarations of the specified kind,
     * and also any subtypes of that kind; for example, a field filter
     * will also select enum constants.
     *
     * @param kind  the kind of declarations to select
     * @return a filter that selects declarations of a particular kind
     */
    public static DeclarationFilter getFilter(
                     final Class<? extends Declaration> kind) {
    return new DeclarationFilter() {
        public boolean matches(Declaration d) {
        return kind.isInstance(d);
        }
    };
    }

    /**
     * Returns a filter that selects those declarations selected
     * by both this filter and another.
     *
     * @param f  filter to be composed with this one
     * @return a filter that selects those declarations selected by
     *        both this filter and another
     */
    public DeclarationFilter and(DeclarationFilter f) {
    final DeclarationFilter f1 = this;
    final DeclarationFilter f2 = f;
    return new DeclarationFilter() {
        public boolean matches(Declaration d) {
        return f1.matches(d) && f2.matches(d);
        }
    };
    }

    /**
     * Returns a filter that selects those declarations selected
     * by either this filter or another.
     *
     * @param f  filter to be composed with this one
     * @return a filter that selects those declarations selected by
     *        either this filter or another
     */
    public DeclarationFilter or(DeclarationFilter f) {
    final DeclarationFilter f1 = this;
    final DeclarationFilter f2 = f;
    return new DeclarationFilter() {
        public boolean matches(Declaration d) {
        return f1.matches(d) || f2.matches(d);
        }
    };
    }

    /**
     * Returns a filter that selects those declarations not selected
     * by this filter.
     *
     * @return a filter that selects those declarations not selected
     * by this filter
     */
    public DeclarationFilter not() {
    return new DeclarationFilter() {
        public boolean matches(Declaration d) {
        return !DeclarationFilter.this.matches(d);
        }
    };
    }



    // Methods to apply a filter.

    /**
     * Tests whether this filter matches a given declaration.
     * The default implementation always returns <tt>true</tt>;
     * subclasses should override this.
     *
     * @param decl  the declaration to match
     * @return <tt>true</tt> if this filter matches the given declaration
     */
    public boolean matches(Declaration decl) {
    return true;
    }

    /**
     * Returns the declarations matched by this filter.
     * The result is a collection of the same type as the argument;
     * the {@linkplain #filter(Collection, Class) two-parameter version}
     * of <tt>filter</tt> offers control over the result type.
     *
     * @param <D>    type of the declarations being filtered
     * @param decls  declarations being filtered
     * @return the declarations matched by this filter
     */
    public <D extends Declaration> Collection<D> filter(Collection<D> decls) {
    ArrayList<D> res = new ArrayList<D>(decls.size());
    for (D d : decls) {
        if (matches(d)) {
        res.add(d);
        }
    }
    return res;
    }

    /**
     * Returns the declarations matched by this filter, with the result
     * being restricted to declarations of a given kind.
     * Similar to the simpler
     * {@linkplain #filter(Collection) single-parameter version}
     * of <tt>filter</tt>, but the result type is specified explicitly.
     *
     * @param <D>      type of the declarations being returned
     * @param decls    declarations being filtered
     * @param resType  type of the declarations being returned --
     *            the reflective view of <tt>D</tt>
     * @return the declarations matched by this filter, restricted to those
     *            of the specified type
     */
    public <D extends Declaration> Collection<D>
        filter(Collection<? extends Declaration> decls, Class<D> resType) {
    ArrayList<D> res = new ArrayList<D>(decls.size());
    for (Declaration d : decls) {
        if (resType.isInstance(d) && matches(d)) {
        res.add(resType.cast(d));
        }
    }
    return res;
    }



    /*
     * A filter based on access modifiers.
     */
    private static class AccessFilter extends DeclarationFilter {

    // The first access modifier to filter on, or null if we're looking
    // for declarations with no access modifiers.
    private Modifier mod1 = null;

    // The second access modifier to filter on, or null if none.
    private Modifier mod2 = null;

    // Returns a filter that matches declarations with no access
    // modifiers.
    AccessFilter() {
    }

    // Returns a filter that matches m.
    AccessFilter(Modifier m) {
        mod1 = m;
    }

    // Returns a filter that matches either m1 or m2.
    AccessFilter(Modifier m1, Modifier m2) {
        mod1 = m1;
        mod2 = m2;
    }

    public boolean matches(Declaration d) {
        Collection<Modifier> mods = d.getModifiers();
        if (mod1 == null) {    // looking for package private
        return !(mods.contains(PUBLIC) ||
             mods.contains(PROTECTED) ||
             mods.contains(PRIVATE));
        }
        return mods.contains(mod1) &&
           (mod2 == null || mods.contains(mod2));
    }
    }
}
