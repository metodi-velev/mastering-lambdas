package com.mastering.lambdas.misc.soap.geoip;

public class IPLocationFinder {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("You need to pass in one IP address");
        } else {
            String ipAddress = args[0];
            GeoIPService ipService = new GeoIPService();
            GeoIPServiceSoap geoIPServiceSoap = ipService.getGeoIPServiceSoap();
            GeoIP geoIP = geoIPServiceSoap.getGeoIP(ipAddress);
            System.out.println(geoIP.getCountryName());
        }

    }
}
