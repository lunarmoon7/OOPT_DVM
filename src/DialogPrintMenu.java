
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DialogPrintMenu extends JDialog {

    private int choiceDrinkNum = 0; // 음료 버튼 눌렀을 때 음료 선택 개수
    private String choiceDrinkCode = ""; // 음료 버튼 눌렀을 때 음료 코드
    private DVM dvm;
    private JPanel printMenuPanel;
    private JButton confirmBtn;
    private JTextField enterDrinkCodeTF;
    private JLabel enterDrinkCodeLbl;
    private JTextField enterDrinkNumTF;
    private JLabel enterDrinkNumLbl;
    private int counter;
    private JLabel item[];
    private int frameW = 600, frameH = 750;


    public DialogPrintMenu(DVM dvm){
        this.dvm = dvm;
        this.printMenuPanel = new JPanel();
        this.item = new JLabel[20];
//        this.drinkBtns = new JButton[this.drinkListlength];
        this.confirmBtn = new JButton("확인");
        this.enterDrinkCodeTF = new JTextField();
        this.enterDrinkNumTF = new JTextField("0");
        this.enterDrinkCodeLbl = new JLabel("번호: ");
        this.enterDrinkNumLbl = new JLabel("개수:");
        init();
        createButtonAndText();
        attachBtnAndText();
    }

    public void init() {
        printMenuPanel.setBounds(20,100, frameW, frameH); // 패널의 위치를 설정
        getContentPane().add(printMenuPanel);

        JScrollPane scrollPane = new JScrollPane(printMenuPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // 스크롤
        scrollPane.setBounds(20,100,frameW-60,frameH-300); // 스크롤 패널 위치 설정
        getContentPane().add(scrollPane);

        GridLayout layout = new GridLayout(7,4,0,0); // 그리드 레이아웃 음료를 한 행에 4개씩 총 7행
        layout.setVgap(10); // 격자 사이 수직 간격
        layout.setHgap(10); // 격자 사이 수평 간격

        printMenuPanel.setLayout(layout); // 패널의 레이아웃을 그리드 레이아웃으로 설정

        getContentPane().add(scrollPane); // 메인 프레임에 스크롤 패널 붙힘
        setTitle("메뉴 선택");

        getContentPane().setLayout(null);
        setVisible(false);
        setSize(600, 750);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 음료 코드 텍스트 필드 위치 설정
        enterDrinkCodeTF.setBounds(45, scrollPane.getY() + scrollPane.getHeight() + 10, 425, 50);

        // 음료 코드 라벨 위치 설정
        enterDrinkCodeLbl.setBounds(18, scrollPane.getY()+scrollPane.getHeight()+10, 30, 50);

        // 음료 개수 필드 위치 설정
        enterDrinkNumTF.setBounds(45, scrollPane.getY()+scrollPane.getHeight()+20+40, 425, 50);

        // 음료 개수 라벨 위치 설정
        enterDrinkNumLbl.setBounds(18, scrollPane.getY()+scrollPane.getHeight()+20+40, 30, 50);
        confirmBtn.setBounds(475, scrollPane.getY()+scrollPane.getHeight()+10, 90, 100); // 버튼의 위치 설정
    }
    public JButton getConfirmBtn() {
        return confirmBtn;
    }


    public String getChoiceDrinkCode() {
        if (Integer.toString(Integer.parseInt(enterDrinkCodeTF.getText().replaceAll("\\s+",""))).matches("^[1-9]\\d*$"))
        {
            if(Integer.parseInt(enterDrinkCodeTF.getText().replaceAll("\\s+","")) < 21 &&
                    Integer.parseInt(enterDrinkCodeTF.getText().replaceAll("\\s+","")) > 0)
            {
                return Integer.parseInt(enterDrinkCodeTF.getText().replaceAll("\\s+","")) < 10 ? "0"
                        + Integer.toString(Integer.parseInt(enterDrinkCodeTF.getText().replaceAll("\\s+",""))): enterDrinkCodeTF.getText().replaceAll("\\s+","");
            }
            else
                JOptionPane.showMessageDialog(null, "음료 코드는 1부터 20까지 입력해주세요");
        }
        else
            JOptionPane.showMessageDialog(null, "음료 코드는 숫자를 입력하세요");
        return "";
    }

    public void setChoiceDrinkCode(String choiceDrinkCode) {
        this.choiceDrinkCode = choiceDrinkCode;
    }

    public int getChoiceDrinkNum() {
        if (enterDrinkNumTF.getText().replaceAll("\\s+","").matches("^[1-9]\\d*$"))
            return Integer.parseInt(enterDrinkNumTF.getText().replaceAll("\\s+",""));
        else
            JOptionPane.showMessageDialog(null, "음료 개수는 숫자를 입력하세요");

        return 0;
    }

    public void setChoiceDrinkNum(int choiceDrinkNum) {
        this.choiceDrinkNum = choiceDrinkNum;
    }

    public boolean isValidInput()
    {
        if(Integer.toString(Integer.parseInt(enterDrinkCodeTF.getText().replaceAll("\\s+",""))).matches("^[1-9]\\d*$") && enterDrinkNumTF.getText().replaceAll("\\s+","").matches("^[1-9]\\d*$"))
        {
            if(Integer.parseInt(enterDrinkCodeTF.getText().replaceAll("\\s+","")) < 21 && Integer.parseInt(enterDrinkCodeTF.getText().replaceAll("\\s+","")) > 0)
            {
                return true;
            }
        }
        return false;
    }

    public void createButtonAndText(){ // 음료 버튼, 텍스트필드 생성
        counter = 1;
        for(int i = 0; i < item.length; i++) {
            item[i] = new JLabel("<html>음료코드: "+Integer.toString(counter)+"<br/>"+
                    dvm.getEntireDrinkList()[counter-1].getPrice()+"원</html>", SwingConstants.CENTER); // 아이템 - "음료코드 : #, #원"
            item[i].setHorizontalAlignment(SwingConstants.CENTER); // 음료 정렬
            TitledBorder border = BorderFactory.createTitledBorder(dvm.getEntireDrinkList()[counter-1].getName()); // 제목 보더
            item[i].setName(Integer.toString(counter)); // 아이템 이름 설정
            item[i].setHorizontalTextPosition(SwingConstants.CENTER); // 아이템의 텍스트 위치 수평정렬
            item[i].setVerticalTextPosition(SwingConstants.BOTTOM); // 아이템의 텍스트 위치 수직정렬
            item[i].setPreferredSize(new Dimension(100,100)); // 아이템의 크기

            clickListener(i);

            item[i].setFont(new Font("Serif", Font.PLAIN, 12)); // 아이템 폰트 설정
            item[i].setPreferredSize(new Dimension(90,130)); // 아이템 크기
            item[i].setForeground(Color.black); // 아이템의 글자색
            item[i].setBorder(border); // 아이템의 보더
            item[i].setName(Integer.toString(counter));

            counter++;
            printMenuPanel.add(item[i]); // 패널에 아이템 추가
        }
    }

    public void attachBtnAndText(){
        getContentPane().add(enterDrinkCodeTF);
        getContentPane().add(enterDrinkCodeLbl);
        getContentPane().add(enterDrinkNumTF);
        getContentPane().add(enterDrinkNumLbl);
        getContentPane().add(confirmBtn);
    }

    public void refresh(){
        enterDrinkCodeTF.setText(choiceDrinkCode);
        enterDrinkNumTF.setText(String.valueOf(choiceDrinkNum));
    }

    public void clickListener(int i) {
        item[i].addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e) {
                enterDrinkCodeTF.setText(item[i].getName()); // 음료 코드 텍스트 필드에 음료 코드 설정
                String dCode = dvm.getEntireDrinkList()[i].getDrinkCode();
                if (choiceDrinkCode.equals(dCode)) {
                    choiceDrinkNum++;
                } else {
                    choiceDrinkCode = dCode;
                    choiceDrinkNum = 1;
                }
                refresh();
            }
        });
    }
}
