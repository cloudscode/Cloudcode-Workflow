package com.cloudcode.workflow.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudcode.framework.controller.CrudController;
import com.cloudcode.workflow.dao.ActivitiDao;
import com.cloudcode.workflow.model.WorkFlowInfo;

@Controller
@RequestMapping({ "/activitis" })
public class ActivitiController extends CrudController<WorkFlowInfo> {
	@Autowired
	ActivitiDao activitiDao;

	protected Validator getValidator() {
		return null;
	}
	@RequestMapping(value = "/{pdid}/showPic")
	public void deleteProcessDefinitionsByPdid(@PathVariable("pdid") String pdid,HttpServletRequest request) {
		String ids =request.getParameter("ids");
		Boolean cascade =Boolean.valueOf(request.getParameter("cascade"));
		activitiDao.deleteProcessDefinitionsByPdid(ids, cascade);
	}

}
