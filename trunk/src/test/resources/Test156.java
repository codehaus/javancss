public class HexadecimalFloatingPointLiterals
{
    // test HARMONY-2132
    public void test_toHexStringF()
    {
        // the follow values come from the Double Javadoc/Spec
        assertEquals( "0x1.01p10", Double.toHexString( 0x1.01p10 ) );
    }

}
