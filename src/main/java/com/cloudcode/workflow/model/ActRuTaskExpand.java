package com.cloudcode.workflow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cloudcode.framework.model.BaseModelObject;
import com.cloudcode.workflow.ProjectConfig;

@Entity(name = ProjectConfig.NAME + "_RU_TASK_EXPAND")
@Table(name = ProjectConfig.NAME + "_RU_TASK_EXPAND")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class ActRuTaskExpand extends BaseModelObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1914133058138301738L;
	private String taskId;
	private String userId;
	private String piid;
	private int isRead;

	@Column(length = 64, name = "PIID")
	public String getPiid() {
		return piid;
	}

	public void setPiid(String piid) {
		this.piid = piid;
	}

	@Column(length = 64, name = "TASK_ID")
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(length = 36, name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "IS_READ", length = 1)
	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
}
