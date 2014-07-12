package project.web.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping( "/acontroller" )
public interface IController
{

    @RequestMapping( "/" )
    public abstract ModelAndView forwardToHomePage();

    /**
     * This method should do something.
     * @param applicationId
     * @param accountName
     * @return a {@link JqGridJSonPage} object populated.
     */
    @RequestMapping( value = "/getaccount", method = RequestMethod.GET )
    public @ResponseBody abstract JqGridJSonPage getAccount( 
                   @RequestParam( value = "applicationId", required = false )
                   Integer applicationId, 
                   @RequestParam( value = "accountName", required = false )
                   String accountName 
       );

}
