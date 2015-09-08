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
import javax.xml.bind.annotation.XmlRootElement;
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
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="container" type="{}container" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="speedNorm" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="timestepDuration" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="optimizationWindowsLenght" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "container"
})
@XmlRootElement(name = "containers")
public class Containers
    implements Cloneable, CopyTo, Equals, HashCode, MergeFrom, ToString
{

    @XmlElement(required = true)
    protected List<Container> container;
    @XmlAttribute(name = "speedNorm", required = true)
    protected double speedNorm;
    @XmlAttribute(name = "timestepDuration", required = true)
    protected int timestepDuration;
    @XmlAttribute(name = "optimizationWindowsLenght", required = true)
    protected int optimizationWindowsLenght;

    /**
     * Gets the value of the container property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the container property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContainer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Container }
     * 
     * 
     */
    public List<Container> getContainer() {
        if (container == null) {
            container = new ArrayList<Container>();
        }
        return this.container;
    }

    /**
     * Recupera il valore della proprietà speedNorm.
     * 
     */
    public double getSpeedNorm() {
        return speedNorm;
    }

    /**
     * Imposta il valore della proprietà speedNorm.
     * 
     */
    public void setSpeedNorm(double value) {
        this.speedNorm = value;
    }

    /**
     * Recupera il valore della proprietà timestepDuration.
     * 
     */
    public int getTimestepDuration() {
        return timestepDuration;
    }

    /**
     * Imposta il valore della proprietà timestepDuration.
     * 
     */
    public void setTimestepDuration(int value) {
        this.timestepDuration = value;
    }

    /**
     * Recupera il valore della proprietà optimizationWindowsLenght.
     * 
     */
    public int getOptimizationWindowsLenght() {
        return optimizationWindowsLenght;
    }

    /**
     * Imposta il valore della proprietà optimizationWindowsLenght.
     * 
     */
    public void setOptimizationWindowsLenght(int value) {
        this.optimizationWindowsLenght = value;
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
            List<Container> theContainer;
            theContainer = (((this.container!= null)&&(!this.container.isEmpty()))?this.getContainer():null);
            strategy.appendField(locator, this, "container", buffer, theContainer);
        }
        {
            double theSpeedNorm;
            theSpeedNorm = (true?this.getSpeedNorm(): 0.0D);
            strategy.appendField(locator, this, "speedNorm", buffer, theSpeedNorm);
        }
        {
            int theTimestepDuration;
            theTimestepDuration = (true?this.getTimestepDuration(): 0);
            strategy.appendField(locator, this, "timestepDuration", buffer, theTimestepDuration);
        }
        {
            int theOptimizationWindowsLenght;
            theOptimizationWindowsLenght = (true?this.getOptimizationWindowsLenght(): 0);
            strategy.appendField(locator, this, "optimizationWindowsLenght", buffer, theOptimizationWindowsLenght);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Containers)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Containers that = ((Containers) object);
        {
            List<Container> lhsContainer;
            lhsContainer = (((this.container!= null)&&(!this.container.isEmpty()))?this.getContainer():null);
            List<Container> rhsContainer;
            rhsContainer = (((that.container!= null)&&(!that.container.isEmpty()))?that.getContainer():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "container", lhsContainer), LocatorUtils.property(thatLocator, "container", rhsContainer), lhsContainer, rhsContainer)) {
                return false;
            }
        }
        {
            double lhsSpeedNorm;
            lhsSpeedNorm = (true?this.getSpeedNorm(): 0.0D);
            double rhsSpeedNorm;
            rhsSpeedNorm = (true?that.getSpeedNorm(): 0.0D);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "speedNorm", lhsSpeedNorm), LocatorUtils.property(thatLocator, "speedNorm", rhsSpeedNorm), lhsSpeedNorm, rhsSpeedNorm)) {
                return false;
            }
        }
        {
            int lhsTimestepDuration;
            lhsTimestepDuration = (true?this.getTimestepDuration(): 0);
            int rhsTimestepDuration;
            rhsTimestepDuration = (true?that.getTimestepDuration(): 0);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "timestepDuration", lhsTimestepDuration), LocatorUtils.property(thatLocator, "timestepDuration", rhsTimestepDuration), lhsTimestepDuration, rhsTimestepDuration)) {
                return false;
            }
        }
        {
            int lhsOptimizationWindowsLenght;
            lhsOptimizationWindowsLenght = (true?this.getOptimizationWindowsLenght(): 0);
            int rhsOptimizationWindowsLenght;
            rhsOptimizationWindowsLenght = (true?that.getOptimizationWindowsLenght(): 0);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "optimizationWindowsLenght", lhsOptimizationWindowsLenght), LocatorUtils.property(thatLocator, "optimizationWindowsLenght", rhsOptimizationWindowsLenght), lhsOptimizationWindowsLenght, rhsOptimizationWindowsLenght)) {
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
            List<Container> theContainer;
            theContainer = (((this.container!= null)&&(!this.container.isEmpty()))?this.getContainer():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "container", theContainer), currentHashCode, theContainer);
        }
        {
            double theSpeedNorm;
            theSpeedNorm = (true?this.getSpeedNorm(): 0.0D);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "speedNorm", theSpeedNorm), currentHashCode, theSpeedNorm);
        }
        {
            int theTimestepDuration;
            theTimestepDuration = (true?this.getTimestepDuration(): 0);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "timestepDuration", theTimestepDuration), currentHashCode, theTimestepDuration);
        }
        {
            int theOptimizationWindowsLenght;
            theOptimizationWindowsLenght = (true?this.getOptimizationWindowsLenght(): 0);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "optimizationWindowsLenght", theOptimizationWindowsLenght), currentHashCode, theOptimizationWindowsLenght);
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
        if (draftCopy instanceof Containers) {
            final Containers copy = ((Containers) draftCopy);
            if ((this.container!= null)&&(!this.container.isEmpty())) {
                List<Container> sourceContainer;
                sourceContainer = (((this.container!= null)&&(!this.container.isEmpty()))?this.getContainer():null);
                @SuppressWarnings("unchecked")
                List<Container> copyContainer = ((List<Container> ) strategy.copy(LocatorUtils.property(locator, "container", sourceContainer), sourceContainer));
                copy.container = null;
                if (copyContainer!= null) {
                    List<Container> uniqueContainerl = copy.getContainer();
                    uniqueContainerl.addAll(copyContainer);
                }
            } else {
                copy.container = null;
            }
            double sourceSpeedNorm;
            sourceSpeedNorm = (true?this.getSpeedNorm(): 0.0D);
            double copySpeedNorm = strategy.copy(LocatorUtils.property(locator, "speedNorm", sourceSpeedNorm), sourceSpeedNorm);
            copy.setSpeedNorm(copySpeedNorm);
            int sourceTimestepDuration;
            sourceTimestepDuration = (true?this.getTimestepDuration(): 0);
            int copyTimestepDuration = strategy.copy(LocatorUtils.property(locator, "timestepDuration", sourceTimestepDuration), sourceTimestepDuration);
            copy.setTimestepDuration(copyTimestepDuration);
            int sourceOptimizationWindowsLenght;
            sourceOptimizationWindowsLenght = (true?this.getOptimizationWindowsLenght(): 0);
            int copyOptimizationWindowsLenght = strategy.copy(LocatorUtils.property(locator, "optimizationWindowsLenght", sourceOptimizationWindowsLenght), sourceOptimizationWindowsLenght);
            copy.setOptimizationWindowsLenght(copyOptimizationWindowsLenght);
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new Containers();
    }

    public void mergeFrom(Object left, Object right) {
        final MergeStrategy strategy = JAXBMergeStrategy.INSTANCE;
        mergeFrom(null, null, left, right, strategy);
    }

    public void mergeFrom(ObjectLocator leftLocator, ObjectLocator rightLocator, Object left, Object right, MergeStrategy strategy) {
        if (right instanceof Containers) {
            final Containers target = this;
            final Containers leftObject = ((Containers) left);
            final Containers rightObject = ((Containers) right);
            {
                List<Container> lhsContainer;
                lhsContainer = (((leftObject.container!= null)&&(!leftObject.container.isEmpty()))?leftObject.getContainer():null);
                List<Container> rhsContainer;
                rhsContainer = (((rightObject.container!= null)&&(!rightObject.container.isEmpty()))?rightObject.getContainer():null);
                List<Container> mergedContainer = ((List<Container> ) strategy.merge(LocatorUtils.property(leftLocator, "container", lhsContainer), LocatorUtils.property(rightLocator, "container", rhsContainer), lhsContainer, rhsContainer));
                target.container = null;
                if (mergedContainer!= null) {
                    List<Container> uniqueContainerl = target.getContainer();
                    uniqueContainerl.addAll(mergedContainer);
                }
            }
            {
                double lhsSpeedNorm;
                lhsSpeedNorm = (true?leftObject.getSpeedNorm(): 0.0D);
                double rhsSpeedNorm;
                rhsSpeedNorm = (true?rightObject.getSpeedNorm(): 0.0D);
                double mergedSpeedNorm = ((double) strategy.merge(LocatorUtils.property(leftLocator, "speedNorm", lhsSpeedNorm), LocatorUtils.property(rightLocator, "speedNorm", rhsSpeedNorm), lhsSpeedNorm, rhsSpeedNorm));
                target.setSpeedNorm(mergedSpeedNorm);
            }
            {
                int lhsTimestepDuration;
                lhsTimestepDuration = (true?leftObject.getTimestepDuration(): 0);
                int rhsTimestepDuration;
                rhsTimestepDuration = (true?rightObject.getTimestepDuration(): 0);
                int mergedTimestepDuration = ((int) strategy.merge(LocatorUtils.property(leftLocator, "timestepDuration", lhsTimestepDuration), LocatorUtils.property(rightLocator, "timestepDuration", rhsTimestepDuration), lhsTimestepDuration, rhsTimestepDuration));
                target.setTimestepDuration(mergedTimestepDuration);
            }
            {
                int lhsOptimizationWindowsLenght;
                lhsOptimizationWindowsLenght = (true?leftObject.getOptimizationWindowsLenght(): 0);
                int rhsOptimizationWindowsLenght;
                rhsOptimizationWindowsLenght = (true?rightObject.getOptimizationWindowsLenght(): 0);
                int mergedOptimizationWindowsLenght = ((int) strategy.merge(LocatorUtils.property(leftLocator, "optimizationWindowsLenght", lhsOptimizationWindowsLenght), LocatorUtils.property(rightLocator, "optimizationWindowsLenght", rhsOptimizationWindowsLenght), lhsOptimizationWindowsLenght, rhsOptimizationWindowsLenght));
                target.setOptimizationWindowsLenght(mergedOptimizationWindowsLenght);
            }
        }
    }

}
