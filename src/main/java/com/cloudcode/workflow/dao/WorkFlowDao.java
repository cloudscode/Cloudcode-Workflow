package com.cloudcode.workflow.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cloudcode.framework.utils.Check;
import com.cloudcode.framework.utils.Convert;
import com.cloudcode.framework.utils.DateFormat;
import com.cloudcode.usersystem.dao.UserDao;
import com.cloudcode.usersystem.model.User;

@Repository("workFlowDao")
public class WorkFlowDao {
	@Autowired
	private UserDao userDao;

	public List<Map<String, Object>> taskListConvert(
			List<Map<String, Object>> mapList) {
		List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : mapList) {
			Map<String, Object> resultMap = taskMapToMap(map);
			resultMapList.add(resultMap);
		}
		return resultMapList;
	}
	
public Map<String, Object> taskMapToMap(Map<String, Object> map) {
		
		String assignee = Convert.toString(map.get("ASSIGNEE_"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("id", map.get("ID_"));
		resultMap.put("name",  map.get("NAME_"));
		resultMap.put("piName",  map.get("PINAME"));
		resultMap.put("pdid",  map.get("PROC_DEF_ID_"));
		resultMap.put("activityId", map.get("TASK_DEF_KEY_"));
		resultMap.put("createTime", map.get("START_TIME_"));
		resultMap.put("endTime", map.get("END_TIME_"));
		Long dur =Convert.toLong(map.get("DURATION_"));
		if (map.get("DURATION_")== null) {
			dur = System.currentTimeMillis() -((Date) resultMap.get("createTime")).getTime();
		}
		resultMap.put("durationInMillis", DateFormat.millisecondTOHHMMSS(dur));
		resultMap.put("piid", map.get("PROC_INST_ID_"));
		resultMap.put("assignee", assignee);
		if (Check.isNoEmpty(assignee)) {
			User user = userDao.loadObject(assignee);
			resultMap.put("assigneeName", user.getName());
		}
		return resultMap;
	}
}
