package com.pentarex.weather;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.pentarex.weather.beans.GeoBean;
import com.pentarex.weather.beans.WeatherBean;


public class WeatherInfo {
	private static final String URL = "http://api.openweathermap.org/data/2.5/";
	private static final String OPEN_WEATHER_MAP_API_KEY = "2a66510f5673ca825475a3bd5280dcee";
	private static final String ICON_URL = "http://openweathermap.org/img/w/";
	
	public WeatherBean getDailyWeather(){
		GeoBean gb = Utils.getWeatherInfo();
		String urlParam = "";
		if(gb.getCity() == null){
			urlParam = "weather?q=," + gb.getCountry() + "&units=imperial&mode=xml&APPID=" + OPEN_WEATHER_MAP_API_KEY;
		} else urlParam = "weather?q=" + gb.getCity() + "," + gb.getCountry() + "&units=imperial&mode=xml&APPID=" + OPEN_WEATHER_MAP_API_KEY;
		Document doc = call(URL + urlParam);
		WeatherBean weatherBean = new WeatherBean();
		if(doc == null) return null;
		try{
            NodeList items = doc.getElementsByTagName("current");
            for(int i = 0; i < items.getLength(); i++){
            	NodeList childItems = items.item(i).getChildNodes();
                for(int j = 0; j < childItems.getLength(); j++){
                    switch (childItems.item(j).getNodeName()) {
					case "temperature":
						weatherBean.setTemp(celsiusConvertion(Double.parseDouble(childItems.item(j).getAttributes().getNamedItem("value").getNodeValue())));
						weatherBean.setTempMin(celsiusConvertion(Double.parseDouble(childItems.item(j).getAttributes().getNamedItem("min").getNodeValue())));
						weatherBean.setTempMax(celsiusConvertion(Double.parseDouble(childItems.item(j).getAttributes().getNamedItem("max").getNodeValue())));
						break;
					case "city":
						weatherBean.setCity(childItems.item(j).getAttributes().getNamedItem("name").getNodeValue());
						break;
					case "weather":
						weatherBean.setValue(childItems.item(j).getAttributes().getNamedItem("value").getNodeValue());
						weatherBean.setIcon(ICON_URL + childItems.item(j).getAttributes().getNamedItem("icon").getNodeValue() + ".png");
						break;
					default:
						break;
					}
                }
                
            }
        } catch(Exception e){
            e.printStackTrace();
        }
		return weatherBean;
	}
	
	private Document call(String url){
		System.out.println(url);
		Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
		try{
            builder = factory.newDocumentBuilder();
            doc = builder.parse(url);
		} catch (Exception e){
			
		}
		return doc;
	}
	
	private int celsiusConvertion(double fahrenheit){
		int celsius = (int) ((5.0/9.0) * (fahrenheit - 32));
		return celsius;
	}
}
