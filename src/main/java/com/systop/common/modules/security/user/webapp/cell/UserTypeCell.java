package com.systop.common.modules.security.user.webapp.cell;

import org.ecside.core.TableModel;
import org.ecside.core.bean.Column;
import org.ecside.table.cell.AbstractCell;

import com.systop.amol.user.AmolUserConstants;

public class UserTypeCell extends AbstractCell {

	@Override
	protected String getCellValue(TableModel model, Column column) {
		return AmolUserConstants.TYPE_MAP.get(column.getValueAsString());
	}

}
