package mypackage.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebResult;

import mypackage.persistence.model.Location;
import mypackage.persistence.model.LocationKey;

@WebService
public interface Test126 {

    @WebResult(name = "helloWorld")
    public String world();

    @WebResult(name = "location")
    public Location getLocation(@WebParam(name = "locationKey") LocationKey key);

    public void insertLocation(@WebParam(name = "location") Location location);

    public void updateLocation(@WebParam(name = "location") Location location);

    public void deleteLocation(@WebParam(name = "location") Location location);

}
