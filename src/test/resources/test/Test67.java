/**
 * Test input for asserts from jdk 1.4.
 */
public class Test67 extends Assert
{
    public void assert( boolean check )
    {
        super.assert( check );

        assert( 1*1==5, "What's that?" );
    }

    public static void main(String[] args)
    {
        Test67 test67 = new Test67();
        test67.assert( 1 == 1 );
        assert( true );
        assert true;
        test67.assert( 1*1==5, "What's that?" );

        assert( args.length == 1 );
        assert 1 == 1;
        assert 1*5 != 1;
        
        if ( expr()*5 != 1 );
        if ( expr() );
        assert( expr() );
        assert expr();
        assert( expr()*5 != 1 );
        if ( ( expr()*5 != 1 ) );
        assert expr;
        assert expr();
        assert expr()*5 != 1;
        assert args.length == 1;
        assert assert( 5 == 7 ) : "That's it!";
        assert false;
        assert c == s.charAt( i );
        assert (n - i >= 2);
        assert n - i >= 2;
        assert cr.isUnderflow();
    assert (scale >= 0);  // && scale <= Integer.MAX_VALUE
    assert (scale == longScale && // bla bla
        Math.abs(longScale) <= Integer.MAX_VALUE)  // bla bla
                                   // bla
        :longScale;
    }
}
