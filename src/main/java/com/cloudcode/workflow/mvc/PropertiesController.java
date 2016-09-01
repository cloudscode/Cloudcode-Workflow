package com.cloudcode.workflow.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cloudcode.framework.controller.CrudController;
import com.cloudcode.workflow.dao.WorkFlowInfoDao;
import com.cloudcode.workflow.model.WorkFlowInfo;

@Controller
@RequestMapping({ "/properties" })
public class PropertiesController extends CrudController<WorkFlowInfo>{
	@Autowired
	private WorkFlowInfoDao workFlowInfoDao;

	protected Validator getValidator() {
		return null;
	}
	@RequestMapping(value = "userTask")
	public ModelAndView userTask() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/properties/userTask.ftl");
		return modelAndView;
	}
	@RequestMapping(value = "sequenceFlow")
	public ModelAndView sequenceFlow() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/properties/sequenceFlow.ftl");
		modelAndView.addObject("entityAction", "create");
		return modelAndView;
	}
	@RequestMapping(value = "gateway")
	public ModelAndView gateway() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/properties/gateway.ftl");
		modelAndView.addObject("entityAction", "create");
		return modelAndView;
	}
	@RequestMapping(value = "workflow")
	public ModelAndView workflow() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/properties/workflow.ftl");
		modelAndView.addObject("entityAction", "create");
		return modelAndView;
	}
	@RequestMapping(value = "startEvent")
	public ModelAndView startEvent() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/properties/startEvent.ftl");
		modelAndView.addObject("entityAction", "create");
		return modelAndView;
	}
	@RequestMapping(value = "endEvent")
	public ModelAndView endEvent() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/properties/endEvent.ftl");
		modelAndView.addObject("entityAction", "create");
		return modelAndView;
	}
	
}
