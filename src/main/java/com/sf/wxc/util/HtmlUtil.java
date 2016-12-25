package com.sf.wxc.util;

import org.w3c.dom.Node;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Created by Administrator on 2016-12-24.
 */
public class HtmlUtil {
    public static String getInnerHTML(Node node)
    {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer proc = factory.newTransformer();
            proc.setOutputProperty(OutputKeys.METHOD, "html");
            for (int i = 0; i < node.getChildNodes().getLength(); i++)
            {
                proc.transform(new DOMSource(node.getChildNodes().item(i)), result);
            }
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return sw.toString();

/*        try {
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(node),result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return sw.toString();*/
    }
}
