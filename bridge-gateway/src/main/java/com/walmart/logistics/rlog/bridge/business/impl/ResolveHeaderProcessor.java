/**
 * @(#)ResolveHeaderProcessor.java
 *
 * Copyright 2014 Wal-Mart Stores, Inc. All rights reserved.
 * Wal-Mart PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.walmart.logistics.rlog.bridge.business.impl;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.util.StringUtils;

import com.walmart.logistics.rlog.bridge.constant.TransformerConstant;
import com.walmart.logistics.rlog.bridge.exception.InvalidFileFormatException;

/**
 * This is an implementation class for <code>Processor</code> which resolves and
 * separates the header and file data
 * 
 * <li>process()</li>
 * 
 * @author cbabur1
 * @version 1.0
 * 
 */
public class ResolveHeaderProcessor implements Processor {

	/**
	 * Method which resolves and separates the header data and the file data.
	 * And sets the header data into Header Object and file data into Body
	 * object
	 * 
	 * @param exchange
	 * @throws Exception
	 */
	public void process(Exchange exchange) throws Exception {

		String fileData = exchange.getIn().getBody(String.class);
		String inputFileName = exchange.getIn().getHeader("CamelFileNameOnly",
				String.class);
		/* Validate that the input file format */
		if (!fileData.contains(TransformerConstant.PIPE_DELIMITER)
				&& inputFileName != null && !inputFileName.contains(".txt")) {
			throw new InvalidFileFormatException();
		}
		if (fileData != null) {
			String rowData[] = StringUtils.delimitedListToStringArray(fileData,
					TransformerConstant.NEW_LINE_CHAR);
			if (rowData != null && rowData.length > 0) {
				/* Remove the header from file data */
				fileData = fileData.replace(rowData[0],
						TransformerConstant.EMPTY_STRING);
				fileData = fileData.replaceFirst(
						TransformerConstant.NEW_LINE_CHAR,
						TransformerConstant.EMPTY_STRING);
				/* Set the File Header Row to camel exchange */
				exchange.getOut().setHeader(TransformerConstant.FILE_HEADER,
						rowData[0]);
				fileData = fileData.replace(TransformerConstant.CARRIGE_RETURN,
						TransformerConstant.EMPTY_STRING);
				/* Set the file data without header in the camel exchange */
				exchange.getOut().setBody(fileData);
			}
		}

	}
}
