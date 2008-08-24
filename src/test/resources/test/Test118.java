public enum TestEnum {
      Test(Type.TEST);
 

    private Type type = null;
    private TestEnum(Type type){
        this.type = type;
    }
    public Type getType() {
        return this.type;
    }
    public enum Type{TEST}
}
