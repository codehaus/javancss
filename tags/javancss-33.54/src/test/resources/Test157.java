public class Java7Literals
{
    // An 8-bit 'byte' value:
    byte aByte = (byte)0b00100001;

    // A 16-bit 'short' value:
    short aShort = (short)0b1010000101000101;

    // Some 32-bit 'int' values:
    int anInt1 = 0b10100001010001011010000101000101;
    int anInt2 = 0b101;
    int anInt3 = 0B101; // The B can be upper or lower case.
    
    long creditCardNumber = 1234_5678_9012_3456L;
    long socialSecurityNumber = 999_99_9999L;
    loat pi =  3.14_15F;
    long hexBytes = 0xFF_EC_DE_5E;
    long hexWords = 0xCAFE_BABE;
    long maxLong = 0x7fff_ffff_ffff_ffffL;
    byte nybbles = 0b0010_0101;
    long bytes = 0b11010010_01101001_10010100_10010010;

    int x2 = 5_2;              // OK (decimal literal)
    int x4 = 5_______2;        // OK (decimal literal)
    int x7 = 0x5_2;            // OK (hexadecimal literal)
    int x9 = 0_52;             // OK (octal literal)
    int x10 = 05_2;            // OK (octal literal)
    
}
