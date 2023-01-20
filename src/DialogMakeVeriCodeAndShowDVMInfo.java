import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DialogMakeVeriCodeAndShowDVMInfo extends JDialog
{
    DVM dvm;
    JLabel noticeLbl;
    JButton returnBtn;
    JPanel provideDrinkPanel;

    public DialogMakeVeriCodeAndShowDVMInfo(DVM dvm)
    {
        this.dvm = dvm;
        this.noticeLbl = new JLabel();
        this.returnBtn = new JButton("처음 화면으로 돌아가기");
        this.provideDrinkPanel = new JPanel();
    }

    public void settingLbl(String vCode, String dstID)
    {
        setSize(300, 400);
        this.returnBtn.setBounds(20, getHeight()-100, getWidth()-40, 50);

        this.noticeLbl.setBounds(20,20, getWidth()-40, 250);
        this.noticeLbl.setText("<html>당신의 인증코드는: " + vCode+" 입니다.<br/><br/>"+ dstID
                +" 에서 가져가세요.<br/><br/>잔액: "+dvm.getCard().getBalance()+"원");

        initLayout();
    }

    private void initLayout()
    {
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().add(noticeLbl);
        getContentPane().add(returnBtn);
        setVisible(true);
    }

}