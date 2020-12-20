package gameClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** a simple GUI that allows the choosing of different
 *  levels, in addition to a login option to upload player
 *  score. there is an option of "Free play" that allows playing
 *  without login in. (mainly for testing the game and it's components)
 */
public class LoginPanel implements ActionListener {

    private static boolean _open = true;
    private static int _scenario = 0;
    private static int _id = -1;
    private static JComboBox Num;
  
    private static JButton _loginBut;
    private static JTextField textUser;
    private static JFrame login;

    public static void loginPanel(){
        JPanel panel = new JPanel();
        login = new JFrame();
        login.setSize(330,160);
        panel.setLayout(null);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.add(panel);
        JLabel user = new JLabel("User:");
        user.setBounds(10,20,80,25);

        JLabel label = new JLabel("Scenario:");
        label.setBounds(10,50,80,25);

        textUser = new JTextField(20);
        textUser.setBounds(100,20,165,25);

        String scenes[] = new String[24];
        for(int i = 0; i < 24; i++){
            scenes[i] = String.valueOf(i);
        }
        Num = new JComboBox(scenes);
        Num.addActionListener(new LoginPanel());
        Num.setBounds(100,50,165,25);

       

        _loginBut = new JButton("Login");
        _loginBut.addActionListener(new LoginPanel());
        _loginBut.setBounds(170,80,120,30);

        panel.add(user);
        panel.add(label);
        panel.add(textUser);
        panel.add(Num);
      
        panel.add(_loginBut);
        login.setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == Num){
            int scenario = Num.getSelectedIndex();
            _scenario = scenario;
        }
       
        if(e.getSource() == _loginBut){
            try{
              int  id = Integer.parseInt(textUser.getText());
                if(id > 0){
                    _id = id;
                    _open = false;
                }
            }
            catch (Exception ex){
            }
        }
    }

    public void dispose(){
        login.dispose();
    }

    public boolean isOpen() {
        return _open;
    }

    public int getID(){
        return _id;
    }

    public int getScenario(){
        return _scenario;
    }
}
