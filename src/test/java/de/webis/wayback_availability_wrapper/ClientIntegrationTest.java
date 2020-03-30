package de.webis.wayback_availability_wrapper;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	public void approveExampleFromWebsiteWithSparklineApi() {
		String url = "example.com";
		List<Integer> expected2009 = Arrays.asList(30, 49, 64, 49, 60, 47, 43, 34, 21, 37, 44, 45);
		List<Integer> expected2012 = Arrays.asList(396, 557, 485, 350, 500, 501, 548, 485, 546, 514, 472, 534);
		List<Integer> expected2015 = Arrays.asList(1965, 419, 2019, 1961, 769, 1988, 227, 1318, 295, 1239, 2794, 2130);
		List<Integer> expected2018 = Arrays.asList(3866, 4176, 6048, 5871, 5556, 5401, 3196, 7470, 3936, 3838, 4322, 4451);
		
		Map<String, List<Integer>> actual = extractYears(url);
		
		Assert.assertEquals(expected2009, actual.get("2009"));
		Assert.assertEquals(expected2012, actual.get("2012"));
		Assert.assertEquals(expected2015, actual.get("2015"));
		Assert.assertEquals(expected2018, actual.get("2018"));
	}
	
	@Test
	public void approveExampleTweetWithSparklineApi() {
		String url = "https://twitter.com/waybackmachine/status/1239404250020503553";
		Map<String, List<Integer>> expected = new HashMap<>();
		expected.put("2020", Arrays.asList(0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		Map<String, List<Integer>> actual = extractYears(url);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void approveExampleTweetNotAvailableWithSparklineApi() {
		String url = "https://twitter.com/waybackmachine/status/123940425002050355s3";
		Map<String, List<Integer>> expected = new HashMap<>();
		Map<String, List<Integer>> actual = extractYears(url);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void approveExampleTweetNotAvailable() {
		Date date = RequestUrlTest.date("01.02.1999 03:04:05");
		String url = "https://twitter.com/waybackmachine/status/123940425002050355s3";
		
		String actualTargetUrl = WaybackAvailabilityClient.targetUrl(url, date);
		Assert.assertNull(actualTargetUrl);
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, List<Integer>> extractYears(String url) {
		try {
			String ret = WaybackAvailabilityClient.sparklineRequest(url);
			Map<String, Object> parsedRet = new ObjectMapper().readValue(ret, new TypeReference<Map<String, Object>>() {});
			
			return (Map<String, List<Integer>>) parsedRet.get("years");
		} catch (Exception e) {
			return new HashMap<>();
		}
	}
}
