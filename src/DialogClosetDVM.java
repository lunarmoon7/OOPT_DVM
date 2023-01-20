
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Formatter;

public class DialogClosetDVM extends JDialog { // 최단거리 DVM 알려주는 다이얼로그

    private DVM dvm;

    private JButton dialogClosetDVMConfirmBtn;
    private String[] calculatedDVMInfo;
    private Formatter formatter;

    public JButton getDialogClosetDVMConfirmBtn() {
        return dialogClosetDVMConfirmBtn;
    }

    public void setDialogClosetDVMConfirmBtn(JButton dialogClosetDVMConfirmBtn) {
        this.dialogClosetDVMConfirmBtn = dialogClosetDVMConfirmBtn;
    }

    public DialogClosetDVM(DVM dvm){
        this.dvm = dvm;
        this.calculatedDVMInfo = dvm.getCalculatedDVMInfo();
        setSize(500, 500);
        formatter = new Formatter();
        formatter.format("%s", this.calculatedDVMInfo[1] + this.calculatedDVMInfo[2]);

        this.dialogClosetDVMConfirmBtn = new JButton("<html>" + "DVM" + this.calculatedDVMInfo[0] + "<br/><br/>X:" + String.valueOf(this.calculatedDVMInfo[1]) + ", Y:" + String.valueOf(this.calculatedDVMInfo[2]) + "</html>");
        dialogClosetDVMConfirmBtn.setBounds(40, 150, getWidth()-90, 150); // 버튼의 크기, 위치 설정
        dialogClosetDVMConfirmBtn.setFont(new Font("Serif", Font.PLAIN, 24)); // 버튼의 폰트 설정

        dialogClosetDVMConfirmBtn.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().setLayout(null);

        attach();
        refresh();
    }
    private void attach(){
        getContentPane().add(dialogClosetDVMConfirmBtn);
    }

    public void refresh() {
        formatter = new Formatter();
        formatter.format("%s", this.calculatedDVMInfo[1] + this.calculatedDVMInfo[2]);
        this.calculatedDVMInfo = dvm.getCalculatedDVMInfo();
        this.dialogClosetDVMConfirmBtn.setText("<html>" + "DVM" + this.calculatedDVMInfo[0] + "<br/><br/>X:" + String.valueOf(this.calculatedDVMInfo[1]) + ", Y:" + String.valueOf(this.calculatedDVMInfo[2]) + "</html>");
        dialogClosetDVMConfirmBtn.setBounds(40, 150, getWidth()-90, 150); // 버튼의 크기, 위치 설정
        dialogClosetDVMConfirmBtn.setFont(new Font("Serif", Font.PLAIN, 24)); // 버튼의 폰트 설정
    }

}
