<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="language" content="en" />
<meta name="author" content="OwlFocus">
<title>流程设计器</title>
<#assign basePath="/cccommon/opensource/mxgraph/examples/editors/">
<style type="text/css" media="screen">
div.base {
	position: absolute;
	overflow: hidden;
	white-space: nowrap;
	font-family: Arial;
	font-size: 8pt;
}

div.base#graph {
	border-style: solid;
	border-color: #F2F2F2;
	border-width: 1px;
	background:
		url('/cccommon/opensource/mxgraph/examples/editors/images/grid.gif');
}
</style>
<#include "classpath:com/cloudcode/framework/common/ftl/require.ftl"/>
<script type="text/javascript">
var page={};

</script>
<script type="text/javascript">
mxBasePath = '/cccommon/opensource/mxgraph/src';
</script>
<script type="text/javascript" src="${basePath}js/mxclient-2.8.2.0.js">
</script>
<script type="text/javascript" src="${basePath}js/mxApplication.js">
</script>
<script type="text/javascript">
mxConstants.DEFAULT_HOTSPOT = 1;
// Enables guides
mxGraphHandler.prototype.guidesEnabled = true;
// Alt disables guides
mxGuide.prototype.isEnabledForEvent = function(evt) {
	return !mxEvent.isAltDown(evt);
};

// Enables snapping waypoints to terminals
mxEdgeHandler.prototype.snapToTerminals = true;

window.onbeforeunload = function() {
	return mxResources.get('changesLost');
};
function createXml(str) {
    if (document.all) {
        var xmlDom = new ActiveXObject("Microsoft.XMLDOM");
        xmlDom.loadXML(str);
        return xmlDom;
    }else{
    return new DOMParser().parseFromString(str, "text/xml");
    }
};

function attToObject(attributes){
	var object = {};
	for ( var i = 0; i < attributes.length; i++) {
			object[attributes[i].nodeName]=attributes[i].nodeValue;
	}
	return object;
}
	<#--var dataId = '<%=request.getParameter("dataId") == null ? "" : request
					.getParameter("dataId")%>';
	var workflowName = '<%=request.getParameter("workflowName") == null ? "" : request
					.getParameter("workflowName")%>';
	var url = '<%=request.getParameter("saveUrl") == null ? "workflow-WorkFlowInfo-save" : request.getParameter("saveUrl")%>';
	var findUrl = '<%=request.getParameter("findUrl") == null ? "" : request.getParameter("findUrl")%>';
	var objectId = '<%=request.getParameter("objectId") == null ? "" : request.getParameter("objectId")%>';
	var mxDocument = null;-->
	var dataId ='${dataId!''}';
	var workflowName ='${workflowName!'22'}';
	var url =contextPath+'${url!'/workFlowInfos/save'}';
	var findUrl ='${findUrl!''}';
	var objectId ='${objectId!''}';
	var mxDocument = null;
	
</script>
<script type="text/javascript">
	<#--
	String editType = Convert.toString(request.getParameter("hipd")).equals("1")?"-pdrev":"";
	if(Convert.toString(request.getParameter("hipd")).equals("2")){
		editType="-pdrev-2";
	}
-->
<#assign editType="">
	var Init = {
		init : function() {
			new mxApplication(
					'/cccommon/opensource/mxgraph/examples/editors/config/workfloweditor${editType}.xml');
		}
	}
	var uitype = 'jquery';
	function init() {
		
	}
	window.onload = function(){
	 	Init.init();
	}
	function	initMxgraph(){
		findObject();
		requirejs(['jquery','Doing'], function( $,Doing ) {
			Doing.hide();
		});
	}
	
	function findObject(){
		if(findUrl && objectId){
			
			Request
			.request(
					findUrl,
					{
						data : {
							id : objectId
						},
						defaultMsg : false,
						callback : function(data) {
							var object = data.result;
							var graph = mxDocument.graph;
							if (object != null) {
								var mxgraphXml = object.mxgraphXml;
								var xml = createXml(mxgraphXml);
								var dec = new mxCodec(
										xml.documentElement.ownerDocument);
								dec.decode(xml.documentElement, graph
										.getModel());
							} else {
								initDefaultMxgraph();
							}
						}
					});
		}else{
			initDefaultMxgraph();
		}
	}
	
	function initDefaultMxgraph(){
		var graph = mxDocument.graph;
		var xml = createXml('<mxGraphModel><root><Workflow label="'
				+ workflowName
				+ '" description="" href="" id="0" hrefParams="" data="{}"><mxCell/></Workflow><Layer label="Default Layer" id="1"><mxCell parent="0"/></Layer></root></mxGraphModel>');
		var dec = new mxCodec(
				xml.documentElement.ownerDocument);
		dec.decode(xml.documentElement, graph
				.getModel());
	}
