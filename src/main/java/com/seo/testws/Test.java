/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seo.testws;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author srampv
 */
@WebService(name = "TestWS", serviceName = "TestWebService", targetNamespace = "urn:com.seo")
@HandlerChain(file = "handler-chain.xml")
public class Test {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "test")
    public String test(@WebParam(name = "test") String test) {
        
        return "welcome";
    }
    
}
