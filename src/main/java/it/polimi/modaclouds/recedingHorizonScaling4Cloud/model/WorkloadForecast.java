//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.11 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2015.09.08 alle 05:54:57 PM CEST 
//


package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 * <p>Classe Java per workloadForecast complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="workloadForecast"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="timeStepAhead" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "workloadForecast")
public class WorkloadForecast
    implements Cloneable, CopyTo, Equals, HashCode, MergeFrom, ToString
{

    @XmlAttribute(name = "timeStepAhead", required = true)
    protected int timeStepAhead;
    @XmlAttribute(name = "value")
    protected Double value;

    /**
     * Recupera il valore della proprietà timeStepAhead.
     * 
     */
    public int getTimeStepAhead() {
        return timeStepAhead;
    }

    /**
     * Imposta il valore della proprietà timeStepAhead.
     * 
     */
    public void setTimeStepAhead(int value) {
        this.timeStepAhead = value;
    }

    /**
     * Recupera il valore della proprietà value.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValue() {
        return value;
    }

    /**
     * Imposta il valore della proprietà value.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValue(Double value) {
        this.value = value;
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
            int theTimeStepAhead;
            theTimeStepAhead = (true?this.getTimeStepAhead(): 0);
            strategy.appendField(locator, this, "timeStepAhead", buffer, theTimeStepAhead);
        }
        {
            Double theValue;
            theValue = this.getValue();
            strategy.appendField(locator, this, "value", buffer, theValue);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WorkloadForecast)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WorkloadForecast that = ((WorkloadForecast) object);
        {
            int lhsTimeStepAhead;
            lhsTimeStepAhead = (true?this.getTimeStepAhead(): 0);
            int rhsTimeStepAhead;
            rhsTimeStepAhead = (true?that.getTimeStepAhead(): 0);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "timeStepAhead", lhsTimeStepAhead), LocatorUtils.property(thatLocator, "timeStepAhead", rhsTimeStepAhead), lhsTimeStepAhead, rhsTimeStepAhead)) {
                return false;
            }
        }
        {
            Double lhsValue;
            lhsValue = this.getValue();
            Double rhsValue;
            rhsValue = that.getValue();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "value", lhsValue), LocatorUtils.property(thatLocator, "value", rhsValue), lhsValue, rhsValue)) {
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
            int theTimeStepAhead;
            theTimeStepAhead = (true?this.getTimeStepAhead(): 0);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "timeStepAhead", theTimeStepAhead), currentHashCode, theTimeStepAhead);
        }
        {
            Double theValue;
            theValue = this.getValue();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "value", theValue), currentHashCode, theValue);
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
        if (draftCopy instanceof WorkloadForecast) {
            final WorkloadForecast copy = ((WorkloadForecast) draftCopy);
            int sourceTimeStepAhead;
            sourceTimeStepAhead = (true?this.getTimeStepAhead(): 0);
            int copyTimeStepAhead = strategy.copy(LocatorUtils.property(locator, "timeStepAhead", sourceTimeStepAhead), sourceTimeStepAhead);
            copy.setTimeStepAhead(copyTimeStepAhead);
            if (this.value!= null) {
                Double sourceValue;
                sourceValue = this.getValue();
                Double copyValue = ((Double) strategy.copy(LocatorUtils.property(locator, "value", sourceValue), sourceValue));
                copy.setValue(copyValue);
            } else {
                copy.value = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WorkloadForecast();
    }

    public void mergeFrom(Object left, Object right) {
        final MergeStrategy strategy = JAXBMergeStrategy.INSTANCE;
        mergeFrom(null, null, left, right, strategy);
    }

    public void mergeFrom(ObjectLocator leftLocator, ObjectLocator rightLocator, Object left, Object right, MergeStrategy strategy) {
        if (right instanceof WorkloadForecast) {
            final WorkloadForecast target = this;
            final WorkloadForecast leftObject = ((WorkloadForecast) left);
            final WorkloadForecast rightObject = ((WorkloadForecast) right);
            {
                int lhsTimeStepAhead;
                lhsTimeStepAhead = (true?leftObject.getTimeStepAhead(): 0);
                int rhsTimeStepAhead;
                rhsTimeStepAhead = (true?rightObject.getTimeStepAhead(): 0);
                int mergedTimeStepAhead = ((int) strategy.merge(LocatorUtils.property(leftLocator, "timeStepAhead", lhsTimeStepAhead), LocatorUtils.property(rightLocator, "timeStepAhead", rhsTimeStepAhead), lhsTimeStepAhead, rhsTimeStepAhead));
                target.setTimeStepAhead(mergedTimeStepAhead);
            }
            {
                Double lhsValue;
                lhsValue = leftObject.getValue();
                Double rhsValue;
                rhsValue = rightObject.getValue();
                Double mergedValue = ((Double) strategy.merge(LocatorUtils.property(leftLocator, "value", lhsValue), LocatorUtils.property(rightLocator, "value", rhsValue), lhsValue, rhsValue));
                target.setValue(mergedValue);
            }
        }
    }

}
