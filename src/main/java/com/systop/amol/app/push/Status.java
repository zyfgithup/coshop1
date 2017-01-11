package com.systop.amol.app.push;

/**
 * 消息状态
 * @author 王会璞
 *
 */
public enum Status {
	SUCCESS {
		@Override
		public String getName() {
			return "成功";
		}
	},FAILURE {
		@Override
		public String getName() {
			return "失败";
		}
	},UNSENT {
		@Override
		public String getName() {
			return "未发送";
		}
	};
	public abstract String getName();
}
