import java.math.BigDecimal;
import java.util.Hashtable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import error.AccountNotFoundException;
import message.MessageUtil;
import utility.TextUtil;

public class ATMServiceImpl {
	Logger logger = LoggerFactory.getLogger(ATMServiceImpl.class);
	int MAX_AMOUNT = 250;
	int MIN_AMOUNT = 20;
	private int FIVE	= 5;
	private int TEN		=10;
	private int TWENTY	=20;
	private int FIFTY	=50;
	public ATMServiceImpl(){
		banknotes= new Hashtable<Integer,Integer>(); 
		banknotes.put(5, 0);
		banknotes.put(10, 0);
		banknotes.put(20, 0);
		banknotes.put(50, 0);
	}

	private Hashtable<Integer,Integer> banknotes;

	public void replenish(int countFifty,int countTwenty,int countTen,int countFive){
		banknotes.put(FIVE	,countFive);
		banknotes.put(TEN	,countTen);
		banknotes.put(TWENTY,countTwenty);
		banknotes.put(FIFTY	,countFifty);
	}

	public String checkBalance(String accountNo){
		AccountService accountService = ServiceFactory.getAccountService();
		try {
			return TextUtil.formatCurrency(accountService.checkBalance(accountNo));
		} catch (AccountNotFoundException e) {
			logger.error(MessageUtil.ACCOUNT_NOT_FOUND);
			return MessageUtil.ACCOUNT_NOT_FOUND;
		}
	}

	public String withdraw(String accountNo,BigDecimal amount){
		int intAmount = amount.intValue();
		if(intAmount > 250){
			return MessageUtil.ATM_AMOUNT_MUST_BE_LESS;
		}
		else if(intAmount < 20){
			return MessageUtil.ATM_AMOUNT_MUST_BE_GREATER;
		}
		else if(intAmount %5 > 0){
			return MessageUtil.ATM_AMOUNT_MUST_BE_MULTIPLES_OF_X;
		}
		AccountService accountService = ServiceFactory.getAccountService();
		BigDecimal balance = null;
		try {
			balance = accountService.checkBalance(accountNo);
		} catch (AccountNotFoundException e) {
			logger.error(MessageUtil.ACCOUNT_NOT_FOUND);
			return MessageUtil.ACCOUNT_NOT_FOUND;
		}
		if(balance.compareTo(amount) < 0){
			return MessageUtil.INSUFFICIENT_BALANCE;
		}
		NoteLogicService noteLogicService = ServiceFactory.getNoteLogicService();
		NoteLogicService.NoteTemplate noteTemplate = new NoteLogicService.NoteTemplate();
		String atmBalanceCheckError  = noteLogicService.checkBalanceApplyProvision(banknotes,amount.intValue(),noteTemplate);
		String withdrawResult = "";
		if(atmBalanceCheckError.isEmpty()){
			withdrawResult = noteLogicService.withdraw(banknotes,amount.intValue(),noteTemplate);
		}
		else{
			return atmBalanceCheckError;
		}
		try {
			accountService.withdraw(accountNo, amount);
		} catch (Exception e) {// We already check the account and balance above so simply ignore Exception assuming this is a single instance single thread platform
		}
		logger.info(MessageUtil.WITHDRAW_SUCCESSFUL, accountNo);
		logger.info("Withdraw detail: Amount: " + amount + " " + withdrawResult);
		logger.info("New Balance:" + checkBalance(accountNo));
		return withdrawResult;
	}

}
