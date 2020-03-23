package de.webis.wayback_availability_wrapper;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class ClientIntegrationTest {
	@Test
	public void approveExampleTweet() {
		Date date = RequestUrlTest.date("01.02.1999 03:04:05");
		String url = "https://twitter.com/waybackmachine/status/1239404250020503553";
		String expectedTargetUrl = "http://web.archive.org/web/20200316041348/https://twitter.com/waybackmachine/status/1239404250020503553";
		String actualTargetUrl = WaybackAvailabilityClient.targetUrl(url, date);

		Assert.assertEquals(expectedTargetUrl, actualTargetUrl);
	}
	
	@Test
	public void approveExampleFromWebsite() {
		Date date = RequestUrlTest.date("01.01.2006 00:00:00");
		String url = "example.com";
		String expectedTargetUrl = "http://web.archive.org/web/20060101064348/http://www.example.com:80/";
		String actualTargetUrl = WaybackAvailabilityClient.targetUrl(url, date);

		Assert.assertEquals(expectedTargetUrl, actualTargetUrl);
	}
	
	@Test
	public void approveExampleTweetNotAvailable() {
		Date date = RequestUrlTest.date("01.02.1999 03:04:05");
		String url = "https://twitter.com/waybackmachine/status/123940425002050355s3";
		
		String actualTargetUrl = WaybackAvailabilityClient.targetUrl(url, date);
		Assert.assertNull(actualTargetUrl);
	}
}
