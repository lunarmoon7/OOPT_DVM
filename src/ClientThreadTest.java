import DVM_Client.DVMClient;
import DVM_Server.DVMServer;
import GsonConverter.Deserializer;
import GsonConverter.Serializer;
import Model.Message;

public class ClientThreadTest extends Thread{
    DVMServer server;
    Serializer serializer = new Serializer();
    public ClientThreadTest() {}

    @Override
    public void run() {
        server = new DVMServer();
        try {
            while(true) { // 실시간으로 MSG 전송 받기위한 '무한루프'
                getMSG();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getMSG() {
            if (server.msgList.size() > 0) {
                System.out.println("I got MSG\n");
                Message msg = server.msgList.get(server.msgList.size() - 1);
                String str = serializer.message2Json(msg);
                System.out.println(str);
                System.out.println("srcID : " + server.msgList.get(server.msgList.size() - 1).getSrcId() + "\n" +
                        "dstID : " + server.msgList.get(server.msgList.size() - 1).getDstID() + "\n" +
                        "msgType : " + server.msgList.get(server.msgList.size() - 1).getMsgType() + "\n" +
                        "--------------msgDesc----------------\n" +
                        "ItemCode : " + server.msgList.get(server.msgList.size() - 1).getMsgDescription().getItemCode() + "\n" +
                        "ItemNum : " + server.msgList.get(server.msgList.size() - 1).getMsgDescription().getItemNum() + "\n" +
                        "dvmXCoord : " + server.msgList.get(server.msgList.size() - 1).getMsgDescription().getDvmXCoord() + "\n" +
                        "dvmYCoord : " + server.msgList.get(server.msgList.size() - 1).getMsgDescription().getDvmYCoord() + "\n" +
                        "authCode : " + server.msgList.get(server.msgList.size() - 1).getMsgDescription().getAuthCode() + "\n");
                /* 필수 */
                server.msgList.remove(server.msgList.size() - 1);
            }
    }
}


