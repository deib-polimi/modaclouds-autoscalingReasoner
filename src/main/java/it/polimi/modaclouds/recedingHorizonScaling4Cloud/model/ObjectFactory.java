//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.16 at 10:42:12 AM CET 
//


package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Containers }
     * 
     */
    public Containers createContainers() {
        return new Containers();
    }

    /**
     * Create an instance of {@link Container }
     * 
     */
    public Container createContainer() {
        return new Container();
    }

    /**
     * Create an instance of {@link Functionality }
     * 
     */
    public Functionality createFunctionality() {
        return new Functionality();
    }

    /**
     * Create an instance of {@link WorkloadForecast }
     * 
     */
    public WorkloadForecast createWorkloadForecast() {
        return new WorkloadForecast();
    }

    /**
     * Create an instance of {@link RunningInstance }
     * 
     */
    public RunningInstance createRunningInstance() {
        return new RunningInstance();
    }

    /**
     * Create an instance of {@link ResponseTimeThreshold }
     * 
     */
    public ResponseTimeThreshold createResponseTimeThreshold() {
        return new ResponseTimeThreshold();
    }

    /**
     * Create an instance of {@link ApplicationTier }
     * 
     */
    public ApplicationTier createApplicationTier() {
        return new ApplicationTier();
    }

}
