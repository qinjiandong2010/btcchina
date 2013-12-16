package test;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService 
public interface IHelloWorld {  
    @WebMethod
    @WebResult String sayHi(@WebParam String text);  
}
