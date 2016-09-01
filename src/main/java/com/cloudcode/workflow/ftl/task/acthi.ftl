<!DOCTYPE html>
<html lang="en">
<head>
   <title>办理历史</title>
   <#include "classpath:com/cloudcode/framework/common/ftl/head.ftl"/>
   <script type="text/javascript">
var params = BaseUtil.getIframeParams();
	var height = 400;
	var width = 500;
	$(function() {
			$.ajax({
				        url: '${request.getContextPath()}/tasks/${piid !""}/queryCommentByActivityId?activityId=${activityId !""}',
				        type: 'post',
				        dataType: 'json',
				        data:{piid:${piid !""}},
				        success: function(data) {
				       			var html = '<table xtype="list">';
								for(var i=0;i<resultList.length;i++){
									var result = resultList[i];
									var table = '<table xtype="form">'
									+'<tr>	<td xtype="label">主办人：</td><td>'+(result.assigneeName||'')+'</td></tr>'
									+'<tr>	<td xtype="label">经办人：</td><td>'+(result.userNames||'')+'</td></tr>'
									+'<tr>	<td xtype="label">创建时间：</td><td>'+result.createTime+'</td></tr>'
									+'<tr>	<td xtype="label">结束时间：</td><td>'+(result.endTime||'')+'</td></tr>'
									+'<tr>	<td xtype="label">耗时：</td><td>'+result.durationInMillis+'</td></tr>'
									+'<tr>	<td xtype="label">意见&附件：</td><td>'+(result.message||'')+'</td></tr>'
									+'</table>';
									html+='<tr><td>'+table+'</td></tr>';
								}
								html+= '</table>';
								var table = $(html);
								table.render();
								//table.find('[xtype=form]').render();
								$('body').append(table);
				           }
			    });
	
		Request.request('workflow-Task-queryCommentByActivityId', {
			data:{
				activityId : params.activityId,
				piid:params.piid
			}
		}, function(resultList) {
			
		});
	});
	
</script>
</head>
<body data-spy="scroll" data-target=".bs-docs-sidebar" data-twttr-rendered="true"> 
	<div id="divInDialog" style="display:none">
	</div>
</body>
</html>