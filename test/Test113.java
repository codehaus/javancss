public enum Test113
{
    ONE(1);

    private final int myKey;
    
    Test113(int key) {
        myKey = key;
    }

    public String bug0(Object bar) {
        return (String) bar;
    }

    
    public String bug1(String bar) {
        return ((String) bar).toString();
    }
        
    
    public String bug2(Object bar) {
        if (bar instanceof Integer) {
            Integer quux = (Integer) bar;
            return quux.toString();
        } else {
            return "Not a number.";
        }
    }
}
