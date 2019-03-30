package com.walmart.logistics.rlog.bridge.test.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.camel.Endpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.SetHeaderDefinition;

public class ESBTestUtil {

	/**
	 * Method to check Camel context has end points
	 * 
	 */
	public static boolean hasEndPoint(final ModelCamelContext context,
			final String endPointName) {
		boolean found = false;
		final Collection<Endpoint> endpointcoll = context.getEndpoints();
		final Iterator<Endpoint> iter = endpointcoll.iterator();
		while (iter.hasNext()) {
			final Endpoint endPoint = iter.next();
			if (endPointName.equals(endPoint.getEndpointUri())) {
				found = true;
				break;
			}
		}
		return found;
	}

	/**
	 * Get the header value from route using RouteDefinition
	 * 
	 */
	public static String getHeaderValueFromRoute(
			final RouteDefinition routeDef, final String headerName) {
		String headerValue = null;
		String proDefHeaderKey = "setHeader";
		List<ProcessorDefinition<?>> outDefList = routeDef.getOutputs();
		for (ProcessorDefinition<?> outDef : outDefList) {
			if (proDefHeaderKey.equals(outDef.getShortName())) {
				final SetHeaderDefinition headerDef = (SetHeaderDefinition) outDef;
				if (headerName.equals(headerDef.getHeaderName())) {
					headerValue = headerDef.getExpression()
							.getExpressionValue().toString();
					break;
				}
			}
		}
		System.out.println("Route =" + routeDef.getLabel() + " headerName="
				+ headerName + " HeaderValue=" + headerValue);
		return headerValue;
	}
}
