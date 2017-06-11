import java.math.BigDecimal;
import java.util.Hashtable;

public class AccountDB {
	private static Hashtable<String,Account> accounts = new Hashtable<String,Account>();
	static{
		init();
	}
	
	public static void init(){ 
		accounts.put("01001",new Account("01001", new BigDecimal("2738.59")));
		accounts.put("01002",new Account("01002", new BigDecimal("23.00"  )));
		accounts.put("01003",new Account("01003", new BigDecimal("0.00"   )));
	}
	
	public static void save(Account account){
		accounts.put(account.accountNo, account);
	}
	
	public static Account get(String accountNo){
		return accounts.get(accountNo);
	}

}
