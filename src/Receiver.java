import DVM_Client.DVMClient;
import DVM_Server.DVMServer;
import GsonConverter.Serializer;
import Model.Message;

import java.sql.SQLOutput;
import java.util.*;

public class Receiver extends Thread { // 상대 DVM에서 발신한 MSG 수신하는 파트
	private DVMServer server;
	private static DVM dvm;
	private Serializer serializer;
	private static HashMap<String, String> ipMap = new HashMap<>();
	private static final String TEAM3IP = "localhost"; // 작동하는지 확인하기 위한 임시 변수
	private static final String TEAM1IP = "";
	private static final String TEAM2IP = "";
	private static final String TEAM4IP = "";
	private static final String TEAM5IP = "";
	private static final String TEAM6IP = "";

	private static Receiver receiver = new Receiver();

	public static Receiver getInstance() {
		receiver.dvm = dvm;
		receiver.server = new DVMServer();
		receiver.serializer = new Serializer();
		return receiver;
	}

//	public Receiver(DVM dvm) {
//		this.dvm = dvm;
//		this.server = new DVMServer();
//		this.serializer = new Serializer();
//	}

	public int responseStockConfirmMsg(Message msg) { // 재고 응답
		dvm.getConfirmedDVMList().add(msg);
		return 0;
	}

	public int responseSalesConfirmMsg(Message msg) { // 판매 응답, 상대 DVM 에서 음료 판매한다는 메세지를 받음 -> List에 받은 메세지 추가
		dvm.getConfirmedDVMList().add(msg);
		return 0;
	}

	public void handlePrepaymentMsg(Message msg) { // 선결제 메세지 받아서 핸들
		String drinkCode = msg.getMsgDescription().getItemCode(); // 메세지에 들어있는 음료코드 가져온다.
		int drinkNum = msg.getMsgDescription().getItemNum(); // 메세지에 들어있느 음료개수 가져온다.
		Drink tempDrink = dvm.getCurrentSellDrink().get(drinkCode); // 현재 DVM3에서 파는 음료 객체를 가져온다.
		tempDrink.setStock(tempDrink.getStock() - drinkNum); // 가져온 음료 객체의 재고를 변경한다.(=재고 차감)
		dvm.getCurrentSellDrink().put(drinkCode, tempDrink); // 작동 완료 확인
		/**/
		// 인증코드 포함된 메세지 넣음
		String verifyCode = msg.getMsgDescription().getAuthCode();
		dvm.getreceivedVerifyCodeMap().put(verifyCode, msg); // 인증코드를 key값으로해서 msg를 value로 넣는다.
	}

