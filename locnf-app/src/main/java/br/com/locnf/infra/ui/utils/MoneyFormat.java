package br.com.locnf.infra.ui.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MoneyFormat {

	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	public static String format(BigDecimal obj) {
		return df.format(obj);
	}
}
