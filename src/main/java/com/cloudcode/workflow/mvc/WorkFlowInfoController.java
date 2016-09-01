package com.cloudcode.workflow.mvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudcode.framework.controller.CrudController;
import com.cloudcode.framework.rest.ReturnResult;
import com.cloudcode.framework.service.ServiceResult;
import com.cloudcode.workflow.dao.WorkFlowInfoDao;
import com.cloudcode.workflow.model.WorkFlowInfo;
import com.google.gson.Gson;

@Controller
@RequestMapping({ "/workFlowInfos" })
public class WorkFlowInfoController extends CrudController<WorkFlowInfo>{
	@Autowired
	private WorkFlowInfoDao workFlowInfoDao;

	protected Validator getValidator() {
		return null;
	}
	
	private WorkFlowInfo workFlowInfo = new WorkFlowInfo();
	private String imgxml;
	private int deploy;
	
	private Integer imageHeight;
	private Integer imageWidth;

	public WorkFlowInfo getModel() {
		return workFlowInfo;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody void save(WorkFlowInfo workFlowInfo,HttpServletRequest request) {
		try {
			System.out.println(request.getContextPath());
			imgxml = request.getParameter("imgxml");
			imageHeight = Integer.parseInt(request.getParameter("imageHeight"));
			imageWidth = Integer.parseInt(request.getParameter("imageWidth"));
			deploy = Integer.parseInt(request.getParameter("deploy"));
			String imgxmlstr = imgxml;//.replaceAll(request.getContextPath(),
//					"");
			System.out.println("imgxmlstr********"+imgxmlstr);
			WorkFlowInfo workFlowInfo2 = workFlowInfoDao.save(
					workFlowInfo, imgxmlstr, deploy,imageHeight,imageWidth,request);
			//this.getResultMap().put("object", workFlowInfo2);
		} catch (Exception e) {
			//this.getResultMap().put("returnModel",
					//new ReturnModel(e.getMessage()));
			e.printStackTrace();
		}
		//this.returnResult();
	}
	
	@RequestMapping(value = "/{id}/findObjectByDataId")
	public @ResponseBody
	Object findObjectByDataId(@PathVariable("id") String id,HttpServletRequest request) {
		WorkFlowInfo hhXtForm = workFlowInfoDao.findObjectByDataId(id);
		if (hhXtForm != null) {
			hhXtForm.setMxgraphXml(hhXtForm.getMxgraphXml()
					.replaceAll("!gt;", "&gt;").replaceAll("!lt;", "&lt;")
					.replaceAll("!quot;", "&quot;")
					.replaceAll("!amp;", "&amp;"));
		}
		return new ServiceResult(ReturnResult.SUCCESS,hhXtForm);
	}
	@RequestMapping(value = "/{dataId}/findObjectAndStartByDataId", method = {RequestMethod.POST,RequestMethod.GET}, produces = "application/json")
	public @ResponseBody
	String findObjectAndStartByDataId(@PathVariable("dataId") String dataId) {
		Map<String, Object> map = workFlowInfoDao
				.findObjectAndStartByDataId(dataId);
		return this.returnGson(map);
	}
	@RequestMapping(value = "/{pdid}/findObjectByPdId")
	public @ResponseBody
	Object findObjectByPdId(@PathVariable("pdid") String pdid) {
		
		WorkFlowInfo hhXtForm = workFlowInfoDao
				.findObjectByPdId(pdid);
		if (hhXtForm != null) {
			hhXtForm.setMxgraphXml(hhXtForm.getMxgraphXml()
					.replaceAll("!gt;", "&gt;").replaceAll("!lt;", "&lt;")
					.replaceAll("!quot;", "&quot;")
					.replaceAll("!amp;", "&amp;"));
		}
		return hhXtForm;
	}
}