requirejs(['jquery','jquery','Request','main'], function( $, jQuery,Request ) {
	 page.saveMxgraphData=function(page,editor,deploy) {
		
		var imageHeight = 100;
		var imageWidth = 100;
		
		var widgetHeight = 40;
		var widgetWidth = 40;
		
		var graph = editor.graph;
		var xmlDoc = mxUtils.createXmlDocument();
		var root = xmlDoc.createElement('output');
		xmlDoc.appendChild(root);
		var xmlCanvas = new mxXmlCanvas2D(root);
		var imgExport = new mxImageExport();
		imgExport.drawState(graph.getView().getState(graph.model.root),
				xmlCanvas);
		var bounds = graph.getGraphBounds();
		var w = Math.round(bounds.x + bounds.width + 4);
		var h = Math.round(bounds.y + bounds.height + 4);
		var imgxml = mxUtils.getXml(root);
		var nodexml = page.writeGraphModel(editor.linefeed);
		//if (this.escapePostData) {
		//   imgxml = encodeURIComponent(imgxml);
		// }
		var text = '';
		var root_cell = page.graph.getModel().getRoot();
		var root_cell_attributes = root_cell.value.attributes;
		var rootObject = attToObject(root_cell_attributes);

			$.extend(rootObject, $.cc.toObject(rootObject.data));
			if(rootObject.href){
				rootObject.href=rootObject.href+'?hrefckeditor='+rootObject.hrefckeditor;
			}

		rootObject.data = null;
		rootObject.label = workflowName;
		var nodeCells = root_cell.children[0].children;

		var startEventId = null;
		var startEventTargetRefId = null;

		var cellObjectList = [];
		if (nodeCells != null) {

			for (var i = 0; i < nodeCells.length; i++) {
				var nodeCell = nodeCells[i];
				var attributes = nodeCell.value.attributes;
				var cellObject = attToObject(attributes);
					$.extend(cellObject, $.cc.toObject(cellObject.data));
					if(cellObject.href){
						cellObject.href=cellObject.href+'?hrefckeditor='+cellObject.hrefckeditor;
					}
				var geometry = nodeCell.geometry;
				cellObject.id = cellObject.type + nodeCell.id;
				cellObject.x = geometry.x;
				cellObject.y = geometry.y;
				cellObject.width = geometry.width;
				cellObject.height = geometry.height;

				if(imageHeight<cellObject.y){
					imageHeight=cellObject.y;
					widgetHeight=cellObject.height;
				}
				if(imageWidth<cellObject.x){
					imageWidth=cellObject.x;
					widgetWidth=cellObject.width;
				}

				if (cellObject.type == 'sequenceFlow') {
					var source = nodeCell.source;
					var target = nodeCell.target;
					var sourceObject = attToObject(source.value.attributes);
					var targetObject = attToObject(target.value.attributes);
					cellObject.sourceRef = sourceObject.type + source.id;
					cellObject.targetRef = targetObject.type + target.id;
				}

				if (cellObject.type == 'startEvent') {
					startEventId = cellObject.id;
				}

				cellObject.data = null;
				cellObjectList.push(cellObject);
			}
			for (var i = 0; i < cellObjectList.length; i++) {
				var cellObject = cellObjectList[i];
				if (cellObject.type == 'sequenceFlow') {
					if (cellObject.sourceRef == startEventId) {
						rootObject.startEventTargetRefId = cellObject.targetRef;
						break;
					}
				}
			}
		}
		nodexml = nodexml.replace(/&gt;/g, '!gt;').replace(/&lt;/g, '!lt;')
				.replace(/&quot;/g, '!quot;').replace(/&amp;/g, '!amp;');
			cellObjectList = $.cc.toString(cellObjectList);
			rootObject = $.cc.toString(rootObject);
			
			imageWidth=imageWidth+widgetWidth+30;
			imageHeight=imageHeight+widgetHeight+30;
			
			Request.request(url, {
				data : {
					cellObjectList : cellObjectList,
					rootObject : rootObject,
					mxgraphXml : nodexml,
					imgxml : imgxml,
					text : text,
					dataId : dataId,
					id : objectId,
					imageWidth:imageWidth,
					imageHeight:imageHeight,
					deploy : deploy,
					pdid : pdid
				}
			}, function(result) {
			});
	}
	});
	var pdid = null;
	
	function showProperties(page,editor, cell){
		cell = cell || page.graph.getSelectionCell();

		var rootcell = page.graph.getCurrentRoot();
		if (rootcell == null) {
			rootcell = page.graph.getModel().getRoot();
		}

		var rootattributes = rootcell.value.attributes;
		var rootobject = {id:rootcell.id};
		for ( var i = 0; i < rootattributes.length; i++) {
				if(rootattributes[i].nodeName=='data'){
						$.extend(rootobject,$.cc.toObject(rootattributes[i].nodeValue));
				}else{
					rootobject[rootattributes[i].nodeName]=rootattributes[i].nodeValue;
				}
		}

		if (cell == null) {
			cell=rootcell;
		}

		var attributes = cell.value.attributes;
		var type ='';
		var object = {id:cell.id};
		for ( var i = 0; i < attributes.length; i++) {
				if(attributes[i].nodeName=='data'){
						$.extend(object,$.cc.toObject(attributes[i].nodeValue));
				}else{
					object[attributes[i].nodeName]=attributes[i].nodeValue;
				}
		}
		var params = {rootobject:rootobject,object:object,cell:cell,objdocument: page}; 
			var winClass = "";
			if(object.type=='userTask'){
				winClass = "jsp-workflow-properties-userTask";
			}else if(object.type=='sequenceFlow'){
				winClass = "jsp-workflow-properties-sequenceFlow";
			}else if(object.type=='parallelGateway'){
				winClass = "jsp-workflow-properties-gateway";
			}else if(object.type=='startEvent'){
				winClass = "jsp-workflow-properties-startEvent";
			}else if(object.type=='endEvent'){
				winClass = "jsp-workflow-properties-endEvent";
			}else if(object.type==null){
				params.object.label = workflowName;
				winClass = "jsp-workflow-properties-workflow";
			}
			if(winClass!=''){
				params.page=window;
				Dialog.open({
					url : winClass,
					params : params
				});
			}else{
				Dialog.infomsg('此节点没有可编辑的属性！');
			}
	}
</script>
</head>
<body >
	<table id="splash" width="100%" height="100%"
		style="background: white; position: absolute; top: 0px; left: 0px; z-index: 4;">
		<tr>
			<td align="center" valign="middle"><img
				src="${basePath}images/loading.gif"></td>
		</tr>
	</table>
	<div id="graph" class="base"></div>
	<div id="status" class="base" align="right"></div>
</body>
</html>
