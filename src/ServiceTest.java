import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import message.MessageUtil;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Before;

public class ServiceTest {
	Logger logger = LoggerFactory.getLogger(ServiceTest.class);

	AccountService accountService;
	ATMServiceImpl atmService;
	AccountDAOService accountDAOService;
	
	@Before
	public void setUp(){
		accountService = ServiceFactory.getAccountService();
		atmService = ServiceFactory.getATMService();
		accountDAOService = ServiceFactory.getAccountDAOService();
	}
	@Test
	public void test(){
		//bulkTest();
		testIndependentOnAccount1FocusOnBanknoteDiversity();
		testIndependentOnAccount3();
		testIndependentOnAccount2();
	}
	
	public void testIndependentOnAccount3(){
		atmService.replenish(0, 0, 0, 0);
		Assert.assertTrue(atmService.withdraw("01003", new BigDecimal("50.00")).equals(MessageUtil.INSUFFICIENT_BALANCE));
		Assert.assertTrue(atmService.withdraw("01003", new BigDecimal("5.00")).equals(MessageUtil.ATM_AMOUNT_MUST_BE_GREATER));
		Assert.assertTrue(atmService.withdraw("01003", new BigDecimal("265.00")).equals(MessageUtil.ATM_AMOUNT_MUST_BE_LESS));
		Assert.assertTrue(atmService.withdraw("01003", new BigDecimal("20.00")).equals(MessageUtil.INSUFFICIENT_BALANCE));
		Assert.assertTrue(atmService.withdraw("01003", new BigDecimal("250.00")).equals(MessageUtil.INSUFFICIENT_BALANCE));
	}
	public void testIndependentOnAccount2(){
		atmService.replenish(0, 0, 10, 0);
		Assert.assertTrue(atmService.withdraw("01002", new BigDecimal("50.00")).equals(MessageUtil.INSUFFICIENT_BALANCE));
		Assert.assertTrue(atmService.withdraw("01002", new BigDecimal("20.00")).startsWith("NOTES:"));
	}
	public void testIndependentOnAccount1(){
		atmService.replenish(50, 50, 50, 50);
		
	}
	public void testIndependentOnAccount1FocusOnBanknoteDiversity(){
		atmService.replenish(5, 1, 6, 6);
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("50.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("50.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("250.00")).startsWith("NOTES:"));
		
	}
	public void bulkTest(){
		atmService.replenish(50, 50, 50, 50);
		Assert.assertTrue(atmService.checkBalance("01003").equals("0.00"));
		Assert.assertTrue(atmService.withdraw("01003",new BigDecimal("150.00")).equals(MessageUtil.INSUFFICIENT_BALANCE));
		/*below poor man test*/
		Assert.assertTrue(atmService.withdraw("01002", new BigDecimal("23.00")).equals(MessageUtil.ATM_AMOUNT_MUST_BE_MULTIPLES_OF_X));
		Assert.assertTrue(atmService.withdraw("01002", new BigDecimal("25.00")).equals(MessageUtil.INSUFFICIENT_BALANCE));
		Assert.assertFalse(atmService.withdraw("01002", new BigDecimal("10.00")).startsWith("NOTES:"));
		/*below rich man test*/
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("20.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("50.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("100.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("110.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("175.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("300.00")).equals(MessageUtil.ATM_AMOUNT_MUST_BE_LESS));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("250.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("15.00")).equals(MessageUtil.ATM_AMOUNT_MUST_BE_GREATER));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("110.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("195.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("245.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("245.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("245.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("245.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("250.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("250.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("250.00")).equals(MessageUtil.INSUFFICIENT_BALANCE));
		Assert.assertFalse(atmService.withdraw("01001", new BigDecimal("250.00")).startsWith("NOTES:"));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("250.00")).equals(MessageUtil.INSUFFICIENT_BALANCE));
		Assert.assertTrue(atmService.withdraw("01001", new BigDecimal("240.00")).startsWith("NOTES:"));
 
	}
	
	public void checkAccountServiceExceptions(){
		try {
			Assert.assertTrue(accountService.withdraw("01002",new BigDecimal("20.00")).equals(new BigDecimal("3.00")) );
			Assert.assertFalse(accountService.withdraw("01002",new BigDecimal("2.00")).equals(new BigDecimal("2.00")) );
		} catch (Exception e) {
			logger.error("Assertion caught exception: " + e.toString());
		}
	}
}
