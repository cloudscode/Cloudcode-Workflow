package com.cloudcode.workflow.dao;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.el.UelExpressionCondition;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cloudcode.framework.ProjectConfig;
import com.cloudcode.framework.dao.ModelObjectDao;
import com.cloudcode.framework.model.ModelObject;
import com.cloudcode.framework.utils.Check;
import com.cloudcode.framework.utils.CheckTree;
import com.cloudcode.framework.utils.Convert;
import com.cloudcode.framework.utils.DateFormat;
import com.cloudcode.framework.utils.MessageException;
import com.cloudcode.framework.utils.PageRange;
import com.cloudcode.usersystem.dao.LoginUserUtilDao;
import com.cloudcode.usersystem.dao.UserDao;
import com.cloudcode.usersystem.model.User;
import com.cloudcode.workflow.model.ActRuTaskExpand;

@Repository("taskWfDao")
public class TaskWfDao extends ActivitiDao{

	@Autowired
	private LoginUserUtilDao loginUserUtilDao;
	@Autowired
	private WorkFlowInfoDao workFlowInfoDao;
	@Autowired
	private ActRuTaskExpandDao actRuTaskExpandDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private WorkFlowDao workFlowDao;
	@Autowired
	private ActivitiUtilDao activitiUtilDao;
	@Resource(name=ProjectConfig.PREFIX+"modelDao")	
	ModelObjectDao<ModelObject> modelDao;
	
	public InputStream findPicByPdid(String pdid) {
		ProcessDefinition procDef = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(pdid.toString()).singleResult();
		if (procDef != null) {
			String diagramResourceName = procDef.getDiagramResourceName();
			return repositoryService.getResourceAsStream(
					procDef.getDeploymentId(), diagramResourceName);
		}
		return null;
	}

	public void submit(Map<String, Object> paraMap,
			Map<String, String> userMap, Map<String, String> conditionMap)
			throws MessageException {

		String taskId = Convert.toString(paraMap.get("taskId"));
		Map<String, Object> map = new HashMap<String, Object>();
		paraMap.remove("taskId");
		map.put("formObject", paraMap);

		map.putAll(userMap);
		map.putAll(conditionMap);
		this.claim(taskId);
		this.taskService.complete(taskId, map);
	}
	
	public void claim(String taskId) {
		taskService.claim(taskId, loginUserUtilDao.findLoginUserId());
	}
	
	public Map<String, Object> queryTaskList(PageRange pageRange) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", loginUserUtilDao.findLoginUserId());
		String whereSql = "from act_ru_task art "
				+ " INNER JOIN act_ru_task_expand arte on art.ID_=arte.TASK_ID "
				+ " INNER JOIN act_hi_procinst ahp on art.PROC_INST_ID_=ahp.ID_ "
				+ " where USER_ID=:userId  ORDER BY art.CREATE_TIME_  DESC ";
		Map<String, Object> resultMap = modelDao.queryPaginationSupport("select "
				+ " art.ID_ id" + ",art.NAME_ name" + ",art.PROC_DEF_ID_ pdid "
				+ ",art.TASK_DEF_KEY_ activityId" + ",ahp.NAME piName"
				+ ",art.CREATE_TIME_ createTime" + " ,art.PROC_INST_ID_ piid"
				+ ",ahp.START_USER_ID_ startUserId "
				+ ",art.ASSIGNEE_ assignee  " + ",arte.USER_ID userId "
				+ whereSql, "select count(art.ID_) " + whereSql, paramMap,
				pageRange);
		List<Map<String, Object>> items = (List<Map<String, Object>>) resultMap
				.get("items");
		for (Map<String, Object> map : items) {
			if (Convert.toString(map.get("assignee")).equals(map.get("userId"))) {
				map.put("managerType", "主办");
			} else {
				map.put("managerType", "经办");
			}
		}

