
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
public class GameFrame extends JFrame implements ActionListener{
    private int widthFrame = 500;
    private int heightFrame = 600;
    private JMenu menuone = new JMenu("遊戲");//建立一個選單
    private JMenuItem newGame = menuone.add("重新開始");//建立一個內建選單選項
    private JMenuItem exitGame = menuone.add("遊戲退出");
    private JMenuItem stopGame = menuone.add("遊戲暫停");
    private JMenuItem goOnGame = menuone.add("遊戲繼續");

    private JMenu menutwo = new JMenu("幫助");//建立第二個選單
    private JMenuItem aboutGame = menutwo.add("關於遊戲");
    GamePanel gamepanel = new GamePanel();
    public GameFrame()//建構函式
    { addKeyListener(gamepanel);
        newGame.addActionListener(this);
        exitGame.addActionListener(this);
        stopGame.addActionListener(this);
        goOnGame.addActionListener(this);
        aboutGame.addActionListener(this);
        this.add(gamepanel);

        JMenuBar menu = new JMenuBar();
        menu.add(menuone);
        menu.add(menutwo);
        this.setJMenuBar(menu);


        this.setTitle("俄羅斯方塊");
        this.setBounds(50, 10, widthFrame, heightFrame);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == newGame)//遊戲重新開始
        {
            gamepanel.NewGame();
        }
        if(e.getSource() == exitGame)//遊戲退出
        {
            System.exit(0);
        }
        if(e.getSource() == stopGame)//遊戲暫停
        {
            gamepanel.StopGame();
        }
        if(e.getSource() == goOnGame)//遊戲繼續
        {
            gamepanel.ContinueGame();
        }
        if(e.getSource() == aboutGame)//關於遊戲資訊
        {
            JOptionPane.showMessageDialog(null, "左右鍵移動，向上建旋轉", "提示", JOptionPane.OK_OPTION);
        }
    }


    public static void main(String[] args) {
        new GameFrame();
    }
}

