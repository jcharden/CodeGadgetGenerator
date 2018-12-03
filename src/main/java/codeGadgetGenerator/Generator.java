package codeGadgetGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.text.rtf.RTFEditorKit;

import com.wjy.test_1.ExtractFuncLoc;

//如果需要修改，可以看cdgLinelist中的每个ArrayList，如果这个ArrayList被其他某个ArrayList包含，
//则可以将这个ArrayList去除。或者可以判断ArrayList的size，如果size小于某个特定值，则可以将其去除。
//89 72 89 ok
public class Generator {
	private Map<String, ArrayList<ArrayList<Integer>>> dataflow = new HashMap<String, ArrayList<ArrayList<Integer>>>();
	private String basePath;
	private ArrayList<CdgWrapper> cdgLineList = new ArrayList<>();
	
	public Generator(String _basePath, ArrayList<DataFlowWrapper> _dataflow) {
		this.basePath = _basePath;
		this.PreProcessDataFlow(_dataflow);
	}
	
	public ArrayList<CdgWrapper> getCdgLineList(){
		return this.cdgLineList;
	}
	
	/**
	 * 预处理数据流，将文件路径作为key，所有具有相同文件路径的dataflow聚合到一个ArrayList中
	 * @param _dataflow 原始数据流
	 */
	private void PreProcessDataFlow(ArrayList<DataFlowWrapper> _dataflow) {
		for(DataFlowWrapper flow : _dataflow) {
			if(this.dataflow.containsKey(flow.getSourceFilePath())) {
				ArrayList<ArrayList<Integer>> v = this.dataflow.get(flow.getSourceFilePath());
				v.add(flow.getDataFlowList());
			}
			else {
				ArrayList<ArrayList<Integer>> v = new ArrayList<ArrayList<Integer>>();
				v.add(flow.getDataFlowList());
				this.dataflow.put(flow.getSourceFilePath(),v);
			}
		}
	}
	
	private Integer getFuncBelongsTo(ArrayList<Integer> funcLocList, Integer Loc) {
		Integer realLoc = Collections.binarySearch(funcLocList, Loc);
		if(realLoc>=0) {
			return funcLocList.get(realLoc);
		}
		else {
			return funcLocList.get(-realLoc - 2);
		}
		
	}
	
	/**
	 * 从后向前遍历，去除重复元素
	 * @param funcLoc
	 * @return 新的funcLoc
	 */
	private LinkedList<Integer> removeDuplicateItem(ArrayList<Integer> funcLoc){
		ListIterator<Integer> iterator = funcLoc.listIterator(funcLoc.size());
		LinkedList<Integer> ret = new LinkedList<>();
		while(iterator.hasPrevious()) {
			Integer prev = iterator.previous();
			if(!ret.contains(prev)) {
				ret.add(0,prev);
			}
		}
		return ret;
	}
	
	
	private LinkedList<CdgWrapper> removeCdgListDup(LinkedList<CdgWrapper> cdgList){
		LinkedList<CdgWrapper> ret = new LinkedList<>();
		for(CdgWrapper i : cdgList) {
			boolean flag = false;
			for(CdgWrapper j : cdgList) {
				if(i != j) { //比较i,j的引用，若不等就不是同一元素
					flag = flag || j.getCdgLineList().containsAll(i.getCdgLineList());
					if(flag==true) {
						break;
					}
				}
			}
			if(flag == false) {
				ret.add(i);
			}
		}
		return ret;
	}
	
