
import javax.swing.*;

public class Admin extends AbstractAdminClass{
	private static Boolean networkConnected;
	private static DVM dvm;

	private static Admin admin;

	private Admin(Boolean networkStatus, DVM dvm) {
		this.networkConnected = networkStatus;
		this.dvm = dvm;
//		if (this.networkConnected){
//			systemStart();
//		}
	}

	public static Admin getInstance(Boolean networkStatus, DVM dvm)
	{
		admin = new Admin(networkStatus, dvm);
		return admin;
	}

//	public static Admin getInstance()
//	{
//		return admin;
//	}

	@Override
	public void refillDrink()
	{
		String editDrinkCode = "01"; //임의의 코드
		for(int i = 0; i < dvm.getEntireDrinkList().length; i++)
		{
			if(dvm.getCurrentSellDrink().get(editDrinkCode) != null)
			{
				dvm.getCurrentSellDrink().get(editDrinkCode).setStock(dvm.getCurrentSellDrink().get(editDrinkCode).getStock()+10);
				System.out.println(dvm.getCurrentSellDrink().get(editDrinkCode).getName()+"의 재고는 이제 "+dvm.getCurrentSellDrink().get(editDrinkCode).getStock()+"개 입니다!");
				editDrinkCode = "0"+Integer.toString(Integer.parseInt(editDrinkCode)+1);
			}
		}
	}

	public void systemStart() {
		System.out.println("flag_systemstart()");
		Controller controller = new Controller(dvm, this);
		controller.printOption();
	}

}