<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="language" content="en" />
<meta name="author" content="OwlFocus">
<title>流程设计器</title>
<link rel="shortcut icon" href="${request.getContextPath()}/static/imgs/system/favicon.ico" />
<link rel="stylesheet" type="text/css"	href="${request.getContextPath()}/static/jquery/layout/layout-default-latest.css">
<link rel="stylesheet" href="${request.getContextPath()}/static/jquery/ui/bootstrap/assets/css/bootstrap.min.css">
<link rel="stylesheet" href="${request.getContextPath()}/static/jquery/ui/bootstrap/css/custom-theme/jquery-ui-1.10.3.custom.css">
<link rel="stylesheet" href="${request.getContextPath()}/static/jquery/ui/bootstrap/assets/css/font-awesome.min.css">
<link rel="stylesheet" href="${request.getContextPath()}/static/jquery/ztree/3.5.15/css/zTreeStyle/zTreeStyle.css" type="text/css">
<!-- CUSTOMIZE/OVERRIDE THE DEFAULT CSS -->
<style type="text/css">
/* remove padding and scrolling from elements that contain an Accordion OR a content-div */
.ui-layout-center, /* has content-div */ .ui-layout-west,
	/* has Accordion */ .ui-layout-east, /* has content-div ... */
	.ui-layout-east .ui-layout-content { /* content-div has Accordion */
	padding: 0;
	overflow: hidden;
}

.ui-layout-center P.ui-layout-content {
	line-height: 1.4em;
	margin: 0; /* remove top/bottom margins from <P> used as content-div */
}

h3,h4 { /* Headers & Footer in Center & East panes */
	font-size: 1.1em;
	background: #EEF;
	border: 1px solid #BBB;
	border-width: 0 0 1px;
	padding: 7px 10px;
	margin: 0;
}

.ui-layout-east h4 { /* Footer in East-pane */
	font-size: 0.9em;
	font-weight: normal;
	border-width: 1px 0 0;
}
.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
	</style>
</head>
<body>
	
	<div class="panel ui-layout-west" style="display: none;">
		<div class="panel panel-default">
			  <div class="panel-body">
			  <div class="container-fluid">
			        <span xtype="button" config="text:'添加' , onClick : page.Add "></span>
			        <span xtype="button" config="text:'刷新' , onClick : page.Refresh "></span>
			        </div>
			        <div >
			        <span xtype="tree"
				config=" id:'workFlowTree' , url:'${request.getContextPath()}/workFlowTrees/queryDataTreeByPid',remove:page.doDelete , edit : page.doEdit,onClick : page.doLoadOper "></span>
			        </div>
			  </div>
		</div>
	</div>
	<div class="ui-layout-center" style="display: none;height:100%;">
		<div class="container-fluid" style="margin-top: 0em;height:100%;">
			<iframe id="workflowiframe" name="workflowiframe" width=100%
				height=100% frameborder=0 src=""></iframe>
		</div>
	</div>
	<#include "classpath:com/cloudcode/framework/common/ftl/require.ftl"/>
	<script type="text/javascript">
	var page={};
requirejs(['jquery','jquery','Request','jqueryui','main','layout','tree'], function( $, jQuery,Request ) {
	var params = $.cc.getIframeParams();
	var width = 720;
	var height = 450
	myLayout = $('body').layout({
			west__size : 300,
			east__size : 0,
			// RESIZE Accordion widget when panes resize
			west__onresize : $.layout.callbacks.resizePaneAccordions,
			east__onresize : $.layout.callbacks.resizePaneAccordions
		});
		page.doDelete =function (treeNode){
			 $.ajax({
			        url: '${request.getContextPath()}/workFlowTrees/deleteAll',
			        type: 'post',
			        dataType: 'json',
			        data: {'ids':treeNode.id},
			        success: function(data) {
 						  $('#btnRefresh').trigger('click');
			         }
			    });
		}
		 page.doLoadOper=function(treeNode) {
			var iframe = window.frames['workflowiframe'];
			 $.ajax({
				        url: '${request.getContextPath()}/workFlowInfos/'+treeNode.id+'/findObjectByDataId',
				        type: 'post',
				        dataType: 'json',
				        success: function(data) {
				            var object = data.result;
				       		var graph = iframe.mxDocument.graph;
									iframe.dataId = treeNode.id;
									iframe.workflowName = treeNode.text;
									if (object != null) {
										iframe.objectId = object.id;
										var mxgraphXml = object.mxgraphXml;
										var xml = iframe.createXml(mxgraphXml);
										var dec = new iframe.mxCodec(
												xml.documentElement.ownerDocument);
										dec.decode(xml.documentElement, graph
												.getModel());
									} else {
										iframe.objectId = '';
										var xml = iframe
												.createXml('<mxGraphModel><root><Workflow label="'
														+ treeNode.text
														+ '" description="" href="" id="0" hrefParams="" data="{}"><mxCell/></Workflow><Layer label="Default Layer" id="1"><mxCell parent="0"/></Layer></root></mxGraphModel>');
										var dec = new iframe.mxCodec(
												xml.documentElement.ownerDocument);
										dec.decode(xml.documentElement, graph
												.getModel());
									}
				       }
			    });
		}
		var options={};
		options.title='添加';
		options.width=1000;
		options.height=800;
		options.params={
			callback : function(data) {
			  $('#btnRefresh').trigger('click');
			}
		};
		page.doEdit=function(treeNode){
			options.url='${request.getContextPath()}/workFlowTrees/'+treeNode.id+'/update';
			Dialog.open(options);
		}
		page.Add=function(){
			options.url='${request.getContextPath()}/workFlowTrees/create';
			Dialog.open(options);
		}
		page.Refresh=function(){
		
		}
	$("[xtype]").each(function() {
		$(this).render('initRender');
	});
});
</script>
</body>
</html>
