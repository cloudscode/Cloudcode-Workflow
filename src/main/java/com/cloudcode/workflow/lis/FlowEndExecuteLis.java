package com.cloudcode.workflow.lis;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.cloudcode.framework.utils.BeanFactoryHelper;
import com.cloudcode.workflow.dao.ProcessInstanceDao;

public class FlowEndExecuteLis implements ExecutionListener {

	private static ProcessInstanceDao processInstanceDao = BeanFactoryHelper
			.getBeanFactory().getBean(ProcessInstanceDao.class);

	public void notify(DelegateExecution execution) throws Exception {
		processInstanceDao.deleteExpandProcessInstance(execution.getProcessInstanceId());
	}

}
