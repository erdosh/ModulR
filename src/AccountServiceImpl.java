import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import error.AccountNotFoundException;
import error.InsufficientBalanceException;


public class AccountServiceImpl implements AccountService{
	Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


	@Override
	public BigDecimal checkBalance(String accountNo) throws AccountNotFoundException {
		Account account = null;
		AccountDAOService accountDAO = ServiceFactory.getAccountDAOService();
		account = accountDAO.get(accountNo);
		if(account == null){
			throw new AccountNotFoundException();
		}
		return account.balance;
	}

	@Override
	public BigDecimal withdraw(String accountNo, BigDecimal amount) throws AccountNotFoundException,InsufficientBalanceException{
		Account account = null;
		AccountDAOService accountDAO = ServiceFactory.getAccountDAOService();
		account = accountDAO.get(accountNo);
		if(account == null){
			throw new AccountNotFoundException();
		}
		if(account.balance.compareTo(amount) >= 0){
			account.balance = account.balance.subtract(amount);
			accountDAO.save(account);
			return account.balance;
		}
		else{
			throw new InsufficientBalanceException();
		}
	}

}
