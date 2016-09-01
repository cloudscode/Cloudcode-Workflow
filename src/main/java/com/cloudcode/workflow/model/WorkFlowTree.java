package com.cloudcode.workflow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.cloudcode.framework.model.BaseTreeNodeModelObject;
import com.cloudcode.workflow.ProjectConfig;

@Entity(name = ProjectConfig.NAME + "tree")
@Table(name = ProjectConfig.NAME + "_tree")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkFlowTree extends BaseTreeNodeModelObject<WorkFlowTree> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8998079406002177595L;
	private int deploy;

	@Column(name = "DEPLOY_", length = 1)
	public int getDeploy() {
		return deploy;
	}

	public void setDeploy(int deploy) {
		this.deploy = deploy;
	}
}
