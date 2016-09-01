package com.cloudcode.workflow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.core.annotation.Order;

import com.cloudcode.framework.model.BaseModelObject;
import com.cloudcode.workflow.ProjectConfig;

@Entity(name = ProjectConfig.NAME + "info")
@Table(name = ProjectConfig.NAME + "_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Order
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class WorkFlowInfo extends BaseModelObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2224152783823145242L;
	private String mxgraphXml;
	private String activitiXml;
	private byte[] activitiImg;
	private String dataId;
	private String rootObject;
	private String cellObjectList;
	private String pdid;
	
	@Lob
	public String getMxgraphXml() {
		return mxgraphXml;
	}

	public void setMxgraphXml(String mxgraphXml) {
		this.mxgraphXml = mxgraphXml;
	}

	@Lob
	public String getActivitiXml() {
		return activitiXml;
	}

	public void setActivitiXml(String activitiXml) {
		this.activitiXml = activitiXml;
	}

	@Lob
	public byte[] getActivitiImg() {
		return activitiImg;
	}

	public void setActivitiImg(byte[] activitiImg) {
		this.activitiImg = activitiImg;
	}

	@Column(nullable = false, length = 36)
	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	@Lob
	public String getRootObject() {
		return rootObject;
	}

	public void setRootObject(String rootObject) {
		this.rootObject = rootObject;
	}

	@Lob
	public String getCellObjectList() {
		return cellObjectList;
	}

	public void setCellObjectList(String cellObjectList) {
		this.cellObjectList = cellObjectList;
	}
	@Column(length = 64, name = "PDID")
	public String getPdid() {
		return pdid;
	}

	public void setPdid(String pdid) {
		this.pdid = pdid;
	}
}
