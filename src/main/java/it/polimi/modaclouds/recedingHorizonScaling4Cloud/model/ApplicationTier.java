//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.11 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2015.09.08 alle 05:54:57 PM CEST 
//


package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBMergeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.MergeFrom;
import org.jvnet.jaxb2_commons.lang.MergeStrategy;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Classe Java per applicationTier complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="applicationTier"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="responseTimeThreshold" type="{}responseTimeThreshold" maxOccurs="24"/&gt;
 *         &lt;element name="functionality" type="{}functionality" maxOccurs="unbounded"/&gt;
 *         &lt;element name="workloadForecast" type="{}workloadForecast" maxOccurs="unbounded"/&gt;
 *         &lt;element name="instances" type="{}instance" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="demand" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="delay" type="{http://www.w3.org/2001/XMLSchema}double" default="0" /&gt;
 *       &lt;attribute name="classIndex" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="responseTimeThresholdRuleID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
public class ApplicationTier
    implements Cloneable, CopyTo, Equals, HashCode, MergeFrom, ToString
{

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
    @XmlAttribute(name = "delay")
    protected Double delay;
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
     * Recupera il valore della proprietà id.
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
     * Imposta il valore della proprietà id.
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
     * Recupera il valore della proprietà demand.
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
     * Imposta il valore della proprietà demand.
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
     * Recupera il valore della proprietà delay.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public double getDelay() {
        if (delay == null) {
            return  0.0D;
        } else {
            return delay;
        }
    }

    /**
     * Imposta il valore della proprietà delay.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDelay(Double value) {
        this.delay = value;
    }

    /**
     * Recupera il valore della proprietà classIndex.
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
     * Imposta il valore della proprietà classIndex.
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
     * Recupera il valore della proprietà responseTimeThresholdRuleID.
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
     * Imposta il valore della proprietà responseTimeThresholdRuleID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseTimeThresholdRuleID(String value) {
        this.responseTimeThresholdRuleID = value;
    }

    public String toString() {
        final ToStringStrategy strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        {
            List<ResponseTimeThreshold> theResponseTimeThreshold;
            theResponseTimeThreshold = (((this.responseTimeThreshold!= null)&&(!this.responseTimeThreshold.isEmpty()))?this.getResponseTimeThreshold():null);
            strategy.appendField(locator, this, "responseTimeThreshold", buffer, theResponseTimeThreshold);
        }
        {
            List<Functionality> theFunctionality;
            theFunctionality = (((this.functionality!= null)&&(!this.functionality.isEmpty()))?this.getFunctionality():null);
            strategy.appendField(locator, this, "functionality", buffer, theFunctionality);
        }
        {
            List<WorkloadForecast> theWorkloadForecast;
            theWorkloadForecast = (((this.workloadForecast!= null)&&(!this.workloadForecast.isEmpty()))?this.getWorkloadForecast():null);
            strategy.appendField(locator, this, "workloadForecast", buffer, theWorkloadForecast);
        }
        {
            List<Instance> theInstances;
            theInstances = (((this.instances!= null)&&(!this.instances.isEmpty()))?this.getInstances():null);
            strategy.appendField(locator, this, "instances", buffer, theInstances);
        }
        {
            String theId;
            theId = this.getId();
            strategy.appendField(locator, this, "id", buffer, theId);
        }
        {
            Double theDemand;
            theDemand = this.getDemand();
            strategy.appendField(locator, this, "demand", buffer, theDemand);
        }
        {
            double theDelay;
            theDelay = ((this.delay!= null)?this.getDelay(): 0.0D);
            strategy.appendField(locator, this, "delay", buffer, theDelay);
        }
        {
            Integer theClassIndex;
            theClassIndex = this.getClassIndex();
            strategy.appendField(locator, this, "classIndex", buffer, theClassIndex);
        }
        {
            String theResponseTimeThresholdRuleID;
            theResponseTimeThresholdRuleID = this.getResponseTimeThresholdRuleID();
            strategy.appendField(locator, this, "responseTimeThresholdRuleID", buffer, theResponseTimeThresholdRuleID);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof ApplicationTier)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final ApplicationTier that = ((ApplicationTier) object);
        {
            List<ResponseTimeThreshold> lhsResponseTimeThreshold;
            lhsResponseTimeThreshold = (((this.responseTimeThreshold!= null)&&(!this.responseTimeThreshold.isEmpty()))?this.getResponseTimeThreshold():null);
            List<ResponseTimeThreshold> rhsResponseTimeThreshold;
            rhsResponseTimeThreshold = (((that.responseTimeThreshold!= null)&&(!that.responseTimeThreshold.isEmpty()))?that.getResponseTimeThreshold():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "responseTimeThreshold", lhsResponseTimeThreshold), LocatorUtils.property(thatLocator, "responseTimeThreshold", rhsResponseTimeThreshold), lhsResponseTimeThreshold, rhsResponseTimeThreshold)) {
                return false;
            }
        }
        {
            List<Functionality> lhsFunctionality;
            lhsFunctionality = (((this.functionality!= null)&&(!this.functionality.isEmpty()))?this.getFunctionality():null);
            List<Functionality> rhsFunctionality;
            rhsFunctionality = (((that.functionality!= null)&&(!that.functionality.isEmpty()))?that.getFunctionality():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "functionality", lhsFunctionality), LocatorUtils.property(thatLocator, "functionality", rhsFunctionality), lhsFunctionality, rhsFunctionality)) {
                return false;
            }
        }
        {
            List<WorkloadForecast> lhsWorkloadForecast;
            lhsWorkloadForecast = (((this.workloadForecast!= null)&&(!this.workloadForecast.isEmpty()))?this.getWorkloadForecast():null);
            List<WorkloadForecast> rhsWorkloadForecast;
            rhsWorkloadForecast = (((that.workloadForecast!= null)&&(!that.workloadForecast.isEmpty()))?that.getWorkloadForecast():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "workloadForecast", lhsWorkloadForecast), LocatorUtils.property(thatLocator, "workloadForecast", rhsWorkloadForecast), lhsWorkloadForecast, rhsWorkloadForecast)) {
                return false;
            }
        }
        {
            List<Instance> lhsInstances;
            lhsInstances = (((this.instances!= null)&&(!this.instances.isEmpty()))?this.getInstances():null);
            List<Instance> rhsInstances;
            rhsInstances = (((that.instances!= null)&&(!that.instances.isEmpty()))?that.getInstances():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "instances", lhsInstances), LocatorUtils.property(thatLocator, "instances", rhsInstances), lhsInstances, rhsInstances)) {
                return false;
            }
        }
        {
            String lhsId;
            lhsId = this.getId();
            String rhsId;
            rhsId = that.getId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "id", lhsId), LocatorUtils.property(thatLocator, "id", rhsId), lhsId, rhsId)) {
                return false;
            }
        }
        {
            Double lhsDemand;
            lhsDemand = this.getDemand();
            Double rhsDemand;
            rhsDemand = that.getDemand();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "demand", lhsDemand), LocatorUtils.property(thatLocator, "demand", rhsDemand), lhsDemand, rhsDemand)) {
                return false;
            }
        }
        {
            double lhsDelay;
            lhsDelay = ((this.delay!= null)?this.getDelay(): 0.0D);
            double rhsDelay;
            rhsDelay = ((that.delay!= null)?that.getDelay(): 0.0D);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "delay", lhsDelay), LocatorUtils.property(thatLocator, "delay", rhsDelay), lhsDelay, rhsDelay)) {
                return false;
            }
        }
        {
            Integer lhsClassIndex;
            lhsClassIndex = this.getClassIndex();
            Integer rhsClassIndex;
            rhsClassIndex = that.getClassIndex();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "classIndex", lhsClassIndex), LocatorUtils.property(thatLocator, "classIndex", rhsClassIndex), lhsClassIndex, rhsClassIndex)) {
                return false;
            }
        }
        {
            String lhsResponseTimeThresholdRuleID;
            lhsResponseTimeThresholdRuleID = this.getResponseTimeThresholdRuleID();
            String rhsResponseTimeThresholdRuleID;
            rhsResponseTimeThresholdRuleID = that.getResponseTimeThresholdRuleID();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "responseTimeThresholdRuleID", lhsResponseTimeThresholdRuleID), LocatorUtils.property(thatLocator, "responseTimeThresholdRuleID", rhsResponseTimeThresholdRuleID), lhsResponseTimeThresholdRuleID, rhsResponseTimeThresholdRuleID)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            List<ResponseTimeThreshold> theResponseTimeThreshold;
            theResponseTimeThreshold = (((this.responseTimeThreshold!= null)&&(!this.responseTimeThreshold.isEmpty()))?this.getResponseTimeThreshold():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "responseTimeThreshold", theResponseTimeThreshold), currentHashCode, theResponseTimeThreshold);
        }
        {
            List<Functionality> theFunctionality;
            theFunctionality = (((this.functionality!= null)&&(!this.functionality.isEmpty()))?this.getFunctionality():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "functionality", theFunctionality), currentHashCode, theFunctionality);
        }
        {
            List<WorkloadForecast> theWorkloadForecast;
            theWorkloadForecast = (((this.workloadForecast!= null)&&(!this.workloadForecast.isEmpty()))?this.getWorkloadForecast():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "workloadForecast", theWorkloadForecast), currentHashCode, theWorkloadForecast);
        }
        {
            List<Instance> theInstances;
            theInstances = (((this.instances!= null)&&(!this.instances.isEmpty()))?this.getInstances():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "instances", theInstances), currentHashCode, theInstances);
        }
        {
            String theId;
            theId = this.getId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "id", theId), currentHashCode, theId);
        }
        {
            Double theDemand;
            theDemand = this.getDemand();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "demand", theDemand), currentHashCode, theDemand);
        }
        {
            double theDelay;
            theDelay = ((this.delay!= null)?this.getDelay(): 0.0D);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "delay", theDelay), currentHashCode, theDelay);
        }
        {
            Integer theClassIndex;
            theClassIndex = this.getClassIndex();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "classIndex", theClassIndex), currentHashCode, theClassIndex);
        }
        {
            String theResponseTimeThresholdRuleID;
            theResponseTimeThresholdRuleID = this.getResponseTimeThresholdRuleID();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "responseTimeThresholdRuleID", theResponseTimeThresholdRuleID), currentHashCode, theResponseTimeThresholdRuleID);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

    public Object clone() {
        return copyTo(createNewInstance());
    }

    public Object copyTo(Object target) {
        final CopyStrategy strategy = JAXBCopyStrategy.INSTANCE;
        return copyTo(null, target, strategy);
    }

    public Object copyTo(ObjectLocator locator, Object target, CopyStrategy strategy) {
        final Object draftCopy = ((target == null)?createNewInstance():target);
        if (draftCopy instanceof ApplicationTier) {
            final ApplicationTier copy = ((ApplicationTier) draftCopy);
            if ((this.responseTimeThreshold!= null)&&(!this.responseTimeThreshold.isEmpty())) {
                List<ResponseTimeThreshold> sourceResponseTimeThreshold;
                sourceResponseTimeThreshold = (((this.responseTimeThreshold!= null)&&(!this.responseTimeThreshold.isEmpty()))?this.getResponseTimeThreshold():null);
                @SuppressWarnings("unchecked")
                List<ResponseTimeThreshold> copyResponseTimeThreshold = ((List<ResponseTimeThreshold> ) strategy.copy(LocatorUtils.property(locator, "responseTimeThreshold", sourceResponseTimeThreshold), sourceResponseTimeThreshold));
                copy.responseTimeThreshold = null;
                if (copyResponseTimeThreshold!= null) {
                    List<ResponseTimeThreshold> uniqueResponseTimeThresholdl = copy.getResponseTimeThreshold();
                    uniqueResponseTimeThresholdl.addAll(copyResponseTimeThreshold);
                }
            } else {
                copy.responseTimeThreshold = null;
            }
            if ((this.functionality!= null)&&(!this.functionality.isEmpty())) {
                List<Functionality> sourceFunctionality;
                sourceFunctionality = (((this.functionality!= null)&&(!this.functionality.isEmpty()))?this.getFunctionality():null);
                @SuppressWarnings("unchecked")
                List<Functionality> copyFunctionality = ((List<Functionality> ) strategy.copy(LocatorUtils.property(locator, "functionality", sourceFunctionality), sourceFunctionality));
                copy.functionality = null;
                if (copyFunctionality!= null) {
                    List<Functionality> uniqueFunctionalityl = copy.getFunctionality();
                    uniqueFunctionalityl.addAll(copyFunctionality);
                }
            } else {
                copy.functionality = null;
            }
            if ((this.workloadForecast!= null)&&(!this.workloadForecast.isEmpty())) {
                List<WorkloadForecast> sourceWorkloadForecast;
                sourceWorkloadForecast = (((this.workloadForecast!= null)&&(!this.workloadForecast.isEmpty()))?this.getWorkloadForecast():null);
                @SuppressWarnings("unchecked")
                List<WorkloadForecast> copyWorkloadForecast = ((List<WorkloadForecast> ) strategy.copy(LocatorUtils.property(locator, "workloadForecast", sourceWorkloadForecast), sourceWorkloadForecast));
                copy.workloadForecast = null;
                if (copyWorkloadForecast!= null) {
                    List<WorkloadForecast> uniqueWorkloadForecastl = copy.getWorkloadForecast();
                    uniqueWorkloadForecastl.addAll(copyWorkloadForecast);
                }
            } else {
                copy.workloadForecast = null;
            }
            if ((this.instances!= null)&&(!this.instances.isEmpty())) {
                List<Instance> sourceInstances;
                sourceInstances = (((this.instances!= null)&&(!this.instances.isEmpty()))?this.getInstances():null);
                @SuppressWarnings("unchecked")
                List<Instance> copyInstances = ((List<Instance> ) strategy.copy(LocatorUtils.property(locator, "instances", sourceInstances), sourceInstances));
                copy.instances = null;
                if (copyInstances!= null) {
                    List<Instance> uniqueInstancesl = copy.getInstances();
                    uniqueInstancesl.addAll(copyInstances);
                }
            } else {
                copy.instances = null;
            }
            if (this.id!= null) {
                String sourceId;
                sourceId = this.getId();
                String copyId = ((String) strategy.copy(LocatorUtils.property(locator, "id", sourceId), sourceId));
                copy.setId(copyId);
            } else {
                copy.id = null;
            }
            if (this.demand!= null) {
                Double sourceDemand;
                sourceDemand = this.getDemand();
                Double copyDemand = ((Double) strategy.copy(LocatorUtils.property(locator, "demand", sourceDemand), sourceDemand));
                copy.setDemand(copyDemand);
            } else {
                copy.demand = null;
            }
            if (this.delay!= null) {
                double sourceDelay;
                sourceDelay = ((this.delay!= null)?this.getDelay(): 0.0D);
                double copyDelay = strategy.copy(LocatorUtils.property(locator, "delay", sourceDelay), sourceDelay);
                copy.setDelay(copyDelay);
            } else {
                copy.delay = null;
            }
            if (this.classIndex!= null) {
                Integer sourceClassIndex;
                sourceClassIndex = this.getClassIndex();
                Integer copyClassIndex = ((Integer) strategy.copy(LocatorUtils.property(locator, "classIndex", sourceClassIndex), sourceClassIndex));
                copy.setClassIndex(copyClassIndex);
            } else {
                copy.classIndex = null;
            }
            if (this.responseTimeThresholdRuleID!= null) {
                String sourceResponseTimeThresholdRuleID;
                sourceResponseTimeThresholdRuleID = this.getResponseTimeThresholdRuleID();
                String copyResponseTimeThresholdRuleID = ((String) strategy.copy(LocatorUtils.property(locator, "responseTimeThresholdRuleID", sourceResponseTimeThresholdRuleID), sourceResponseTimeThresholdRuleID));
                copy.setResponseTimeThresholdRuleID(copyResponseTimeThresholdRuleID);
            } else {
                copy.responseTimeThresholdRuleID = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new ApplicationTier();
    }

    public void mergeFrom(Object left, Object right) {
        final MergeStrategy strategy = JAXBMergeStrategy.INSTANCE;
        mergeFrom(null, null, left, right, strategy);
    }

    public void mergeFrom(ObjectLocator leftLocator, ObjectLocator rightLocator, Object left, Object right, MergeStrategy strategy) {
        if (right instanceof ApplicationTier) {
            final ApplicationTier target = this;
            final ApplicationTier leftObject = ((ApplicationTier) left);
            final ApplicationTier rightObject = ((ApplicationTier) right);
            {
                List<ResponseTimeThreshold> lhsResponseTimeThreshold;
                lhsResponseTimeThreshold = (((leftObject.responseTimeThreshold!= null)&&(!leftObject.responseTimeThreshold.isEmpty()))?leftObject.getResponseTimeThreshold():null);
                List<ResponseTimeThreshold> rhsResponseTimeThreshold;
                rhsResponseTimeThreshold = (((rightObject.responseTimeThreshold!= null)&&(!rightObject.responseTimeThreshold.isEmpty()))?rightObject.getResponseTimeThreshold():null);
                List<ResponseTimeThreshold> mergedResponseTimeThreshold = ((List<ResponseTimeThreshold> ) strategy.merge(LocatorUtils.property(leftLocator, "responseTimeThreshold", lhsResponseTimeThreshold), LocatorUtils.property(rightLocator, "responseTimeThreshold", rhsResponseTimeThreshold), lhsResponseTimeThreshold, rhsResponseTimeThreshold));
                target.responseTimeThreshold = null;
                if (mergedResponseTimeThreshold!= null) {
                    List<ResponseTimeThreshold> uniqueResponseTimeThresholdl = target.getResponseTimeThreshold();
                    uniqueResponseTimeThresholdl.addAll(mergedResponseTimeThreshold);
                }
            }
            {
                List<Functionality> lhsFunctionality;
                lhsFunctionality = (((leftObject.functionality!= null)&&(!leftObject.functionality.isEmpty()))?leftObject.getFunctionality():null);
                List<Functionality> rhsFunctionality;
                rhsFunctionality = (((rightObject.functionality!= null)&&(!rightObject.functionality.isEmpty()))?rightObject.getFunctionality():null);
                List<Functionality> mergedFunctionality = ((List<Functionality> ) strategy.merge(LocatorUtils.property(leftLocator, "functionality", lhsFunctionality), LocatorUtils.property(rightLocator, "functionality", rhsFunctionality), lhsFunctionality, rhsFunctionality));
                target.functionality = null;
                if (mergedFunctionality!= null) {
                    List<Functionality> uniqueFunctionalityl = target.getFunctionality();
                    uniqueFunctionalityl.addAll(mergedFunctionality);
                }
            }
            {
                List<WorkloadForecast> lhsWorkloadForecast;
                lhsWorkloadForecast = (((leftObject.workloadForecast!= null)&&(!leftObject.workloadForecast.isEmpty()))?leftObject.getWorkloadForecast():null);
                List<WorkloadForecast> rhsWorkloadForecast;
                rhsWorkloadForecast = (((rightObject.workloadForecast!= null)&&(!rightObject.workloadForecast.isEmpty()))?rightObject.getWorkloadForecast():null);
                List<WorkloadForecast> mergedWorkloadForecast = ((List<WorkloadForecast> ) strategy.merge(LocatorUtils.property(leftLocator, "workloadForecast", lhsWorkloadForecast), LocatorUtils.property(rightLocator, "workloadForecast", rhsWorkloadForecast), lhsWorkloadForecast, rhsWorkloadForecast));
                target.workloadForecast = null;
                if (mergedWorkloadForecast!= null) {
                    List<WorkloadForecast> uniqueWorkloadForecastl = target.getWorkloadForecast();
                    uniqueWorkloadForecastl.addAll(mergedWorkloadForecast);
                }
            }
            {
                List<Instance> lhsInstances;
                lhsInstances = (((leftObject.instances!= null)&&(!leftObject.instances.isEmpty()))?leftObject.getInstances():null);
                List<Instance> rhsInstances;
                rhsInstances = (((rightObject.instances!= null)&&(!rightObject.instances.isEmpty()))?rightObject.getInstances():null);
                List<Instance> mergedInstances = ((List<Instance> ) strategy.merge(LocatorUtils.property(leftLocator, "instances", lhsInstances), LocatorUtils.property(rightLocator, "instances", rhsInstances), lhsInstances, rhsInstances));
                target.instances = null;
                if (mergedInstances!= null) {
                    List<Instance> uniqueInstancesl = target.getInstances();
                    uniqueInstancesl.addAll(mergedInstances);
                }
            }
            {
                String lhsId;
                lhsId = leftObject.getId();
                String rhsId;
                rhsId = rightObject.getId();
                String mergedId = ((String) strategy.merge(LocatorUtils.property(leftLocator, "id", lhsId), LocatorUtils.property(rightLocator, "id", rhsId), lhsId, rhsId));
                target.setId(mergedId);
            }
            {
                Double lhsDemand;
                lhsDemand = leftObject.getDemand();
                Double rhsDemand;
                rhsDemand = rightObject.getDemand();
                Double mergedDemand = ((Double) strategy.merge(LocatorUtils.property(leftLocator, "demand", lhsDemand), LocatorUtils.property(rightLocator, "demand", rhsDemand), lhsDemand, rhsDemand));
                target.setDemand(mergedDemand);
            }
            {
                double lhsDelay;
                lhsDelay = ((leftObject.delay!= null)?leftObject.getDelay(): 0.0D);
                double rhsDelay;
                rhsDelay = ((rightObject.delay!= null)?rightObject.getDelay(): 0.0D);
                double mergedDelay = ((double) strategy.merge(LocatorUtils.property(leftLocator, "delay", lhsDelay), LocatorUtils.property(rightLocator, "delay", rhsDelay), lhsDelay, rhsDelay));
                target.setDelay(mergedDelay);
            }
            {
                Integer lhsClassIndex;
                lhsClassIndex = leftObject.getClassIndex();
                Integer rhsClassIndex;
                rhsClassIndex = rightObject.getClassIndex();
                Integer mergedClassIndex = ((Integer) strategy.merge(LocatorUtils.property(leftLocator, "classIndex", lhsClassIndex), LocatorUtils.property(rightLocator, "classIndex", rhsClassIndex), lhsClassIndex, rhsClassIndex));
                target.setClassIndex(mergedClassIndex);
            }
            {
                String lhsResponseTimeThresholdRuleID;
                lhsResponseTimeThresholdRuleID = leftObject.getResponseTimeThresholdRuleID();
                String rhsResponseTimeThresholdRuleID;
                rhsResponseTimeThresholdRuleID = rightObject.getResponseTimeThresholdRuleID();
                String mergedResponseTimeThresholdRuleID = ((String) strategy.merge(LocatorUtils.property(leftLocator, "responseTimeThresholdRuleID", lhsResponseTimeThresholdRuleID), LocatorUtils.property(rightLocator, "responseTimeThresholdRuleID", rhsResponseTimeThresholdRuleID), lhsResponseTimeThresholdRuleID, rhsResponseTimeThresholdRuleID));
                target.setResponseTimeThresholdRuleID(mergedResponseTimeThresholdRuleID);
            }
        }
    }

}
