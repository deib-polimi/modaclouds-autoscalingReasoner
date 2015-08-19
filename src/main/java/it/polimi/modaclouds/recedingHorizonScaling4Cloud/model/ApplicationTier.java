//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.19 at 06:33:27 PM CEST 
//


package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for applicationTier complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="applicationTier">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="responseTimeThreshold" type="{}responseTimeThreshold" maxOccurs="24"/>
 *         &lt;element name="functionality" type="{}functionality" maxOccurs="unbounded"/>
 *         &lt;element name="workloadForecast" type="{}workloadForecast" maxOccurs="unbounded"/>
 *         &lt;element name="instances" type="{}instance" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="demand" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="classIndex" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="responseTimeThresholdRuleID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "applicationTier", propOrder = {
    "responseTimeThreshold",
    "functionality",
    "workloadForecast",
    "instances"
})
public class ApplicationTier {

    @XmlElement(required = true)
    protected List<ResponseTimeThreshold> responseTimeThreshold;
    @XmlElement(required = true)
    protected List<Functionality> functionality;
    @XmlElement(required = true)
    protected List<WorkloadForecast> workloadForecast;
    @XmlElement(required = true)
    protected List<Instance> instances;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "demand")
    protected Double demand;
    @XmlAttribute(name = "classIndex")
    protected Integer classIndex;
    @XmlAttribute(name = "responseTimeThresholdRuleID", required = true)
    protected String responseTimeThresholdRuleID;

    /**
     * Gets the value of the responseTimeThreshold property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the responseTimeThreshold property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResponseTimeThreshold().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResponseTimeThreshold }
     * 
     * 
     */
    public List<ResponseTimeThreshold> getResponseTimeThreshold() {
        if (responseTimeThreshold == null) {
            responseTimeThreshold = new ArrayList<ResponseTimeThreshold>();
        }
        return this.responseTimeThreshold;
    }

    /**
     * Gets the value of the functionality property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the functionality property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFunctionality().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Functionality }
     * 
     * 
     */
    public List<Functionality> getFunctionality() {
        if (functionality == null) {
            functionality = new ArrayList<Functionality>();
        }
        return this.functionality;
    }

    /**
     * Gets the value of the workloadForecast property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the workloadForecast property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWorkloadForecast().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WorkloadForecast }
     * 
     * 
     */
    public List<WorkloadForecast> getWorkloadForecast() {
        if (workloadForecast == null) {
            workloadForecast = new ArrayList<WorkloadForecast>();
        }
        return this.workloadForecast;
    }

    /**
     * Gets the value of the instances property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the instances property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstances().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Instance }
     * 
     * 
     */
    public List<Instance> getInstances() {
        if (instances == null) {
            instances = new ArrayList<Instance>();
        }
        return this.instances;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the demand property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDemand() {
        return demand;
    }

    /**
     * Sets the value of the demand property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDemand(Double value) {
        this.demand = value;
    }

    /**
     * Gets the value of the classIndex property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getClassIndex() {
        return classIndex;
    }

    /**
     * Sets the value of the classIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setClassIndex(Integer value) {
        this.classIndex = value;
    }

    /**
     * Gets the value of the responseTimeThresholdRuleID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseTimeThresholdRuleID() {
        return responseTimeThresholdRuleID;
    }

    /**
     * Sets the value of the responseTimeThresholdRuleID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseTimeThresholdRuleID(String value) {
        this.responseTimeThresholdRuleID = value;
    }

}
