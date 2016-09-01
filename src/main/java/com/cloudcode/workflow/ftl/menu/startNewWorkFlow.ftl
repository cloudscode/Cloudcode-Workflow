<!DOCTYPE html>
<html lang="en">
<head>
   <title>新建工作</title>
   <#include "classpath:com/cloudcode/framework/common/ftl/head.ftl"/>
   <script type="text/javascript" src="${request.getContextPath()}/static/mxgraph/examples/editors/properties/jqueruiproperties.js"></script> 
</head>
<body data-spy="scroll" data-target=".bs-docs-sidebar" data-twttr-rendered="true"> 
<div id="dialogDiv">
<div class="container" id="layout">
<form role="form" class="form-horizontal" id="myFormId" action="" method="post">
   <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
       <button type="button" id="btnNew" class="btn btn-default">新建工作</button>
       <button type="button" id="btnWFImg" class="btn btn-warning">查看流程图</button>
    </div>
  </div>
  <div class="form-group">
	    <div class="col-sm-4">
	        <ul id="workFlowTree" class="ztree">
	    </div>
	    <div class="col-sm-10">   
	    <iframe id="startserviceiframe" name="startserviceiframe" width=100%
					height=100% frameborder=0
					src="${request.getContextPath()}/workFlowMenus/msg?text=请选择要新建的流程！！"></iframe>
		</div>
    </div>
   <input type="hidden" value="" id="oid" name="id" >
</form>

</div>
<#include "classpath:com/cloudcode/framework/common/ftl/vendor.ftl"/>
<script type="text/javascript">
var hm = $("body").wHumanMsg();
var startParams=null;
var workflowName = null;
$(function () {
  $('#btnNew').click( function() {
  		var iframe = window.frames['startserviceiframe'];
		var formObject = iframe.save('start');
		if (formObject) {
			formObject.pdkey = startParams.id;
			formObject.workflowName = workflowName + '-date';
			 $.ajax({
			    url: '${request.getContextPath()}/workFlowInfos/'+treeNode.id+'/startByObject',
			    type: 'post',
			    dataType: 'json',
			    data : formObject,
			    success: function(result) {debugger
			   		     
			   		     
			       }
			});
		}
  
  });
   $('#btnWFImg').click( function() {
	   $( "#divInDialog" ).dialog({
		 	modal: true,
		 	width:800,
			open: function(event, ui) {
	 		 	  $(this).load('${request.getContextPath()}/workFlowMenus/'+startParams.pdid+'/toShowPic');
		 	 },	   
		    close: function (event, ui) {  
		    }  
		});
  });
});
var setting = {
	edit: {
		enable: true
	},			
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {			
		onClick: onCheck				
	},
	async: {
        enable: true,
        async : true, 
      	dataType: 'JSON',
        //返回的JSON数据的名字
        dataName: 'treeNodes',
        autoParam:["id"],
        url:'${request.getContextPath()}/workFlowTrees/queryDataTreeByPid'
       }
};
$(document).ready(function(){
	$.fn.zTree.init($("#workFlowTree"), setting);
});
function onCheck(e, treeId, treeNode) {
	//if (treeNode.leaf == 1) {
		 $.ajax({
		    url: '${request.getContextPath()}/workFlowInfos/'+treeNode.id+'/findObjectAndStartByDataId',
		    type: 'post',
		    dataType: 'json',
		    success: function(result) {debugger
		   		     startParams = result;
		   		     workflowName =  ' '+ treeNode.text;
		   		     $('#startserviceiframe').attr('src',
										ajaxframework.getHref(result.startEvent.href,{type:'workflow'}));
		       }
		});
	//}
}
</script>
</div>
	<div id="divInDialog" style="display:none">
	</div>
</body>
</html>