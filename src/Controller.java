import DVM_Client.DVMClient;
import Model.Message;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

// DVMClient에서 msg는 json 타입이다. 제공해준 converter로 json으로 변환 후 인자로 전달
// DVMClient에서 run을 실행하면 msg가 서버로 전송된다. 그 후 Client가 스스로 종료함

public class Controller extends JDialog {

	private DVM dvm;
	private int choiceDrinkNum = 0;
	private String choiceDrinkCode = "00";
	private DialogVerficationCode dialogVerificationCode;
	private DialogOption dialogPrintOption;
	private DialogClosetDVM dialogClosetDVM;
	private DialogPrintMenu dialogPrintMenu;
	private DialogProvideDrink dialogProvideDrink;
	private DialogConfirmPayment dialogConfirmPayment;
	private DialogMakeVeriCodeAndShowDVMInfo dialogMakeVeriCodeAndShowInfo;
	private Admin admin;
	private int clickCounter = 0;

	public Controller(DVM dvm, Admin admin) {
		this.dvm = dvm;
		this.dialogClosetDVM = new DialogClosetDVM(this.dvm);
		this.admin = admin;
//		printOption();
	}

	public void printMenu() {
		this.dialogPrintOption.setVisible(false);
		this.dialogPrintMenu = new DialogPrintMenu(this.dvm);
		dialogPrintMenu.setLocationRelativeTo(null);
		dialogPrintMenu.setVisible(true);

		dialogPrintMenu.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				clickCounter++;
				if(clickCounter == 10)
				{
					System.out.println("Clicked 10 times!");
					admin.refillDrink();
					clickCounter = 0;
				}
			}
		});

		JButton printMenuConfirmBtn = dialogPrintMenu.getConfirmBtn();

		printMenuConfirmBtn.addActionListener(new ActionListener() { // 확인버튼
			@Override
			public void actionPerformed(ActionEvent e) {
				choiceDrinkNum = dialogPrintMenu.getChoiceDrinkNum();
				choiceDrinkCode = dialogPrintMenu.getChoiceDrinkCode();
				dvm.setChoiceDrinkCode(choiceDrinkCode);
				dvm.setChoiceDrinkNum(choiceDrinkNum);
				dvm.createNetwork();

				synchronized (this) {
					try
					{
						dvm.getNetwork().checkOtherDVMDrinkExists();
						this.wait(500);
						if(dvm.getConfirmedDVMList() != null)
						{
							ArrayList<Message> tempList = (ArrayList<Message>) (dvm.getConfirmedDVMList().clone());

							dvm.getConfirmedDVMList().clear();
							//여기안
							dvm.getNetwork().checkOtherDVMStock(tempList);
							this.wait(500);
							dvm.calcClosestDVMLoc(); // 계산헀음
							dialogClosetDVM.refresh(); // 리프레쉬
							dialogPrintMenu.dispose();
							printClosestDVMInfo();
						}
						else
						{ // null
							if(dialogPrintMenu.isValidInput())
							{
								if(dvm.getCurrentSellDrink().get(choiceDrinkCode) != null)
								{
									if(dvm.checkOurDVMStock(choiceDrinkCode, choiceDrinkNum))
									{
										dvm.calcClosestDVMLoc(); // 계산헀음
										dialogClosetDVM.refresh(); // 리프레쉬
										dialogPrintMenu.dispose();
										printClosestDVMInfo();
									}
									else
										JOptionPane.showMessageDialog(null, "재고가 부족합니다!");
								}
								else
								{
									dvm.setChoiceDrinkCode(dialogPrintMenu.getChoiceDrinkCode());
									dvm.setChoiceDrinkNum(dialogPrintMenu.getChoiceDrinkNum());
									choiceDrinkNum = dvm.getChoiceDrinkNum();
									choiceDrinkCode = dvm.getChoiceDrinkCode();
									dvm.calcClosestDVMLoc(); // 계산헀음
									dialogClosetDVM.refresh(); // 리프레쉬
									dialogPrintMenu.dispose();
									printClosestDVMInfo();
								}
							}
						}
						dvm.calcClosestDVMLoc(); // 계산헀음
					} catch (Exception ex) {
						ex.printStackTrace();
						System.out.println("error!!!");
					}
				}
			}
		});
	}

	public void insertVerifyCode() {
		// TODO implement here
		this.dialogVerificationCode = new DialogVerficationCode(this.dvm);
		dialogVerificationCode.setVisible(true);
		JButton okButton = dialogVerificationCode.getOkButton();
		final String[] verifyCode = {dialogVerificationCode.getVerifyCodeField()};

		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				verifyCode[0] = dialogVerificationCode.getVerifyCodeField();
				boolean flag = dvm.checkVerificationCode(verifyCode[0]);
				System.out.println("입력한 인증코드 : " + verifyCode[0]);
				if(!flag) {
					JOptionPane.showMessageDialog(dialogVerificationCode, "인증코드가 다릅니다.","Message", JOptionPane.ERROR_MESSAGE);
					dialogVerificationCode.setVerifyCodeField("");
				} else {
					dialogVerificationCode.dispose();
					dialogPrintOption.setVisible(true); // 수정!
					provideDrinkWhenPrepayment(verifyCode[0],true);
				}
			}
		});
	}

	public void printClosestDVMInfo() {
			dialogClosetDVM = new DialogClosetDVM(this.dvm);
			this.dialogClosetDVM.setLocationRelativeTo(null);
		if(dvm.getCalculatedDVMInfo()[0].equals("")) {
			JOptionPane.showMessageDialog(dialogPrintMenu, "음료가 있는 DVM이 존재하지 않습니다.", "Error", JOptionPane.INFORMATION_MESSAGE);
			printMenu();
		} else {
			int totalPrice = 0;

			if (dvm.getCalculatedDVMInfo()[0].equals("Team3")) {
				totalPrice = dvm.getCurrentSellDrink().get(this.choiceDrinkCode).getPrice() * this.choiceDrinkNum;
			} else {
				totalPrice = dvm.getEntireDrinkList()[Integer.parseInt(choiceDrinkCode) - 1].getPrice() * this.choiceDrinkNum;
			}
			dialogClosetDVM.setVisible(true);
			int t = totalPrice;
			JButton printClosetDVMInfoConfirmBtn = dialogClosetDVM.getDialogClosetDVMConfirmBtn();
			printClosetDVMInfoConfirmBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dialogClosetDVM.dispose();
					System.out.println("---------계산된 금액은 ?--------" + t + "원");
					confirmPayment(choiceDrinkCode, choiceDrinkNum, t);
				}
			});
		}
	}

	public void provideDrink(boolean isMyDVM) { // 음료 제공, 음료 제공시 해당 음료를 구매한 개수만큼 기존 재고에서 차감
		this.dialogProvideDrink = new DialogProvideDrink(this.dvm, isMyDVM);
		JButton returnBtn = this.dialogProvideDrink.returnBtn;
		dialogProvideDrink.settingLbl(dvm.getChoiceDrinkNum(), this.dvm.getEntireDrinkList()[Integer.parseInt(dvm.getChoiceDrinkCode())-1].getName());
		dialogProvideDrink.setVisible(true);
		returnBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogProvideDrink.setVisible(false);
			}
		});
	}

	public void provideDrinkWhenPrepayment(String verifyCode, boolean isMyDVM) { // verifyCode는 사용자가 입력한 인증코드로 ODRCHashMap의 msg를 가져오기 위함.
		// 이 함수를 호출했다는 것은 인증코드가 일치한다는 전제가 깔려있음.
		this.dialogProvideDrink = new DialogProvideDrink(this.dvm, isMyDVM);
		Message msg = dvm.getreceivedVerifyCodeMap().get(verifyCode); // 메세지 가져옴.
		dvm.getreceivedVerifyCodeMap().remove(verifyCode);
		String drinkCode = msg.getMsgDescription().getItemCode();
		String drinkName = dvm.getCurrentSellDrink().get(drinkCode).getName();
		int drinkNum = msg.getMsgDescription().getItemNum();
		JButton returnBtn = this.dialogProvideDrink.returnBtn;
		dialogProvideDrink.settingLbl(drinkNum, drinkName);
		dialogProvideDrink.setVisible(true);

		returnBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				dialogProvideDrink.setVisible(false);
				dialogProvideDrink.dispose();
			}
		});
	}
	public void provideDrinkWhenPrePaymentToOtherDVM(String vCode, String dstID)
	{
		this.dialogMakeVeriCodeAndShowInfo = new DialogMakeVeriCodeAndShowDVMInfo(this.dvm);
		this.dialogMakeVeriCodeAndShowInfo.setVisible(true);
		JButton returnBtn = dialogMakeVeriCodeAndShowInfo.returnBtn;

		returnBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dialogMakeVeriCodeAndShowInfo.dispose();
			}
		});

		dialogMakeVeriCodeAndShowInfo.settingLbl(vCode, dstID);
	}

	public void confirmPayment(String drinkCode, int drinkNum, int totalPrice) {
		this.dialogConfirmPayment = new DialogConfirmPayment(this.dvm);
		dialogConfirmPayment.settingTextArea(choiceDrinkNum, dvm.getEntireDrinkList()[Integer.parseInt(choiceDrinkCode) - 1].getName(), totalPrice);
		dialogConfirmPayment.setLocationRelativeTo(null);
		dialogConfirmPayment.setVisible(true);
		JButton okBtn = dialogConfirmPayment.getYesBtn();
		JButton noBtn = dialogConfirmPayment.getNoBtn();

		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(dvm.getCard().getBalance() < totalPrice) { // 잔액 확인
					JOptionPane.showMessageDialog(null, "잔액이 부족합니다!");
					dialogConfirmPayment.dispose();
					printOption();
				} else {
					if (dvm.getCalculatedDVMInfo()[0].equals("Team3")) { //우리 시스템일 때 내부 계산 후
						dialogConfirmPayment.dispose();

						dvm.getCard().setBalance(dvm.getEntireDrinkList()[Integer.parseInt(drinkCode) - 1].getPrice() * drinkNum);
						System.out.println("---------남은 잔액은 ?----------" + dvm.getCard().getBalance());
						dialogPrintOption.setVisible(true);
						if (dvm.purchaseDrink(drinkCode, drinkNum)) {
							System.out.println(dvm.getCurrentSellDrink().get(choiceDrinkCode).getName() + "의 남은 재고는?" + dvm.getCurrentSellDrink().get(choiceDrinkCode).getStock());
							provideDrink(true);
						}
					} else if (dvm.getCalculatedDVMInfo()[0].equals(dvm.getConfirmedDVMList().get(0).getSrcId())) {
						// 외부 시스템일때 recheckStock
						dialogConfirmPayment.dispose();

						ArrayList<Message> tempList = (ArrayList<Message>) (dvm.getConfirmedDVMList().clone());
						dvm.getConfirmedDVMList().clear();

						Message msg = tempList.get(0);

						msg.setSrcId(msg.getDstID());
						msg.setDstID(msg.getSrcId());
						msg.getMsgDescription().setItemCode(drinkCode);
						msg.getMsgDescription().setItemNum(drinkNum);

						tempList.clear();
						tempList.add(msg);
						dvm.getNetwork().checkOtherDVMStock(tempList); // 실제로 메세지 보냄, 다시 recheck 하는 것

						dvm.getCard().setBalance(dvm.getEntireDrinkList()[Integer.parseInt(drinkCode) - 1].getPrice() * drinkNum);
						System.out.println("---------남은 잔액은 ?----------" + dvm.getCard().getBalance());
						// 다이얼로그(인증코드, 좌표 보여주는) 추가해야 됨
						String vCode = dvm.getVerificationCode();
						dvm.getNetwork().sendSoldDrinkInfo(msg.getSrcId(), msg.getDstID(), vCode); // 선결제 메세지 상대 DVM으로 보냄
						dialogPrintOption.setVisible(true);
						provideDrinkWhenPrePaymentToOtherDVM(vCode, msg.getDstID()); // 다이얼로그가 안뜨는 현상 발쌩
					}
				}
			}
		});

		noBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogConfirmPayment.dispose();
				dialogPrintMenu.setChoiceDrinkNum(0);
				dialogPrintMenu.setChoiceDrinkCode("00");
				choiceDrinkCode = "00";
				choiceDrinkNum = 0;
				dvm.setChoiceDrinkCode(choiceDrinkCode);
				dvm.setChoiceDrinkNum(choiceDrinkNum);
				dialogPrintOption.setVisible(true);
				dialogPrintMenu.refresh();
			}
		});
		dialogConfirmPayment.attach();
	}

	public void printOption() {
		this.dialogPrintOption = new DialogOption();
		dialogPrintOption.setVisible(true);
		JButton printMenuBtn = this.dialogPrintOption.getPrintMenuBtn();
		JButton verificationCodeInpBtn = this.dialogPrintOption.getVerificationCodeInpBtn();

		printMenuBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				printMenu();
			}
		});

		verificationCodeInpBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertVerifyCode();
			}
		});
	}
}
