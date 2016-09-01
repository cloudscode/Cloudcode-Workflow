package com.cloudcode.workflow.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cloudcode.framework.dao.BaseModelObjectDao;
import com.cloudcode.framework.dao.ModelObjectDao;
import com.cloudcode.workflow.ProjectConfig;
import com.cloudcode.workflow.model.ActRuTaskExpand;

@Repository
public class ActRuTaskExpandDao extends BaseModelObjectDao<ActRuTaskExpand> {
	
	@Resource(name=ProjectConfig.PREFIX+"actRuTaskExpandDao")	
	private  ModelObjectDao<ActRuTaskExpand> actRuTaskExpandDao;

	public void create(String userId, String piid, String taskId, int isRead) {
		ActRuTaskExpand actRuTaskExpand = new ActRuTaskExpand();
		actRuTaskExpand.setUserId(userId);
		actRuTaskExpand.setTaskId(taskId);
		actRuTaskExpand.setIsRead(isRead);
		actRuTaskExpand.setPiid(piid);
		actRuTaskExpandDao.createObject(actRuTaskExpand);
	}
	
	public void deleteByProperty(String property, Object value) {
		if (value instanceof List) {
			actRuTaskExpandDao.deleteObject(ActRuTaskExpand.class, property, (List) value);
		} else {
			actRuTaskExpandDao.deleteObject(ActRuTaskExpand.class, property, (List<String>) value);
		}
		
	}
	public List<ActRuTaskExpand> queryListByProperty(String property, Object value) {
		List<ActRuTaskExpand> entityList = actRuTaskExpandDao.queryList(ActRuTaskExpand.class,
				Restrictions.eq(property, value));
		return entityList;
	}
}
