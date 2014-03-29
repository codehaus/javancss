public class Test134
{
    public static void main( String[] args )
    {
        @SuppressWarnings("unused")
        String s;
        @SuppressWarnings("unchecked")
        List<IPortfolio> portfolioList =
            (List<IPortfolio>)getPropertyValue(PORTFOLIO_LIST);
    }
}
