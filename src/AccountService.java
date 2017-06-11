import java.math.BigDecimal;
import error.AccountNotFoundException;
import error.InsufficientBalanceException;

public interface AccountService {
	public BigDecimal checkBalance(String accountNo) throws AccountNotFoundException;
	public BigDecimal withdraw(String accountNo,BigDecimal amount)throws AccountNotFoundException,InsufficientBalanceException;
}
