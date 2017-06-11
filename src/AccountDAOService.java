
public class AccountDAOService {
	
	public void save(Account account) {
		AccountDB.save(account);
	}
	
	public Account get(String accountNo){
		return AccountDB.get(accountNo);
	}
	
	public void initAccountDB(){
		
	}
}
