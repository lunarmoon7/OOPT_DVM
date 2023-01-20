import DVM_Client.DVMClient;
import DVM_Server.DVMServer;
import GsonConverter.Serializer;
import Model.Message;

import java.util.*;

public class Network {
	private String choiceDrinkCode;
	private int choiceDrinkNum;
	private Serializer serializer;
	private static final String TEAM3IP = "localhost";

	/* IP 수정 필요 */
	private static final String TEAM1IP = "";
	private static final String TEAM2IP = "";
	private static final String TEAM4IP = "";
	private static final String TEAM5IP = "";
	private static final String TEAM6IP = "";

	private static HashMap<String, String> ipMap = new HashMap<>();

	public Network(String choiceDrinkCode, int choiceDrinkNum) {
		this.choiceDrinkCode = choiceDrinkCode;
		this.choiceDrinkNum = choiceDrinkNum;
		this.serializer = new Serializer();
//		initIP();
	}

	public void initIP() {
		ipMap.put("Team1", TEAM1IP);
		ipMap.put("Team2", TEAM2IP);
		ipMap.put("Team4", TEAM4IP);
		ipMap.put("Team5", TEAM5IP);
		ipMap.put("Team6", TEAM6IP);
	}

	public void sendBroadcastMsg(String ip, Message msg) {
		DVMClient dvmClient = new DVMClient(ip, serializer.message2Json(msg));
		try {
			dvmClient.run();
			System.out.println("send");
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("error send");
		}
	}

	// 상대 DVM들의 IP를 알아야 함. 개인적으로는 보낼 수 없음.
	// 수업 시간에 같이 있을 때 테스트 할 떄 필요함.

	public void checkOtherDVMDrinkExists(){	// 모두에 전송
		Message msg1 = new Message();
		Message msg2 = new Message();
		Message msg4 = new Message();
		Message msg5 = new Message();
		Message msg6 = new Message();

		Message.MessageDescription msgDesc = new Message.MessageDescription();

		msgDesc.setItemCode(this.choiceDrinkCode);
		msgDesc.setItemNum(this.choiceDrinkNum);

		msgSetting(msg1, "Team3", "Team1", "SalesCheckRequest", msgDesc);
		msgSetting(msg2, "Team3", "Team2", "SalesCheckRequest", msgDesc);
		msgSetting(msg4, "Team3", "Team4", "SalesCheckRequest", msgDesc);
		msgSetting(msg5, "Team3", "Team5", "SalesCheckRequest", msgDesc);
		msgSetting(msg6, "Team3", "Team6", "SalesCheckRequest", msgDesc);


		// 실행 하기 전 주석 풀어야 합니다!
		// IP 인자 수정 필요!
//		sendBroadcastMsg(ipMap.get(msg1.getDstID()), msg1); // Team1
//		sendBroadcastMsg(ipMap.get(msg2.getDstID()), msg2); // Team2
//		sendBroadcastMsg(ipMap.get(msg4.getDstID()), msg4); // Team4
//		sendBroadcastMsg(ipMap.get(msg5.getDstID()), msg5); // Team5
//		sendBroadcastMsg(ipMap.get(msg6.getDstID()), msg6); // Team6
	}

	private void msgSetting(Message msgOfSendingToOther, String srcID, String dstID, String msgType, Message.MessageDescription msgDesc){
		msgOfSendingToOther.setSrcId(srcID);
		msgOfSendingToOther.setDstID(dstID);
		msgOfSendingToOther.setMsgType(msgType);
		msgOfSendingToOther.setMsgDescription(msgDesc);
	}

	public void checkOtherDVMStock(ArrayList<Message> confirmedList) { // 재고 확인 메세지 보냄, 음료 재고 확인 메세지 설정

		for(Message msg: confirmedList) {
			Message msgOfSendingToOther = new Message(); // 상대 DVM으로 보내는 메세지
			Message.MessageDescription msgDesc = new Message.MessageDescription();

			String srcID = msg.getDstID(); // 도착지 설정
			String dstID = msg.getSrcId();
			String msgType = "StockCheckRequest";
			String drinkCode = msg.getMsgDescription().getItemCode();
			int drinkNum = msg.getMsgDescription().getItemNum();

			msgDesc.setItemCode(drinkCode);
			msgDesc.setItemNum(drinkNum);

			msgSetting(msgOfSendingToOther, srcID, dstID, msgType, msgDesc);

//			sendBroadcastMsg(TEAM3IP, msgOfSendingToOther);
			sendBroadcastMsg(ipMap.get(dstID), msgOfSendingToOther);
		}
	}

	public void sendSoldDrinkInfo(String srcID, String dstID, String verificationCode) {
		Message msgOfSendingToOther = new Message();
		Message.MessageDescription msgDesc = new Message.MessageDescription();

		msgDesc.setItemCode(this.choiceDrinkCode);
		msgDesc.setItemNum(this.choiceDrinkNum);
		msgDesc.setAuthCode(verificationCode);

		msgSetting(msgOfSendingToOther, srcID, dstID, "PrepaymentCheck", msgDesc);

//		sendBroadcastMsg(TEAM3IP, msgOfSendingToOther);
		sendBroadcastMsg(dstID, msgOfSendingToOther);
	}
}