package com.cloudcode.workflow.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudcode.framework.controller.CrudController;
import com.cloudcode.framework.utils.CheckTree;
import com.cloudcode.framework.utils.MessageException;
import com.cloudcode.framework.utils.PageRange;
import com.cloudcode.workflow.dao.TaskWfDao;
import com.cloudcode.workflow.model.WorkFlowInfo;

@Controller
@RequestMapping({ "/tasks" })
public class TaskController extends CrudController<WorkFlowInfo>{
	@Autowired
	private TaskWfDao taskWfDao;

	private Map<String, String> userMap = new HashMap<String, String>();
	private Map<String, String> conditionMap = new HashMap<String, String>();
	
	protected Validator getValidator() {
		return null;
	}
	/**
	 * 流程设计器
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/{pdid}/showPic")
	public ResponseEntity<byte[]> showPic(@PathVariable("pdid") String pdid) throws IOException {
		InputStream inputStream = null;
		inputStream = taskWfDao.findPicByPdid(pdid);
	    final HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);
	    return new ResponseEntity<byte[]>(IOUtils.toByteArray(inputStream), headers, HttpStatus.CREATED);
	}
	/**
	 * 提交任务
	 */
	public void submit(@PathVariable("id") String id,HttpServletRequest request) {
		Map<String, Object> paraMap =null;// Request.getParamMapByRequest(request);
		try {
			taskWfDao.submit(paraMap, userMap, conditionMap);
		} catch (MessageException e) {
		}
	}
	
	@RequestMapping(value = "queryPagingData", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Map<String, Object> queryPagingData(PageRange pageRange) {
		return taskWfDao.queryTaskList(pageRange);
	}
	@RequestMapping(value = "/{taskId}/manager", method = RequestMethod.POST)
	public @ResponseBody Object manager(@PathVariable("taskId") String taskId,HttpServletRequest request) {
		String actionType =request.getParameter("actionType");
		return taskWfDao.manager(actionType, taskId);
	}
	/**
	 * 流程图
	 * @param piid
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/{piid}/queryPicHiTask", method = RequestMethod.POST)
	public @ResponseBody Object queryPicHiTask(@PathVariable("piid") String piid,HttpServletRequest request) {
		//String piid =request.getParameter("piid");
		String pdid =request.getParameter("pdid");
		List<Map<String, Object>> result = taskWfDao.queryPicHiTask(piid,pdid);
		return result;
	}
	/**
	 * 办理历史
	 * @param piid
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/{piid}/queryCommentByActivityId", method = RequestMethod.POST)
	public @ResponseBody Object queryCommentByActivityId(@PathVariable("piid") String piid,HttpServletRequest request) {
		String activityId =request.getParameter("activityId");
		return taskWfDao.queryCommentByActivityId(piid,activityId);
	}
	
	@RequestMapping(value = "/{piid}/queryHiTaskListByAssignee", method = RequestMethod.POST)
	public @ResponseBody Object queryHiTaskListByAssignee(@PathVariable("piid") String piid,PageRange pageRange,HttpServletRequest request) {
		return taskWfDao.queryHiTaskListByAssignee(pageRange);
	}
	@RequestMapping(value = "/{piid}/queryStepByActivityId", method = RequestMethod.POST)
	public Object queryStepByActivityId(@PathVariable("piid") String piid,HttpServletRequest request) {
		String pdid = request.getParameter("pdid");
		String actId = request.getParameter("activityId");
		List<CheckTree> checkTrees = taskWfDao.queryStepByActivityId(
				pdid, piid, actId);
	    return checkTrees;
	}
}
