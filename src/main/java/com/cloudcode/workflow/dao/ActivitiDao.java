package com.cloudcode.workflow.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cloudcode.framework.utils.Convert;
import com.cloudcode.framework.utils.Json;
import com.cloudcode.workflow.model.WorkFlowInfo;

@Repository("activitiDao")
public class ActivitiDao {
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	protected RuntimeService runtimeService;
	@Autowired
	protected IdentityService identityService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected TaskService taskService;
	@Autowired
	protected FormService formService;
	@Autowired
	protected ManagementService managementService;
	@Autowired
	private WorkFlowInfoDao workFlowInfoDao;
	/*@Autowired
	protected ModelObjectDao<ModelObject> sqldao;*/

	@Autowired
	private WorkFlowTreeDao workFlowTreeDao;

	@Transactional
	public void deploy(String dataId) throws Exception {

		// 得到配置的流程详细信息
		WorkFlowInfo workFlowInfo = workFlowInfoDao
				.findObjectByDataId(dataId);

		deploy(workFlowInfo);
	}

	public void deploy(WorkFlowInfo workFlowInfo) throws Exception,
			IOException {
		InputStream is = null;
		try {
			// 把图片字节转成流
			is = new ByteArrayInputStream(workFlowInfo.getActivitiImg());
			String actXml = workFlowInfo.getActivitiXml();
			Map<String, Object> rootMap = Json.toMap(workFlowInfo
					.getRootObject());
			String name = Convert.toString(rootMap.get("label"));
			Deployment deployment = this.repositoryService
					.createDeployment()
					.addString(
							name + ".bpmn20.xml",
							actXml.replace("UTF-8",
									System.getProperty("file.encoding")))
					.addInputStream(name + ".png", is).deploy();

			String pdid = this.repositoryService.createProcessDefinitionQuery()
					.deploymentId(deployment.getId()).singleResult().getId();

			WorkFlowInfo workFlowInfoCopy = new WorkFlowInfo();
			//workFlowInfo.setCreateDateTime(new Date());
			//workFlowInfo.setUpdateDateTime(new Date());
			BeanUtils.copyProperties(workFlowInfoCopy, workFlowInfo);
			workFlowInfoCopy.setId(UUID.randomUUID().toString());
			workFlowInfoCopy.setPdid(pdid);
			workFlowInfoDao.createObject(workFlowInfoCopy);
			workFlowTreeDao.update(workFlowInfo.getDataId(), "deploy", 1);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	public List<Map<String, Object>> queryProcessDefinition() {
		List<ProcessDefinition> processDefinitions = this.repositoryService
				.createProcessDefinitionQuery()
				.orderByProcessDefinitionVersion().desc().list();
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		for (ProcessDefinition processDefinition : processDefinitions) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", processDefinition.getId());
			map.put("key", processDefinition.getKey());
			map.put("name", processDefinition.getName());
			map.put("deploymentId", processDefinition.getDeploymentId());
			map.put("description", processDefinition.getDescription());
			map.put("version", "版本：" + processDefinition.getVersion());
			maps.add(map);
		}
		return maps;
	}

	public void deleteProcessDefinitionsByPdid(String ids, boolean cascade) {

		List<String> pdidList = Convert.strToList(ids);
		List<String> pdkeyList = new ArrayList<String>();
		for (String pdid : pdidList) {
			ProcessDefinition processDefinition = this.repositoryService
					.createProcessDefinitionQuery().processDefinitionId(pdid).singleResult();
			if (processDefinition!=null) {
				pdkeyList.add(processDefinition.getKey());
				this.repositoryService.deleteDeployment(processDefinition.getDeploymentId(), cascade);
			}
		}
		deletePd(pdidList, pdkeyList);
	}

	private void deletePd(List<String> pdidList, List<String> pdkeyList) {
		workFlowInfoDao.deleteByPdidList(pdidList);
		for (String pdkey : pdkeyList) {
			List<ProcessDefinition> processDefinitions = this.repositoryService
					.createProcessDefinitionQuery().processDefinitionKey(pdkey)
					.list();
			if (processDefinitions.size() == 0) {
				WorkFlowInfo WorkFlowInfo = workFlowInfoDao
						.findObjectById(pdkey);
				if (WorkFlowInfo != null) {
					workFlowTreeDao.update(WorkFlowInfo.getDataId(),
							"deploy", 0);
				}
			}
		}
	}

	public void deleteProcessDefinitions(String ids, boolean cascade) {
		List<String> pdidList = new ArrayList<String>();
		List<String> idList = Convert.strToList(ids);

		List<String> pdkeyList = new ArrayList<String>();

		for (String id : idList) {
			ProcessDefinition processDefinition = this.repositoryService
					.createProcessDefinitionQuery().deploymentId(id)
					.singleResult();
			pdidList.add(processDefinition.getId());
			if (!pdkeyList.contains(processDefinition.getKey())) {
				pdkeyList.add(processDefinition.getKey());
			}
			this.repositoryService.deleteDeployment(id, cascade);
		}
		deletePd(pdidList, pdkeyList);
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	public IdentityService getIdentityService() {
		return identityService;
	}

	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public FormService getFormService() {
		return formService;
	}

	public void setFormService(FormService formService) {
		this.formService = formService;
	}

	public ManagementService getManagementService() {
		return managementService;
	}

	public void setManagementService(ManagementService managementService) {
		this.managementService = managementService;
	}

}
