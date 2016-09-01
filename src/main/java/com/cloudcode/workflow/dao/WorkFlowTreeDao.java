package com.cloudcode.workflow.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cloudcode.framework.dao.TreeNodeModelObjectDao;
import com.cloudcode.framework.dao.impl.BaseTreeNodeDaoImpl;
import com.cloudcode.framework.utils.Check;
import com.cloudcode.framework.utils.HQLObjectParamList;
import com.cloudcode.framework.utils.HQLParamList;
import com.cloudcode.framework.utils.Json;
import com.cloudcode.framework.utils.PageRange;
import com.cloudcode.framework.utils.PaginationSupport;
import com.cloudcode.framework.utils.UUID;
import com.cloudcode.workflow.ProjectConfig;
import com.cloudcode.workflow.model.WorkFlowInfo;
import com.cloudcode.workflow.model.WorkFlowTree;

@Repository
public class WorkFlowTreeDao extends BaseTreeNodeDaoImpl<WorkFlowTree> {

	@Resource(name = ProjectConfig.PREFIX + "workFlowTreeDao")
	private TreeNodeModelObjectDao<WorkFlowTree> workFlowTreeDao;

	@Autowired
	WorkFlowInfoDao workFlowInfoDao;

	public void update(String id, String key, Object value) {
		workFlowTreeDao.updateProperty(WorkFlowTree.class, id, key, value);
	}

	public void addWorkFlowTree(WorkFlowTree entity) {
		if (null != entity.getId() && "".equals(entity.getId())) {
			entity.setId(UUID.generateUUID());
		}
		workFlowTreeDao.createObject(entity);
	}

	public PaginationSupport<WorkFlowTree> queryPagingData(WorkFlowTree hhXtCd,
			PageRange pageRange) {
		HQLParamList hqlParamList = new HQLParamList();
		List<Object> list = null;
		return this.queryPaginationSupport(WorkFlowTree.class, hqlParamList,
				pageRange);
	}

	public List<WorkFlowTree> queryDataTreeByPid(String node) {
		HQLObjectParamList hqlParamList = new HQLObjectParamList()
				.addCondition(Restrictions.eq("node", node));

		List<WorkFlowTree> orgs = workFlowTreeDao.queryTreeList(
				WorkFlowTree.class, hqlParamList);
		return orgs;
	}

	public List<WorkFlowTree> queryTreeList(List<Object> paramList) {
		return workFlowTreeDao.queryTreeList(WorkFlowTree.class, paramList);
	}

	public List<WorkFlowTree> queryDeployTreeList(String node) {
		return this.queryTreeList(new HQLParamList().addCondition(
				Restrictions.eq("node", node)).addCondition(
				Restrictions.or(Restrictions.eq("deploy", 1),
						Restrictions.eq("leaf", 0))));
	}

	public List<WorkFlowTree> queryDeployPdTreeList(String node) {
		List<WorkFlowTree> hhWorkFlowTrees = this
				.queryTreeList(new HQLParamList().addCondition(
						Restrictions.eq("node", node)).addCondition(
						Restrictions.or(Restrictions.eq("deploy", 1),
								Restrictions.eq("leaf", 0))));
		addTreeRevNode(hhWorkFlowTrees);
		return hhWorkFlowTrees;
	}

	private void addTreeRevNode(List<WorkFlowTree> hhWorkFlowTrees) {
		for (WorkFlowTree hhWorkFlowTree : hhWorkFlowTrees) {
			if (Check.isNoEmpty(hhWorkFlowTree.getChildren())) {
				addTreeRevNode(hhWorkFlowTree.getChildren());
			}
			if (hhWorkFlowTree.getLeaf() == 1
					&& hhWorkFlowTree.getDeploy() == 1) {
				List<WorkFlowInfo> hhWorkFlowInfoList = workFlowInfoDao
						.findObjectRevByDataId(hhWorkFlowTree.getId());

				List<WorkFlowTree> hhWorkFlowTrees2 = new ArrayList<WorkFlowTree>();
				for (WorkFlowInfo hhWorkFlowInfo : hhWorkFlowInfoList) {
					WorkFlowTree workFlowTree = new WorkFlowTree();
					workFlowTree.setId(hhWorkFlowInfo.getPdid());
					String rev = hhWorkFlowInfo.getPdid().substring(
							hhWorkFlowInfo.getPdid().indexOf(":") + 1,
							hhWorkFlowInfo.getPdid().lastIndexOf(":"));

					Map<String, Object> rootmap = Json.toMap(hhWorkFlowInfo
							.getRootObject());
					workFlowTree.setText(rootmap.get("label") + "：版本：" + rev);
					workFlowTree.setLeaf(1);
					hhWorkFlowTrees2.add(workFlowTree);
				}
				if (hhWorkFlowTrees2.size() > 0) {
					hhWorkFlowTree.setParent(true);
					hhWorkFlowTree.setExpanded(1);
					hhWorkFlowTree.setChildren(hhWorkFlowTrees2);
					hhWorkFlowTree.setName(hhWorkFlowTree.getName() + "（流程）");
				}
			}
		}
	}
}
