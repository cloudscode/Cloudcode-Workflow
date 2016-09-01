package com.cloudcode.workflow.dao;

import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.springframework.stereotype.Repository;

@Repository("activitiUtilDao")
public class ActivitiUtilDao extends ActivitiDao {
	/**
	 * 根据流程定义ID和节点ID获取节点
	 * 
	 * @param pdid
	 *            流程定义ID
	 * @param actid
	 *            流程节点ID
	 * @return
	 */
	public ActivityImpl currentAct(String pdid, String actid) {
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(pdid);
		return def.findActivity(actid);
	}
}
