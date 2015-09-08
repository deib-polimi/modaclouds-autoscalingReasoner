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
 * <p>Classe Java per container complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="container"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="applicationTier" type="{}applicationTier" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="capacity" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="nCore" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="processingRate" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="vmType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="maxReserved" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="reservedCost" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="onDemandCost" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "container", propOrder = {
    "applicationTier"
})
public class Container
    implements Cloneable, CopyTo, Equals, HashCode, MergeFrom, ToString
{

    @XmlElement(required = true)
    protected List<ApplicationTier> applicationTier;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "capacity", required = true)
    protected double capacity;
    @XmlAttribute(name = "nCore", required = true)
    protected int nCore;
    @XmlAttribute(name = "processingRate", required = true)
    protected double processingRate;
    @XmlAttribute(name = "vmType", required = true)
    protected String vmType;
    @XmlAttribute(name = "maxReserved", required = true)
    protected int maxReserved;
    @XmlAttribute(name = "reservedCost", required = true)
    protected double reservedCost;
    @XmlAttribute(name = "onDemandCost", required = true)
    protected double onDemandCost;

    /**
     * Gets the value of the applicationTier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the applicationTier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApplicationTier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ApplicationTier }
     * 
     * 
     */
    public List<ApplicationTier> getApplicationTier() {
        if (applicationTier == null) {
            applicationTier = new ArrayList<ApplicationTier>();
        }
        return this.applicationTier;
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
     * Recupera il valore della proprietà capacity.
     * 
     */
    public double getCapacity() {
        return capacity;
    }

    /**
     * Imposta il valore della proprietà capacity.
     * 
     */
    public void setCapacity(double value) {
        this.capacity = value;
    }

    /**
     * Recupera il valore della proprietà nCore.
     * 
     */
    public int getNCore() {
        return nCore;
    }

    /**
     * Imposta il valore della proprietà nCore.
     * 
     */
    public void setNCore(int value) {
        this.nCore = value;
    }

    /**
     * Recupera il valore della proprietà processingRate.
     * 
     */
    public double getProcessingRate() {
        return processingRate;
    }

    /**
     * Imposta il valore della proprietà processingRate.
     * 
     */
    public void setProcessingRate(double value) {
        this.processingRate = value;
    }

    /**
     * Recupera il valore della proprietà vmType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVmType() {
        return vmType;
    }

    /**
     * Imposta il valore della proprietà vmType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVmType(String value) {
        this.vmType = value;
    }

    /**
     * Recupera il valore della proprietà maxReserved.
     * 
     */
    public int getMaxReserved() {
        return maxReserved;
    }

    /**
     * Imposta il valore della proprietà maxReserved.
     * 
     */
    public void setMaxReserved(int value) {
        this.maxReserved = value;
    }

    /**
     * Recupera il valore della proprietà reservedCost.
     * 
     */
    public double getReservedCost() {
        return reservedCost;
    }

    /**
     * Imposta il valore della proprietà reservedCost.
     * 
     */
    public void setReservedCost(double value) {
        this.reservedCost = value;
    }

    /**
     * Recupera il valore della proprietà onDemandCost.
     * 
     */
    public double getOnDemandCost() {
        return onDemandCost;
    }

    /**
     * Imposta il valore della proprietà onDemandCost.
     * 
     */
    public void setOnDemandCost(double value) {
        this.onDemandCost = value;
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
            List<ApplicationTier> theApplicationTier;
            theApplicationTier = (((this.applicationTier!= null)&&(!this.applicationTier.isEmpty()))?this.getApplicationTier():null);
            strategy.appendField(locator, this, "applicationTier", buffer, theApplicationTier);
        }
        {
            String theId;
            theId = this.getId();
            strategy.appendField(locator, this, "id", buffer, theId);
        }
        {
            double theCapacity;
            theCapacity = (true?this.getCapacity(): 0.0D);
            strategy.appendField(locator, this, "capacity", buffer, theCapacity);
        }
        {
            int theNCore;
            theNCore = (true?this.getNCore(): 0);
            strategy.appendField(locator, this, "nCore", buffer, theNCore);
        }
        {
            double theProcessingRate;
            theProcessingRate = (true?this.getProcessingRate(): 0.0D);
            strategy.appendField(locator, this, "processingRate", buffer, theProcessingRate);
        }
        {
            String theVmType;
            theVmType = this.getVmType();
            strategy.appendField(locator, this, "vmType", buffer, theVmType);
        }
        {
            int theMaxReserved;
            theMaxReserved = (true?this.getMaxReserved(): 0);
            strategy.appendField(locator, this, "maxReserved", buffer, theMaxReserved);
        }
        {
            double theReservedCost;
            theReservedCost = (true?this.getReservedCost(): 0.0D);
            strategy.appendField(locator, this, "reservedCost", buffer, theReservedCost);
        }
        {
            double theOnDemandCost;
            theOnDemandCost = (true?this.getOnDemandCost(): 0.0D);
            strategy.appendField(locator, this, "onDemandCost", buffer, theOnDemandCost);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Container)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Container that = ((Container) object);
        {
            List<ApplicationTier> lhsApplicationTier;
            lhsApplicationTier = (((this.applicationTier!= null)&&(!this.applicationTier.isEmpty()))?this.getApplicationTier():null);
            List<ApplicationTier> rhsApplicationTier;
            rhsApplicationTier = (((that.applicationTier!= null)&&(!that.applicationTier.isEmpty()))?that.getApplicationTier():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "applicationTier", lhsApplicationTier), LocatorUtils.property(thatLocator, "applicationTier", rhsApplicationTier), lhsApplicationTier, rhsApplicationTier)) {
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
            double lhsCapacity;
            lhsCapacity = (true?this.getCapacity(): 0.0D);
            double rhsCapacity;
            rhsCapacity = (true?that.getCapacity(): 0.0D);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "capacity", lhsCapacity), LocatorUtils.property(thatLocator, "capacity", rhsCapacity), lhsCapacity, rhsCapacity)) {
                return false;
            }
        }
        {
            int lhsNCore;
            lhsNCore = (true?this.getNCore(): 0);
            int rhsNCore;
            rhsNCore = (true?that.getNCore(): 0);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "nCore", lhsNCore), LocatorUtils.property(thatLocator, "nCore", rhsNCore), lhsNCore, rhsNCore)) {
                return false;
            }
        }
        {
            double lhsProcessingRate;
            lhsProcessingRate = (true?this.getProcessingRate(): 0.0D);
            double rhsProcessingRate;
            rhsProcessingRate = (true?that.getProcessingRate(): 0.0D);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "processingRate", lhsProcessingRate), LocatorUtils.property(thatLocator, "processingRate", rhsProcessingRate), lhsProcessingRate, rhsProcessingRate)) {
                return false;
            }
        }
        {
            String lhsVmType;
            lhsVmType = this.getVmType();
            String rhsVmType;
            rhsVmType = that.getVmType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "vmType", lhsVmType), LocatorUtils.property(thatLocator, "vmType", rhsVmType), lhsVmType, rhsVmType)) {
                return false;
            }
        }
        {
            int lhsMaxReserved;
            lhsMaxReserved = (true?this.getMaxReserved(): 0);
            int rhsMaxReserved;
            rhsMaxReserved = (true?that.getMaxReserved(): 0);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "maxReserved", lhsMaxReserved), LocatorUtils.property(thatLocator, "maxReserved", rhsMaxReserved), lhsMaxReserved, rhsMaxReserved)) {
                return false;
            }
        }
        {
            double lhsReservedCost;
            lhsReservedCost = (true?this.getReservedCost(): 0.0D);
            double rhsReservedCost;
            rhsReservedCost = (true?that.getReservedCost(): 0.0D);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "reservedCost", lhsReservedCost), LocatorUtils.property(thatLocator, "reservedCost", rhsReservedCost), lhsReservedCost, rhsReservedCost)) {
                return false;
            }
        }
        {
            double lhsOnDemandCost;
            lhsOnDemandCost = (true?this.getOnDemandCost(): 0.0D);
            double rhsOnDemandCost;
            rhsOnDemandCost = (true?that.getOnDemandCost(): 0.0D);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "onDemandCost", lhsOnDemandCost), LocatorUtils.property(thatLocator, "onDemandCost", rhsOnDemandCost), lhsOnDemandCost, rhsOnDemandCost)) {
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
            List<ApplicationTier> theApplicationTier;
            theApplicationTier = (((this.applicationTier!= null)&&(!this.applicationTier.isEmpty()))?this.getApplicationTier():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "applicationTier", theApplicationTier), currentHashCode, theApplicationTier);
        }
        {
            String theId;
            theId = this.getId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "id", theId), currentHashCode, theId);
        }
        {
            double theCapacity;
            theCapacity = (true?this.getCapacity(): 0.0D);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "capacity", theCapacity), currentHashCode, theCapacity);
        }
        {
            int theNCore;
            theNCore = (true?this.getNCore(): 0);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "nCore", theNCore), currentHashCode, theNCore);
        }
        {
            double theProcessingRate;
            theProcessingRate = (true?this.getProcessingRate(): 0.0D);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "processingRate", theProcessingRate), currentHashCode, theProcessingRate);
        }
        {
            String theVmType;
            theVmType = this.getVmType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "vmType", theVmType), currentHashCode, theVmType);
        }
        {
            int theMaxReserved;
            theMaxReserved = (true?this.getMaxReserved(): 0);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "maxReserved", theMaxReserved), currentHashCode, theMaxReserved);
        }
        {
            double theReservedCost;
            theReservedCost = (true?this.getReservedCost(): 0.0D);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "reservedCost", theReservedCost), currentHashCode, theReservedCost);
        }
        {
            double theOnDemandCost;
            theOnDemandCost = (true?this.getOnDemandCost(): 0.0D);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "onDemandCost", theOnDemandCost), currentHashCode, theOnDemandCost);
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
        if (draftCopy instanceof Container) {
            final Container copy = ((Container) draftCopy);
            if ((this.applicationTier!= null)&&(!this.applicationTier.isEmpty())) {
                List<ApplicationTier> sourceApplicationTier;
                sourceApplicationTier = (((this.applicationTier!= null)&&(!this.applicationTier.isEmpty()))?this.getApplicationTier():null);
                @SuppressWarnings("unchecked")
                List<ApplicationTier> copyApplicationTier = ((List<ApplicationTier> ) strategy.copy(LocatorUtils.property(locator, "applicationTier", sourceApplicationTier), sourceApplicationTier));
                copy.applicationTier = null;
                if (copyApplicationTier!= null) {
                    List<ApplicationTier> uniqueApplicationTierl = copy.getApplicationTier();
                    uniqueApplicationTierl.addAll(copyApplicationTier);
                }
            } else {
                copy.applicationTier = null;
            }
            if (this.id!= null) {
                String sourceId;
                sourceId = this.getId();
                String copyId = ((String) strategy.copy(LocatorUtils.property(locator, "id", sourceId), sourceId));
                copy.setId(copyId);
            } else {
                copy.id = null;
            }
            double sourceCapacity;
            sourceCapacity = (true?this.getCapacity(): 0.0D);
            double copyCapacity = strategy.copy(LocatorUtils.property(locator, "capacity", sourceCapacity), sourceCapacity);
            copy.setCapacity(copyCapacity);
            int sourceNCore;
            sourceNCore = (true?this.getNCore(): 0);
            int copyNCore = strategy.copy(LocatorUtils.property(locator, "nCore", sourceNCore), sourceNCore);
            copy.setNCore(copyNCore);
            double sourceProcessingRate;
            sourceProcessingRate = (true?this.getProcessingRate(): 0.0D);
            double copyProcessingRate = strategy.copy(LocatorUtils.property(locator, "processingRate", sourceProcessingRate), sourceProcessingRate);
            copy.setProcessingRate(copyProcessingRate);
            if (this.vmType!= null) {
                String sourceVmType;
                sourceVmType = this.getVmType();
                String copyVmType = ((String) strategy.copy(LocatorUtils.property(locator, "vmType", sourceVmType), sourceVmType));
                copy.setVmType(copyVmType);
            } else {
                copy.vmType = null;
            }
            int sourceMaxReserved;
            sourceMaxReserved = (true?this.getMaxReserved(): 0);
            int copyMaxReserved = strategy.copy(LocatorUtils.property(locator, "maxReserved", sourceMaxReserved), sourceMaxReserved);
            copy.setMaxReserved(copyMaxReserved);
            double sourceReservedCost;
            sourceReservedCost = (true?this.getReservedCost(): 0.0D);
            double copyReservedCost = strategy.copy(LocatorUtils.property(locator, "reservedCost", sourceReservedCost), sourceReservedCost);
            copy.setReservedCost(copyReservedCost);
            double sourceOnDemandCost;
            sourceOnDemandCost = (true?this.getOnDemandCost(): 0.0D);
            double copyOnDemandCost = strategy.copy(LocatorUtils.property(locator, "onDemandCost", sourceOnDemandCost), sourceOnDemandCost);
            copy.setOnDemandCost(copyOnDemandCost);
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new Container();
    }

    public void mergeFrom(Object left, Object right) {
        final MergeStrategy strategy = JAXBMergeStrategy.INSTANCE;
        mergeFrom(null, null, left, right, strategy);
    }

    public void mergeFrom(ObjectLocator leftLocator, ObjectLocator rightLocator, Object left, Object right, MergeStrategy strategy) {
        if (right instanceof Container) {
            final Container target = this;
            final Container leftObject = ((Container) left);
            final Container rightObject = ((Container) right);
            {
                List<ApplicationTier> lhsApplicationTier;
                lhsApplicationTier = (((leftObject.applicationTier!= null)&&(!leftObject.applicationTier.isEmpty()))?leftObject.getApplicationTier():null);
                List<ApplicationTier> rhsApplicationTier;
                rhsApplicationTier = (((rightObject.applicationTier!= null)&&(!rightObject.applicationTier.isEmpty()))?rightObject.getApplicationTier():null);
                List<ApplicationTier> mergedApplicationTier = ((List<ApplicationTier> ) strategy.merge(LocatorUtils.property(leftLocator, "applicationTier", lhsApplicationTier), LocatorUtils.property(rightLocator, "applicationTier", rhsApplicationTier), lhsApplicationTier, rhsApplicationTier));
                target.applicationTier = null;
                if (mergedApplicationTier!= null) {
                    List<ApplicationTier> uniqueApplicationTierl = target.getApplicationTier();
                    uniqueApplicationTierl.addAll(mergedApplicationTier);
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
                double lhsCapacity;
                lhsCapacity = (true?leftObject.getCapacity(): 0.0D);
                double rhsCapacity;
                rhsCapacity = (true?rightObject.getCapacity(): 0.0D);
                double mergedCapacity = ((double) strategy.merge(LocatorUtils.property(leftLocator, "capacity", lhsCapacity), LocatorUtils.property(rightLocator, "capacity", rhsCapacity), lhsCapacity, rhsCapacity));
                target.setCapacity(mergedCapacity);
            }
            {
                int lhsNCore;
                lhsNCore = (true?leftObject.getNCore(): 0);
                int rhsNCore;
                rhsNCore = (true?rightObject.getNCore(): 0);
                int mergedNCore = ((int) strategy.merge(LocatorUtils.property(leftLocator, "nCore", lhsNCore), LocatorUtils.property(rightLocator, "nCore", rhsNCore), lhsNCore, rhsNCore));
                target.setNCore(mergedNCore);
            }
            {
                double lhsProcessingRate;
                lhsProcessingRate = (true?leftObject.getProcessingRate(): 0.0D);
                double rhsProcessingRate;
                rhsProcessingRate = (true?rightObject.getProcessingRate(): 0.0D);
                double mergedProcessingRate = ((double) strategy.merge(LocatorUtils.property(leftLocator, "processingRate", lhsProcessingRate), LocatorUtils.property(rightLocator, "processingRate", rhsProcessingRate), lhsProcessingRate, rhsProcessingRate));
                target.setProcessingRate(mergedProcessingRate);
            }
            {
                String lhsVmType;
                lhsVmType = leftObject.getVmType();
                String rhsVmType;
                rhsVmType = rightObject.getVmType();
                String mergedVmType = ((String) strategy.merge(LocatorUtils.property(leftLocator, "vmType", lhsVmType), LocatorUtils.property(rightLocator, "vmType", rhsVmType), lhsVmType, rhsVmType));
                target.setVmType(mergedVmType);
            }
            {
                int lhsMaxReserved;
                lhsMaxReserved = (true?leftObject.getMaxReserved(): 0);
                int rhsMaxReserved;
                rhsMaxReserved = (true?rightObject.getMaxReserved(): 0);
                int mergedMaxReserved = ((int) strategy.merge(LocatorUtils.property(leftLocator, "maxReserved", lhsMaxReserved), LocatorUtils.property(rightLocator, "maxReserved", rhsMaxReserved), lhsMaxReserved, rhsMaxReserved));
                target.setMaxReserved(mergedMaxReserved);
            }
            {
                double lhsReservedCost;
                lhsReservedCost = (true?leftObject.getReservedCost(): 0.0D);
                double rhsReservedCost;
                rhsReservedCost = (true?rightObject.getReservedCost(): 0.0D);
                double mergedReservedCost = ((double) strategy.merge(LocatorUtils.property(leftLocator, "reservedCost", lhsReservedCost), LocatorUtils.property(rightLocator, "reservedCost", rhsReservedCost), lhsReservedCost, rhsReservedCost));
                target.setReservedCost(mergedReservedCost);
            }
            {
                double lhsOnDemandCost;
                lhsOnDemandCost = (true?leftObject.getOnDemandCost(): 0.0D);
                double rhsOnDemandCost;
                rhsOnDemandCost = (true?rightObject.getOnDemandCost(): 0.0D);
                double mergedOnDemandCost = ((double) strategy.merge(LocatorUtils.property(leftLocator, "onDemandCost", lhsOnDemandCost), LocatorUtils.property(rightLocator, "onDemandCost", rhsOnDemandCost), lhsOnDemandCost, rhsOnDemandCost));
                target.setOnDemandCost(mergedOnDemandCost);
            }
        }
    }

}
