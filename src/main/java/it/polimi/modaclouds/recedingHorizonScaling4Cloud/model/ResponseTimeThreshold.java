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
 * <p>Classe Java per responseTimeThreshold complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="responseTimeThreshold"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="hour" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *             &lt;minInclusive value="0"/&gt;
 *             &lt;maxInclusive value="23"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseTimeThreshold")
public class ResponseTimeThreshold
    implements Cloneable, CopyTo, Equals, HashCode, MergeFrom, ToString
{

    @XmlAttribute(name = "hour", required = true)
    protected int hour;
    @XmlAttribute(name = "value", required = true)
    protected double value;

    /**
     * Recupera il valore della proprietà hour.
     * 
     */
    public int getHour() {
        return hour;
    }

    /**
     * Imposta il valore della proprietà hour.
     * 
     */
    public void setHour(int value) {
        this.hour = value;
    }

    /**
     * Recupera il valore della proprietà value.
     * 
     */
    public double getValue() {
        return value;
    }

    /**
     * Imposta il valore della proprietà value.
     * 
     */
    public void setValue(double value) {
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
            int theHour;
            theHour = (true?this.getHour(): 0);
            strategy.appendField(locator, this, "hour", buffer, theHour);
        }
        {
            double theValue;
            theValue = (true?this.getValue(): 0.0D);
            strategy.appendField(locator, this, "value", buffer, theValue);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof ResponseTimeThreshold)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final ResponseTimeThreshold that = ((ResponseTimeThreshold) object);
        {
            int lhsHour;
            lhsHour = (true?this.getHour(): 0);
            int rhsHour;
            rhsHour = (true?that.getHour(): 0);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "hour", lhsHour), LocatorUtils.property(thatLocator, "hour", rhsHour), lhsHour, rhsHour)) {
                return false;
            }
        }
        {
            double lhsValue;
            lhsValue = (true?this.getValue(): 0.0D);
            double rhsValue;
            rhsValue = (true?that.getValue(): 0.0D);
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
            int theHour;
            theHour = (true?this.getHour(): 0);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "hour", theHour), currentHashCode, theHour);
        }
        {
            double theValue;
            theValue = (true?this.getValue(): 0.0D);
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
        if (draftCopy instanceof ResponseTimeThreshold) {
            final ResponseTimeThreshold copy = ((ResponseTimeThreshold) draftCopy);
            int sourceHour;
            sourceHour = (true?this.getHour(): 0);
            int copyHour = strategy.copy(LocatorUtils.property(locator, "hour", sourceHour), sourceHour);
            copy.setHour(copyHour);
            double sourceValue;
            sourceValue = (true?this.getValue(): 0.0D);
            double copyValue = strategy.copy(LocatorUtils.property(locator, "value", sourceValue), sourceValue);
            copy.setValue(copyValue);
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new ResponseTimeThreshold();
    }

    public void mergeFrom(Object left, Object right) {
        final MergeStrategy strategy = JAXBMergeStrategy.INSTANCE;
        mergeFrom(null, null, left, right, strategy);
    }

    public void mergeFrom(ObjectLocator leftLocator, ObjectLocator rightLocator, Object left, Object right, MergeStrategy strategy) {
        if (right instanceof ResponseTimeThreshold) {
            final ResponseTimeThreshold target = this;
            final ResponseTimeThreshold leftObject = ((ResponseTimeThreshold) left);
            final ResponseTimeThreshold rightObject = ((ResponseTimeThreshold) right);
            {
                int lhsHour;
                lhsHour = (true?leftObject.getHour(): 0);
                int rhsHour;
                rhsHour = (true?rightObject.getHour(): 0);
                int mergedHour = ((int) strategy.merge(LocatorUtils.property(leftLocator, "hour", lhsHour), LocatorUtils.property(rightLocator, "hour", rhsHour), lhsHour, rhsHour));
                target.setHour(mergedHour);
            }
            {
                double lhsValue;
                lhsValue = (true?leftObject.getValue(): 0.0D);
                double rhsValue;
                rhsValue = (true?rightObject.getValue(): 0.0D);
                double mergedValue = ((double) strategy.merge(LocatorUtils.property(leftLocator, "value", lhsValue), LocatorUtils.property(rightLocator, "value", rhsValue), lhsValue, rhsValue));
                target.setValue(mergedValue);
            }
        }
    }

}
