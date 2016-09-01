<!DOCTYPE html>
<html lang="en">
<head>
   <title>流程属性</title>
   <#include "classpath:com/cloudcode/framework/common/ftl/head.ftl"/>
   <script type="text/javascript" src="${request.getContextPath()}/static/mxgraph/examples/editors/properties/jqueruiproperties.js"></script> 
<script type="text/javascript">var allOperPower={};var myOperPower={};var myurl = this.location.pathname.replace(this.contextPath + '/', '');allOperPower = allOperPower[myurl];myOperPower = myOperPower[myurl];var operPower = {};var myOperPowerMap = {};if (allOperPower) {	if (myOperPower) {		for (var i = 0; i < myOperPower.length; i++) {			myOperPowerMap[myOperPower[i]] = true;		}	}	for (var i = 0; i < allOperPower.length; i++) {		if (myOperPowerMap[allOperPower[i]] != true) {			operPower[allOperPower[i]] = true;		}	}	}delete myurl;delete allOperPower;delete myOperPower;delete myOperPowerMap;</script>
</head>
<body data-spy="scroll" data-target=".bs-docs-sidebar" data-twttr-rendered="true"> 
<div id="dialogDiv">
<div class="container" id="layout">
<form role="form" class="form-horizontal" id="myFormId" action="${request.getContextPath()}/users/createUser" method="post">
  <div class="form-group">
    <label for="inputEmail3" class="col-sm-2 control-label">名称</label>
	    <div class="col-sm-4">
	      <input type="text" name="label" class="form-control" id="label" placeholder="名称">
	    </div>
	    <label for="inputPassword3" class="col-sm-2 control-label">表单地址</label>
	    <div class="col-sm-4">   
	     	<input type="text" name="href" class="form-control" id="href" placeholder="表单地址">
		</div>
    </div>
   <div class="form-group">
    	<label for="inputEmail3" class="col-sm-2 control-label">配置表单</label>
	    <div class="col-sm-10">
	      <input type="text" name="hrefckeditor" class="form-control" id="hrefckeditor" placeholder="配置表单">
	    </div>    
  </div>
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
       <button type="button" id="updateButton" class="btn btn-default">确定</button>
       <button type="button" id="cancelButton" class="btn btn-warning">取消</button>
    </div>
  </div>
   <input type="hidden" value="" id="oid" name="id" >
</form>

</div>
<#include "classpath:com/cloudcode/framework/common/ftl/vendor.ftl"/>
<script type="text/javascript">
var hm = $("body").wHumanMsg();
var height = 350;
var width = 600;
var params = window.params;//.parent.dialog.dialog("option","params");
var object = params.object;
$(function () {
  $('#updateButton').click( function() {
  		var values = $('form#myFormId').serializeObject();
		saveProperties(values);
  	    $('body').wHumanMsg('theme', 'black').wHumanMsg('msg', '数据保存成功！', {fadeIn: 300, fadeOut: 300});
  	    $('.ui-dialog-titlebar-close').trigger('click');
  });
  
   $('#cancelButton').click( function() {
   		$('body').wHumanMsg('theme', 'black').wHumanMsg('msg', '数据保存成功！', {fadeIn: 300, fadeOut: 300});
  	    $('.ui-dialog-titlebar-close').trigger('click');
  });
});
</script>
</div>
</body>
</html>