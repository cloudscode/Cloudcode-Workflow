package com.cloudcode.workflow.lis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cloudcode.framework.utils.BeanFactoryHelper;
import com.cloudcode.framework.utils.Check;
import com.cloudcode.framework.utils.Convert;
import com.cloudcode.workflow.dao.ActRuTaskExpandDao;

public class UserTaskCreateEvent implements TaskListener {

	private static ActRuTaskExpandDao actRuTaskExpandDao = BeanFactoryHelper
			.getBeanFactory().getBean(ActRuTaskExpandDao.class);

	public void notify(DelegateTask delegateTask) {

		String userids = Convert.toString(delegateTask
				.getVariable("remove_users_"
						+ delegateTask.getTaskDefinitionKey()));

		if (Check.isEmpty(userids)) {
			userids = Convert.toString(delegateTask
					.getVariable("remove_setp_users"));

		}

		String isStart = Convert.toString(delegateTask
				.getVariable("remove_start"));
		int isRead = 0;
		if ("1".equals(isStart)) {
			isRead = 1;
		}

		List<String> userList = Convert.strToList(userids);
		for (String userId : userList) {
			actRuTaskExpandDao.create(userId, delegateTask.getProcessInstanceId(),delegateTask.getId(), isRead);
		}
		removeVar(delegateTask);
	}

	private void removeVar(DelegateTask delegateTask) {
		Map<String, Object> varMap = delegateTask.getVariables();
		Set<String> set = varMap.keySet();
		delegateTask.removeVariable("formObject");
		delegateTask.removeVariable("remove_users_"
						+ delegateTask.getTaskDefinitionKey());
		for (String key : set) {
			if (key.startsWith("remove_") && !key.startsWith("remove_users_")) {
				delegateTask.removeVariable(key);
			}
		}
	}

}
