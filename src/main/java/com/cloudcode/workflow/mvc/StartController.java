package com.cloudcode.workflow.mvc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudcode.framework.controller.CrudController;
import com.cloudcode.framework.utils.Check;
import com.cloudcode.framework.utils.DateFormat;
import com.cloudcode.framework.utils.MessageException;
import com.cloudcode.framework.utils.PageRange;
import com.cloudcode.workflow.dao.ProcessInstanceDao;
import com.cloudcode.workflow.dao.TaskWfDao;
import com.cloudcode.workflow.model.WorkFlowInfo;

@Controller
@RequestMapping({ "/starts" })
public class StartController extends CrudController<WorkFlowInfo>{
	@Autowired
	private TaskWfDao taskWfDao;
	@Autowired
	private ProcessInstanceDao startDao;
	private String pdkey;
	private String workflowName;
	protected Validator getValidator() {
		return null;
	}
	/**
	 * 流程设计器
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/{id}/startByObject")
	public void startByObject(@PathVariable("id") String id,HttpServletRequest request) {
		try {
			Map<String, Object> paraMap =null;// Request.getParamMapByRequest(request);
			paraMap.remove("pdkey");
			paraMap.remove("workflowName");
			String workflowName_ = "";
			if (Check.isNoEmpty(workflowName)) {
				workflowName_=workflowName.replace("-date", " "+DateFormat.getDate());
			}
			startDao.start(pdkey,workflowName_,paraMap);
		} catch (MessageException e) {
			
		}
	
	}
	/**
	 * 流程实例
	 * @param pdkey
	 * @param pageRange
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/{id}/queryPagingData", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Object queryPagingData(@PathVariable("pdkey") String pdkey, PageRange pageRange,HttpServletRequest request) {
		Integer allpi =Integer.parseInt(request.getParameter("allpi"));
		return  startDao.queryPagingData(pageRange, pdkey,allpi);
	}
	@RequestMapping(value = "/{id}/end")
	public void end(@PathVariable("id") String id) {
		startDao.end(id);
	}
	@RequestMapping(value = "/{id}/deleteByIds")
	public void deleteByIds(@PathVariable("id") String id) {
		startDao.deleteByIds(id);
	}
	@RequestMapping(value = "/{id}/updatePiServiceDataByBusId")
	public void updatePiServiceDataByBusId(@PathVariable("objectId") String objectId,HttpServletRequest request) {
		String busData = request.getParameter("busData");
		startDao.updatePiServiceDataByBusId(objectId,busData);
	}
	@RequestMapping(value = "/{id}/findPiServiceDataByBusId")
	public void findPiServiceDataByBusId(@PathVariable("objectId") String objectId,HttpServletRequest request) {
		startDao.findPiServiceDataByBusId(objectId);
	}
}
