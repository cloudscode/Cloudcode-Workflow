<!DOCTYPE html>
<html lang="en">
<head>
   <title>开始节点属性</title>
   <#include "classpath:com/cloudcode/framework/common/ftl/head.ftl"/>
   <script type="text/javascript" src="${request.getContextPath()}/static/mxgraph/examples/editors/properties/jqueruiproperties.js"></script> 
</head>
<body data-spy="scroll" data-target=".bs-docs-sidebar" data-twttr-rendered="true"> 
<div xtype="hh_content">
<div class="container" id="layout" style="width: 100%;">
<form id="form" xtype="form">
  <div class="form-group">
    <label for="inputEmail3" class="col-sm-2 control-label">名称</label>
	    <div class="col-sm-10">
	       <span xtype="text" config=" name : 'label',required :true"></span>
	    </div>
    </div>
    <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
       <span xtype="button" config="text:'确定' , onClick : page.save "></span>
       <span xtype="button" config="text:'取消' , onClick : Dialog.close "></span>
    </div>
  </div>
   <input type="hidden" value="" id="oid" name="id" >
</form>

</div>
<#include "classpath:com/cloudcode/framework/common/ftl/require.ftl"/>
<script type="text/javascript">
var height = 350;
var width = 600;
var params = window.params;//.parent.dialog.dialog("option","params");
//var object = params.object;
var page={};
requirejs(['jquery','jquery','Request','jqueryui','main','text','select','date','radio','checkbox','textarea','password','ckeditor','button','validation','Request','combobox'], function( $, jQuery,Request ) {
	var params = $.cc.getIframeParams();
	var width = 720;
	var height = 450
	$("[xtype=form]").each(function() {
		var config = $.cc.getConfig($(this));
		if ($(this).is('form')) {
			$.cc.validation.validation($(this), config);
		}
	});
	
	page.save=function () {
		saveProperties(values);
	};
	page.cancel=function () {
		saveProperties(values);
	};
	$("[xtype]").each(function() {
		$(this).render('initRender');
	});
});
</script>
</div>
</body>
</html>