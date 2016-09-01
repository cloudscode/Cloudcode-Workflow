<!DOCTYPE html>
<html lang="en">
<head>
   <title>测试表单</title>
   <#include "classpath:com/cloudcode/framework/common/ftl/head.ftl"/>
</head>
<body data-spy="scroll" data-target=".bs-docs-sidebar" data-twttr-rendered="true"> 
<div id="dialogDiv">
<div class="container" id="layout">
<form role="form" class="form-horizontal" id="myFormId" action="${request.getContextPath()}/workFlowTrees/createWorkFlowTree" method="post">
  <div class="form-org">
    <label for="inputEmail3" class="col-sm-2 control-label">名称</label>
	    <div class="col-sm-4">
	      <input type="text" name="text" class="form-control" id="text" placeholder="名称">
	    </div>
    <!-- /input-org -->
       <input type="hidden" value="" id="oid" name="id" >
    </div>
  
  <div class="form-org">
    <div class="col-sm-offset-2 col-sm-10">
       <button type="button" id="updateButton" class="btn btn-default">save</button>
    </div>
  </div>
</form>

</div>
<#include "classpath:com/cloudcode/framework/common/ftl/vendor.ftl"/>
<script type="text/javascript">
var hm = $("body").wHumanMsg();
$(function () {
			    $.ajax({
			        url: '${request.getContextPath()}/starts/${objectId !""}/findPiServiceDataByBusId',
			        type: 'post',
			        dataType: 'json',
			        //data: $('form#myFormId').serialize(),
			        success: function(data) {
			       		
			         }
			    });
 });
 function save(submit) {
 
  $.ajax({
			        url: '${request.getContextPath()}/starts/${objectId !""}/updatePiServiceDataByBusId',
			        type: 'post',
			        dataType: 'json',
			        data: {busData:$('form#myFormId').serialize()},
			        success: function(data) {
			       			if (result.success) {
								return object;
							}else{
								return null;
							}
			         }
			    });
			    
	//if ($("#form").validationEngine('validate')) {
		var object = $("#form").getValue();
		if(submit==null){
			Request.request('workflow-Start-updatePiServiceDataByBusId', {
				async : false,
				data : {
					objectId:'<%=request.getParameter("objectId")%>'
					,busData:BaseUtil.toString(object)
				}
			},function(result) {
				if (result.success) {
					return object;
				}else{
					return null;
				}
			});
		}
		return object;
	//} else {
		//Dialog.errormsg("验证失败！！");
	//}
}	
</script>
</div>
</body>
</html>