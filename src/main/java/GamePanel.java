import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Statement;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener{
    private int mapRow = 21;
    private int mapCol = 12;
    private int mapGame[][] = new int[mapRow][mapCol];

    private Timer timer;
    private int score = 0;//記錄成績
    Random random = new Random();
    private int curShapeType = -1;
    private int curShapeState = -1;//設定當前的形狀型別和當前的形狀狀態
    private int nextShapeType = -1;
    private int nextShapeState = -1;//設定下一次出現的方塊組的型別和狀態

    private int posx = 0;
    private int posy = 0;

    private final int shapes[][][] = new int[][][]{
            {
                    {0,1,0,0, 1,1,1,0, 0,0,0,0, 0,0,0,0},
                    {0,1,0,0, 1,1,0,0, 0,1,0,0, 0,0,0,0},
                    {1,1,1,0, 0,1,0,0, 0,0,0,0, 0,0,0,0},
                    {0,1,0,0, 0,1,1,0, 0,1,0,0, 0,0,0,0}
            },
            //I字形按逆時針的順序儲存
            {
                    {0,0,0,0, 1,1,1,1, 0,0,0,0, 0,0,0,0},
                    {0,1,0,0, 0,1,0,0, 0,1,0,0, 0,1,0,0},
                    {0,0,0,0, 1,1,1,1, 0,0,0,0, 0,0,0,0},
                    {0,1,0,0, 0,1,0,0, 0,1,0,0, 0,1,0,0}
            },
            //倒Z形按逆時針的順序儲存
            {
                    {0,1,1,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
                    {1,0,0,0, 1,1,0,0, 0,1,0,0, 0,0,0,0},
                    {0,1,1,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
                    {1,0,0,0, 1,1,0,0, 0,1,0,0, 0,0,0,0}
            },
            //Z形按逆時針的順序儲存
            {
                    {1,1,0,0, 0,1,1,0, 0,0,0,0, 0,0,0,0},
                    {0,1,0,0, 1,1,0,0, 1,0,0,0, 0,0,0,0},
                    {1,1,0,0, 0,1,1,0, 0,0,0,0, 0,0,0,0},
                    {0,1,0,0, 1,1,0,0, 1,0,0,0, 0,0,0,0}
            },
            //J字形按逆時針的順序儲存
            {
                    {0,1,0,0, 0,1,0,0, 1,1,0,0, 0,0,0,0},
                    {1,1,1,0, 0,0,1,0, 0,0,0,0, 0,0,0,0},
                    {1,1,0,0, 1,0,0,0, 1,0,0,0, 0,0,0,0},
                    {1,0,0,0, 1,1,1,0, 0,0,0,0, 0,0,0,0}
            },
            //L字形按逆時針的順序儲存
            {
                    {1,0,0,0, 1,0,0,0, 1,1,0,0, 0,0,0,0},
                    {0,0,1,0, 1,1,1,0, 0,0,0,0, 0,0,0,0},
                    {1,1,0,0, 0,1,0,0, 0,1,0,0, 0,0,0,0},
                    {1,1,1,0, 1,0,0,0, 0,0,0,0, 0,0,0,0}
            },
            //田字形按逆時針的順序儲存
            {
                    {1,1,0,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
                    {1,1,0,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
                    {1,1,0,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
                    {1,1,0,0, 1,1,0,0, 0,0,0,0, 0,0,0,0}
            }
    };
    private int rowRect = 4;
    private int colRect = 4;//這裡我們把儲存的影象看成是一個4*4的二維陣列，雖然在上面我們採用一維陣列來儲存，但實際還是要看成二維陣列來實現
    private int RectWidth = 10;

    public GamePanel()//建構函式----建立好地圖
    {CreateRect();
        initMap();//初始化這個地圖
        SetWall();//設定牆
// CreateRect();
        timer = new Timer(500,new TimerListener());
        timer.start();
    }
    class TimerListener implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            MoveDown();
        }
    }
    public void SetWall()//第0列和第11列都是牆，第20行也是牆
    {
        for(int i = 0; i < mapRow; i++)//先畫列
        {
            mapGame[i][0] = 2;
            mapGame[i][11] = 2;
        }
        for(int j = 1; j < mapCol-1; j++)//畫最後一行
        {
            mapGame[20][j] = 2;
        }
    }

    public void initMap()//初始化這個地圖，牆的ID是2，空格的ID是0，方塊的ID是1
    {
        for(int i = 0; i < mapRow; i++)
        {
            for(int j = 0; j < mapCol; j++)
            {
                mapGame[i][j] = 0;
            }
        }
    }
    public void CreateRect()//建立方塊---如果當前的方塊型別和狀態都存在就設定下一次的，如果不存在就設定當前的並且設定下一次的狀態和型別
    {
        if(curShapeType == -1 && curShapeState == -1)//當前的方塊狀態都為1，表示遊戲才開始
        {
            curShapeType = random.nextInt(shapes.length);
            curShapeState = random.nextInt(shapes[0].length);
        }
        else
        {
            curShapeType = nextShapeType;
            curShapeState = nextShapeState;
        }
        nextShapeType = random.nextInt(shapes.length);
        nextShapeState = random.nextInt(shapes[0].length);
        posx = 0;
        posy = 1;//牆的左上角建立方塊
        if(GameOver(posx,posy,curShapeType,curShapeState))
        {
            JOptionPane.showConfirmDialog(null, "遊戲結束！", "提示", JOptionPane.OK_OPTION);
            Sqlcon X=new Sqlcon();
            X.MYSQLlogin("127.0.0.1","database","testuser","v4GtTyH1ws9RFZ1r",3306);
            try{
                Statement statement = Sqlcon.getConnection().createStatement();
                statement.execute("INSERT INTO ur (score) VALUES ('" + score + "');");
                statement.close();

            }catch (Exception e){
                e.printStackTrace();
            }

            System.exit(0);

        }
    }
    public boolean GameOver(int x, int y, int ShapeType, int ShapeState)//判斷遊戲是否結束
    { if(IsOrNoMove(x,y,ShapeType,ShapeState))
    {
        return false;
    }
        return true;
    }
    public boolean IsOrNoMove(int x, int y, int ShapeType, int ShapeState)//判斷當前的這個圖形是否可以移動,這裡重點強調x,y的座標是指4*4的二維陣列（描述圖形的那個陣列）的左上角目標
    {
        for(int i = 0; i < rowRect ; i++)
        {for(int j = 0; j < colRect; j++)
        {
            if(shapes[ShapeType][ShapeState][i*colRect+j] == 1 && mapGame[x+i][y+j] == 1
                    || shapes[ShapeType][ShapeState][i*colRect+j] == 1 && mapGame[x+i][y+j] == 2)
            {
                return false;
            }
        }
        }
        return true;
    }


    public void Turn()//旋轉
    {
        int temp = curShapeState;
        curShapeState = (curShapeState+1) % shapes[0].length;
        if(IsOrNoMove(posx,posy,curShapeType,curShapeState))
        {
        }
        else
        {
            curShapeState = temp;
        }
        repaint();
    }

    public void MoveDown()//向下移動
    {
        if(IsOrNoMove(posx+1,posy,curShapeType,curShapeState))
        {
            posx++;
        }
        else
        {
            AddToMap();//將此行固定在地圖中
            CheckLine();
            CreateRect();//重新建立一個新的方塊
        }
        repaint();
    }

    public void MoveLeft()//向左移動
    {
        if(IsOrNoMove(posx,posy-1,curShapeType,curShapeState))
        {
            posy--;
        }
        repaint();
    }
    public void MoveRight()//向右移動
    {
        if(IsOrNoMove(posx,posy+1,curShapeType,curShapeState))
        {
            posy++;
        }
        repaint();
    }

    public void AddToMap()//固定掉下來的這一影象到地圖中
    {
        for(int i = 0; i < rowRect; i++)
        {
            for(int j = 0; j < colRect; j++)
            {
                if(shapes[curShapeType][curShapeState][i*colRect+j] == 1)
                {
                    mapGame[posx+i][posy+j] = shapes[curShapeType][curShapeState][i*colRect+j];
                }
            }
        }
    }
    public void CheckLine()//檢查一下這些行中是否有滿行的
    {
        int count = 0;
        for(int i = mapRow-2; i >= 0; i--)
        {
            count = 0;
            for(int j = 1; j < mapCol-1; j++)
            {
                if(mapGame[i][j] == 1)
                {
                    count++;
                }
                else
                    break;
            }
            if(count >= mapCol-2)
            {
                for(int k = i; k > 0; k--)
                {
                    for(int p = 1; p < mapCol-1; p++)
                    {
                        mapGame[k][p] = mapGame[k-1][p];
                    }
                }
                score += 10;
                i++;
            }
        }
    }
    public void paint(Graphics g)//重新繪製視窗
    {
        super.paint(g);
        for(int i = 0; i < rowRect; i++)//繪製正在下落的方塊
        {
            for(int j = 0; j < colRect; j++)
            {
                if(shapes[curShapeType][curShapeState][i*colRect+j] == 1)
                {
                    g.fillRect((posy+j+1)*RectWidth, (posx+i+1)*RectWidth, RectWidth, RectWidth);
                }
            }
        }
        for(int i = 0; i < mapRow; i++)//繪製地圖上面已經固定好的方塊資訊
        {
            for(int j = 0; j < mapCol; j++)
            {
                if(mapGame[i][j] == 2)//畫牆
                {
                    g.drawRect((j+1)*RectWidth, (i+1)*RectWidth, RectWidth, RectWidth);
                }
                if(mapGame[i][j] == 1)//畫小方格
                {
                    g.fillRect((j+1)*RectWidth, (i+1)*RectWidth, RectWidth, RectWidth);
                }
            }
        }
        g.drawString("score = "+ score, 225, 15);
        g.drawString("下一個方塊：", 225, 50);
        for(int i = 0; i < rowRect; i++)
        {
            for(int j = 0; j < colRect; j++)
            {
                if(shapes[nextShapeType][nextShapeState][i*colRect+j] == 1)
                {
                    g.fillRect(225+(j*RectWidth), 100+(i*RectWidth), RectWidth, RectWidth);
                }
            }
        }
    }
    public void NewGame()//遊戲重新開始
    {
        score = 0;
        initMap();
        SetWall();
        CreateRect();
        repaint();
    }
    public void StopGame()//遊戲暫停
    {
        timer.stop();
    }
    public void ContinueGame()
    {
        timer.start();
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_UP://上----旋轉
                Turn();
                break;
            case KeyEvent.VK_DOWN://下----向下移動
                MoveDown();
                break;
            case KeyEvent.VK_LEFT://左----向左移動
                MoveLeft();
                break;
            case KeyEvent.VK_RIGHT://右----向右移動
                MoveRight();
                break;
        }

    }

    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}