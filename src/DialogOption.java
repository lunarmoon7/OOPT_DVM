import javax.swing.*;
import java.awt.*;

public class DialogOption extends JFrame { // 맨 처음 화면 보여주는 다이얼로그
    private DVM dvm;
    private JButton printMenuBtn;
    private JButton verificationCodeInpBtn;
    private Dimension screenSize; // 모니터 화면 크기 & 메인 프레임 크기
    private int xLoc = 0, yLoc = 0;

    public DialogOption() {
        this.printMenuBtn = new JButton("메뉴 출력");
        this.verificationCodeInpBtn = new JButton("인증 코드 입력");
        initLayout();
    }

    public void initLayout() {
        setLocationRelativeTo(null);
        setVisible(false);
        setSize(600, 750); // 메인 프레임 크기 설정
        screenSizeLocation(); // 모니터, 메인 프레임 크기 설정
        setTitle("객지방 3조 분산자판기"); // 프레임 제목
        setLocation(xLoc, yLoc);
        getContentPane().setForeground(Color.WHITE); //
        setResizable(false); // 메인 프레임 사이즈 조절 불가능
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X 버튼 누르면 종료
        getContentPane().setLayout(null);
        verificationCodeInpBtn.setBounds(getWidth()/2-150, getHeight()/2-150, 300, 100);
        printMenuBtn.setBounds(getWidth()/2-150,getHeight()/2-50, 300, 100);
        getContentPane().add(printMenuBtn);
        getContentPane().add(verificationCodeInpBtn);
    }

    private void screenSizeLocation() { // 매인 프레임의 위치 (x, y) 값을 구함
        screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 화면의 크기 구하기
        // (모니터 화면 가로 - 프레임 화면 가로) / 2, (모니터 화면 세로 - 프레임 화면 세로) / 2
        xLoc = (screenSize.width - getWidth()) / 2;
        yLoc = (screenSize.height - getHeight()) / 2;
    }

    public JButton getPrintMenuBtn() {
        return this.printMenuBtn;
    }

    public JButton getVerificationCodeInpBtn() {
        return this.verificationCodeInpBtn;
    }
}
