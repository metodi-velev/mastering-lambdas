
package com.mastering.lambdas.misc.soap.geoip;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetGeoIPContextResult" type="{http://www.webservicex.net/}GeoIP" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getGeoIPContextResult"
})
@XmlRootElement(name = "GetGeoIPContextResponse")
public class GetGeoIPContextResponse {

    @XmlElement(name = "GetGeoIPContextResult")
    protected GeoIP getGeoIPContextResult;

    /**
     * Gets the value of the getGeoIPContextResult property.
     * 
     * @return
     *     possible object is
     *     {@link GeoIP }
     *     
     */
    public GeoIP getGetGeoIPContextResult() {
        return getGeoIPContextResult;
    }

    /**
     * Sets the value of the getGeoIPContextResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeoIP }
     *     
     */
    public void setGetGeoIPContextResult(GeoIP value) {
        this.getGeoIPContextResult = value;
    }

}
