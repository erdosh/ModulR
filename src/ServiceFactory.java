
public class ServiceFactory {

	public static ATMServiceImpl atmService;
	public static AccountService accountService;
	public static AccountDAOService accountDAOService;
	public static NoteLogicService noteLogicService;
	
	public static ATMServiceImpl getATMService(){
		if(atmService == null){
			atmService = new ATMServiceImpl();
		}
		return atmService;
	}
	
	public static AccountService getAccountService(){
		if(accountService == null){
			accountService = new AccountServiceImpl();
		}
		return accountService;
	}
	
	public static AccountDAOService getAccountDAOService(){
		if(accountDAOService == null){
			accountDAOService = new AccountDAOService();
		}
		return accountDAOService;
	}
	
	public static NoteLogicService getNoteLogicService(){
		if(noteLogicService == null){
			noteLogicService = new NoteLogicService();
		}
		return noteLogicService;
	}
}
