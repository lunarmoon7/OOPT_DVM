import javax.swing.*;

public class DialogConfirmPayment extends JDialog
{
    DVM dvm;
    JPanel confirmPaymentPanel;
    JButton yesBtn;
    JButton noBtn;
    JLabel noticeLabel;

    public DialogConfirmPayment(DVM dvm)
    {
        this.dvm = dvm;
        this.yesBtn = new JButton("YES");
        this.noBtn = new JButton("NO");
        this.noticeLabel = new JLabel("");
        this.confirmPaymentPanel = new JPanel();
    }

    public void settingTextArea(int choiceNum, String choiceDrinkName, int totalPrice)
    {
        noticeLabel.setText("<html>Are you sure you want to purchase <br/>"+ choiceNum + " " + choiceDrinkName+"?<br/>결제 금액: "+totalPrice+"원</html>");
        initLayout();
    }

    public JButton getNoBtn()
    {
        return this.noBtn;
    }

    public JButton getYesBtn() { return this.yesBtn; }

    private void initLayout()
    {
        getContentPane().setLayout(null);
        setSize(300, 400);
        noticeLabel.setBounds(10, 20, getWidth()-20, 50);
        noticeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        yesBtn.setBounds(30,100, getWidth()/2-40, 50);
        noBtn.setBounds(160,100, getWidth()/2-40, 50);
    }

    public void attach()
    {
        getContentPane().add(noticeLabel);
        getContentPane().add(yesBtn);
        getContentPane().add(noBtn);
    }
}