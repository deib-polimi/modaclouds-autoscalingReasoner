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
 * <p>Classe Java per functionality complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="functionality"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="workloadForecast" type="{}workloadForecast" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="demand" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "functionality", propOrder = {
    "workloadForecast"
})
public class Functionality
    implements Cloneable, CopyTo, Equals, HashCode, MergeFrom, ToString
{

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
            List<WorkloadForecast> theWorkloadForecast;
            theWorkloadForecast = (((this.workloadForecast!= null)&&(!this.workloadForecast.isEmpty()))?this.getWorkloadForecast():null);
            strategy.appendField(locator, this, "workloadForecast", buffer, theWorkloadForecast);
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
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Functionality)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Functionality that = ((Functionality) object);
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
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            List<WorkloadForecast> theWorkloadForecast;
            theWorkloadForecast = (((this.workloadForecast!= null)&&(!this.workloadForecast.isEmpty()))?this.getWorkloadForecast():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "workloadForecast", theWorkloadForecast), currentHashCode, theWorkloadForecast);
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
        if (draftCopy instanceof Functionality) {
            final Functionality copy = ((Functionality) draftCopy);
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
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new Functionality();
    }

    public void mergeFrom(Object left, Object right) {
        final MergeStrategy strategy = JAXBMergeStrategy.INSTANCE;
        mergeFrom(null, null, left, right, strategy);
    }

    public void mergeFrom(ObjectLocator leftLocator, ObjectLocator rightLocator, Object left, Object right, MergeStrategy strategy) {
        if (right instanceof Functionality) {
            final Functionality target = this;
            final Functionality leftObject = ((Functionality) left);
            final Functionality rightObject = ((Functionality) right);
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
        }
    }

}
