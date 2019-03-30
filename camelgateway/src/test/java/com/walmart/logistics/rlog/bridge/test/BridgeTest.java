package com.walmart.logistics.rlog.bridge.test;

import javax.inject.Inject;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.model.ModelCamelContext;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.walmart.logistics.rlog.bridge.test.base.AbstractBaseTest;
import com.walmart.logistics.rlog.bridge.test.constants.ESBTestConstant;
import com.walmart.logistics.rlog.bridge.test.vo.RouteTestVO;

public class BridgeTest extends AbstractBaseTest{
	
	private static final Logger LOG = LoggerFactory.getLogger(BridgeTest.class);
	
	@Inject
	private ModelCamelContext bridgeContext;
	
	@Produce(uri = ESBTestConstant.MOCK_FROM_URI, context = "bridgeContext")
	protected ProducerTemplate bridgeContextTemplate;
	
	@Value("${store_source}")
	private String storeFromURI;
	
	@Value("${store_destination}")
	private String storeToURI;
	
	@Value("${storeFeedName}")
	private String storeFeedName;

	@Value("${relo_source}")
	private String reloFromURI;
	
	@Value("${relo_destination}")
	private String reloToURI;
	
	@Value("${reloFeedName}")
	private String reloFeedName;
	
	@Value("${vendor_source}")
	private String vendorFromURI;
	
	@Value("${vendor_destination}")
	private String vendorToURI;
	
	@Value("${vendorFeedName}")
	private String vendorFeedName;
	
	/**
	 * Test the Store route sends the message from
	 * source to destination
	 */
	@Test
	public void testStoreRoute() {
		try {
			boolean isErrorhandle = false;
			super.testApplRoutes(bridgeContext, bridgeContextTemplate,
					populateStoreRouteTestVo(), isErrorhandle);
		} catch (Exception e) {
			LOG.error(" testStoreRoute Error ", e);
			Assert.assertFalse(true);
		}
	}
	/**
	 *  Test the error handling of Store route sends the message from
	 * source to destination
	 */
	@Test
	public void testStoreRouteErrorHandle() {
		try {
			boolean isErrorhandle = true;
			super.testApplRoutes(bridgeContext, bridgeContextTemplate,
					populateStoreRouteTestVo(), isErrorhandle);
		} catch (Exception e) {
			LOG.error(" testStoreRouteErrorHandle Error ", e);
			Assert.assertFalse(true);
		}
	}
	/**
	 * Prepare RouteTestVo for InboundLog Channel
	 * 
	 * @return RouteTestVO
	 */
	private RouteTestVO populateStoreRouteTestVo() {
		final RouteTestVO routeTestVO=new RouteTestVO();
		routeTestVO.setContextName(bridgeContext.getName());
		routeTestVO.setFeedName(storeFeedName);
		routeTestVO.setFromURI(storeFromURI);
		routeTestVO.setMessage(" Bridge sends message to ESB");
		routeTestVO.setRouteId(ESBTestConstant.STORE_ROUTE_ID);
		routeTestVO.setToURI(storeToURI);
		return routeTestVO;
	}

	/**
	 * Test the Relocation route sends the message from
	 * source to destination
	 */
	@Test
	public void testRelocationRoute() {
		try {
			boolean isErrorhandle = false;
			super.testApplRoutes(bridgeContext, bridgeContextTemplate,
					populateRelocationRouteTestVo(), isErrorhandle);
		} catch (Exception e) {
			LOG.error(" testRelocationRoute Error ", e);
			Assert.assertFalse(true);
		}
	}
	/**
	 *  Test the error handling of Relocation route sends the message from
	 * source to destination
	 */
	@Test
	public void testRelocationRouteErrorHandle() {
		try {
			boolean isErrorhandle = true;
			super.testApplRoutes(bridgeContext, bridgeContextTemplate,
					populateRelocationRouteTestVo(), isErrorhandle);
		} catch (Exception e) {
			LOG.error(" testRelocationRouteErrorHandle Error ", e);
			Assert.assertFalse(true);
		}
	}
	/**
	 * Prepare RouteTestVo for InboundLog Channel
	 * 
	 * @return RouteTestVO
	 */
	private RouteTestVO populateRelocationRouteTestVo() {
		final RouteTestVO routeTestVO=new RouteTestVO();
		routeTestVO.setContextName(bridgeContext.getName());
		routeTestVO.setFeedName(reloFeedName);
		routeTestVO.setFromURI(reloFromURI);
		routeTestVO.setMessage(" Bridge sends message to ESB");
		routeTestVO.setRouteId(ESBTestConstant.STORE_ROUTE_ID);
		routeTestVO.setToURI(reloToURI);
		return routeTestVO;
	}
	
	/**
	 * Test the Vendor route sends the message from
	 * source to destination
	 */
	@Test
	public void testVendorRoute() {
		try {
			boolean isErrorhandle = false;
			super.testApplRoutes(bridgeContext, bridgeContextTemplate,
					populateVendorRouteTestVo(), isErrorhandle);
		} catch (Exception e) {
			LOG.error(" testVendorRoute Error ", e);
			Assert.assertFalse(true);
		}
	}
	/**
	 *  Test the error handling of Vendor route sends the message from
	 * source to destination
	 */
	@Test
	public void testVendorRouteErrorHandle() {
		try {
			boolean isErrorhandle = true;
			super.testApplRoutes(bridgeContext, bridgeContextTemplate,
					populateVendorRouteTestVo(), isErrorhandle);
		} catch (Exception e) {
			LOG.error(" testVendorRouteErrorHandle Error ", e);
			Assert.assertFalse(true);
		}
	}
	/**
	 * Prepare RouteTestVo for InboundLog Channel
	 * 
	 * @return RouteTestVO
	 */
	private RouteTestVO populateVendorRouteTestVo() {
		final RouteTestVO routeTestVO=new RouteTestVO();
		routeTestVO.setContextName(bridgeContext.getName());
		routeTestVO.setFeedName(vendorFeedName);
		routeTestVO.setFromURI(vendorFromURI);
		routeTestVO.setMessage(" Bridge sends message to ESB");
		routeTestVO.setRouteId(ESBTestConstant.VENDOR_ROUTE_ID);
		routeTestVO.setToURI(vendorToURI);
		return routeTestVO;
	}
}
