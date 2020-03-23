package de.webis.wayback_availability_wrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class RequestUrlTest {
	@Test
	public void testExampleFromWebsite() {
		Date date = date("01.01.2006 00:00:00");
		String url = "example.com";
		
		String expected = "http://archive.org/wayback/available?url=example.com&timestamp=20060101120000";
		String actual = WaybackAvailabilityClient.requestUrl(url, date);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testExampleFromWebsiteWithDifferentDate() {
		Date date = date("12.12.2012 12:12:12");
		String url = "example.com";
		
		String expected = "http://archive.org/wayback/available?url=example.com&timestamp=20121212121212";
		String actual = WaybackAvailabilityClient.requestUrl(url, date);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testExampleFromWebsiteWithGoodTestDate() {
		Date date = date("01.02.1999 03:04:05");
		String url = "example.com";
		
		String expected = "http://archive.org/wayback/available?url=example.com&timestamp=19990201030405";
		String actual = WaybackAvailabilityClient.requestUrl(url, date);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testTweet() {
		Date date = date("01.02.1999 03:04:05");
		String url = "https://twitter.com/waybackmachine/status/1239404250020503553";
		
		String expected = "http://archive.org/wayback/available?url=https://twitter.com/waybackmachine/status/1239404250020503553&timestamp=19990201030405";
		String actual = WaybackAvailabilityClient.requestUrl(url, date);

		Assert.assertEquals(expected, actual);
	}
	
	public static final Date date(String date) {
		try {
			return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
