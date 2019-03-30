package com.walmart.logistics.rlog.bridge.test.constants;

/**
 * ESBTestConstant is having the constants for all classes
 * 
 * @author Cognizant
 */
public final class ESBTestConstant {
	private ESBTestConstant(){
		
	}
	public static final String TEST_CONTEXT_PATH = "classpath:test-context.xml";
	

	public static final String MOCK_SCH_TO_URI = "mock:result";
	
	
	public static final String MOCK_FROM_URI = "direct:esb-test-uri";
	public static final String MOCK_TO_URI = "mock:esb-test-to";
	
	public static final String ERROR_HANDLE_CONTEXT = "errorhandlingContext";
	public static final String ERROR_HANDLE_ROUTE = "errorHandleRoute";
	public static final String ERROR_HANDLE_PRO_ID = "errorHandleProcessorId";
	public static final String ERROR_FROM_URI = "error-activemq:msg";


	public static final String FEED_NAME = "feedType";


	public static final String STORE_ROUTE_ID = "storeFeedRoute";
	public static final String RELO_ROUTE_ID = "relocationFeedRoute";
	public static final String VENDOR_ROUTE_ID = "vendorFeedRoute";
	public static final String RPT_ROUTE_ID = "RPTFeedRoute";
	public static final String RECALL_ROUTE_ID = "recallFeedRoute";

}
