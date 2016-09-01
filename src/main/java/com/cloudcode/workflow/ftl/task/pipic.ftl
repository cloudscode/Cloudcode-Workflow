<!DOCTYPE html>
<html lang="en">
<head>
   <title>流程图</title>
   <#include "classpath:com/cloudcode/framework/common/ftl/head.ftl"/>
   <script type="text/javascript">
	var params = BaseUtil.getIframeParams();
	$(function() {
		$.ajax({
				        url: '${request.getContextPath()}/tasks/${pdid !""}/queryPicHiTask',
				        type: 'post',
				        dataType: 'json',
				        data:{piid:${piid !""}},
				        success: function(data) {
				       			var html = '';
								for(var i=0;i<resultList.length;i++){
									var result = resultList[i];
									$('body').append('<div style=\'position:absolute; border:0px solid ;left:'+result.left+'px;top:'+result.top+'px;width:'+result.width+'px;height:'+result.height+'px;\'>'
											+'<img onClick="javascript:imgclick(\''+result.actid+'\');" style=\'cursor: pointer;\' src=\''+result.src+'\' /></div>');
								}
				           }
			    });
		
		
		
		/*Request.request('workflow-Task-queryPicHiTask', {
			data:{
				pdid : '<%=request.getParameter("pdid")%>',
				piid : '<%=request.getParameter("piid")%>'
			}
		}, function(resultList) {
			var html = '';
			for(var i=0;i<resultList.length;i++){
				var result = resultList[i];
				$('body').append('<div style=\'position:absolute; border:0px solid ;left:'+result.left+'px;top:'+result.top+'px;width:'+result.width+'px;height:'+result.height+'px;\'>'
						+'<img onClick="javascript:imgclick(\''+result.actid+'\');" style=\'cursor: pointer;\' src=\''+result.src+'\' /></div>');
			}
		});*/
	});
	
	
	function imgclick(actid){
	    $( "#divInDialog" ).dialog({
			 	modal: true,
			 	width:800,
				open: function(event, ui) {
					$(this).load('${request.getContextPath()}/workFlowMenus/'+piid+'/acthi?activityId='+actid);
			  }
		});
	}
</script>
</head>
<body data-spy="scroll" data-target=".bs-docs-sidebar" data-twttr-rendered="true"> 
	<img src="${request.getContextPath()}/tasks/${pdid !""}/showPic?pdid=${pdid!""}"  onClick="javascript:imgclick();" />
	<div id="divInDialog" style="display:none">
	</div>
</body>
</html>