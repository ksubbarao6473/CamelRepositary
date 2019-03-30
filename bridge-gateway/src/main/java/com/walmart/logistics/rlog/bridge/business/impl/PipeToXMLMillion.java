package com.walmart.logistics.rlog.bridge.business.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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

public class PipeToXMLMillion implements Processor {
	String fileNamePrefix = "D:/asn/million/ASN_IN_";
	String fileNameExtn = ".xml";
	int maxBufferSize = 500000;

	public void process(Exchange exchange) throws Exception {
		File inputFile = new File("D:/asn/input/SampleFile2 Million.txt");
		FileReader fileReader = new FileReader(inputFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line = "";
		int bufferCount = 0;
		/*
		 * Get the feedType which will be used as the root tag from camel
		 * exchange
		 */
		String feed = exchange.getIn().getHeader("feedType", String.class);
		List<String> fileData = new ArrayList<String>();
		String headerData = bufferedReader.readLine();
		if (headerData != null) {
			String inputFileName = exchange.getIn().getHeader(
					"CamelFileNameOnly", String.class);

			/* Validate that the input file format */
			if (!headerData.contains(TransformerConstant.PIPE_DELIMITER)
					&& inputFileName != null && !inputFileName.contains(".txt")) {
				throw new InvalidFileFormatException();
			}
			String[] tagNames = StringUtils.delimitedListToStringArray(
					headerData, TransformerConstant.PIPE_DELIMITER);
			while ((line = bufferedReader.readLine()) != null) {
				bufferCount++;
				if (bufferCount < maxBufferSize) {
					fileData.add(line);
				} else {
					formXML(feed, fileData, tagNames);
					bufferCount = 0;
					fileData = new ArrayList<String>();
					fileData.add(line);
				}

			}

		}
	}

	public void formXML(String feed, List<String> fileData, String[] tagNames)
			throws Exception {
		DocumentBuilderFactory domFactory = null;
		DocumentBuilder domBuilder = null;
		domFactory = DocumentBuilderFactory.newInstance();
		domBuilder = domFactory.newDocumentBuilder();
		int tagNameCount = tagNames.length;
		int rowCount = 1;
		for (int j = 1; j < fileData.size(); j++) {
			String rowData = fileData.get(j);
			Document newDoc = domBuilder.newDocument();

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
							/*
							 * Set the value to the child tag if it is not null
							 */
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
