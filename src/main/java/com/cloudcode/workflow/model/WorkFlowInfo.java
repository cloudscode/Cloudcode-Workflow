package com.cloudcode.workflow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cloudcode.framework.model.BaseModelObject;
import com.cloudcode.workflow.ProjectConfig;

@Entity(name = ProjectConfig.NAME + "info")
@Table(name = ProjectConfig.NAME + "_info")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Order
//@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class WorkFlowInfo extends BaseModelObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2224152783823145242L;
	@Column(columnDefinition = "LONGTEXT")
	private String mxgraphXml;
	@Column(columnDefinition = "LONGTEXT")
	private String activitiXml;
	@Column(columnDefinition = "LONGBLOB")
	private byte[] activitiImg;
	@Column(nullable = false, length = 36)
	private String dataId;
	@Column(columnDefinition = "LONGTEXT")
	private String rootObject;
	@Column(columnDefinition = "LONGTEXT")
	private String cellObjectList;
	@Column(length = 64, name = "PDID")
	private String pdid;
	
	public String getMxgraphXml() {
		return mxgraphXml;
	}

	public void setMxgraphXml(String mxgraphXml) {
		this.mxgraphXml = mxgraphXml;
	}

	public String getActivitiXml() {
		return activitiXml;
	}

	public void setActivitiXml(String activitiXml) {
		this.activitiXml = activitiXml;
	}

	public byte[] getActivitiImg() {
		return activitiImg;
	}

	public void setActivitiImg(byte[] activitiImg) {
		this.activitiImg = activitiImg;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getRootObject() {
		return rootObject;
	}

	public void setRootObject(String rootObject) {
		this.rootObject = rootObject;
	}

	public String getCellObjectList() {
		return cellObjectList;
	}

	public void setCellObjectList(String cellObjectList) {
		this.cellObjectList = cellObjectList;
	}
	
	public String getPdid() {
		return pdid;
	}

	public void setPdid(String pdid) {
		this.pdid = pdid;
	}
}
