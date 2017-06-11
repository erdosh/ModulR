package utility;

import java.math.BigDecimal;

public class TextUtil {

	public static String formatCurrency(BigDecimal currency){
		return currency.toPlainString();
	}
	
}
