package com.cloudcode.workflow.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cloudcode.framework.controller.CrudController;
import com.cloudcode.workflow.dao.WorkFlowInfoDao;
import com.cloudcode.workflow.model.WorkFlowInfo;

@Controller
@RequestMapping({ "/workFlowMenus" })
public class WorkFlowMenuController extends CrudController<WorkFlowInfo>{
	@Autowired
	private WorkFlowInfoDao workFlowInfoDao;

	protected Validator getValidator() {
		return null;
	}
	/**
	 * 流程设计器
	 * @return
	 */
	@RequestMapping(value = "/{pdid}/design")
	public ModelAndView design(@PathVariable("pdid") String pdid) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/menu/designWF.ftl");
		return modelAndView;
	}
	/**
	 * 新建工作
	 * @return
	 */
	@RequestMapping(value = "/{pdid}/startNewWorkFlow")
	public ModelAndView startNewWorkFlow(@PathVariable("pdid") String pdid) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/menu/startNewWorkFlow.ftl");
		return modelAndView;
	}
	/**
	 * 我的工作
	 * @return
	 */
	@RequestMapping(value = "rutasklist")
	public ModelAndView rutasklist() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/menu/rutasklist.ftl");
		return modelAndView;
	}
	/**
	 * 已办任务
	 * @return
	 */
	@RequestMapping(value = "hitasklist")
	public ModelAndView hitasklist() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/menu/hitasklist.ftl");
		return modelAndView;
	}
	/**
	 * 我发起的流程
	 * @return
	 */
	@RequestMapping(value = "hipi")
	public ModelAndView hipi() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/menu/hipi.ftl");
		modelAndView.addObject("entityAction", "create");
		return modelAndView;
	}
	/**
	 * 流程定义管理
	 * @return
	 */
	@RequestMapping(value = "hipd")
	public ModelAndView hipd() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/menu/hipd.ftl");
		modelAndView.addObject("entityAction", "create");
		return modelAndView;
	}
	
	@RequestMapping(value = "/{pdid}/toShowPic")
	public ModelAndView toShowPic(@PathVariable("pdid") String pdid) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/pd/pdpic.ftl");
		modelAndView.addObject("pdid", pdid);
		return modelAndView;
	}
	@RequestMapping(value = "/{pdid}/hipd")
	public ModelAndView hipd(@PathVariable("pdid") String pdid) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/pd/hipd.ftl");
		modelAndView.addObject("pdid", pdid);
		return modelAndView;
	}
	@RequestMapping(value = "/{pdid}/startpipage")
	public ModelAndView startpipage(@PathVariable("pdid") String pdid) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/pd/startpipage.ftl");
		modelAndView.addObject("pdid", pdid);
		return modelAndView;
	}
	@RequestMapping(value = "/{pdid}/tasksubmit")
	public ModelAndView tasksubmit(@PathVariable("pdid") String pdid) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/task/tasksubmit.ftl");
		modelAndView.addObject("pdid", pdid);
		return modelAndView;
	}
	/**
	 * 我的工作
	 * @param pdid
	 * @return
	 */
	@RequestMapping(value = "/{pdid}/rutasklist")
	public ModelAndView rutasklist(@PathVariable("pdid") String pdid) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/task/rutasklist.ftl");
		modelAndView.addObject("pdid", pdid);
		return modelAndView;
	}
	@RequestMapping(value = "/{pdid}/pipic")
	public ModelAndView pipic(@PathVariable("pdid") String pdid) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/task/pipic.ftl");
		modelAndView.addObject("pdid", pdid);
		return modelAndView;
	}
	@RequestMapping(value = "/{pdid}/hitasklist")
	public ModelAndView hitasklist(@PathVariable("pdid") String pdid) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/task/hitasklist.ftl");
		modelAndView.addObject("pdid", pdid);
		return modelAndView;
	}
	@RequestMapping(value = "/{pdid}/hipi")
	public ModelAndView hipi(@PathVariable("pdid") String pdid) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/task/hipi.ftl");
		modelAndView.addObject("pdid", pdid);
		return modelAndView;
	}
	/**
	 * 办理历史
	 * @param piid
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/{piid}/acthi")
	public ModelAndView acthi(@PathVariable("piid") String piid,HttpServletRequest request) {
		String activityId = request.getParameter("activityId");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/task/acthi.ftl");
		modelAndView.addObject("piid", piid);
		modelAndView.addObject("activityId", activityId);
		return modelAndView;
	}
	@RequestMapping(value = "/{objectId}/formtest")
	public ModelAndView formtest(@PathVariable("objectId") String objectId) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/service/formtest.ftl");
		modelAndView.addObject("objectId", objectId);
		return modelAndView;
	}
	@RequestMapping(value = "/{pdid}/servicemain")
	public ModelAndView servicemain(@PathVariable("pdid") String pdid) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/service/servicemain.ftl");
		modelAndView.addObject("pdid", pdid);
		return modelAndView;
	}
	@RequestMapping(value = "/msg")
	public ModelAndView msg(HttpServletRequest request) {
		String text = request.getParameter("text");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/msg/msg.ftl");
		modelAndView.addObject("text", "请选择要新建的流程！！！");
		return modelAndView;
	}
}
