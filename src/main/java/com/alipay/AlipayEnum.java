package com.alipay;

/**
 * 支付宝移动支付枚举
 * @author wanghuipu
 *
 */
public enum AlipayEnum {
	WAIT_BUYER_PAY{
		@Override
		public String getName() {
			return "交易创建，等待买家付款。";
		}
	},TRADE_CLOSED{
		@Override
		public String getName() {
			return "在指定时间段内未支付时关闭的交易；在交易完成全额退款成功时关闭的交易。";
		}
	},TRADE_SUCCESS{
		@Override
		public String getName() {
			return "交易成功，且可对该交易做操作，如：多级分润、退款等。";
		}
	},TRADE_FINISHED{
		@Override
		public String getName() {
			return "交易成功且结束，即不可再做任何操作。";
		}
	};
	
	public abstract String getName();
}
