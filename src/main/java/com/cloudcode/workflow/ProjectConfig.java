package com.cloudcode.workflow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import com.cloudcode.framework.annotation.ModuleConfig;
import com.cloudcode.framework.bean.ProjectBeanNameGenerator;
import com.cloudcode.framework.dao.ModelObjectDao;
import com.cloudcode.framework.dao.TreeNodeModelObjectDao;
import com.cloudcode.framework.dao.impl.BaseDaoImpl;
import com.cloudcode.framework.dao.impl.BaseTreeNodeDaoImpl;
import com.cloudcode.workflow.model.ActRuTaskExpand;
import com.cloudcode.workflow.model.WorkFlowInfo;
import com.cloudcode.workflow.model.WorkFlowTree;

@ModuleConfig(name = ProjectConfig.NAME, domainPackages = { "com.cloudcode.workflow.model" })
@ComponentScan(basePackages={"com.cloudcode.workflow.*"},nameGenerator=ProjectBeanNameGenerator.class)
@PropertySource(name = "cloudcode.evn", value = { "classpath:proj.properties" })
public class ProjectConfig {
	public static final String NAME = "wf";
	public static final String PREFIX = NAME + ".";

	@Bean(name = PREFIX + "workFlowTreeDao")
	public TreeNodeModelObjectDao<WorkFlowTree> generateWorkFlowTreeDao() {
		return new BaseTreeNodeDaoImpl<WorkFlowTree>(WorkFlowTree.class);
	}

	@Bean(name = PREFIX + "actRuTaskExpandDao")
	public ModelObjectDao<ActRuTaskExpand> generateActRuTaskExpandDao() {
		return new BaseDaoImpl<ActRuTaskExpand>(ActRuTaskExpand.class);
	}

	@Bean(name = PREFIX + "workFlowInfoDao")
	public ModelObjectDao<WorkFlowInfo> generateWorkFlowInfoDao() {
		return new BaseDaoImpl<WorkFlowInfo>(WorkFlowInfo.class);
	}
}
