/**
 * @(#)PipeFileToXMLTransformer.java
 *
 * Copyright 2014 Wal-Mart Stores, Inc. All rights reserved.
 * Wal-Mart PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.walmart.logistics.rlog.bridge.business.impl;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.walmart.logistics.rlog.bridge.constant.TransformerConstant;

/**
 * This is an implementation class for <code>Processor</code> which performs
 * conversion of pipe delimited flat file row to XML file
 * 
 * <li>process()</li>
 * 
 * @author cbabur1
 * @version 1.0
 * 
 */
public class PipeFileToXMLTransformer implements Processor {

	/**
	 * Method to convert each row record of the input pipe delimited file into
	 * each XML
	 * 
	 * @param exchange
	 * @throws Exception
	 */
	public void process(Exchange exchange) throws Exception {

		int fieldCount = 0;
		DocumentBuilderFactory domFactory = null;
		DocumentBuilder domBuilder = null;
		domFactory = DocumentBuilderFactory.newInstance();
		domBuilder = domFactory.newDocumentBuilder();

		String[] tagNames = null;
		/* Get the File Header Row from camel exchange */
		String headerData = (String) exchange.getIn().getHeader(
				TransformerConstant.FILE_HEADER);
		String rowData = exchange.getIn().getBody(String.class);
		Document newDoc = domBuilder.newDocument();
		/*
		 * Get the feedType which will be used as the root tag from camel
		 * exchange
		 */
		String feed = exchange.getIn().getHeader("feedType", String.class);
		/* Create the parent tag */
		Element rootElement = newDoc.createElement(feed);
		newDoc.appendChild(rootElement);
		/* Get the tag names from File Header */
		if (headerData != null) {
			tagNames = StringUtils.delimitedListToStringArray(headerData,
					TransformerConstant.PIPE_DELIMITER);
			fieldCount = tagNames.length;
		}
		if (rowData != null) {
			/* Get the tag values from File Header */
			String tagValues[] = StringUtils.delimitedListToStringArray(
					rowData, TransformerConstant.PIPE_DELIMITER);
			if (fieldCount > 0) {
				String tagValue = "";
				for (int i = 0; i < fieldCount; i++) {
					try {
						tagValue = String.valueOf(tagValues[i]);
						/* Create the child tag */
						Element curElement = newDoc.createElement(tagNames[i]
								.replaceAll(TransformerConstant.SPACE,
										TransformerConstant.EMPTY_STRING));
						/* Set the value to the child tag if it is not null */
						if (tagValue != null && !tagValue.isEmpty()) {
							curElement.appendChild(newDoc
									.createTextNode(tagValue.replaceAll(
											TransformerConstant.CARRIGE_RETURN,
											TransformerConstant.EMPTY_STRING)));
						}
						/* Append the child tag to the parent */
						rootElement.appendChild(curElement);
					} catch (Exception exp) {
						throw new RuntimeException(
								"Exception while create XML : " + exp + " :: "
										+ exp.getMessage());

					}
				}
			}

		}
		/* Generate the XML using TransformerFactory */
		TransformerFactory tranFactory = TransformerFactory.newInstance();
		Transformer aTransformer = tranFactory.newTransformer();
		Source src = new DOMSource(newDoc);
		StringWriter outWriter = new StringWriter();
		StreamResult result = new StreamResult(outWriter);
		aTransformer.transform(src, result);
		StringBuffer sb = outWriter.getBuffer();
		/*Set the XML in the exchange object */
		exchange.getIn().setBody(sb.toString());
	}
}
