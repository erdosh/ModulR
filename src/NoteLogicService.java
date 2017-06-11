import java.math.BigDecimal;
import java.util.Hashtable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import message.MessageUtil;

public class NoteLogicService {
	Logger logger = LoggerFactory.getLogger(NoteLogicService.class);
	private int FIVE	= 5;
	private int TEN		=10;
	private int TWENTY	=20;
	private int FIFTY	=50;
	
	public String withdraw(Hashtable<Integer,Integer> bankNotes,int amount,NoteTemplate noteTemplate){
		bankNotes.put(FIFTY, bankNotes.get(FIFTY)-noteTemplate.fifty);
		bankNotes.put(TWENTY, bankNotes.get(TWENTY)-noteTemplate.twenty);
		bankNotes.put(TEN, bankNotes.get(TEN)-noteTemplate.ten);
		bankNotes.put(FIVE, bankNotes.get(FIVE)-noteTemplate.five);
		
		return  "NOTES: "+
				(noteTemplate.fifty>0  ? Integer.toString(noteTemplate.fifty)+	"X50 "	: "")+
				(noteTemplate.twenty>0 ? Integer.toString(noteTemplate.twenty) +"X20 "	: "")+
				(noteTemplate.ten>0    ? Integer.toString(noteTemplate.ten) + 	"X10 "	: "")+
				(noteTemplate.five>0   ? Integer.toString(noteTemplate.five) +	"X5 "		: "");
	}
	
	public String checkBalanceApplyProvision(Hashtable<Integer,Integer> bankNotes,int intAmount,NoteTemplate noteTemplate){
		int numberOfFifty = 0;
		int numberOfTwenty = 0;
		int numberOfTen = 0;
		int numberOfFive = 0;
		int atmFiftyAmount = bankNotes.get(FIFTY)*50;
		int atmTwentyAmount = bankNotes.get(TWENTY)*20;
		int atmTenAmount = bankNotes.get(TEN)*10;
		int atmFiveAmount =bankNotes.get(FIVE)*5;
		if(atmFiftyAmount == 0){
			logger.warn(MessageUtil.RUN_OUT_OF_NOTES_50_CHECK_ATM_UNIT);
		}
		if(atmTwentyAmount == 0){
			logger.warn(MessageUtil.RUN_OUT_OF_NOTES_20_CHECK_ATM_UNIT);
		}
		if(atmTenAmount == 0){
			logger.warn(MessageUtil.RUN_OUT_OF_NOTES_10_CHECK_ATM_UNIT);
		}
		if(atmFiveAmount == 0){
			logger.warn(MessageUtil.RUN_OUT_OF_NOTES_5_CHECK_ATM_UNIT);
		}
		int atmBalance = atmFiftyAmount+atmTwentyAmount+atmTenAmount+atmFiveAmount;
		if(atmBalance < intAmount){
			logger.warn(MessageUtil.ATM_BALANCE_INSUFFICIENT_CHECK_ATM_UNIT);
			return MessageUtil.ATM_BALANCE_INSUFFICIENT+new BigDecimal(atmBalance).toPlainString();
		}
		while((atmFiftyAmount-50 >=0)&&(intAmount-50>=0)){
			atmFiftyAmount-=50;
			intAmount-=50;
			numberOfFifty++;
		}
		while((atmTwentyAmount-20>=0)&&(intAmount-20>=0)){
			atmTwentyAmount-=20;
			intAmount-=20;
			numberOfTwenty++;
		}
		while((atmTenAmount-10>=0)&&(intAmount-10>=0)){
			atmTenAmount-=10;
			intAmount-=10;
			numberOfTen++;
		}
		while((atmFiveAmount-5>=0)&&(intAmount-5>=0)){
			atmFiveAmount-=5;
			intAmount-=5;
			numberOfFive++;
		}
		if(intAmount == 0){
			//Just below is the refinement for "Always disburse at least one 5 note, if possible " spec,this sort of refinement is enough
			if((numberOfFive == 0 && numberOfTen == 0 && numberOfTwenty == 0 && numberOfFifty > 0) && 
					(atmFiveAmount > 5 && atmTwentyAmount > 20)){
				numberOfFifty--;
				numberOfTwenty++;
				numberOfTwenty++;
				numberOfFive++;
				numberOfFive++;
			}
			else if( (numberOfFive == 0 && numberOfTen == 0 && numberOfTwenty == 0 && numberOfFifty > 0) && 
				(atmFiveAmount > 5 && atmTenAmount > 10 && atmTwentyAmount > 0)){
				numberOfFifty--;
				numberOfTwenty++;
				numberOfTen++;
				numberOfTen++;
				numberOfFive++;
				numberOfFive++;
			}
			else if(numberOfFive == 0 && numberOfTen == 0 && numberOfTwenty > 0 && atmFiveAmount > 5 && atmTenAmount > 0){
				numberOfTwenty--;
				numberOfTen++;
				numberOfFive++;
				numberOfFive++;
			}
			else if(numberOfFive == 0 && numberOfTen>0 && atmFiveAmount > 5){
				numberOfTen--;
				numberOfFive++;
				numberOfFive++;
			}
			noteTemplate.setValues(numberOfFive, numberOfTen, numberOfTwenty, numberOfFifty);
			return "";
		}
		else if(intAmount %10 > 0){
			return MessageUtil.RUN_OUT_OF_SMALL_NOTES_ENTER_MULTIPLES_OF_TEN;
		}
		else if(intAmount %20 > 0){
			return MessageUtil.RUN_OUT_OF_SMALL_NOTES_ENTER_MULTIPLES_OF_TWENTY;
		}
		else if(intAmount %50 >0){
			return MessageUtil.RUN_OUT_OF_SMALL_NOTES_ENTER_MULTIPLES_OF_FIFTY;
		}
		return "";
	}
	
	public static class NoteTemplate{
		int five;
		int ten;
		int twenty;
		int fifty;
		public void setValues(int five,int ten,int twenty,int fifty){
			this.five = five;
			this.ten = ten;
			this.twenty = twenty;
			this.fifty = fifty;
		}
	}
}
