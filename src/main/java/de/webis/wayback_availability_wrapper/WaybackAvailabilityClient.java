package de.webis.wayback_availability_wrapper;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WaybackAvailabilityClient {

	public static String requestUrl(String url, Date date) {
		return "http://archive.org/wayback/available?url=" + url + "&timestamp=" + format(date);
	}

	private static String format(Date date) {
		return new SimpleDateFormat("yyyyMMddhhmmss").format(date);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String targetUrl(String url, Date date) {
		try {
			Map<String, Object> parsed = new ObjectMapper().readValue(availabilityRequest(url, date), new TypeReference<Map<String, Object>>() {});
			if(parsed.containsKey("archived_snapshots")) {
				Map<String, Map<String, String>> snapshots = (Map) parsed.get("archived_snapshots");
				
				if(snapshots.containsKey("closest")) {
					return snapshots.get("closest").get("url");
				}
			}
			
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String availabilityRequest(String url, Date date) {
		return availabilityRequestOrFail(url, date);
	}
	
	private static String availabilityRequestOrFail(String url, Date date) {
		try {
			return IOUtils.toString(new URL(requestUrl(url, date)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
