<!DOCTYPE html>
<html lang="en">
<head>
   <title>流程定义管理</title>
   <#include "classpath:com/cloudcode/framework/common/ftl/head.ftl"/>
   <script type="text/javascript" src="${request.getContextPath()}/static/mxgraph/examples/editors/properties/jqueruiproperties.js"></script> 
</head>
<body data-spy="scroll" data-target=".bs-docs-sidebar" data-twttr-rendered="true"> 
<div id="dialogDiv">
<div class="container" id="layout">
<form role="form" class="form-horizontal" id="myFormId" action="${request.getContextPath()}/users/createUser" method="post">
   <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
       <button type="button" id="btnNew" class="btn btn-default">刷新</button>
       <button type="button" id="btnWFImg" class="btn btn-warning">删除</button>
    </div>
      <div class="col-sm-4">
	        <ul id="workFlowTree" class="ztree">
	    </div>
  </div>
  <div class="form-group">
	    <div class="col-sm-12">   
	    <div id="workflowiframediv"  style="overflow: visible;">
			<iframe id="workflowiframe" name="workflowiframe" width=100%
				height=100% frameborder=0
				src="jsp-workflow-wfdes-workfloweditor?hipd=1"></iframe>
		</div>
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
  		$.fn.zTree.init($("#workFlowTree"), setting);
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
		var selectNode = TreeUtil.getSelectNode('workFlowTree');
			Dialog
					.confirm({
						message : '您确认要删除数据吗？',
						yes : function(result) {
							var id = '';
							if (selectNode.name.indexOf('：版本：') > -1) {
								id = selectNode.id;
							} else if (selectNode.name.indexOf('（流程）') > -1) {
								id = BaseUtil.objsToStr(selectNode.children);
							}
							if (id) {
							
							 $.ajax({
								    url: '${request.getContextPath()}/Activitis/collection/deleteProcessDefinitionsByPdid',
								    type: 'post',
								    dataType: 'json',
								    data : {
														ids : id,
														cascade : true
													},
								    success: function(result) {debugger
								   		     if (result.success) {
															TreeUtil
																	.refresh('workFlowTree');
														}
								   		     
								       }
								});
							} else {
								Dialog.infomsg("请选择流程或流程版本！");
							}

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
        url:'${request.getContextPath()}/workFlowTrees/queryDeployPdTreeList'
       }
};
$(document).ready(function(){
	$.fn.zTree.init($("#workFlowTree"), setting);
});
function onCheck(e, treeId, treeNode) {
	if (treeNode.isParent == false) {
			$("#workflowiframediv").undisabled();
			var iframe = window.frames['workflowiframe'];
			 $.ajax({
			    url: '${request.getContextPath()}/workFlowInfos/'+treeNode.id+'/findObjectByPdId',
			    type: 'post',
			    dataType: 'json',
			    data : formObject,
			    success: function(result) {debugger
			   		     var graph = iframe.mxDocument.graph;
					iframe.workflowName = treeNode.text.substr(0, treeNode.text
							.indexOf('：'));
					iframe.pdid = treeNode.id;
					if (object != null) {
						iframe.dataId = object.dataId;
						iframe.objectId = object.id;
						var mxgraphXml = object.mxgraphXml;
						var xml = iframe.createXml(mxgraphXml);
						var dec = new iframe.mxCodec(
								xml.documentElement.ownerDocument);
						dec.decode(xml.documentElement, graph.getModel());
					} else {
						$("#workflowiframediv").disabled(
								'您选择的流程不存在，请重新选择流程，修改正在运行的流程配置！');
					}
			   		     
			       }
			});
		}
}
</script>
</div>
	<div id="divInDialog" style="display:none">
	</div>
</body>
</html>