		return resultMap;
	}
	
	public Map<String, Object> manager(String actionType, String taskId) {
		ActRuTaskExpand actRuTaskExpand = new ActRuTaskExpand();
		actRuTaskExpand.setUserId(loginUserUtilDao.findLoginUserId());
		actRuTaskExpand.setIsRead(1);
		actRuTaskExpand.setTaskId(taskId);
		actRuTaskExpandDao
				.updateObject(
						"update "
								+ ActRuTaskExpand.class.getName()
								+ " set isRead=:isRead where taskId=:taskId and userId=:userId ",
						actRuTaskExpand);

		String piid = "";
		String pdid = "";
		String activityId = "";
		if ("manager".equals(actionType)) {
			Task task = this.taskService.createTaskQuery().taskId(taskId)
					.singleResult();
			piid = task.getProcessInstanceId();
			pdid = task.getProcessDefinitionId();
			activityId = task.getTaskDefinitionKey();
		} else {
			HistoricTaskInstance task = this.historyService
					.createHistoricTaskInstanceQuery().taskId(taskId)
					.singleResult();
			piid = task.getProcessInstanceId();
			pdid = task.getProcessDefinitionId();
			activityId = task.getTaskDefinitionKey();
		}

		Map<String, Map<String, Object>> cellMaps = workFlowInfoDao
				.findCellObjectListByPdid(pdid);
		Map<String, Object> activityMap = cellMaps.get(activityId);

		HistoricProcessInstance processInstance = this.historyService
				.createHistoricProcessInstanceQuery().processInstanceId(piid)
				.singleResult();
		activityMap.put("objectId", processInstance.getBusinessKey());
		activityMap.put("taskId", taskId);
		activityMap.put("piid", processInstance.getId());
		activityMap.put("activityId", activityId);
		activityMap.put("pdid", processInstance.getProcessDefinitionId());
		return activityMap;
	}
	
	public List<Map<String, Object>> queryPicHiTask(String piid, String pdid) {
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(pdid);
		List<Map<String, Object>> resultListMap = new ArrayList<Map<String, Object>>();
		List<HistoricActivityInstance> historicActivityInstances = historyService
				.createHistoricActivityInstanceQuery().processInstanceId(piid)
				.orderByHistoricActivityInstanceEndTime().asc().list();
		for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
			ActivityImpl activityImpl = def
					.findActivity(historicActivityInstance.getActivityId());
			if ("userTask".equals(activityImpl.getProperty("type"))) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("width", activityImpl.getWidth());
				map.put("height", activityImpl.getHeight());
				map.put("top", activityImpl.getY());
				map.put("left", activityImpl.getX());
				map.put("actid", activityImpl.getId());
				if (historicActivityInstance.getEndTime() == null
						|| "".equals(historicActivityInstance.getEndTime())) {
					map.put("src",
							"/static/mxgraph/examples/editors/wfimg/userTask_ru.gif");
				} else {
					map.put("src",
							"/static/mxgraph/mxgraph/examples/editors/wfimg/userTask_hi.gif");
				}
				resultListMap.add(map);
			}
		}
		return resultListMap;
	}
	
	public List<Map<String, Object>> queryCommentByActivityId(String piid,
			String actid) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService
				.createHistoricTaskInstanceQuery().processInstanceId(piid);
		if (Check.isNoEmpty(actid)) {
			historicTaskInstanceQuery.taskDefinitionKey(actid);
		}
		List<HistoricTaskInstance> taskList = historicTaskInstanceQuery.list();

		for (HistoricTaskInstance historicTaskInstance : taskList) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (historicTaskInstance.getAssignee() != null) {
				User user = userDao.findObjectById(historicTaskInstance
						.getAssignee());
				if (user != null) {
					map.put("assigneeName", user.getName());
				}
			}

			map.put("createTime",
					DateFormat.dateToStr(historicTaskInstance.getStartTime()));

			if (historicTaskInstance.getEndTime() != null) {
				map.put("endTime",
						DateFormat.dateToStr(historicTaskInstance.getEndTime()));
			}
			Long dur = historicTaskInstance.getDurationInMillis();
			if (historicTaskInstance.getDurationInMillis() == null) {
				dur = System.currentTimeMillis()
						- historicTaskInstance.getStartTime().getTime();
			}
			map.put("durationInMillis", DateFormat.millisecondTOHHMMSS(dur));

			StringBuffer message = new StringBuffer();
			message.append("<table border=1 cellspacing=0 cellpadding=3>");
			List<Comment> comments = taskService
					.getTaskComments(historicTaskInstance.getId());
			for (Comment comment : comments) {
				String userName = "";
				if (comment.getUserId() != null) {
					User user = userDao.findObjectById(comment
							.getUserId());
					if (user != null) {
						userName = user.getName();
					}
				}

				message.append("<tr><td><span class=maxlabelText>" + userName
						+ DateFormat.dateToStr(comment.getTime())
						+ "</span></td></tr>");
				message.append("<tr><td><span class=maxlabelText>内容：</span>&nbsp;&nbsp;<font size=2>"
						+ comment.getFullMessage() + "</font></td></tr>");
			}
			message.append("</table>");

			/*List<Map<String, Object>> attachMaps = fileService
					.queryAttachmentTaskId(historicTaskInstance.getId());
			message.append("<table border=1 cellspacing=0 cellpadding=3>");
			for (Map<String, Object> map2 : attachMaps) {
				String attachmentFileName = Convert.toString(map2
						.get("attachmentFileName"));
				String path = Convert.toString(map2.get("path"));
				String userName = Convert.toString(map2.get("userName"));

				message.append("<tr><td><span class=maxlabelText>" + userName
						+ "</span></td>");
				message.append("<td><span class=maxlabelText></span>&nbsp;&nbsp;<font size=2>"
						+ "<a href=\"javascript:Href.download('"
						+ attachmentFileName
						+ "','"
						+ path
						+ "')\">"
						+ attachmentFileName + "</a>" + "</font></td></tr>");
			}
			message.append("</table>");*/
			map.put("message", message);

			List<ActRuTaskExpand> actRuTaskExpandList = actRuTaskExpandDao
					.queryListByProperty("taskId", historicTaskInstance.getId());
			String userNames = "";
			for (ActRuTaskExpand actRuTaskExpand : actRuTaskExpandList) {
				User user = userDao.findObjectById(actRuTaskExpand
						.getUserId());
				if (user != null) {
					String img = "";
					if (actRuTaskExpand.getIsRead() == 1) {
						img = "<img src='/hhcommon/images/icons/email/email_open.png' />";
					} else {
						img = "<img src='/hhcommon/images/icons/email/email.png' />";
					}
					userNames += img + user.getName() + "，";
				}
			}
			if (Check.isNoEmpty(userNames)) {
				userNames.substring(0, userNames.length() - 1);
			}
			map.put("userNames", userNames);
			mapList.add(map);
		}
		return mapList;
	}
	private static String taskpi_sql = "select aht.*,ahp.NAME PINAME from act_hi_taskinst aht LEFT JOIN  act_hi_procinst ahp on(aht.PROC_INST_ID_=ahp.PROC_INST_ID_)";
	public Map<String, Object> queryHiTaskListByAssignee(PageRange pageRange) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("assignee", loginUserUtilDao.findLoginUserId());
		Map<String, Object> resultMap = modelDao
				.queryPaginationSupport(
						taskpi_sql
								+ "  where ASSIGNEE_=:assignee  order by START_TIME_ desc ",
						"select count(id_) from act_hi_taskinst where ASSIGNEE_=:assignee",
						paramMap, pageRange);
		resultMap.put("items", workFlowDao
				.taskListConvert((List<Map<String, Object>>) resultMap
						.get("items")));
		return resultMap;
	}
	
	public List<CheckTree> queryStepByActivityId(String pdid, String piid,
			String activityId) {
		List<CheckTree> extCheckTrees = new ArrayList<CheckTree>();
		ActivityImpl activityImpl = activitiUtilDao.currentAct(pdid,
				activityId);
		List<PvmTransition> pvmTransitionList = activityImpl
				.getOutgoingTransitions();

		Map<String, Map<String, Object>> cellMaps = workFlowInfoDao
				.findCellObjectListByPdid(pdid);

		for (PvmTransition pvmTransition : pvmTransitionList) {

			CheckTree extCheckTree = new CheckTree();
			extCheckTree.setIcon("/hhcommon/images/extjsico/17460346.png");
			Map<String, Object> propertys = new HashMap<String, Object>();
			propertys.put("activityParentType",
					activityImpl.getProperty("type"));

			ActivityImpl destination = (ActivityImpl) pvmTransition
					.getDestination();

			propertys.put("activityType", destination.getProperty("type"));
			propertys.put("activityId", destination.getId());

			Map<String, Object> activityMap = cellMaps.get(destination.getId());
			propertys.putAll(activityMap);

			UelExpressionCondition uelExpressionCondition = (UelExpressionCondition) pvmTransition
					.getProperty("condition");
			if (uelExpressionCondition != null) {
				String condition = uelExpressionCondition.getExpression()
						.toString().replace("${", "").replace("}", "");
				if (("1==remove_condition_" + destination.getId())
						.equals(condition)) {
					propertys.put("sdxz_condition", true);
				} else {
					propertys.put("condition", condition);
					propertys.put("expressionKeyList",
							Convert.expressionToListKey(condition));
					propertys.put("isChange", false);
					extCheckTree
							.setIcon("/hhcommon/images/icons/lock/lock.png");
					propertys.put("sdxz_condition", false);
				}
			}

			String activityTypeText = "";
			if ("exclusiveGateway".equals(destination.getProperty("type"))) {
				activityTypeText = "<font color=red>（单一分支）</font>";
			} else if ("userTask".equals(destination.getProperty("type"))) {
				activityTypeText = "<font color=red>（任务节点）</font>";
			} else if ("parallelGateway"
					.equals(destination.getProperty("type"))) {
				activityTypeText = "<font color=red>（并行分支）</font>";
			} else if ("inclusiveGateway".equals(destination
					.getProperty("type"))) {
				activityTypeText = "<font color=red>（任意分支）</font>";
			} else if ("inclusiveGateway".equals(destination
					.getProperty("type"))) {
				activityTypeText = "<font color=red>（任意分支）</font>";
			} else if ("startEvent".equals(destination.getProperty("type"))) {
				activityTypeText = "<font color=red>（结束节点）</font>";
			}
			extCheckTree.setId(destination.getId());
			extCheckTree.setText(Convert.toString(pvmTransition
					.getProperty("name"))
					+ Convert.toString(destination.getProperty("name"))
					+ activityTypeText);

			if ("userTask".equals(destination.getProperty("type"))) {
				extCheckTree.setLeaf(1);
			}
			extCheckTree.setExpanded(1);
			extCheckTree.setPropertys(propertys);
			if (!"userTask".equals(propertys.get("type"))) {
				extCheckTree.setChildren(queryStepByActivityId(pdid, piid,
						extCheckTree.getId()));
			}
			extCheckTrees.add(extCheckTree);
		}
		if (extCheckTrees.size() == 1) {
			extCheckTrees.get(0)
					.setIcon("/hhcommon/images/icons/lock/lock.png");
			extCheckTrees.get(0).getPropertys().put("isChange", false);
			extCheckTrees.get(0).setChecked(true);
		}

		return extCheckTrees;
	}
}
