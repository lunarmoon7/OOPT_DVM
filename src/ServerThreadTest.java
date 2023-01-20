import DVM_Server.DVMServer;

public class ServerThreadTest extends Thread {
    DVMServer server;
    private static ServerThreadTest serverThreadTest = new ServerThreadTest();
    private ServerThreadTest() {
    }
    @Override
    public void run() {
        server = new DVMServer();

        try {
            System.out.println("Server running.....");
            server.run();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static ServerThreadTest getInstance()
    {
        return serverThreadTest;
    }
}
