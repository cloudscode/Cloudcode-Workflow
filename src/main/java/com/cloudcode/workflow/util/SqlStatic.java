package com.cloudcode.workflow.util;

public class SqlStatic {
	public static String deleteCommentSql = "delete from act_hi_comment  where task_id_=:taskId and user_id_=:userId";
}
