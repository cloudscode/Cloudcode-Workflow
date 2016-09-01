package com.cloudcode.workflow.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.mxgraph.canvas.mxGraphicsCanvas2D;
import com.mxgraph.canvas.mxICanvas2D;
import com.mxgraph.reader.mxSaxOutputHandler;
import com.mxgraph.util.mxUtils;

public class ImageUtil {
	/**
	 * 导出图片文件
	 * 
	 * @param w
	 *            图片宽
	 * @param h
	 *            图片高
	 * @param imageXML
	 *            graph对应的xml代码
	 * @throws IOException
	 */
	public static byte[] exportImage(int w, int h, String imageXML) {
		BufferedImage image = mxUtils.createBufferedImage(w, h, Color.WHITE);
		Graphics2D g2 = image.createGraphics();
		mxUtils.setAntiAlias(g2, true, true);
		mxGraphicsCanvas2D gc2 = new mxGraphicsCanvas2D(g2);
		gc2.setAutoAntiAlias(true);
		parseXmlSax(imageXML, gc2);
		gc2.close();
		// ImageIO.write(image, "png", png);
		return imageToByte(image, "png");
	}

	public static byte[] imageToByte(BufferedImage bi, String format) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, format, baos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				baos.flush();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}

	/**
	 * 创建并返回请求的图片
	 * 
	 * @param request
	 * @return
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	protected static void parseXmlSax(String xml, mxICanvas2D canvas) {
		// Creates SAX handler for drawing to graphics handle
		mxSaxOutputHandler handler = new mxSaxOutputHandler(canvas);
		// Creates SAX parser for handler
		XMLReader reader;
		try {
			reader = SAXParserFactory.newInstance().newSAXParser()
					.getXMLReader();
			reader.setContentHandler(handler);
			// Renders XML data into image
			reader.parse(new InputSource(new StringReader(xml)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
