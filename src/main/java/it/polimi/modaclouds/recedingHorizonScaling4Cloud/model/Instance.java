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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
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
 * <p>Classe Java per instance complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="instance"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="startTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
 *       &lt;attribute name="status" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="usedForScale" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instance")
public class Instance
    implements Cloneable, CopyTo, Equals, HashCode, MergeFrom, ToString
{

    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "startTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar startTime;
    @XmlAttribute(name = "status", required = true)
    protected String status;
    @XmlAttribute(name = "usedForScale")
    protected Boolean usedForScale;

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
     * Recupera il valore della proprietà startTime.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartTime() {
        return startTime;
    }

    /**
     * Imposta il valore della proprietà startTime.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartTime(XMLGregorianCalendar value) {
        this.startTime = value;
    }

    /**
     * Recupera il valore della proprietà status.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Imposta il valore della proprietà status.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Recupera il valore della proprietà usedForScale.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isUsedForScale() {
        if (usedForScale == null) {
            return false;
        } else {
            return usedForScale;
        }
    }

    /**
     * Imposta il valore della proprietà usedForScale.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUsedForScale(Boolean value) {
        this.usedForScale = value;
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
            String theId;
            theId = this.getId();
            strategy.appendField(locator, this, "id", buffer, theId);
        }
        {
            XMLGregorianCalendar theStartTime;
            theStartTime = this.getStartTime();
            strategy.appendField(locator, this, "startTime", buffer, theStartTime);
        }
        {
            String theStatus;
            theStatus = this.getStatus();
            strategy.appendField(locator, this, "status", buffer, theStatus);
        }
        {
            boolean theUsedForScale;
            theUsedForScale = ((this.usedForScale!= null)?this.isUsedForScale():false);
            strategy.appendField(locator, this, "usedForScale", buffer, theUsedForScale);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Instance)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Instance that = ((Instance) object);
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
            XMLGregorianCalendar lhsStartTime;
            lhsStartTime = this.getStartTime();
            XMLGregorianCalendar rhsStartTime;
            rhsStartTime = that.getStartTime();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "startTime", lhsStartTime), LocatorUtils.property(thatLocator, "startTime", rhsStartTime), lhsStartTime, rhsStartTime)) {
                return false;
            }
        }
        {
            String lhsStatus;
            lhsStatus = this.getStatus();
            String rhsStatus;
            rhsStatus = that.getStatus();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "status", lhsStatus), LocatorUtils.property(thatLocator, "status", rhsStatus), lhsStatus, rhsStatus)) {
                return false;
            }
        }
        {
            boolean lhsUsedForScale;
            lhsUsedForScale = ((this.usedForScale!= null)?this.isUsedForScale():false);
            boolean rhsUsedForScale;
            rhsUsedForScale = ((that.usedForScale!= null)?that.isUsedForScale():false);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "usedForScale", lhsUsedForScale), LocatorUtils.property(thatLocator, "usedForScale", rhsUsedForScale), lhsUsedForScale, rhsUsedForScale)) {
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
            String theId;
            theId = this.getId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "id", theId), currentHashCode, theId);
        }
        {
            XMLGregorianCalendar theStartTime;
            theStartTime = this.getStartTime();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "startTime", theStartTime), currentHashCode, theStartTime);
        }
        {
            String theStatus;
            theStatus = this.getStatus();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "status", theStatus), currentHashCode, theStatus);
        }
        {
            boolean theUsedForScale;
            theUsedForScale = ((this.usedForScale!= null)?this.isUsedForScale():false);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "usedForScale", theUsedForScale), currentHashCode, theUsedForScale);
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
        if (draftCopy instanceof Instance) {
            final Instance copy = ((Instance) draftCopy);
            if (this.id!= null) {
                String sourceId;
                sourceId = this.getId();
                String copyId = ((String) strategy.copy(LocatorUtils.property(locator, "id", sourceId), sourceId));
                copy.setId(copyId);
            } else {
                copy.id = null;
            }
            if (this.startTime!= null) {
                XMLGregorianCalendar sourceStartTime;
                sourceStartTime = this.getStartTime();
                XMLGregorianCalendar copyStartTime = ((XMLGregorianCalendar) strategy.copy(LocatorUtils.property(locator, "startTime", sourceStartTime), sourceStartTime));
                copy.setStartTime(copyStartTime);
            } else {
                copy.startTime = null;
            }
            if (this.status!= null) {
                String sourceStatus;
                sourceStatus = this.getStatus();
                String copyStatus = ((String) strategy.copy(LocatorUtils.property(locator, "status", sourceStatus), sourceStatus));
                copy.setStatus(copyStatus);
            } else {
                copy.status = null;
            }
            if (this.usedForScale!= null) {
                boolean sourceUsedForScale;
                sourceUsedForScale = ((this.usedForScale!= null)?this.isUsedForScale():false);
                boolean copyUsedForScale = strategy.copy(LocatorUtils.property(locator, "usedForScale", sourceUsedForScale), sourceUsedForScale);
                copy.setUsedForScale(copyUsedForScale);
            } else {
                copy.usedForScale = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new Instance();
    }

    public void mergeFrom(Object left, Object right) {
        final MergeStrategy strategy = JAXBMergeStrategy.INSTANCE;
        mergeFrom(null, null, left, right, strategy);
    }

    public void mergeFrom(ObjectLocator leftLocator, ObjectLocator rightLocator, Object left, Object right, MergeStrategy strategy) {
        if (right instanceof Instance) {
            final Instance target = this;
            final Instance leftObject = ((Instance) left);
            final Instance rightObject = ((Instance) right);
            {
                String lhsId;
                lhsId = leftObject.getId();
                String rhsId;
                rhsId = rightObject.getId();
                String mergedId = ((String) strategy.merge(LocatorUtils.property(leftLocator, "id", lhsId), LocatorUtils.property(rightLocator, "id", rhsId), lhsId, rhsId));
                target.setId(mergedId);
            }
            {
                XMLGregorianCalendar lhsStartTime;
                lhsStartTime = leftObject.getStartTime();
                XMLGregorianCalendar rhsStartTime;
                rhsStartTime = rightObject.getStartTime();
                XMLGregorianCalendar mergedStartTime = ((XMLGregorianCalendar) strategy.merge(LocatorUtils.property(leftLocator, "startTime", lhsStartTime), LocatorUtils.property(rightLocator, "startTime", rhsStartTime), lhsStartTime, rhsStartTime));
                target.setStartTime(mergedStartTime);
            }
            {
                String lhsStatus;
                lhsStatus = leftObject.getStatus();
                String rhsStatus;
                rhsStatus = rightObject.getStatus();
                String mergedStatus = ((String) strategy.merge(LocatorUtils.property(leftLocator, "status", lhsStatus), LocatorUtils.property(rightLocator, "status", rhsStatus), lhsStatus, rhsStatus));
                target.setStatus(mergedStatus);
            }
            {
                boolean lhsUsedForScale;
                lhsUsedForScale = ((leftObject.usedForScale!= null)?leftObject.isUsedForScale():false);
                boolean rhsUsedForScale;
                rhsUsedForScale = ((rightObject.usedForScale!= null)?rightObject.isUsedForScale():false);
                boolean mergedUsedForScale = ((boolean) strategy.merge(LocatorUtils.property(leftLocator, "usedForScale", lhsUsedForScale), LocatorUtils.property(rightLocator, "usedForScale", rhsUsedForScale), lhsUsedForScale, rhsUsedForScale));
                target.setUsedForScale(mergedUsedForScale);
            }
        }
    }

}
