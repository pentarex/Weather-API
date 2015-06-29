package com.pentarex.weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;
import com.pentarex.weather.beans.GeoBean;

public class Utils {
	private static String getIPAddress() {
		String ipAddress = "";
		try {
			URL whatismyip = new URL("http://icanhazip.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));
			ipAddress = in.readLine();
			System.out.println(ipAddress);
		} catch (Exception e) {
			// TODO log4j
		}
		return ipAddress;
	}

	public static GeoBean getWeatherInfo() {
		GeoBean gb = new GeoBean();
		LookupService lookupService = null;
		File dbfile = new File(Utils.class.getResource(
				"/com/pentarex/weather/resources/GeoLiteCity.dat").getFile());
		try {
			lookupService = new LookupService(dbfile,
					LookupService.GEOIP_STANDARD);
		} catch (Exception e) {

		}
		if(lookupService != null){
			Location location = lookupService.getLocation(getIPAddress());
			// Populate region. Note that regionName is a MaxMind class, not an
			// instance variable
			if (location != null) {
				gb.setCity(location.city);
				gb.setCountry(location.countryName);
			}
		}
		return gb;
	}
}