	public void Process() throws Exception {
		Iterator<Map.Entry<String, ArrayList<ArrayList<Integer>>>> entryItor = this.dataflow.entrySet().iterator();
		while(entryItor.hasNext()) { // for each file
			Map.Entry<String, ArrayList<ArrayList<Integer>>> entry = entryItor.next();
			String filePath = this.basePath + entry.getKey();
			ExtractFuncLoc extract = new ExtractFuncLoc(filePath);
			ArrayList<Integer> funcLocListTmp = extract.getFuncLoc(); // ArrayList contains all of the func definition;
			funcLocListTmp.add(0,0);
			LinkedList<CdgWrapper> fileCdgList = new LinkedList<>();
			for(ArrayList<Integer> dataflowItem : entry.getValue()) { // for each api
				ArrayList<ArrayList<Integer>> funcLocList = new ArrayList<ArrayList<Integer>>(); // 包含API中每个参数的数据流所涉及的函数的定义行号
				Integer targetApiLine = dataflowItem.get(dataflowItem.size() - 1); // API所在行号
				ArrayList<Integer> funcLoc = new ArrayList<Integer>(); // 单个参数所包含的函数定义行号的集合
				for(Integer line : dataflowItem) {
					Integer i = Collections.binarySearch(funcLocListTmp, line);//find this line belongs to which func
					if(i>=0) {
						/*
						if(!funcLoc.contains(funcLocListTmp.get(i))) {
							funcLoc.add(funcLocListTmp.get(i));
						}
						*/
						funcLoc.add(funcLocListTmp.get(i));
					}
					else {
						/*
						if(!funcLoc.contains(funcLocListTmp.get(-i-2))) {
							funcLoc.add(funcLocListTmp.get(-i-2));
						}
						*/
						funcLoc.add(funcLocListTmp.get(-i-2));
					}
					if(line.equals(targetApiLine)) { // 至此说明一个参数的数据流以追踪完毕
						ArrayList<Integer> f = new ArrayList<Integer>();
						LinkedList<Integer> g = this.removeDuplicateItem(funcLoc);
						for(Iterator<Integer> iterator = g.iterator();iterator.hasNext();) {
							Integer l = iterator.next();
							f.add(l);
						}
						funcLocList.add(f);
						funcLoc.clear();
					}
				}
				// 找出funcLocList中包含元素最多的那个Treeset
				ArrayList<Integer> maxItemArrayList = Collections.max(funcLocList, new Comparator<ArrayList<Integer>>() {
					public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
						return o1.size() - o2.size();
					}
				});
				
				//ArrayList<Integer> maxItemArrayList = new ArrayList<Integer>(maxItemSet);
				
				//对api的dataflow的ArrayList去重
				TreeSet<Integer> treeSet = new TreeSet<Integer>(dataflowItem);
		        List<Integer> newDataFlowItem = new ArrayList<Integer>();
		        newDataFlowItem.addAll(treeSet);
		        
		        Map<Integer, ArrayList<Integer>> destMap = new HashMap<Integer, ArrayList<Integer>>();
		        for(Integer l : funcLocListTmp) {
		        	destMap.put(l, new ArrayList<Integer>());
		        }
		        
		        for(Integer newItem : newDataFlowItem) {
		        	Integer realLoc = this.getFuncBelongsTo(funcLocListTmp, newItem);
		        	destMap.get(realLoc).add(newItem);
		        }
				ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
				for(Integer i : maxItemArrayList) {
					result.add(destMap.get(i));
				}
				destMap.forEach((k,v)->{
					if(!maxItemArrayList.contains(k)) {
						result.add(0, v);
					}
				});
				ArrayList<Integer> r = new ArrayList<>();
				for(ArrayList<Integer> i : result) {
					for(Integer j : i) {
						r.add(j);
					}
				}
				CdgWrapper ret = new CdgWrapper(filePath,targetApiLine,"cppfunc",r);
				fileCdgList.add(ret);
			}
			LinkedList<CdgWrapper> finalCdgList = this.removeCdgListDup(fileCdgList);
			for(CdgWrapper i : finalCdgList) {
				this.cdgLineList.add(i);
			}
		}
		/*
		for(Map.Entry<String, ArrayList<ArrayList<Integer>>> entry : this.dataflow.entrySet()) {
			
		}
		*/
	}
	
}
