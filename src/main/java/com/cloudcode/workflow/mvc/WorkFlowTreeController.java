package com.cloudcode.workflow.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cloudcode.framework.controller.CrudController;
import com.cloudcode.framework.rest.ReturnResult;
import com.cloudcode.framework.service.ServiceResult;
import com.cloudcode.framework.utils.BeanUpdater;
import com.cloudcode.framework.utils.Check;
import com.cloudcode.framework.utils.PageRange;
import com.cloudcode.framework.utils.PaginationSupport;
import com.cloudcode.workflow.dao.WorkFlowTreeDao;
import com.cloudcode.workflow.model.WorkFlowTree;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/workFlowTrees")
public class WorkFlowTreeController extends CrudController<WorkFlowTree> {
	@Autowired
	private WorkFlowTreeDao workFlowTreeDao;	
	
	@RequestMapping(value = "/createWorkFlowTree", method = RequestMethod.POST)
	public @ResponseBody
	Object createWorkFlowTree(@ModelAttribute WorkFlowTree workFlowTree, HttpServletRequest request) {
		workFlowTreeDao.addWorkFlowTree(workFlowTree);
		return new ServiceResult(ReturnResult.SUCCESS);
	}

	@RequestMapping(value = "/{id}/updateWorkFlowTree", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody
	Object updateWorkFlowTree(@PathVariable("id") String id,
			@ModelAttribute WorkFlowTree updateObject, HttpServletRequest request) {
		WorkFlowTree workFlowTree = workFlowTreeDao.loadObject(id);
		if (workFlowTree != null) {
			BeanUpdater.copyProperties(updateObject, workFlowTree);
			workFlowTreeDao.updateObject(workFlowTree);
			return new ServiceResult(ReturnResult.SUCCESS);
		}
		return new ServiceResult(ReturnResult.FAILURE);
	}

	@RequestMapping(value = "workFlowTreeList")
	public ModelAndView workFlowTreeList() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/tree/list.ftl");
		modelAndView.addObject("result", "cloudcode");
		return modelAndView;
	}

	@RequestMapping(value = "create")
	public ModelAndView create() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/tree/detail.ftl");
		modelAndView.addObject("result", "cloudcode");
		modelAndView.addObject("entityAction", "create");
		return modelAndView;
	}

	@RequestMapping(value = "/{id}/update")
	public ModelAndView update(@PathVariable("id") String id) {
		WorkFlowTree workFlowTree = workFlowTreeDao.loadObject(id);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/tree/detail.ftl");
		modelAndView.addObject("workFlowTree", workFlowTree);
		modelAndView.addObject("entityAction", "update");
		return modelAndView;
	}
	
	@RequestMapping(value = "/deleteAll")
	public @ResponseBody
	Object deleteAll(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		String[] arrayId = ids.split(",");
		for (String id : arrayId) {
			workFlowTreeDao.deleteObject(id);
		}
		return new ServiceResult(ReturnResult.SUCCESS);
	}
	
	@RequestMapping(value = "query", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	PaginationSupport<WorkFlowTree> query(WorkFlowTree workFlowTree, PageRange pageRange) {
		PaginationSupport<WorkFlowTree> workFlowTrees = workFlowTreeDao
				.queryPagingData(workFlowTree, pageRange);
		return workFlowTrees;
	}
	@RequestMapping(value = "queryDataTreeByPid", method = {
			RequestMethod.POST, RequestMethod.GET }, produces = "application/json")
	public @ResponseBody
	JSONArray queryDataTreeByPid(HttpServletRequest request) {
		String node =request.getParameter("id");
		List<WorkFlowTree> lists = null;
		if (Check.isEmpty(node) || "root".equals(node)) {
			lists = workFlowTreeDao.queryDataTreeByPid("root");
		}else{
			lists = workFlowTreeDao.queryDataTreeByPid(node);
		}
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

		for (WorkFlowTree workFlowTree : lists) {
			Map<String, Object> maps = new HashMap<String, Object>();
			maps.put("id", workFlowTree.getId());
			maps.put("name", workFlowTree.getName());
			//maps.put("node", workFlowTree.getNode());
			maps.put("text", workFlowTree.getName());
			maps.put("nocheck", false);
			maps.put("node", workFlowTree.getNode());
			if (Check.isEmpty(node) || "root".equals(node)) {
				maps.put("leaf","1");
				maps.put("isParent",true);
			}
			else{
				maps.put("leaf","0");
				maps.put("isParent",false);
			}
			//maps.put("isParent", workFlowTree.getNode()==null?false:true);
			listMap.add(maps);
		}
		return JSONArray.fromObject(listMap);
	}
	@RequestMapping(value = "/{node}/queryDeployTreeList")
	public @ResponseBody Object queryDeployTreeList(@PathVariable("node") String node,HttpServletRequest request) {
		return workFlowTreeDao.queryDeployTreeList(node);
	}
	
	@RequestMapping(value = "/{node}/queryDeployPdTreeList")
	public @ResponseBody Object queryDeployPdTreeList(@PathVariable("node") String node,HttpServletRequest request) {
		return workFlowTreeDao.queryDeployPdTreeList(node);
	}
	
	@RequestMapping(value = "tree")
	public ModelAndView tree() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("classpath:com/cloudcode/workflow/ftl/menu/tree.ftl");
		return modelAndView;
	}
}
