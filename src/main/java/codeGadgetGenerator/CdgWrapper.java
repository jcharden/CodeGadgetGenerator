package codeGadgetGenerator;

import java.util.ArrayList;

public class CdgWrapper {
	private String filePath;
	private Integer apiLine;
	private String rule;
	private ArrayList<Integer> cdgLineList;
	
	public CdgWrapper(String _filePath, Integer _apiLine, String _rule, ArrayList<Integer> _cdgLineList) {
		this.filePath = _filePath;
		this.apiLine = _apiLine;
		this.rule = _rule;
		this.cdgLineList = _cdgLineList;
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Integer getApiLine() {
		return apiLine;
	}
	public void setApiLine(Integer apiLine) {
		this.apiLine = apiLine;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public ArrayList<Integer> getCdgLineList() {
		return cdgLineList;
	}
	public void setCdgLineList(ArrayList<Integer> cdgLineList) {
		this.cdgLineList = cdgLineList;
	}
	
}
