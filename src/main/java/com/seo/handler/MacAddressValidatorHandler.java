/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seo.handler;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

/**
 *
 * @author srampv
 */
public class MacAddressValidatorHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public Set<QName> getHeaders() {
        System.out.println("<----getHeaders------>");
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        try {
            System.out.println("<----HANDLE_MESSAGE------>");
            SOAPMessage message = context.getMessage();
            SOAPBody body = message.getSOAPBody();

            SOAPPart part = message.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPHeader header =message.getSOAPHeader(); //envelope.getHeader();
            Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            System.out.println("IS_REQUEST:---->" + isRequest);
            try {
                    message.writeTo(System.out);
                } catch (IOException ex) {
                    Logger.getLogger(MacAddressValidatorHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            if (!isRequest) {
                if (header == null) {
                    header = envelope.addHeader();
                    generateSOAPErrorMessage(message, "No SOAP Header");
                }
                Iterator it=header.extractAllHeaderElements();
                System.out.println("<---------IT IS HERE---------->");
//                while(it.hasNext()){
//                    Node n=(Node)it.next();
//                    System.out.println("Node:--->"+n.getNodeName()+":"+n.getNodeValue()+":"+n.getValue());
//                }
//               Iterator it= header.extractHeaderElements(SOAPConstants.URI_SOAP_ACTOR_NEXT);
               if(it==null || !it.hasNext()){
                   generateSOAPErrorMessage(message, "VENKATA No header block for soap actor next");
               }
               Node macNode=(Node) it.next();
               String macValue= (macNode==null?null:macNode.getValue());
               if(macValue==null){
                   generateSOAPErrorMessage(message, "Mac value is null");
               }
               if(!macValue.equalsIgnoreCase("B8-8D-12-28-C0-B6")){
                   generateSOAPErrorMessage(message, "MAC value is not matching 90-4C-E5-44-B9-8F access denied");
               }
                
            }
            return true;
        } catch (SOAPException ex) {
            Logger.getLogger(MacAddressValidatorHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        System.out.println("<----HANDLE_FAULT------>");
        return true;
    }

    @Override
    public void close(MessageContext context) {
        System.out.println("<----CLOSE------>");
    }

    private void generateSOAPErrorMessage(SOAPMessage msg, String reason) {

        try {
            SOAPBody body = msg.getSOAPPart().getEnvelope().getBody();
            SOAPFault fault = body.addFault();
            fault.setFaultString(reason);
            throw new SOAPFaultException(fault);
        } catch (SOAPException ex) {
            Logger.getLogger(MacAddressValidatorHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