	public void handleStockCheckRequestAndSend(Message msg) { // 재고 확인 메세지
		String srcID = msg.getSrcId(); // 상대 DVM
		String dstID = msg.getDstID(); // 우리 DVM
		int myCoordX = dvm.getDvm3X();
		int myCoordY = dvm.getDvm3Y();
		String drinkCode = msg.getMsgDescription().getItemCode();
		int drinkNum = msg.getMsgDescription().getItemNum();
		boolean isOurDVMHasStock = dvm.checkOurDVMStock(drinkCode, drinkNum);
		Message sendToMsg = new Message();
		Message.MessageDescription sendToMsgDesc = new Message.MessageDescription();

		if(isOurDVMHasStock) { // 재고 있을 때만 보냄
			sendToMsgDesc.setItemCode(drinkCode);
			sendToMsgDesc.setItemNum(drinkNum);
			sendToMsgDesc.setDvmXCoord(myCoordX);
			sendToMsgDesc.setDvmYCoord(myCoordY);
			// msgDesc 클래스에 setDstID 없음.. 호출 불가

			msgSetting(sendToMsg, dstID, srcID, "StockCheckResponse", sendToMsgDesc);

			// 메세지를 json 타입으로 변환
			String msgToJson = serializer.message2Json(sendToMsg);

//			DVMClient client = new DVMClient(TEAM3IP, msgToJson); // 메세지 보내기위해 클라이언트 선언
//
//			// 클라이언트에 메세지 실어서 보낸다.
//			try {
//				client.run();
//			}catch (Exception e) {
//				e.printStackTrace();
//				System.out.println("Message send Failed..");
//			}
		}
	}
	public void handleSaleCheckRequestAndSend(Message msg) { // 판매 확인 메세지에 대해 응답 보내는 것
		String srcID = msg.getSrcId(); // 상대 DVM
		String dstID = msg.getDstID(); // 우리 DVM
		int myCoordX = dvm.getDvm3X();
		int myCoordY = dvm.getDvm3Y();
		String drinkCode = msg.getMsgDescription().getItemCode();
		boolean isOurDVMSellDrink = (dvm.getCurrentSellDrink().get(drinkCode) != null);
		Message sendToMsg = new Message();
		Message.MessageDescription sendToMsgDesc = new Message.MessageDescription();
		if(isOurDVMSellDrink) { // 판매하면 보냄
			// 판매하지 않으면 안보냄?
			sendToMsgDesc.setItemCode(drinkCode);
			sendToMsgDesc.setDvmXCoord(myCoordX);
			sendToMsgDesc.setDvmYCoord(myCoordY);
			// msgDesc 클래스에 setDstID 없음.. 호출 불가

			msgSetting(sendToMsg, dstID, srcID, "SalesCheckResponse", sendToMsgDesc);

			// 메세지를 json 타입으로 변환
			String msgToJson = serializer.message2Json(sendToMsg);

//			DVMClient client = new DVMClient(TEAM3IP, msgToJson); // 메세지 보내기위해 클라이언트 선언
//
//			// 클라이언트에 메세지 실어서 보낸다.
//			try {
//				client.run();
//			}catch (Exception e) {
//				e.printStackTrace();
//				System.out.println("Message send Failed..");
//			}
		}
	}

	private void msgSetting(Message sendToOtherMsg, String srcID, String dstID, String msgType, Message.MessageDescription msgDesc){
		sendToOtherMsg.setSrcId(srcID);
		sendToOtherMsg.setDstID(dstID);
		sendToOtherMsg.setMsgType(msgType);
		sendToOtherMsg.setMsgDescription(msgDesc);
	}

	@Override
	public void run() {
		super.run();
		try {
			while(true){
				getMSG();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getMSG() {
		/* 여기서 제한시간 걸기 ? 약 1.5초 */
		if (!server.msgList.isEmpty()) {
			Message msg = server.msgList.get(this.server.msgList.size() - 1);
			System.out.println(server.msgList.get(0).getMsgType());
			String msgType = msg.getMsgType();

			switch (msgType){
				case "StockCheckRequest" : // 우리 DVM에서 응답 필수
					/* 상대 DVM 에서 보낸 메세지 수신하는 파트 */
					System.out.println("RECEIVED");
					handleStockCheckRequestAndSend(msg);
					break;
				case "StockCheckResponse" :
					responseStockConfirmMsg(msg);
					break;
				case "PrepaymentCheck":
					/* 우리 DVM에서 상대 쪽에서 받은 MSG를 기반으로 음료 코드에 맞는 음료의 개수를 UPDATE */
					// 재고 먼저 차감
					handlePrepaymentMsg(msg);
					// dvm의 hashmap에 인증코드를 key로, msg를 value로 넣는다
					// 음료랑 개수를 팝업형태로 마지막에 출력해줘야함.
					break;
				case "SalesCheckRequest": // 우리 DVM에서 응답 필수
					/* 상대 DVM 에서 보낸 메세지 수신하는 파트 */
					System.out.println("RECEIVED");
					handleSaleCheckRequestAndSend(msg);
					break;
				case "SalesCheckResponse" :
					responseSalesConfirmMsg(msg);
					break;
				default:
					System.out.println("error!");
					break;
			}
			server.msgList.remove(server.msgList.size() - 1); // add한 메세지 제거
		}
	}
}