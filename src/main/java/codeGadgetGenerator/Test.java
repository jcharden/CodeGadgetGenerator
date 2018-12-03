package codeGadgetGenerator;

import java.util.ArrayList;
import java.util.Collections;

import org.dom4j.DocumentException;

import com.wjy.test_1.*;

public class Test {
	public static void main(String[] args) throws Exception {
		XmlReader reader = new XmlReader("D:\\test_20181102_1.xml");
		ArrayList<DataFlowWrapper> x = reader.getDataFlow();
		/*
		for(DataFlowWrapper d : x) {
			System.out.println(d.getSourceFilePath());
			for(Integer l : d.getDataFlowList()) {
				System.out.println(l);
			}
			System.out.println("---");
		}
		*/
		/*
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(0);
		list.add(3);
		list.add(8);
		list.add(15);
		list.add(21);
		Integer i = Collections.binarySearch(list, 1);
		System.out.println(-i-2);
		*/
		Generator g = new Generator("C:/Users/12201/Desktop", x);
		g.Process();
		ArrayList<CdgWrapper> ret = g.getCdgLineList();
		FileOutput fOutput = new FileOutput(ret, "D:/output_1112.txt");
		fOutput.output();
		/*
		ArrayList<ArrayList<Integer>> ret = g.getCdgLineList();
		for(ArrayList<Integer> i: ret) {
			for(Integer j : i) {
				System.out.println(j);
			}
			System.out.println("-----");
		}
		*/
	}
}
