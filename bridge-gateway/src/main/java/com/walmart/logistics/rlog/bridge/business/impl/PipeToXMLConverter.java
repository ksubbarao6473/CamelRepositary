package com.walmart.logistics.rlog.bridge.business.impl;

import java.io.File;
import java.io.FileWriter;
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
import com.walmart.logistics.rlog.bridge.exception.InvalidFileFormatException;

public class PipeToXMLConverter implements Processor {
	public void process(Exchange exchange) throws Exception {
		String fileData = exchange.getIn().getBody(String.class);
		String inputFileName = exchange.getIn().getHeader("CamelFileNameOnly",
				String.class);
		String[] tagNames = null;
		int tagNameCount = 0;
		String fileNamePrefix = "D:/asn/split/ASN_IN_";
		String fileNameExtn = ".xml";

		/* Validate that the input file format */
		if (!fileData.contains(TransformerConstant.PIPE_DELIMITER)
				&& inputFileName != null && !inputFileName.contains(".txt")) {
			throw new InvalidFileFormatException();
		}
		if (fileData != null) {
			String lines[] = StringUtils.delimitedListToStringArray(fileData,
					TransformerConstant.NEW_LINE_CHAR);
			String headerData = (String) exchange.getIn().getHeader(
					TransformerConstant.FILE_HEADER);
			if (headerData != null) {
				/* Get the File Header Row from camel exchange */
				if (lines[0] == headerData) {
					fileData = fileData.replace(lines[0],
							TransformerConstant.EMPTY_STRING);
				}
				/* Get the tag names from File Header */
				if (headerData != null) {
					tagNames = StringUtils.delimitedListToStringArray(
							headerData, TransformerConstant.PIPE_DELIMITER);
					tagNameCount = tagNames.length;
				}
				/* Remove the header from file data */
				/*
				 * fileData = fileData.replace(lines[0],
				 * TransformerConstant.EMPTY_STRING);
				 */
				// fileData = fileData.replaceFirst(
				// TransformerConstant.NEW_LINE_CHAR,
				// TransformerConstant.EMPTY_STRING);

				// fileData =
				// fileData.replace(TransformerConstant.CARRIGE_RETURN,
				// TransformerConstant.EMPTY_STRING);

			}
		}

		DocumentBuilderFactory domFactory = null;
		DocumentBuilder domBuilder = null;
		domFactory = DocumentBuilderFactory.newInstance();
		domBuilder = domFactory.newDocumentBuilder();
		String rows[] = null;
		if (fileData.contains(TransformerConstant.NEW_LINE_CHAR)) {
			rows = StringUtils.delimitedListToStringArray(fileData,
					TransformerConstant.NEW_LINE_CHAR);
		} else if (fileData.contains(TransformerConstant.CARRIGE_RETURN)) {
			rows = StringUtils.delimitedListToStringArray(fileData,
					TransformerConstant.CARRIGE_RETURN);
		}

		int rowCount = 1;
		for (int j = 1; j < rows.length; j++) {
			String rowData = rows[j];
			Document newDoc = domBuilder.newDocument();
			/*
			 * Get the feedType which will be used as the root tag from camel
			 * exchange
			 */
			String feed = exchange.getIn().getHeader("feedType", String.class);
			/* Create the parent tag */
			Element rootElement = newDoc.createElement(feed);
			newDoc.appendChild(rootElement);

			if (rowData != null) {
				/* Get the tag values from File Header */
				String tagValues[] = StringUtils.delimitedListToStringArray(
						rowData, TransformerConstant.PIPE_DELIMITER);
				int tagValueCount = tagValues.length;
				if (tagValueCount == tagNameCount) {
					String tagValue = "";
					for (int i = 0; i < tagNameCount; i++) {
						try {
							tagValue = String.valueOf(tagValues[i]);
							/* Create the child tag */
							Element curElement = newDoc
									.createElement(tagNames[i].replaceAll(
											TransformerConstant.SPACE,
											TransformerConstant.EMPTY_STRING));
							/* Set the value to the child tag if it is not null */
							if (tagValue != null && !tagValue.isEmpty()) {
								curElement
										.appendChild(newDoc.createTextNode(tagValue
												.replaceAll(
														TransformerConstant.CARRIGE_RETURN,
														TransformerConstant.EMPTY_STRING)));
							}
							/* Append the child tag to the parent */
							rootElement.appendChild(curElement);
						} catch (Exception exp) {
							throw new RuntimeException(
									"Exception while create XML for row: "
											+ rowCount + ".TagValue::"
											+ tagValue + ".TagName::"
											+ tagNames[i] + "Exception:" + exp
											+ " :: " + exp.getMessage());

						}
					}
					/* Generate the XML using TransformerFactory */
					TransformerFactory tranFactory = TransformerFactory
							.newInstance();
					Transformer aTransformer = tranFactory.newTransformer();
					Source src = new DOMSource(newDoc);
					StringWriter outWriter = new StringWriter();
					StreamResult result = new StreamResult(outWriter);
					aTransformer.transform(src, result);
					StringBuffer sb = outWriter.getBuffer();
					File xmlFile = new File(fileNamePrefix + rowCount
							+ fileNameExtn);
					try {
						if (!xmlFile.exists()) {
							xmlFile.createNewFile();
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(0);
					}
					FileWriter fileWriter = new FileWriter(xmlFile);
					fileWriter.write(sb.toString());
					fileWriter.flush();
					fileWriter.close();
				} else {
					throw new RuntimeException(
							"Header Field count does not match Row Field Count for row: "
									+ rowCount + " TagValueCount::"
									+ tagValueCount + ".TagNameCount:: "
									+ tagNameCount);
				}

			}

			rowCount++;
		}

	}
}
