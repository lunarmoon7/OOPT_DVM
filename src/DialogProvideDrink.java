import javax.swing.*;

public class DialogProvideDrink extends JDialog {

    DVM dvm;
    JLabel noticeLbl;
    JButton returnBtn;
    JPanel provideDrinkPanel;
    boolean isMyDVM;

    public DialogProvideDrink(DVM dvm, boolean isMyDVM)
    {
        this.dvm = dvm;
        this.isMyDVM = isMyDVM;
        this.noticeLbl = new JLabel();
        this.returnBtn = new JButton("처음 화면으로 돌아가기");
        this.provideDrinkPanel = new JPanel();
    }

    public void settingLbl(int choiceDrinkNum, String drinkName)
    {
        setSize(300, 400);
        this.returnBtn.setBounds(20, getHeight()-100, getWidth()-40, 50);

        /* 임의로 만든 이름과 장소 */
        String closestDVMName = "VM_06";
        double closestDVMDistance = 61.98;
        /* 임의로 만든 이름과 장소 */

        if (this.isMyDVM)
        {
            String drinkNumStr = "<html>";
            for (int i=0; i<choiceDrinkNum; i++)
                drinkNumStr += drinkName + " 뿅!<br/>";

            this.noticeLbl.setBounds(20,20, getWidth()-40, 280);
            this.noticeLbl.setText(drinkNumStr + "나왔어요 ~~~~ </html>");
        }
        else
        {
            dvm.createVerificationCode();
            this.noticeLbl.setBounds(20,20, getWidth()-40, 100+30*choiceDrinkNum);
            this.noticeLbl.setText("<html>당신의 인증코드는: " + dvm.getVerificationCode()+" 입니다.<br/>"+closestDVMName
                    +", "+closestDVMDistance+"m 에서 가져가세요.");
        }
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
