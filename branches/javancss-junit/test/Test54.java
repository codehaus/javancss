public class Test54 extends BondModel
{
    public Test54(Fd2Record fd2Record) throws ValidationException {
    
    super((String)null, (java.sql.Timestamp)null);

    try {
        setSecId(fd2Record.sec_id);
        setCurrentMaturity(Conversion.convertTimestampToDate(Conversion.convertIFFDateToTimestamp(fd2Record.maturity)));
        setCurrentCoupon(Conversion.convertIFFCouponToDouble(fd2Record.coupon, fd2Record.coupon_fi));
        setCurrentTicker(fd2Record.ticker);
    } catch (Conversion.ConversionException ce) {
        throw new ValidationException(ce.toString());
    }
    }
}
