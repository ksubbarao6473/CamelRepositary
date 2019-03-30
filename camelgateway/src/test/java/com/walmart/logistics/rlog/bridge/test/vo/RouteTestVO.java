package com.walmart.logistics.rlog.bridge.test.vo;

import com.walmart.logistics.rlog.bridge.test.constants.ESBTestConstant;

import java.io.Serializable;


/**
 * RouteTestVO is a class used to keep all message details
 * 
 * @author Cognizant
 * 
 */
public class RouteTestVO implements Serializable {

	private static final long serialVersionUID = 8024991472287559553L;
	private String contextName;
	private String routeId;
	private String toURI;
	private String fromURI;
	private String feedName;
	private String message;

	public String getMockFromURI() {
		return ESBTestConstant.MOCK_FROM_URI;
	}

	public String getMockToURI() {
		return ESBTestConstant.MOCK_TO_URI;
	}

	/**
	 * @return the contextName
	 */
	public String getContextName() {
		return contextName;
	}

	/**
	 * @param contextName the contextName to set
	 */
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	/**
	 * @return the routeId
	 */
	public String getRouteId() {
		return routeId;
	}

	/**
	 * @param routeId the routeId to set
	 */
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	/**
	 * @return the toURI
	 */
	public String getToURI() {
		return toURI;
	}

	/**
	 * @param toURI the toURI to set
	 */
	public void setToURI(String toURI) {
		this.toURI = toURI;
	}

	/**
	 * @return the fromURI
	 */
	public String getFromURI() {
		return fromURI;
	}

	/**
	 * @param fromURI the fromURI to set
	 */
	public void setFromURI(String fromURI) {
		this.fromURI = fromURI;
	}

	/**
	 * @return the feedName
	 */
	public String getFeedName() {
		return feedName;
	}

	/**
	 * @param feedName the feedName to set
	 */
	public void setFeedName(String feedName) {
		this.feedName = feedName;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	




}
