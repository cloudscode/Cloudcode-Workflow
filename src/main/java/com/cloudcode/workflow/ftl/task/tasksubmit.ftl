<!DOCTYPE html>
<html lang="en">
<head>
   <title>任务提交</title>
   <#include "classpath:com/cloudcode/framework/common/ftl/head.ftl"/>
</head>

<body data-spy="scroll" data-target=".bs-docs-sidebar" data-twttr-rendered="true"> 
<div id="dialogDiv">
<div class="container" id="layout">
<form role="form" class="form-horizontal" id="myFormId" >
  <div class="form-org">
	    <div class="col-sm-12">
	     <div id="maindiv" style="border: 1px solid #cccccc; padding: 8px;"></div>
	    </div>
  </div>
  <div class="form-org">
    <div class="col-sm-offset-2 col-sm-10">
       <button type="button" id="btnSubmit" class="btn btn-default">提交</button>
    </div>
  </div>
</form>

</div>
<#include "classpath:com/cloudcode/framework/common/ftl/vendor.ftl"/>
<script type="text/javascript">
var hm = $("body").wHumanMsg();
var height = 350;
	var width = 600;
	var params = BaseUtil.getIframeParams();
	var taskResult = params.taskResult;

	var activityMap = {};
	function init() {
			 $.ajax({
				        url: '${request.getContextPath()}/tasks/'+taskResult.piid+'/queryStepByActivityId',
				        type: 'post',
				        dataType: 'json',
				        data:{
								pdid : taskResult.pdid,
								activityId : taskResult.activityId,
								piid : taskResult.piid
							},
				        success: function(data) {
				       		  gouzhaoselect($("#maindiv"), data);
				           }
			    });		
	}

	function gouzhaoselect(div, activityList, parentActivity) {
		for (var i = 0; i < activityList.length; i++) {
			var activity = activityList[i];
			var id = activity.id;
			activityMap[id] = activity;
			var text = activity.propertys.label;
			var type = 'checkbox';
			var activityType = activity.propertys.type;
			if (parentActivity && parentActivity.propertys
					&& parentActivity.propertys.type == 'exclusiveGateway') {
				type = 'radio';
			}
			var disabledHtml = '';
			if (activity.propertys.isChange == false || parentActivity.propertys.type=='parallelGateway') {
				disabledHtml = 'disabled=true';
			}
			
			var checkedHtml = '';
			if (activity.checked == true || parentActivity.propertys.type=='parallelGateway') {
				checkedHtml = 'checked=true';
			}

			var nameHtml = '';
			if (parentActivity) {
				nameHtml = 'name=parentActivity_' + parentActivity.id;
			}
			
			var activityTypeHtml = 'activityType='+activityType;
			
			var buttonhtml = '<input  type="'+type+'"  id="'+id+'" '+checkedHtml+'  '+disabledHtml+' '+nameHtml+'  '+activityTypeHtml+'   />';
			var button = $(buttonhtml);

			var textspanhtml = '<span>&nbsp;' + text + '&nbsp;&nbsp;</span>';
			var textspan = $(textspanhtml);

			div.append(button).append(textspan);
			if (checkedHtml== 'checked=true') {
				if (activity.propertys.type == 'userTask') {
					div.append(getUserField(activity));
				} else {
					var parentDiv = $('<div style="border: 1px solid #cccccc; padding: 8px;"></div>');
					div.append(parentDiv);
					gouzhaoselect(parentDiv, activity.children, activity);
				}
			}

			if (type == 'radio') {
				button.click(function(result) {
					for (var j = 0; j < activityList.length; j++) {
						removeUserField(activityList[j].id + '_div');
					}
					var activityId = result.currentTarget.id;
					if (document.getElementById(activityId).checked) {
						$('#' + activityId).parent().append(
								getUserField(activityMap[activityId]));
					}
				});
			} else {
				button.change(function(result) {
					var activityId = result.currentTarget.id;
					if (document.getElementById(activityId).checked) {
						$('#' + activityId).parent().append(
								getUserField(activityMap[activityId]));
					} else {
						removeUserField(activityId + '_div');
					}
				});
			}
		}
	}

	function removeUserField(divid) {
		$('#' + divid).empty();
		$('#' + divid).remove();
	}

	function getUserField(activity) {
		var id = activity.id + '_div';

		var userName = 'userMap.remove_users_' + activity.id;

		var html = '<div id="'+id+'" style="border: 1px solid #cccccc; padding: 8px;"><span>下一步骤：'
				+ activity.propertys.label + '</span><br/><br/></div>';
		var user = $('<span xtype="selectUser" config=" name : \''+userName+'\',required :true"></span>');
		user.render();
		return $(html).append(user);
	}

	function submit() {
		var object = $("#maindiv").getValue();
		object.taskId = taskResult.taskId;

		var selectUser = false;

		for ( var p in object) {
			if (p.indexOf('userMap.') > -1) {
				if (object[p] == null || object[p] == '') {
					Dialog.infomsg('请选择用户！');
					return;
				}
				selectUser = true;
			}
		}
		
		if (selectUser == false  && $('input[activitytype=endEvent]:checked').length==0) {
			Dialog.infomsg('请选择步骤！');
			return;
		}

		var inputs = document.getElementsByTagName('input');
		for (var i = 0; i < inputs.length; i++) {
			var input = inputs[i];
			if (input.type == 'radio' || input.type == 'checkbox') {
				var activity = activityMap[input.id];
				if (activity.propertys.sdxz_condition == true) {
					if (input.checked) {
						object['conditionMap.remove_condition_' + input.id] = 1;
					} else {
						object['conditionMap.remove_condition_' + input.id] = 0;
					}
				}
			}
		}		
		$.ajax({
				        url: '${request.getContextPath()}/tasks/collection/submit',
				        type: 'post',
				        dataType: 'json',
				        data:object,
				        success: function(data) {
				       		 $('body').wHumanMsg('theme', 'black').wHumanMsg('msg', '数据保存成功！', {fadeIn: 300, fadeOut: 300});
				        	 $('.ui-dialog-titlebar-close').trigger('click');
				           }
	     });
	}
</script>
</div>
</body>
</html>