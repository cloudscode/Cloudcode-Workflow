<!DOCTYPE html>
<html lang="en">
<head>
 	<title>已办任务</title>
    <#include "classpath:com/cloudcode/framework/common/ftl/head.ftl"/>
</head>
<body data-spy="scroll" data-target=".bs-docs-sidebar" data-twttr-rendered="true">
<div id="dialogDiv">
	<div class="container" id="layout">
			<div class="col-lg-9 col-sm-9" id="article">
			<section id="button">
			        <button id="select" class="ui-button-primary">查看</button>
			        <button id="pipic" class="ui-button-warning">查看流程图</button>
			        <button id="refresh" class="ui-button-info">刷新</button>
			</section>
			</div>
	</div>
	<div class="row">
	    <div class="col-lg-12 col-sm-12">
	        <table id="jqGrid01" style="width:100%;"></table>
	        <div id="jqGridPager01"></div>
	    </div>
	</div>
<#include "classpath:com/cloudcode/framework/common/ftl/vendor.ftl"/>
<!--jqGrid-->
<script src="${request.getContextPath()}/static/jquery/ui/bootstrap/third-party/jqGrid/jqGrid/js/i18n/grid.locale-cn.js" type="text/javascript"></script>
<script src="${request.getContextPath()}/static/jquery/ui/bootstrap/third-party/jqGrid/jqGrid/js/jquery.jqGrid.js" type="text/javascript"></script>
<!--end jqGrid-->
<script src="${request.getContextPath()}/wf/js/workflow.js" type="text/javascript"></script>
<script type="text/javascript">
var grid = null;
$(function(){
    if ($.fn.jqGrid){
        grid = $("#jqGrid01").jqGrid({
            url:"${request.getContextPath()}/tasks/collection/queryHiTaskListByAssignee",
            datatype: "json",
            height: 250,
            rowNum: 10,
            prmNames : {PageRange:{page:"page",rows:"rows"}  },
            rowList: [10,20,30],
            colNames:['Id', '流程标识','流程名称','办理人','节点名称','流程开始时间','流程结束时间','耗时'],
            colModel:[
                {name:'id',index:'id', width:60, hidden:true},
                {name:'piid',index:'piid', width:90},
                {name:'piName',index:'piName', width:90},
                {name:'assigneeName',index:'assigneeName', width:90},
                {name:'name',index:'name', width:90},
                {name:'startTime',index:'startTime', width:90},
                {name:'endTime',index:'endTime', width:90},
                {name:'durationInMillis',index:'durationInMillis', width:90}
            ],
            autowidth: true,
            height: "auto",            
            pager: "#jqGridPager01",
            viewrecords: true,
            caption: "已办任务",
            hidegrid:false,
            multiselect: true,
            altRows: true
        });     
    }
function doManager(actionType) {
var id; 
	id = grid.jqGrid('getGridParam','selarrrow');
	  if(id.toString() != null && id.toString() != ""){
		  	if(id.length >1){
			  	ajaxframework.createDialog("操作提示！","请选择一条要编辑的数据！",{});
			  	return ;
		  	}
		  	WorkFlow.manager({
				id : row.id,
				actionType : actionType,
				callback : doRefresh
			});
	}else{
    	ajaxframework.createDialog("操作提示！","请选择一条要编辑的数据！",{});
    }		
}
$( "#select" ).click(function(){
	doManager(select);
});
$( "#pipic" ).click(function(){
	 var id; 
	id = grid.jqGrid('getGridParam','selarrrow');
	  if(id.toString() != null && id.toString() != ""){
		  	if(id.length >1){
			  	ajaxframework.createDialog("操作提示！","请选择一条要编辑的数据！",{});
			  	return ;
		  	}
		  	WorkFlow.showpic(row);
	}else{
    	ajaxframework.createDialog("操作提示！","请选择一条要编辑的数据！",{});
    }	
});

$( "#refresh" ).click(function(){
	grid.trigger('reloadGrid');	
});
 $("#layout button,.button,#sampleButton").button();
}); 
</script>
	<div id="divInDialog" style="display:none">
	</div>
</div>
</body>
</html>