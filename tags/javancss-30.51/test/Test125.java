public interface Test125
{
    //public ManagedFile save( ManagedFile f);
    public ManagedFile save(@Secured(UpdateAction.class) ManagedFile f);
}
