import java.math.BigDecimal;

public class Account {

	public Account(String accountNo, BigDecimal balance) {
		this.accountNo = accountNo;
		this.balance = balance;
	}
	
	public String accountNo;
	public BigDecimal balance;

}
