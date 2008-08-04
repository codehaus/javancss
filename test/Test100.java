public abstract class Provider extends Properties {
    
    private Service(Provider provider) {
        this.provider = provider;
        aliases = Collections.<String>emptyList();
        attributes = Collections.<UString,String>emptyMap();
    }
}

