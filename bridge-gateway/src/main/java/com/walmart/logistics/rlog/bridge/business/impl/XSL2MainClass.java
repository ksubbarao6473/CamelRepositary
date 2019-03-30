package com.walmart.logistics.rlog.bridge.business.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.TransformerFactoryImpl;

public class XSL2MainClass {
	public static void main(String[] args) {
		System.out.println("Start!");
		TransformerFactory transformerFactory = new TransformerFactoryImpl();
		Transformer tr;

		try {
			tr = transformerFactory.newTransformer(new StreamSource(
					new FileReader("./csvToXML.xsl")));
			Result result = new StreamResult(new FileOutputStream("out.xml"));

			tr.transform(
					new StreamSource(new FileInputStream("./logback.xml")),
					result);
		} catch (FileNotFoundException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done!");
	}
}
