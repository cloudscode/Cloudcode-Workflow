package com.cloudcode.workflow.dao;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cloudcode.framework.dao.BaseModelObjectDao;
import com.cloudcode.framework.dao.ModelObjectDao;
import com.cloudcode.framework.utils.Check;
import com.cloudcode.framework.utils.Convert;
import com.cloudcode.framework.utils.HQLParamList;
import com.cloudcode.framework.utils.Json;
import com.cloudcode.workflow.ProjectConfig;
import com.cloudcode.workflow.lis.FlowEndExecuteLis;
import com.cloudcode.workflow.lis.UserTaskCreateEvent;
import com.cloudcode.workflow.model.WorkFlowInfo;
import com.cloudcode.workflow.util.ImageUtil;
import com.cloudcode.workflow.util.Static;

@Repository
public class WorkFlowInfoDao extends BaseModelObjectDao<WorkFlowInfo> {

	@Resource(name = ProjectConfig.PREFIX + "workFlowInfoDao")
	private ModelObjectDao<WorkFlowInfo> workFlowInfoDao;
	@Autowired
	protected ActivitiDao activitiDao;
	@Autowired
	protected RepositoryService repositoryService;
	
	public String createObject(WorkFlowInfo workFlowInfo){
		workFlowInfo.setCreateDateTime(new Date());
		workFlowInfo.setUpdateDateTime(new Date());
		return  workFlowInfoDao.createObject(workFlowInfo);
	}
	public WorkFlowInfo save(WorkFlowInfo workFlowInfo, String imgxml,
			int deploy, int imageHeight, int imageWidth,
			HttpServletRequest request) throws Exception {
		if (!imgxml.endsWith("</output>")) {
			imgxml = "<output>" + imgxml + "</output>";
		}

		if (!workFlowInfo.getMxgraphXml().endsWith("</mxGraphModel>")) {
			workFlowInfo.setMxgraphXml("<mxGraphModel>"
					+ workFlowInfo.getMxgraphXml() + "</mxGraphModel>");
		}
		// HttpServletRequest request = ServletActionContext.getRequest();
		String path = request.getContextPath();
		//imgxml="<output><fontfamily family=\"Arial,Helvetica\"/><fontsize size=\"11\"/><shadowcolor color=\"gray\"/><shadowalpha alpha=\"1\"/><shadowoffset dx=\"2\" dy=\"3\"/><save/><shadow enabled=\"1\"/><image x=\"190\" y=\"230\" w=\"40\" h=\"40\" src=\"http://localhost:8080/hhcommon/opensource/mxgraph/examples/editors/wfimg/userTask_no.gif\" aspect=\"1\" flipH=\"0\" flipV=\"0\"/><restore/><save/><fontcolor color=\"black\"/><fontbackgroundcolor color=\"white\"/><fontsize size=\"12\"/><fontstyle style=\"1\"/><text x=\"210\" y=\"272\" w=\"36\" h=\"36\" str=\"用户任务\" align=\"center\" valign=\"top\" wrap=\"0\" format=\"\" overflow=\"visible\" clip=\"0\" rotation=\"0\"/><restore/></output>";
		byte[] imageByte = ImageUtil.exportImage(imageWidth, imageHeight,
				imgxml);
		workFlowInfo.setActivitiImg(imageByte);
		if (Check.isEmpty(workFlowInfo.getId())) {
			workFlowInfo.setId("WORK"
					+ UUID.randomUUID().toString().replace("-", ""));
			String activitiXml = getActivitiXml(workFlowInfo);
			workFlowInfo.setActivitiXml(activitiXml);
			this.createObject(workFlowInfo);
		} else {
			String activitiXml = getActivitiXml(workFlowInfo);
			workFlowInfo.setActivitiXml(activitiXml);
			workFlowInfoDao.updateObject(workFlowInfo);
		}
		if (deploy == 1) {
			try {
				activitiDao.deploy(workFlowInfo);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return workFlowInfo;
	}

	private String getActivitiXml(WorkFlowInfo workFlowInfo) {
		Map<String, Object> rootMap = Json.toMap(workFlowInfo.getRootObject());
		List<Map<String, Object>> cellMaps = Json.toMapList(workFlowInfo
				.getCellObjectList());
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		Element definitions = document.addElement("definitions",
				"http://www.omg.org/spec/BPMN/20100524/MODEL");
		definitions.addAttribute("typeLanguage",
				"http://www.w3.org/2001/XMLSchema");
		definitions.addAttribute("expressionLanguage",
				"http://www.w3.org/1999/XPath");
		definitions.addAttribute("targetNamespace",
				"http://www.activiti.org/test");
		definitions.addNamespace("xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		definitions.addNamespace("activiti", "http://activiti.org/bpmn");
		definitions.addNamespace("bpmndi",
				"http://www.omg.org/spec/BPMN/20100524/DI");
		definitions.addNamespace("omgdc",
				"http://www.omg.org/spec/DD/20100524/DC");
		definitions.addNamespace("omgdi",
				"http://www.omg.org/spec/DD/20100524/DI");
		addProcess(workFlowInfo, rootMap, cellMaps, definitions);

		return document.asXML().replaceAll("&gt;", ">").replaceAll("&lt;", "<")
				.replaceAll("&amp;", "&");
	}

	private void addProcess(WorkFlowInfo workFlowInfo,
			Map<String, Object> rootMap, List<Map<String, Object>> cellMaps,
			Element definitions) {
		Element process = definitions.addElement("process");
		Element documentationElements = process.addElement("documentation");
		documentationElements.addText("<![CDATA[ " + rootMap.get("description")
				+ " ]]>");
		process.addAttribute("id", workFlowInfo.getId());
		process.addAttribute("name", rootMap.get("label").toString());

		// 流程结束执行
		Element extensionElements = process.addElement("extensionElements");
		Element varLis2 = extensionElements
				.addElement("activiti:executionListener");
		varLis2.addAttribute("event", "end");
		varLis2.addAttribute("class", FlowEndExecuteLis.class.getName());

		// ///////////////////////////////////////////////////////////////////////////////////
		Element bpmndi = DocumentHelper.createElement("bpmndi:BPMNDiagram");
		definitions.add(bpmndi);
		bpmndi.addAttribute("id", "BPMNDiagram_" + workFlowInfo.getId());

		Element bpmnPlane = DocumentHelper.createElement("bpmndi:BPMNPlane");
		bpmndi.add(bpmnPlane);

		bpmnPlane.addAttribute("bpmnElement", workFlowInfo.getId());
		bpmnPlane.addAttribute("id", "BPMNPlane_" + workFlowInfo.getId());

		addNodes(cellMaps, process, bpmnPlane);
	}

	private void addNodes(List<Map<String, Object>> cellMaps, Element process,
			Element bpmnPlane) {
		Map<String, Integer> activitySourceRefSize = new HashMap<String, Integer>();
		for (Map<String, Object> map : cellMaps) {
			String type = map.get("type").toString();
			String id = Convert.toString(map.get("sourceRef"));
			if ("sequenceFlow".equals(type)) {
				Integer size = activitySourceRefSize.get(id);
				if (size == null) {
					activitySourceRefSize.put(id, 1);
				} else {
					activitySourceRefSize.put(id, size + 1);
				}
			}
		}
		for (Map<String, Object> map : cellMaps) {
			String type = map.get("type").toString();
			String id = map.get("id").toString();
			String label = Convert.toString(map.get("label"));
			Element element = process.addElement(type);
			element.addAttribute("id", id);
			element.addAttribute("name", label);

			addBpmdDi(bpmnPlane, map);

			if ("sequenceFlow".equals(type)) {
				String sourceRef = Convert.toString(map.get("sourceRef"));
				String targetRef = Convert.toString(map.get("targetRef"));
				element.addAttribute("sourceRef", sourceRef);
				element.addAttribute("targetRef", targetRef);
				if (sourceRef.indexOf("startEvent") > -1
						|| activitySourceRefSize.get(sourceRef) == null
						|| activitySourceRefSize.get(sourceRef) == 1
				// || targetRef.indexOf("exclusiveGateway") > -1
				// || targetRef.indexOf("parallelGateway") > -1
				// || targetRef.indexOf("inclusiveGateway") > -1
				) {
				} else {
					String conditionType = Convert.toString(map
							.get("conditionType"));
					if ("1".equals(conditionType)) {
						Element conditionExpression = element
								.addElement("conditionExpression");
						conditionExpression.addText("<![CDATA[${"
								+ "1==remove_condition_" + targetRef + "}]]>");
					} else {
						String condition = Convert.toString(map
								.get("condition"));
						if (!Check.isEmpty(condition)) {
							Element conditionExpression = element
									.addElement("conditionExpression");
							conditionExpression.addText("<![CDATA[${"
									+ condition + "}]]>");
						}
					}
				}
			} else if ("startEvent".equals(type)) {

			} else if ("endEvent".equals(type)) {

			} else if ("userTask".equals(type)) {
				// element.addAttribute("activiti:candidateUsers",
				// "${candidateUsers_" + id + "}");
				Element extensionElements = element
						.addElement("extensionElements");
				Element taskListener = extensionElements
						.addElement("activiti:taskListener");
				taskListener.addAttribute("event", "create");
				taskListener.addAttribute("class",
						UserTaskCreateEvent.class.getName());
			} else if ("exclusiveGateway".equals(type)) {

			} else if ("parallelGateway".equals(type)) {

			} else if ("inclusiveGateway".equals(type)) {

			}
		}

	}

	private void addBpmdDi(Element bpmnPlane, Map<String, Object> map) {
		String type = map.get("type").toString();
		if ("userTask".equals(type)) {
			String id = Convert.toString(map.get("id"));
			String height = Convert.toString(map.get("height"));
			String width = Convert.toString(map.get("width"));
			String x = Convert.toString(map.get("x"));
			String y = Convert.toString(map.get("y"));
			Element bpmnShape = DocumentHelper
					.createElement("bpmndi:BPMNShape");
			bpmnPlane.add(bpmnShape);
			bpmnShape.addAttribute("bpmnElement", id);
			bpmnShape.addAttribute("id", "BPMNShape_" + id);
			Element bounds = DocumentHelper.createElement("omgdc:Bounds");
			bpmnShape.add(bounds);
			bounds.addAttribute("height", height);
			bounds.addAttribute("width", width);
			bounds.addAttribute("x", x);
			bounds.addAttribute("y", y);
		}

	}
	
	public WorkFlowInfo findObjectByDataId(String dataId) {
		WorkFlowInfo workFlowInfo = workFlowInfoDao.findEntity(
				WorkFlowInfo.class,
				new HQLParamList().addCondition(
						Restrictions.eq("dataId", dataId)).addConditionIsNull("pdid"));
		return workFlowInfo;
	}
	
	public void deleteByPdidList(List<String> idList) {
		workFlowInfoDao.deleteObject(WorkFlowInfo.class, "pdid", idList);
	}
	
	public WorkFlowInfo findObjectById(String id) {
		return workFlowInfoDao.findEntityByPK(WorkFlowInfo.class, id);
	}
	
	public Map<String, Map<String, Object>> findCellObjectListByPdid(String id) {
		WorkFlowInfo hhWorkFlowInfo = new WorkFlowInfo();
		hhWorkFlowInfo.setPdid(id);
		Object[] objectMap = (Object[]) workFlowInfoDao.findPropertys(hhWorkFlowInfo,
				new String[] { "cellObjectList", "rootObject" },
				new String[] { "pdid" });
		String object = Convert.toString(objectMap[0]);
		String rootObject = Convert.toString(objectMap[1]);
		Map<String, Object> rootmap = Json.toMap(rootObject);
		List<Map<String, Object>> mapList = Json.toMapList(object);
		Map<String, Map<String, Object>> returnMap = new HashMap<String, Map<String, Object>>();
		returnMap.put(Static.WORKFLOW_MAP_KEY, rootmap);
		for (Map<String, Object> map : mapList) {
			copyWfToAct(map, rootmap);
			returnMap.put(map.get("id").toString(), map);
		}
		return returnMap;
	}
	
	public static String[] copyPros = new String[] { "attachment", "comment" };

	public void copyWfToAct(Map<String, Object> activityMap,
			Map<String, Object> workflowMap) {
		Set<String> actKeySet = activityMap.keySet();
		if (Check.isEmpty(activityMap.get("href"))
				|| "null".equals(activityMap.get("href") + "")) {
			activityMap.put("href", workflowMap.get("href"));
			activityMap.put("hrefParams", workflowMap.get("hrefParams"));
		}
		for (String proStr : copyPros) {
			if (!actKeySet.contains(proStr)) {
				activityMap.put(proStr, workflowMap.get(proStr));
			}
		}
	}
	
	public Map<String, Object> findObjectAndStartByDataId(String dataId) {
		WorkFlowInfo hhXtForm = this.findObjectByDataId(dataId);

		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey(hhXtForm.getId()).latestVersion()
				.singleResult();
		Map<String, Object> activityMap= new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		if(null != processDefinition){
			Map<String, Map<String, Object>> cellMaps = this
					.findCellObjectListByPdid(processDefinition.getId());
			Map<String, Object> workflowMap = cellMaps.get(Static.WORKFLOW_MAP_KEY);
			activityMap = cellMaps.get(Convert
					.toString(workflowMap.get("startEventTargetRefId")));
			map.put("id", hhXtForm.getId());
			map.put("startEvent", activityMap);
			map.put("pdid", processDefinition.getId());
		}
		return map;
	}
	
	public WorkFlowInfo findObjectByPdId(String pdid) {
		WorkFlowInfo workFlowInfo = workFlowInfoDao.findEntity(
				WorkFlowInfo.class,
				new HQLParamList().addCondition(
						Restrictions.eq("pdid", pdid)));
		return workFlowInfo;
	}
	
	public List<WorkFlowInfo> findObjectRevByDataId(String dataId) {
		List<WorkFlowInfo> workFlowInfo = workFlowInfoDao.queryList(
				WorkFlowInfo.class,
				new HQLParamList().addCondition(
						Restrictions.eq("dataId", dataId)).addConditionIsNotNull("pdid"));
		return workFlowInfo;
	}
}
