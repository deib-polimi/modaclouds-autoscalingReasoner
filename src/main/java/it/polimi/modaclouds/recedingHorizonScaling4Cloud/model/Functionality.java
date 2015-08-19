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
 * <p>Java class for functionality complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="functionality">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="workloadForecast" type="{}workloadForecast" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="demand" type="{http://www.w3.org/2001/XMLSchema}double" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "functionality", propOrder = {
    "workloadForecast"
})
public class Functionality {

    @XmlElement(required = true)
    protected List<WorkloadForecast> workloadForecast;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "demand")
    protected Double demand;

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

}
