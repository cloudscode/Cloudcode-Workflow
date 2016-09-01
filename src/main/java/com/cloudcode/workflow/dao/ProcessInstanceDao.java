package com.cloudcode.workflow.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cloudcode.framework.utils.Check;
import com.cloudcode.framework.utils.Convert;
import com.cloudcode.framework.utils.DateFormat;
import com.cloudcode.framework.utils.Json;
import com.cloudcode.framework.utils.MessageException;
import com.cloudcode.framework.utils.PageRange;
import com.cloudcode.usersystem.dao.LoginUserUtilDao;
import com.cloudcode.workflow.model.WorkFlowInfo;

@Service
public class ProcessInstanceDao extends ActivitiDao {
	@Autowired
	private WorkFlowInfoDao workFlowInfoDao;
	@Autowired
	private LoginUserUtilDao loginUserUtilDao;
	@Autowired
	private ActRuTaskExpandDao actRuTaskExpandDao;
	/*@Autowired
	private IHibernateDAO dao;
	@Autowired
	private MongoFormOperService mongoFormOperService;*/
	
	@Transactional(propagation = Propagation.SUPPORTS)
	public Map<String, Object> start(String pdkey, String workflowName,
			Map<String, Object> objectMap) throws MessageException {
		String tableName = Convert.toString(objectMap.get("tableName"));
		objectMap.remove("tableName");
		ProcessDefinition processDefinition = this.repositoryService
				.createProcessDefinitionQuery().processDefinitionKey(pdkey)
				.latestVersion().singleResult();
		if (processDefinition == null) {
			throw new MessageException("流程还未部署！");
		}
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinition.getId());
		List<ActivityImpl> activityImpls = def.getActivities();

		String userkey = "assignee";
		for (ActivityImpl activityImpl : activityImpls) {
			if ("startEvent".equals(activityImpl.getProperty("type"))) {
				List<PvmTransition> pvmTransitionList = activityImpl
						.getOutgoingTransitions();
				userkey = "remove_users_"
						+ pvmTransitionList.get(0).getDestination().getId();
				break;
			}
		}
		String userid = loginUserUtilDao.findLoginUserId();
		this.identityService.setAuthenticatedUserId(loginUserUtilDao
				.findLoginUserId());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(userkey, userid);
		String objectId = UUID.randomUUID().toString();
		if (Check.isNoEmpty(objectMap.get("id"))) {
			objectId = objectMap.get("id").toString();
		}
		paramMap.put("remove_start", "1");
		paramMap.put("formObject", objectMap);
		ProcessInstance processInstance = this.runtimeService
				.startProcessInstanceByKey(processDefinition.getKey(),
						workflowName, objectId, paramMap);
		List<Task> tasks = this.taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).list();
		this.taskService.claim(tasks.get(0).getId(), userid);
		Map<String, Map<String, Object>> cellMaps = workFlowInfoDao
				.findCellObjectListByPdid(processInstance
						.getProcessDefinitionId());
		Map<String, Object> activityMap = cellMaps.get(tasks.get(0)
				.getTaskDefinitionKey());
		activityMap.put("objectId", objectId);
		activityMap.put("taskId", tasks.get(0).getId());
		activityMap.put("piid", processInstance.getId());
		activityMap.put("pdid", processInstance.getProcessDefinitionId());
		activityMap.put("activityId", tasks.get(0).getTaskDefinitionKey());
		activityMap.put("piName", workflowName);
		activityMap.put("name", tasks.get(0).getName());
		activityMap.put("serviceObject", objectMap);
		objectMap.put("tableName", tableName);
		objectMap.put("id", objectId);
		//mongoFormOperService.save(objectMap);
		return activityMap;
	}

	public Map<? extends String, ? extends Object> queryPagingData(
			PageRange pageRange, String pdkey, int allpi) {
		HistoricProcessInstanceQuery historicProcessInstanceQuery = this.historyService
				.createHistoricProcessInstanceQuery();
		HistoricProcessInstanceQuery historicProcessInstanceQueryCount = this.historyService
				.createHistoricProcessInstanceQuery();

		if (Check.isNoEmpty(pdkey)) {
			historicProcessInstanceQuery.processDefinitionKey(pdkey);
			historicProcessInstanceQueryCount.processDefinitionKey(pdkey);
		}
		
		if (allpi==0) {
			historicProcessInstanceQuery.startedBy(loginUserUtilDao.findLoginUserId());
			historicProcessInstanceQueryCount.startedBy(loginUserUtilDao.findLoginUserId());
		}
		
		List<HistoricProcessInstance> processInstances = historicProcessInstanceQuery
				.orderByProcessInstanceStartTime().desc()
				.listPage(pageRange.getStart(), pageRange.getEnd());
		List<Map<String, Object>> resultMaps = new ArrayList<Map<String, Object>>();
		for (HistoricProcessInstance historicProcessInstance : processInstances) {
			HistoricProcessInstanceEntity historicProcessInstanceEntity = (HistoricProcessInstanceEntity) historicProcessInstance;
			Map<String, Object> objectMap = Convert
					.objectToMapAll(historicProcessInstanceEntity);

			objectMap.put("piName", historicProcessInstanceEntity.getName());
			Long dur = historicProcessInstance.getDurationInMillis();
			if (historicProcessInstance.getDurationInMillis() == null) {
				dur = System.currentTimeMillis()
						- historicProcessInstance.getStartTime().getTime();
			}
			objectMap.put("durationInMillis",
					DateFormat.millisecondTOHHMMSS(dur));
			objectMap.put("pdid",
					historicProcessInstance.getProcessDefinitionId());
			resultMaps.add(objectMap);
		}

		Long count = historicProcessInstanceQueryCount.count();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("items", resultMaps);
		map.put("total", count);
		return map;
	}

	public WorkFlowInfo findWorkFlowByDataId(String dataId) {
		WorkFlowInfo workFlowInfo = workFlowInfoDao
				.findObjectByDataId(dataId);
		return workFlowInfo;
	}

	public void deleteByIds(String ids) {
		List<String> piidList = Convert.strToList(ids);
		for (String piid : piidList) {
			List<ProcessInstance> processInstanceList = runtimeService
					.createProcessInstanceQuery().processInstanceId(piid)
					.list();
			if (processInstanceList == null ? false : processInstanceList
					.size() == 0 ? false : true) {
				runtimeService.deleteProcessInstance(piid, null);
			}
			this.historyService.deleteHistoricProcessInstance(piid);
		}

	}

	public void end(String ids) {
		List<String> piidList = Convert.strToList(ids);
		for (String piid : piidList) {
			runtimeService.deleteProcessInstance(piid,
					loginUserUtilDao.findLoginUserId());
		}
	}

	public void deleteExpandProcessInstance(String piid) {
		actRuTaskExpandDao.deleteByProperty("piid", piid);
	}

	public Object findPiServiceDataByBusId(String objectId) {
		if (Check.isEmpty(objectId)) {
			return new HashMap<String, Object>();
		}
		HistoricProcessInstance historicProcessInstance = historyService
				.createHistoricProcessInstanceQuery()
				.processInstanceBusinessKey(objectId).singleResult();
		if (historicProcessInstance == null) {
			return new HashMap<String, Object>();
		}
		return Json.toMap(historicProcessInstance.getBusData());
	}

	public void updatePiServiceDataByBusId(String objectId,String busData) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("busdata", busData);
		parameterMap.put("objectId", objectId);
		/*dao.updateEntityBySql(
				"update act_hi_procinst set busdata=:busdata where BUSINESS_KEY_ =:objectId",
				parameterMap);*/
	}

}
