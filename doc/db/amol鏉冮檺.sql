# --------------------------------------------------------
# Host:                         127.0.0.1
# Server version:               5.0.19-nt
# Server OS:                    Win32
# HeidiSQL version:             5.1.0.3468
# Date/time:                    2011-09-27 14:15:49
# --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

# Dumping database structure for amol
CREATE DATABASE IF NOT EXISTS `amol` /*!40100 DEFAULT CHARACTER SET gb2312 */;
USE `amol`;


# Dumping structure for table amol.permissions
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE IF NOT EXISTS `permissions` (
  `ID` int(11) NOT NULL,
  `descn` varchar(255) default NULL,
  `is_sys` char(1) default '0',
  `name` varchar(255) default NULL,
  `operation` varchar(255) default NULL,
  `status` varchar(255) default NULL,
  `version` int(11) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table amol.permissions: ~11 rows (approximately)
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` (`ID`, `descn`, `is_sys`, `name`, `operation`, `status`, `version`) VALUES
	(229376, '系统管理员权限', NULL, 'AUTH_SYSTEM', 'target_url', '1', NULL),
	(229377, '银行用户权限', NULL, 'AUTH_BANK', 'target_url', '1', NULL),
	(229378, '生产厂家角色权限', NULL, 'AUTH_SUPPLIER', 'target_url', '1', NULL),
	(229379, '注册经销商高级权限', NULL, 'AUTH_TOP_DEALER', 'target_url', '1', NULL),
	(229380, '注册经销商普通权限', NULL, 'AUTH_TOP_DEALER_GENERAL', 'target_url', '1', NULL),
	(229381, '分销商高级权限', NULL, 'AUTH_END_DEALER', 'target_url', '1', NULL),
	(229382, '分销商普通权限', NULL, 'AUTH_END_DEALER_GENERAL', 'target_url', '1', NULL),
	(229383, '采购管理权限', NULL, 'AUTH_EMPLOYEE_PURCHASE', 'target_url', '1', NULL),
	(229384, '库存管理权限', NULL, 'AUTH_EMPLOYEE_STOCK', 'target_url', '1', NULL),
	(229385, '销售管理权限', NULL, 'AUTH_EMPLOYEE_SALE', 'target_url', '1', NULL),
	(229386, '财务管理权限', NULL, 'AUTH_EMPLOYEE_FINANCE', 'target_url', '1', NULL);
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;


# Dumping structure for table amol.permis_resc
DROP TABLE IF EXISTS `permis_resc`;
CREATE TABLE IF NOT EXISTS `permis_resc` (
  `permission_id` int(11) NOT NULL,
  `resc_id` int(11) NOT NULL,
  PRIMARY KEY  (`permission_id`,`resc_id`),
  KEY `FK427D10C89A58A5E5` (`permission_id`),
  KEY `FK427D10C8E9AD0070` (`resc_id`),
  CONSTRAINT `FK427D10C89A58A5E5` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`ID`),
  CONSTRAINT `FK427D10C8E9AD0070` FOREIGN KEY (`resc_id`) REFERENCES `resources` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table amol.permis_resc: ~409 rows (approximately)
/*!40000 ALTER TABLE `permis_resc` DISABLE KEYS */;
INSERT INTO `permis_resc` (`permission_id`, `resc_id`) VALUES
	(229376, 196633),
	(229376, 622592),
	(229377, 196620),
	(229377, 196621),
	(229377, 196622),
	(229377, 196623),
	(229377, 196624),
	(229377, 196625),
	(229377, 196653),
	(229377, 196654),
	(229377, 425988),
	(229377, 425989),
	(229377, 425990),
	(229377, 622597),
	(229377, 2195467),
	(229377, 5111808),
	(229378, 196613),
	(229378, 196644),
	(229378, 196645),
	(229378, 196652),
	(229378, 196653),
	(229378, 196654),
	(229378, 196658),
	(229378, 425990),
	(229378, 2195456),
	(229378, 2195458),
	(229378, 2195459),
	(229378, 2195461),
	(229379, 196608),
	(229379, 196610),
	(229379, 196613),
	(229379, 196614),
	(229379, 196615),
	(229379, 196616),
	(229379, 196617),
	(229379, 196618),
	(229379, 196619),
	(229379, 196622),
	(229379, 196628),
	(229379, 196629),
	(229379, 196630),
	(229379, 196631),
	(229379, 196632),
	(229379, 196633),
	(229379, 196635),
	(229379, 196636),
	(229379, 196637),
	(229379, 196638),
	(229379, 196639),
	(229379, 196640),
	(229379, 196641),
	(229379, 196642),
	(229379, 196643),
	(229379, 196644),
	(229379, 196645),
	(229379, 196646),
	(229379, 196647),
	(229379, 196648),
	(229379, 196649),
	(229379, 196650),
	(229379, 196651),
	(229379, 196652),
	(229379, 196653),
	(229379, 196654),
	(229379, 196655),
	(229379, 196656),
	(229379, 196657),
	(229379, 196658),
	(229379, 196659),
	(229379, 196660),
	(229379, 196661),
	(229379, 196662),
	(229379, 196663),
	(229379, 196664),
	(229379, 196665),
	(229379, 196666),
	(229379, 196667),
	(229379, 196668),
	(229379, 196669),
	(229379, 196670),
	(229379, 196671),
	(229379, 196672),
	(229379, 196675),
	(229379, 425984),
	(229379, 425985),
	(229379, 425986),
	(229379, 425987),
	(229379, 425990),
	(229379, 589824),
	(229379, 622593),
	(229379, 622594),
	(229379, 622595),
	(229379, 622596),
	(229379, 622597),
	(229379, 1507330),
	(229379, 2195457),
	(229379, 2195459),
	(229379, 2195461),
	(229379, 2195462),
	(229379, 2195463),
	(229379, 2195467),
	(229379, 2195468),
	(229379, 2195469),
	(229379, 2195470),
	(229379, 2195471),
	(229379, 2195472),
	(229379, 2195473),
	(229379, 2326528),
	(229379, 2326529),
	(229379, 2326530),
	(229379, 2326531),
	(229379, 2326532),
	(229379, 2326533),
	(229379, 2326535),
	(229379, 2326536),
	(229379, 2326537),
	(229379, 2326538),
	(229379, 2326540),
	(229379, 2326541),
	(229379, 2916352),
	(229379, 2916353),
	(229379, 2916354),
	(229379, 2916355),
	(229379, 5111808),
	(229379, 5111809),
	(229379, 5111810),
	(229379, 5111811),
	(229379, 5111812),
	(229380, 196608),
	(229380, 196610),
	(229380, 196613),
	(229380, 196614),
	(229380, 196617),
	(229380, 196618),
	(229380, 196619),
	(229380, 196622),
	(229380, 196628),
	(229380, 196633),
	(229380, 196650),
	(229380, 196651),
	(229380, 196652),
	(229380, 196653),
	(229380, 196654),
	(229380, 196655),
	(229380, 196656),
	(229380, 196657),
	(229380, 196658),
	(229380, 196659),
	(229380, 196660),
	(229380, 196661),
	(229380, 196662),
	(229380, 196672),
	(229380, 196675),
	(229380, 425984),
	(229380, 425985),
	(229380, 425986),
	(229380, 425987),
	(229380, 425990),
	(229380, 589824),
	(229380, 622593),
	(229380, 622594),
	(229380, 622595),
	(229380, 622596),
	(229380, 622597),
	(229380, 1507330),
	(229380, 2195467),
	(229380, 2195468),
	(229380, 2195469),
	(229380, 2195470),
	(229380, 2195471),
	(229380, 2195472),
	(229380, 2195473),
	(229380, 2326529),
	(229380, 2326530),
	(229380, 2326531),
	(229380, 2326532),
	(229380, 2326533),
	(229380, 2326535),
	(229380, 2326536),
	(229380, 2326537),
	(229380, 2326538),
	(229380, 2326540),
	(229380, 2326541),
	(229380, 2916355),
	(229380, 5111808),
	(229380, 5111809),
	(229380, 5111810),
	(229380, 5111811),
	(229380, 5111812),
	(229381, 196608),
	(229381, 196610),
	(229381, 196613),
	(229381, 196614),
	(229381, 196615),
	(229381, 196616),
	(229381, 196617),
	(229381, 196618),
	(229381, 196619),
	(229381, 196622),
	(229381, 196651),
	(229381, 196654),
	(229381, 196655),
	(229381, 196656),
	(229381, 196657),
	(229381, 196658),
	(229381, 196659),
	(229381, 196660),
	(229381, 589824),
	(229381, 622594),
	(229381, 622595),
	(229381, 622596),
	(229381, 2195467),
	(229381, 2195468),
	(229381, 2195470),
	(229381, 2195471),
	(229381, 2195472),
	(229381, 2195473),
	(229381, 2326529),
	(229381, 2326531),
	(229381, 2326532),
	(229381, 2326533),
	(229381, 2326535),
	(229381, 2326536),
	(229381, 2326537),
	(229381, 2326538),
	(229381, 2326539),
	(229381, 2326540),
	(229381, 2326541),
	(229381, 2916355),
	(229381, 5111808),
	(229381, 5111809),
	(229381, 5111810),
	(229382, 196608),
	(229382, 196610),
	(229382, 196613),
	(229382, 196614),
	(229382, 196615),
	(229382, 196616),
	(229382, 196617),
	(229382, 196618),
	(229382, 196619),
	(229382, 196622),
	(229382, 196651),
	(229382, 196654),
	(229382, 196655),
	(229382, 196656),
	(229382, 196657),
	(229382, 196658),
	(229382, 196659),
	(229382, 196660),
	(229382, 589824),
	(229382, 622594),
	(229382, 622595),
	(229382, 622596),
	(229382, 2195467),
	(229382, 2195468),
	(229382, 2195470),
	(229382, 2195471),
	(229382, 2195472),
	(229382, 2195473),
	(229382, 2326529),
	(229382, 2326531),
	(229382, 2326532),
	(229382, 2326533),
	(229382, 2326535),
	(229382, 2326536),
	(229382, 2326537),
	(229382, 2326538),
	(229382, 2326539),
	(229382, 2326540),
	(229382, 2326541),
	(229382, 2916355),
	(229382, 5111808),
	(229382, 5111809),
	(229382, 5111810),
	(229383, 196639),
	(229383, 196643),
	(229383, 196644),
	(229383, 196645),
	(229383, 196646),
	(229383, 589824),
	(229383, 622593),
	(229383, 622594),
	(229383, 622595),
	(229383, 2195457),
	(229383, 2195459),
	(229383, 2195461),
	(229383, 2326535),
	(229383, 2326540),
	(229383, 2916354),
	(229384, 196622),
	(229384, 196635),
	(229384, 196636),
	(229384, 196637),
	(229384, 196640),
	(229384, 196644),
	(229384, 196645),
	(229384, 196646),
	(229384, 196653),
	(229384, 196654),
	(229384, 196655),
	(229384, 196663),
	(229384, 196664),
	(229384, 196665),
	(229384, 196666),
	(229384, 196667),
	(229384, 196668),
	(229384, 196669),
	(229384, 589824),
	(229384, 622593),
	(229384, 622594),
	(229384, 622595),
	(229384, 622596),
	(229384, 2195457),
	(229384, 2195459),
	(229384, 2195461),
	(229384, 2195467),
	(229384, 2195468),
	(229384, 2195469),
	(229384, 2195470),
	(229384, 2195471),
	(229384, 2195472),
	(229384, 2195473),
	(229384, 2326531),
	(229384, 2326532),
	(229384, 2326535),
	(229384, 2326538),
	(229384, 2326539),
	(229384, 2326540),
	(229384, 2916352),
	(229384, 2916353),
	(229384, 2916354),
	(229384, 5111808),
	(229384, 5111809),
	(229384, 5111810),
	(229384, 5111811),
	(229384, 5111812),
	(229385, 196608),
	(229385, 196610),
	(229385, 196613),
	(229385, 196614),
	(229385, 196615),
	(229385, 196616),
	(229385, 196617),
	(229385, 196618),
	(229385, 196619),
	(229385, 196622),
	(229385, 196650),
	(229385, 196651),
	(229385, 196652),
	(229385, 196653),
	(229385, 196654),
	(229385, 196655),
	(229385, 196658),
	(229385, 196659),
	(229385, 196660),
	(229385, 589824),
	(229385, 622594),
	(229385, 622595),
	(229385, 622596),
	(229385, 2195467),
	(229385, 2195468),
	(229385, 2195469),
	(229385, 2195470),
	(229385, 2195471),
	(229385, 2195472),
	(229385, 2195473),
	(229385, 2326531),
	(229385, 2326532),
	(229385, 2326535),
	(229385, 2326539),
	(229385, 2326540),
	(229385, 5111808),
	(229385, 5111811),
	(229385, 5111812),
	(229386, 196622),
	(229386, 196630),
	(229386, 196631),
	(229386, 196638),
	(229386, 196641),
	(229386, 196642),
	(229386, 196644),
	(229386, 196645),
	(229386, 196646),
	(229386, 196647),
	(229386, 196656),
	(229386, 196657),
	(229386, 589824),
	(229386, 622593),
	(229386, 622595),
	(229386, 622596),
	(229386, 1507330),
	(229386, 2195457),
	(229386, 2195459),
	(229386, 2195462),
	(229386, 2195463),
	(229386, 2195467),
	(229386, 2195468),
	(229386, 2195469),
	(229386, 2195472),
	(229386, 2195473),
	(229386, 2326528),
	(229386, 2326529),
	(229386, 2326530),
	(229386, 2326531),
	(229386, 2326532),
	(229386, 2326533),
	(229386, 2326535),
	(229386, 2326536),
	(229386, 2326537),
	(229386, 2326538),
	(229386, 2326541),
	(229386, 2916352),
	(229386, 2916353),
	(229386, 2916355),
	(229386, 5111808),
	(229386, 5111811),
	(229386, 5111812);
/*!40000 ALTER TABLE `permis_resc` ENABLE KEYS */;


# Dumping structure for table amol.resources
DROP TABLE IF EXISTS `resources`;
CREATE TABLE IF NOT EXISTS `resources` (
  `ID` int(11) NOT NULL,
  `descn` varchar(255) default NULL,
  `name` varchar(255) default NULL,
  `res_string` varchar(255) default NULL,
  `res_type` varchar(255) default NULL,
  `version` int(11) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table amol.resources: ~129 rows (approximately)
/*!40000 ALTER TABLE `resources` DISABLE KEYS */;
INSERT INTO `resources` (`ID`, `descn`, `name`, `res_string`, `res_type`, `version`) VALUES
	(196608, '客户管理', '.base.customer.*.do*', '/base/customer/*.do*', 'url_resource', 14),
	(196610, '员工管理', '.base.employee.*.do*', '/base/employee/*.do*', 'url_resource', 10),
	(196613, '商品管理', '.base.product.*.do*', '/base/product/*.do*', 'url_resource', 11),
	(196614, '商品类型管理', '.base.prosort.*.do*', '/base/prosort/*.do*', 'url_resource', 8),
	(196615, '仓库管理', '.base.storage.*.do*', '/base/storage/*.do*', 'url_resource', 7),
	(196616, '经销商对分销商的仓库管理', '.base.storage.second.*.do*', '/base/storage/second/*.do*', 'url_resource', 7),
	(196617, '供应商管理', '.base.supplier.*.do*', '/base/supplier/*.do*', 'url_resource', 8),
	(196618, '计量单位管理', '.base.units.*.do*', '/base/units/*.do*', 'url_resource', 8),
	(196619, '单位换算管理', '.base.units.item.*.do*', '/base/units/item/*.do*', 'url_resource', 8),
	(196620, '卡的管理', '.card.*.do*', '/card/*.do*', 'url_resource', 12),
	(196621, '卡充值管理', '.card.up.*.do*', '/card/up/*.do*', 'url_resource', 12),
	(196622, '卡消费管理', '.card.spend.*.do*', '/card/spend/*.do*', 'url_resource', 9),
	(196623, '补卡管理', '.card.replace.*.do*', '/card/replace/*.do*', 'url_resource', 12),
	(196624, '代币卡重置密码', '.card.rest.*.do*', '/card/rest/*.do*', 'url_resource', 12),
	(196625, '发卡管理', '.card.grant.*.do*', '/card/grant/*.do*', 'url_resource', 12),
	(196628, '经销商数据备份操作', '.databackup.*.do*', '/databackup/*.do*', 'url_resource', 10),
	(196629, '收支类别', '.finance.costSort.*.do*', '/finance/costSort/*.do*', 'url_resource', 7),
	(196630, '资金收入支出', '.finance.cost.*.do*', '/finance/cost/*.do*', 'url_resource', 3),
	(196631, '资金收入支出明细', '.finance.costDetail.*.do*', '/finance/costDetail/*.do*', 'url_resource', 3),
	(196632, '资金类型', '.finance.fundsSort.*.do*', '/finance/fundsSort/*.do*', 'url_resource', 4),
	(196633, NULL, '.init.*.do*', '/init/*.do*', 'url_resource', 7),
	(196634, NULL, '.pda.sales.*.do*', '/pda/sales/*.do*', 'url_resource', 0),
	(196635, '采购管理', '.purchase.*.do*', '/purchase/*.do*', 'url_resource', 5),
	(196636, '采购退货管理', '.purchase.returns.*.do*', '/purchase/returns/*.do*', 'url_resource', 5),
	(196637, '采购明细管理', '.purchase.purchasedetail.*.do*', '/purchase/purchasedetail/*.do*', 'url_resource', 5),
	(196638, '采购付款明细管理', '.purchase.paydetail.*.do*', '/purchase/paydetail/*.do*', 'url_resource', 6),
	(196639, '采购订单明细', '.purchase.orderdetail.*.do*', '/purchase/orderdetail/*.do*', 'url_resource', 3),
	(196640, '采购退货明细', '.purchase.returnsdetail.*.do*', '/purchase/returnsdetail/*.do*', 'url_resource', 5),
	(196641, '即时应付', '.purchase.payable.*.do*', '/purchase/payable/*.do*', 'url_resource', 5),
	(196642, '采购应付', '.purchase.pay.*.do*', '/purchase/pay/*.do*', 'url_resource', 5),
	(196643, '采购订单', '.purchase.order.*.do*', '/purchase/order/*.do*', 'url_resource', 3),
	(196644, '采购查询管理', '.purchase.selectAll.*.do*', '/purchase/selectAll/*.do*', 'url_resource', 8),
	(196645, '采购查询明细', '.purchase.selectDetail.*.do*', '/purchase/selectDetail/*.do*', 'url_resource', 8),
	(196646, '采购汇总', '.purchase.purchasetotal.*.do*', '/purchase/purchasetotal/*.do*', 'url_resource', 7),
	(196647, '应付明细', '.purchase.paytotal.*.do*', '/purchase/paytotal/*.do*', 'url_resource', 5),
	(196648, '采购应付初始化', '.purchase.payinit.*.do*', '/purchase/payinit/*.do*', 'url_resource', 4),
	(196649, '应付初始化自动导入', '.purchase.payinit.importData.do*', '/purchase/payinit/importData.do*', 'url_resource', 4),
	(196650, '销售订单【现金】', '.salesOrder.*.do*', '/salesOrder/*.do*', 'url_resource', 7),
	(196651, '销售订单【卡】', '.salesOrder.cardSalesOrder.*.do*', '/salesOrder/cardSalesOrder/*.do*', 'url_resource', 8),
	(196652, '销售【订单 出库单 退货单】（现金 卡）详情', '.salesDetail.*.do*', '/salesDetail/*.do*', 'url_resource', 9),
	(196653, '销售出库【现金】', '.sales.*.do*', '/sales/*.do*', 'url_resource', 9),
	(196654, '销售出库【卡】', '.sales.cardSales.*.do*', '/sales/cardSales/*.do*', 'url_resource', 11),
	(196655, '销售退货（现金 卡）', '.salesReturns.*.do*', '/salesReturns/*.do*', 'url_resource', 7),
	(196656, '销售回款单（现金 卡）', '.receive.*.do*', '/receive/*.do*', 'url_resource', 14),
	(196657, '销售回款单详情（现金 卡）', '.receiveDetail.*.do*', '/receiveDetail/*.do*', 'url_resource', 12),
	(196658, '商品条形码', '.barcode.*.do*', '/barcode/*.do*', 'url_resource', 7),
	(196659, '销售查询管理', '.salesEnquiries.*.do*', '/salesEnquiries/*.do*', 'url_resource', 6),
	(196660, '销售汇总', '.salesSummary.*.do*', '/salesSummary/*.do*', 'url_resource', 6),
	(196661, '期初应收', '.receiveInit.*.do*', '/receiveInit/*.do*', 'url_resource', 11),
	(196662, '期初应收导入', '.receiveInit.importData.do*', '/receiveInit/importData.do*', 'url_resource', 12),
	(196663, '库存上下限提醒', '.stock.awoke.*.do*', '/stock/awoke/*.do*', 'url_resource', 11),
	(196664, '库存盘点', '.stock.check.*.do*', '/stock/check/*.do*', 'url_resource', 11),
	(196665, '库存盘点详单', '.stock.check.detail.*.do*', '/stock/check/detail/*.do*', 'url_resource', 11),
	(196666, '库存盘点报损盈', '.stock.check.lp.*.do*', '/stock/check/lp/*.do*', 'url_resource', 11),
	(196667, '即时库存管理', '.stock.*.do*', '/stock/*.do*', 'url_resource', 11),
	(196668, '库存调拨', '.stock.trac.*.do*', '/stock/trac/*.do*', 'url_resource', 11),
	(196669, '库存调拨详单', '.stock.trac.detail.*.do*', '/stock/trac/detail/*.do*', 'url_resource', 11),
	(196670, '库存期初设置', '.stock.init.*.do*', '/stock/init/*.do*', 'url_resource', 8),
	(196671, '通过Excel导入期初库存信息', '.stock.init.importData.do*', '/stock/init/importData.do*', 'url_resource', 8),
	(196672, '分销商管理', '.user.agent.*.do*', '/user/agent/*.do*', 'url_resource', 5),
	(196675, '部门管理', '.dept.*.do*', '/dept/*.do*', 'url_resource', 11),
	(196676, NULL, '.fileattch.*.do*', '/fileattch/*.do*', 'url_resource', 0),
	(196677, NULL, '.admin.mail.view.do*', '/admin/mail/view.do*', 'url_resource', 0),
	(196678, NULL, '.admin.mail.edit.do*', '/admin/mail/edit.do*', 'url_resource', 0),
	(196679, NULL, '.admin.mail.save.do*', '/admin/mail/save.do*', 'url_resource', 0),
	(196680, NULL, '.admin.mail.check.do*', '/admin/mail/check.do*', 'url_resource', 0),
	(196681, NULL, '.admin.region.*.do*', '/admin/region/*.do*', 'url_resource', 4),
	(196682, NULL, '.register.*.do*', '/register/*.do*', 'url_resource', 0),
	(196683, NULL, '.register.regMemo.*.do*', '/register/regMemo/*.do*', 'url_resource', 0),
	(196684, NULL, '.restorePassword.*.do*', '/restorePassword/*.do*', 'url_resource', 0),
	(196685, NULL, '.security.*.*.do*', '/security/*/*.do*', 'url_resource', 0),
	(196686, NULL, '.user.*.*.do*', '/user/*/*.do*', 'url_resource', 0),
	(196687, NULL, '.user.changePass.do*', '/user/changePass.do*', 'url_resource', 0),
	(196688, NULL, '.user.indexLoginUser.do*', '/user/indexLoginUser.do*', 'url_resource', 0),
	(196689, NULL, '.userHistory.*.do*', '/userHistory/*.do*', 'url_resource', 0),
	(425984, '职员密码', '.base.employee.restPass.do*', '/base/employee/restPass.do*', 'url_resource', 4),
	(425985, '职员重置密码', '.base.employee.restPassword.do*', '/base/employee/restPassword.do*', 'url_resource', 4),
	(425986, '分销商密码', '.user.agent.restPass.do*', '/user/agent/restPass.do*', 'url_resource', 2),
	(425987, '分销商重置密码', '.user.agent.restPassword.do*', '/user/agent/restPassword.do*', 'url_resource', 2),
	(425988, '修改密码', '.card.grant.updatePassword.do*', '/card/grant/updatePassword.do*', 'url_resource', 1),
	(425989, '修改密码', '.card.grant.updatePass.do*', '/card/grant/updatePass.do*', 'url_resource', 1),
	(425990, '查询经销商', '.user.agent.selectAgent.do*', '/user/agent/selectAgent.do*', 'url_resource', 4),
	(589824, '采购计量单位加载', '.purchase.getUnitsItem.do*', '/purchase/getUnitsItem.do*', 'url_resource', 8),
	(622592, '数据库所有数据备份操作', ' .databackup.all.*.do* ', '/databackup/all/*.do* ', 'url_resource', 7),
	(622593, '选择供应商', '.base.supplier.showIndex.do*', '/base/supplier/showIndex.do*', 'url_resource', 7),
	(622594, '采购商品查询', '.base.product.purchaseshowIndex.do*', '/base/product/purchaseshowIndex.do*', 'url_resource', 8),
	(622595, '选择员工', '.base.employee.showIndex.do*', '/base/employee/showIndex.do*', 'url_resource', 10),
	(622596, '选择客户', '.base.customer.showIndex.do*', '/base/customer/showIndex.do*', 'url_resource', 7),
	(622597, '查询客户代币卡', '.card.grant.search.do*', '/card/grant/search.do*', 'url_resource', 3),
	(1507330, '选择分销商', '.user.agent.selectEndAgent.do*', '/user/agent/selectEndAgent.do*', 'url_resource', 3),
	(2195456, '生产厂家库存状态查询', '.stock.stockIndex.do*', '/stock/stockIndex.do*', 'url_resource', 3),
	(2195457, '采购入库查看', '.purchase.view.do*', '/purchase/view.do*', 'url_resource', 5),
	(2195458, '生产厂家库存状态详情查询', '.stock.stockIndexView.do*', '/stock/stockIndexView.do*', 'url_resource', 4),
	(2195459, '采购退货查看', '.purchase.returns.view.do*', '/purchase/returns/view.do*', 'url_resource', 6),
	(2195461, '采购订单查看', '.purchase.order.view.do*', '/purchase/order/view.do*', 'url_resource', 5),
	(2195462, '采购付款查看', '.purchase.pay.view.do*', '/purchase/pay/view.do*', 'url_resource', 2),
	(2195463, '收支类别列表', '.finance.costSort.index.do*', '/finance/costSort/index.do*', 'url_resource', 2),
	(2195465, '货品流向', '.barcode.indexADealer.do', '/barcode/indexADealer.do', 'url_resource', 0),
	(2195466, '销售汇总查询', '.salesSummary.index.do', '/salesSummary/index.do', 'url_resource', 0),
	(2195467, '经销商查询消费记录', '.card.spend.index.do', '/card/spend/index.do', 'url_resource', 10),
	(2195468, '销售查询【卡】', '.salesEnquiries.cardIndex.do', '/salesEnquiries/cardIndex.do', 'url_resource', 7),
	(2195469, '销售查询【现金】', '.salesEnquiries.index.do', '/salesEnquiries/index.do', 'url_resource', 5),
	(2195470, '检测代币卡是否存在 是否可用', '.card.replace.getCard.do', '/card/replace/getCard.do', 'url_resource', 6),
	(2195471, '检测代币卡的密码是否正确', '.card.grant.validatePassoword.do', '/card/grant/validatePassoword.do', 'url_resource', 6),
	(2195472, '商品详情', '.salesDetail.view.do', '/salesDetail/view.do', 'url_resource', 9),
	(2195473, '销售订单【现金】详情', '.salesOrder.view.do', '/salesOrder/view.do', 'url_resource', 9),
	(2326528, '收支类别', '.finance.costSort.costSortTree.do*', '/finance/costSort/costSortTree.do*', 'url_resource', 3),
	(2326529, '应收账款【现金】', '.sales.receiveAble.do', '/sales/receiveAble.do', 'url_resource', 7),
	(2326530, '应收账款【现金】详情', '.sales.queryCashIndex.do', '/sales/queryCashIndex.do', 'url_resource', 3),
	(2326531, '销售出库【现金】详情', '.sales.view.do', '/sales/view.do', 'url_resource', 8),
	(2326532, '销售退回【现金】详情', '.salesReturns.view.do', '/salesReturns/view.do', 'url_resource', 7),
	(2326533, '分销商销售【卡】', '.salesSummary.indexDistributor.do', '/salesSummary/indexDistributor.do', 'url_resource', 5),
	(2326534, '商品类型选择', '.base.prosort.prosortTree.do*', '/base/prosort/prosortTree.do*', 'url_resource', 0),
	(2326535, '部门查询选择列表', '.dept.deptTree.do*', '/dept/deptTree.do*', 'url_resource', 9),
	(2326536, '应收账款【卡】', '.sales.receiveAbleCard.do', '/sales/receiveAbleCard.do', 'url_resource', 6),
	(2326537, '应收账款【卡】', '.sales.cardSales.queryCardIndex.do', '/sales/cardSales/queryCardIndex.do', 'url_resource', 6),
	(2326538, '销售应收【卡】详情', '.sales.cardSales.view.do', '/sales/cardSales/view.do', 'url_resource', 7),
	(2326539, '分销商即时库存', '.stock.salesStockIndex.do', '/stock/salesStockIndex.do', 'url_resource', 4),
	(2326540, '判断商品是不是在仓库中', '.stock.judgeStockByProduct.do', '/stock/judgeStockByProduct.do', 'url_resource', 9),
	(2326541, '销售回款【卡】', '.receive.cardIndex.do', '/receive/cardIndex.do', 'url_resource', 7),
	(2916352, '采购入库导出', '.purchase.exportExcel.do*', '/purchase/exportExcel.do*', 'url_resource', 3),
	(2916353, '采购退货导出', '.purchase.returns.exportExcel.do*', '/purchase/returns/exportExcel.do*', 'url_resource', 4),
	(2916354, '采购订单查找', '.purchase.order.find.do*', '/purchase/order/find.do*', 'url_resource', 3),
	(2916355, '选择会员客户', '.base.customer.showIndexHy.do', '/base/customer/showIndexHy.do', 'url_resource', 5),
	(4128768, '销售订单【现金】详情导出', '.salesOrder.exportExcel.do', '/salesOrder/exportExcel.do', 'url_resource', 0),
	(5111808, '消费查询', '/card/grant/indexSales.do', '/card/grant/indexSales.do', 'url_resource', 8),
	(5111809, '选择订单【卡】', '.salesOrder.cardSalesOrder.showIndex.do', '/salesOrder/cardSalesOrder/showIndex.do', 'url_resource', 6),
	(5111810, '选择订单【卡】', '.salesOrder.showIndex.do', '/salesOrder/showIndex.do', 'url_resource', 5),
	(5111811, '销售出库【现金】Excel导出', '.sales.exportExcel.do', '/sales/exportExcel.do', 'url_resource', 5),
	(5111812, '销售出库【现金】详情打印', '.sales.print.do', '/sales/print.do', 'url_resource', 5);
/*!40000 ALTER TABLE `resources` ENABLE KEYS */;


# Dumping structure for table amol.roles
DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
  `ID` int(11) NOT NULL,
  `descn` varchar(255) default NULL,
  `is_sys` char(1) default '0',
  `name` varchar(255) default NULL,
  `version` int(11) default NULL,
  `parent` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK67A8EBDE147EB` (`parent`),
  CONSTRAINT `FK67A8EBDE147EB` FOREIGN KEY (`parent`) REFERENCES `roles` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table amol.roles: ~12 rows (approximately)
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` (`ID`, `descn`, `is_sys`, `name`, `version`, `parent`) VALUES
	(1, '超级管理员角色', '1', 'ROLE_ADMIN', NULL, NULL),
	(2, '系统管理员角色', '1', 'ROLE_SYSTEM', NULL, NULL),
	(3, '银行用户角色', '1', 'ROLE_BANK', NULL, NULL),
	(4, '生产厂家角色', '1', 'ROLE_SUPPLIER', NULL, NULL),
	(5, '注册经销商高级角色', '1', 'ROLE_TOP_DEALER', NULL, NULL),
	(6, '注册经销商普通角色', '1', 'ROLE_TOP_DEALER_GENERAL', NULL, NULL),
	(7, '分销商高级角色', '2', 'ROLE_END_DEALER', NULL, NULL),
	(8, '分销商普通角色', '2', 'ROLE_END_DEALER_GENERAL', NULL, NULL),
	(9, '采购管理角色', '2', 'ROLE_EMPLOYEE_PURCHASE', NULL, NULL),
	(10, '库存管理角色', '2', 'ROLE_EMPLOYEE_STOCK', NULL, NULL),
	(11, '销售管理角色', '2', 'ROLE_EMPLOYEE_SALE', NULL, NULL),
	(12, '财务管理角色', '2', 'ROLE_EMPLOYEE_FINANCE', NULL, NULL);
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;


# Dumping structure for table amol.role_permis
DROP TABLE IF EXISTS `role_permis`;
CREATE TABLE IF NOT EXISTS `role_permis` (
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY  (`role_id`,`permission_id`),
  KEY `FK28B8B0038E47D4C5` (`role_id`),
  KEY `FK28B8B0039A58A5E5` (`permission_id`),
  CONSTRAINT `FK28B8B0038E47D4C5` FOREIGN KEY (`role_id`) REFERENCES `roles` (`ID`),
  CONSTRAINT `FK28B8B0039A58A5E5` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table amol.role_permis: ~15 rows (approximately)
/*!40000 ALTER TABLE `role_permis` DISABLE KEYS */;
INSERT INTO `role_permis` (`role_id`, `permission_id`) VALUES
	(1, 229376),
	(1, 229377),
	(1, 229378),
	(1, 229379),
	(2, 229376),
	(3, 229377),
	(4, 229378),
	(5, 229379),
	(6, 229380),
	(7, 229381),
	(8, 229382),
	(9, 229383),
	(10, 229384),
	(11, 229385),
	(12, 229386);
/*!40000 ALTER TABLE `role_permis` ENABLE KEYS */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
