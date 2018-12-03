package codeGadgetGenerator;

import java.util.List;
import java.io.File;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;


public class XmlReader{
	private String filePath = "";
	private ArrayList<DataFlowWrapper> dataFlow = new ArrayList<DataFlowWrapper>();
	
	public XmlReader(String filePath) {
		this.filePath = filePath;
	}

	public ArrayList<DataFlowWrapper> getDataFlow() throws DocumentException {
		File file = new File(this.filePath);
		Document document = new SAXReader().read(file);
		List<Element> resultList = document.selectNodes("/CxXMLResults/Query/Result");
		for(Element ele : resultList) {
			DataFlowWrapper wrapper = new DataFlowWrapper();
			String sourceFilePath = ele.attributeValue("FileName");
			wrapper.setSourceFilePath(sourceFilePath);
			//System.out.println(sourceFilePath);
			Element pathElement = ele.element("Path");
			List<Element> nodeList = pathElement.elements();
			for(Element node : nodeList) {
				String line = node.elementText("Line");
				Integer lineInt = Integer.valueOf(line);
				wrapper.addDataFlowNode(lineInt);
				//System.out.println(line);
			}
			this.dataFlow.add(wrapper);
		}
		return dataFlow;
	}
	
	@Test
	public void test1() {
		this.filePath = "D:\\360安全浏览器下载\\test_20181106_2.xml";
		
		try {
			ArrayList<DataFlowWrapper> dataFlowWrappers = this.getDataFlow();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
