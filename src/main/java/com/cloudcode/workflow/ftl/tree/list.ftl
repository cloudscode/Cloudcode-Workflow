<!DOCTYPE html>
<html lang="en">
<head>
 	<title>集团信息</title>
    <#include "classpath:com/cloudcode/framework/common/ftl/head.ftl"/>
</head>
<body data-spy="scroll" data-target=".bs-docs-sidebar" data-twttr-rendered="true">
<div id="dialogDiv">
	<div class="container" id="layout">
			<div class="col-lg-9 col-sm-9" id="article">
			<section id="button">
			        <button id="openwindow" class="ui-button-success">添加</button>
			        <button id="edit" class="ui-button-primary">编辑</button>
			        <button id="view" class="ui-button-warning">查看</button>
			        <button id="delete" class="ui-button-danger">删除</button>
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
<script type="text/javascript">
var grid = null;
$(function(){
    if ($.fn.jqGrid){
        grid = $("#jqGrid01").jqGrid({
            url:"${request.getContextPath()}/workFlowTrees/query",
            datatype: "json",
            height: 250,
            rowNum: 10,
            prmNames : {PageRange:{page:"page",rows:"rows"}  },
            rowList: [10,20,30],
            colNames:['Id', '名称','编码','简称'],
            colModel:[
                {name:'id',index:'id', width:60, hidden:true},
                {name:'name',index:'name', width:100},
                {name:'code',index:'node', width:90},
                {name:'shortName',index:'shortName', width:90}
            ],
            autowidth: true,
            height: "auto",            
            pager: "#jqGridPager01",
            viewrecords: true,
            caption: "集团信息",
            hidegrid:false,
            multiselect: true,
            altRows: true
        });     
    }

$( "#openwindow" ).click(function(){
	$( "#divInDialog" ).dialog({
	 	 modal: true,
	 	 width:800,
		 open: function(event, ui) {
 		 	  $(this).load('${request.getContextPath()}/workFlowTrees/create');
	 	 },	   
	    close: function (event, ui) {  
	       grid.trigger("reloadGrid");
	    }  
	});
});
var divInDialogs =null;
$( "#edit" ).click(function(){
	var id; 
	id = grid.jqGrid('getGridParam','selarrrow');
	  if(id.toString() != null && id.toString() != ""){
		  	if(id.length >1){
			  	ajaxframework.createDialog("操作提示！","请选择一条要编辑的数据！",{});
			  	return ;
		  	}
			divInDialogs =$( "#divInDialog" ).dialog({
			 	 modal: true,
			 	 width:800,
				open: function(event, ui) {
					$(this).load('${request.getContextPath()}/workFlowTrees/'+id+'/update');
			  },	   
		    close: function (event, ui) {  
		       grid.trigger("reloadGrid");
		    }  	   
		});
	}else{
		    	ajaxframework.createDialog("操作提示！","请选择一条要编辑的数据！",{});
		    }
}); 
$( "#delete" ).click(function(){
		var id;
	     id = $("#jqGrid01").jqGrid('getGridParam','selarrrow');
	     if(id.toString() != null && id.toString() != ""){
		    $.ajax({
			        url: '${request.getContextPath()}/workFlowTrees/deleteAll',
			        type: 'post',
			        dataType: 'json',
			        data: {'ids':id.toString()},
			        success: function(data) {
 						grid.trigger("reloadGrid");
			                 }
			    });
			    }else{
			    	ajaxframework.createDialog("操作提示！","请选择要删除的数据！",{});
			    }
	});
	$( "#view" ).click(function(){
		$('body').wHumanMsg('theme', 'red').wHumanMsg('msg', 'Testing red');
		grid.trigger('reloadGrid');
	});
	$( "#refresh" ).click(function(){
		grid.trigger('reloadGrid');
		var hm = $("body").wHumanMsg();
		hm.wHumanMsg('数据保存成功！', {theme: 'white', fadeIn: 300, fadeOut: 300});
	});
	 $("#layout button,.button,#sampleButton").button();
}); 
</script>
	<div id="divInDialog" style="display:none">
	</div>
</div>
</body>
</html>