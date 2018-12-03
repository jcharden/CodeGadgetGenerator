package codeGadgetGenerator;

import java.util.ArrayList;

public class DataFlowWrapper {
	private String sourceFilePath = "";
	private ArrayList<Integer> dataFlowList = new ArrayList<Integer>();

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public ArrayList<Integer> getDataFlowList() {
		return dataFlowList;
	}
	
	public void addDataFlowNode(Integer node) {
		this.dataFlowList.add(node);
	}
	
	public void setSourceFilePath(String _path) {
		this.sourceFilePath = _path;
	}
	
}
