<!DOCTYPE html>
<html lang="en">
<head>
   <title>任务办理</title>
   <#include "classpath:com/cloudcode/framework/common/ftl/head.ftl"/>
<script type="text/javascript">
var hm = $("body").wHumanMsg();
var params ={}; // BaseUtil.getIframeParams();
var actionType = params.actionType;
var taskResult = params.taskResult;
var href = ajaxframework.getHref(taskResult.href,{type:'workflow',objectId:taskResult.objectId,actionType:actionType}) ;
var iframe = null;
$(function () {
	   $('#btnSubmit').click( function() {
	   		var formObject = save(0);
			taskResult.serviceObject=formObject;
			if(formObject){
		      		  $( "#divInDialog" ).dialog({
						 	 modal: true,
						 	 width:800,
							open: function(event, ui) {
								$(this).load('${request.getContextPath()}/workFlowMenus/collection/tasksubmit?taskResult='+taskResult);
						  },	   
					    close: function (event, ui) {  
					       //grid.trigger("reloadGrid");
					    }  	   
					});
			}
		});
	    $('#btnSave').click( function() {
	      		    var iframe = window.frames['serviceiframe'];
					var formObject = iframe.save();
					if(close==null && formObject){
						Dialog.close();
					}
					return formObject;
		});
		$('#btnCancle').click( function() {
				      $('.ui-dialog-titlebar-close').trigger('click');
			    });
		});
});	
</script>
</head>
<body data-spy="scroll" data-target=".bs-docs-sidebar" data-twttr-rendered="true"> 
	<iframe id="serviceiframe" name="serviceiframe" frameborder=0 width=100% height=100% src="" ></iframe>
	 <div class="form-org">
    <div class="col-sm-offset-2 col-sm-10">
       <button type="button" id="btnSubmit" class="btn btn-default">提交</button>
    </div>
     <div class="col-sm-offset-2 col-sm-10">
       <button type="button" id="btnSave" class="btn btn-default">保存</button>
    </div>
     <div class="col-sm-offset-2 col-sm-10">
       <button type="button" id="btnCancle" class="btn btn-default">取消</button>
    </div>
  </div>
	<div id="divInDialog" style="display:none">
	</div>
</body>
</html>