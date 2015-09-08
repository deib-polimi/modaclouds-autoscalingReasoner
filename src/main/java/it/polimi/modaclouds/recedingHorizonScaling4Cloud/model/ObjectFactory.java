//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.11 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2015.09.08 alle 05:54:57 PM CEST 
//


package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.polimi.modaclouds.recedingHorizonScaling4Cloud.model package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.polimi.modaclouds.recedingHorizonScaling4Cloud.model
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
     * Create an instance of {@link ApplicationTier }
     * 
     */
    public ApplicationTier createApplicationTier() {
        return new ApplicationTier();
    }

    /**
     * Create an instance of {@link Functionality }
     * 
     */
    public Functionality createFunctionality() {
        return new Functionality();
    }

    /**
     * Create an instance of {@link ResponseTimeThreshold }
     * 
     */
    public ResponseTimeThreshold createResponseTimeThreshold() {
        return new ResponseTimeThreshold();
    }

    /**
     * Create an instance of {@link WorkloadForecast }
     * 
     */
    public WorkloadForecast createWorkloadForecast() {
        return new WorkloadForecast();
    }

    /**
     * Create an instance of {@link Instance }
     * 
     */
    public Instance createInstance() {
        return new Instance();
    }

}
