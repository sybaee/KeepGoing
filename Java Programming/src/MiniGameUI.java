import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.midi.Track;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;


public class MiniGameUI extends JFrame{
	
	int[] status = new int[8];
	String ch;
   private Image screenImage;
   private Graphics screenGraphic;
   //private JFrame  = new JFrame();

   private ImageIcon exitButtonBasicImage = new ImageIcon(Main.class.getResource("./img/exitButton.png"));
   private ImageIcon startButtonBasicImage = new ImageIcon(Main.class.getResource("./img/startButton.png"));
   private ImageIcon quitButtonBasicImage = new ImageIcon(Main.class.getResource("./img/quitButton.png"));
   private ImageIcon Button1Image = new ImageIcon(Main.class.getResource("./img/1Button.png"));
   private ImageIcon Button2Image = new ImageIcon(Main.class.getResource("./img/2Button.png"));
   private ImageIcon Button3Image = new ImageIcon(Main.class.getResource("./img/3Button.png"));
   private ImageIcon Button4Image = new ImageIcon(Main.class.getResource("./img/4Button.png"));
   private ImageIcon Button5Image = new ImageIcon(Main.class.getResource("./img/5Button.png"));
   private ImageIcon Button6Image = new ImageIcon(Main.class.getResource("./img/6Button.png"));
   private ImageIcon Button7Image = new ImageIcon(Main.class.getResource("./img/7Button.png"));
   private ImageIcon Button8Image = new ImageIcon(Main.class.getResource("./img/8Button.png"));
   private ImageIcon Button9Image = new ImageIcon(Main.class.getResource("./img/9Button.png"));
   private ImageIcon Button0Image = new ImageIcon(Main.class.getResource("./img/0Button.png"));
   private ImageIcon ButtonEImage = new ImageIcon(Main.class.getResource("./img/EButton.png"));
   private ImageIcon ButtonBImage = new ImageIcon(Main.class.getResource("./img/BButton.png"));
   private ImageIcon NextBasicImage = new ImageIcon(Main.class.getResource("./img/NextBasic.png"));
   private ImageIcon NextEnteredImage = new ImageIcon(Main.class.getResource("./img/NextEntered.png"));

   private Image Background = new ImageIcon(Main.class.getResource("./img/start.jpg")).getImage();
   
   private JLabel menuBar = new JLabel(new ImageIcon(Main.class.getResource("./img/bar.png")));

   private JButton exitButton = new JButton(exitButtonBasicImage);
   private JButton startButton = new JButton(startButtonBasicImage);
   private JButton quitButton = new JButton(quitButtonBasicImage);
   private JButton Button1 = new JButton(Button1Image);
   private JButton Button2 = new JButton(Button2Image);
   private JButton Button3 = new JButton(Button3Image);
   private JButton Button4 = new JButton(Button4Image);
   private JButton Button5 = new JButton(Button5Image);
   private JButton Button6 = new JButton(Button6Image);
   private JButton Button7 = new JButton(Button7Image);
   private JButton Button8 = new JButton(Button8Image);
   private JButton Button9 = new JButton(Button9Image);
   private JButton Button0 = new JButton(Button0Image);
   private JButton ButtonE = new JButton(ButtonEImage);
   private JButton ButtonB = new JButton(ButtonBImage);
   private JButton CorrectNext = new JButton(NextBasicImage);
   private JButton WrongNext = new JButton(NextBasicImage);
   private JLabel CorrectMessage = new JLabel(new ImageIcon(Main.class.getResource("./img/Correct.png")));
   private JLabel WrongMessage = new JLabel(new ImageIcon(Main.class.getResource("./img/Wrong.png")));

   private int mouseX, mouseY;

   private boolean isMainScreen = false;
   public boolean end = false;

   private Image selectedImage1, selectedImage2;
   //private int startNum = 0, CountNum = 0, answer = 0;
   private String stranswer = "";
   private String UserAnswer = "";
   private String temp = "";   
   private JLabel label = new JLabel(UserAnswer);
   private JLabel label2 = new JLabel();
   private JLabel label3 = new JLabel();
   
  private int randomOperrend[] = new int [5];
  private int randomOperator[] = new int [4];
  private String hangulOperator[] = new String [4];
  private int sum = 0;
  private int intel = 0;
  private int answer;
  private int check_m = 0;
  
  File file = new File("C:\\Users\\user\\Desktop\\Status.txt");
  static Random random = new Random(System.currentTimeMillis());

  public MiniGameUI() {
    //JFrame  = new JFrame();
    setUndecorated(true);
    setTitle("Where is README file?");
    setSize(980, 700);
    setResizable(false);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    setBackground(new Color(0, 0, 0, 0));
    setLayout(null);
      
    changeGame();
    label2.setVisible(false);
    label3.setVisible(false);

    exitButton.setBounds(960, 0, 20, 20);
    exitButton.setBorderPainted(false);
    exitButton.setContentAreaFilled(false);
    exitButton.setFocusPainted(false);
    exitButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        FileReading fored = new FileReading();
        status = fored.getstatus();
        status[1]++;
        try {
          FileWriter fw = new FileWriter(file, true);
          fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
                  status[3] + " " + status[4] + " " + status[5] + " " + 
                  status[6] + " " + status[7] + " " + ch + "\r\n");
          fw.close();
        } catch (IOException f) {
          f.printStackTrace();
        }
          dispose();
        }
    });
    add(exitButton);

    menuBar.setBounds(0, 0, 980, 20);
    menuBar.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          mouseX = e.getX();
          mouseY = e.getY();
        }
    });
    menuBar.addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
          int x = e.getXOnScreen();
          int y = e.getYOnScreen();
          setLocation(x-mouseX, y-mouseY);
        }
    });
    add(menuBar);
    
    WrongNext.setVisible(false);
    WrongNext.setBounds(400, 300, 100, 100);
    WrongNext.setBorderPainted(false);
    WrongNext.setContentAreaFilled(false);
    WrongNext.setFocusPainted(false);
    WrongNext.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
          WrongNext.setIcon(NextEnteredImage);
        }

        @Override
        public void mouseExited(MouseEvent e) {
          WrongNext.setIcon(NextBasicImage);
        }

        @Override
        public void mousePressed(MouseEvent e) {
          WrongMessage.setVisible(false);
          CorrectMessage.setVisible(false);
          WrongNext.setVisible(false);
          label.setText("");
          changeGame();
          gameScreen();
        }
    });
    add(WrongNext);
    
    CorrectNext.setVisible(false);
    CorrectNext.setBounds(400, 300, 100, 100);
    CorrectNext.setBorderPainted(false);
    CorrectNext.setContentAreaFilled(false);
    CorrectNext.setFocusPainted(false);
    CorrectNext.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        CorrectNext.setIcon(NextEnteredImage);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        CorrectNext.setIcon(NextBasicImage);
      }

      @Override
      public void mousePressed(MouseEvent e) {
        WrongMessage.setVisible(false);
        CorrectMessage.setVisible(false);
        CorrectNext.setVisible(false);
        label.setText("");
        FileReading fored = new FileReading();
        status = fored.getstatus();
        ch = fored.getch();
        status[7]++; status[3]++;
        System.out.println(status[7]);
        try {
          FileWriter fw = new FileWriter(file, true);
          fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
                  status[3] + " " + status[4] + " " + status[5] + " " + 
                  status[6] + " " + status[7] + " " + ch + "\r\n");
          fw.close();
        } catch (IOException f) {
          f.printStackTrace();
        }  
        setVisible(false);
      }
    });
    add(CorrectNext);

    CorrectMessage.setBounds(300, 150, 300, 300);
    add(CorrectMessage);
    
    CorrectMessage.setVisible(false);
    WrongMessage.setBounds(300, 150, 300, 300);
    
    add(WrongMessage);
    WrongMessage.setVisible(false);

    startButton.setBounds(170, 230, 250, 100);
    startButton.setBorderPainted(false);
    startButton.setContentAreaFilled(false);
    startButton.setFocusPainted(false);
    startButton.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          gameScreen();
        }
    });
    add(startButton);

    quitButton.setBounds(170, 310, 250, 100);
    quitButton.setBorderPainted(false);
    quitButton.setContentAreaFilled(false);
    quitButton.setFocusPainted(false);
    quitButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        FileReading fored = new FileReading();
        status = fored.getstatus();
        status[1]++;
        try {
          FileWriter fw = new FileWriter(file, true);
          fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
                    status[3] + " " + status[4] + " " + status[5] + " " + 
                    status[6] + " " + status[7] + " " + ch + "\r\n");
          fw.close();
        } catch (IOException f) {
          f.printStackTrace();
        }
        dispose();
      }
    });
    add(quitButton);
    
    Button1.setVisible(false);
    Button1.setBounds(550, 170, 80, 100);
    Button1.setBorderPainted(false);
    Button1.setContentAreaFilled(false);
    Button1.setFocusPainted(false);
    Button1.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          temp = "1";
          isright(temp);
        }
    });
    add(Button1);
    
    Button2.setVisible(false);
    Button2.setBounds(650, 170, 100, 100);
    Button2.setBorderPainted(false);
    Button2.setContentAreaFilled(false);
    Button2.setFocusPainted(false);
    Button2.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        temp = "2";
        isright(temp);
      }
    });
    add(Button2);
    
    Button3.setVisible(false);
    Button3.setBounds(750, 170, 100, 100);
    Button3.setBorderPainted(false);
    Button3.setContentAreaFilled(false);
    Button3.setFocusPainted(false);
    Button3.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        temp = "3";
        isright(temp);
      }
    });
    add(Button3);
    
    Button4.setVisible(false);
    Button4.setBounds(550, 270, 100, 100);
    Button4.setBorderPainted(false);
    Button4.setContentAreaFilled(false);
    Button4.setFocusPainted(false);
    Button4.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        temp = "4";
        isright(temp);
      }
    });
    add(Button4);
    
    Button5.setVisible(false);
    Button5.setBounds(650, 270, 100, 100);
    Button5.setBorderPainted(false);
    Button5.setContentAreaFilled(false);
    Button5.setFocusPainted(false);
    Button5.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        temp = "5";
        isright(temp);
      }
    });
    add(Button5);
    
    Button6.setVisible(false);
    Button6.setBounds(750, 270, 100, 100);
    Button6.setBorderPainted(false);
    Button6.setContentAreaFilled(false);
    Button6.setFocusPainted(false);
    Button6.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        temp = "6";
        isright(temp);
      }
    });
    add(Button6);
    
    Button7.setVisible(false);
    Button7.setBounds(550, 370, 100, 100);
    Button7.setBorderPainted(false);
    Button7.setContentAreaFilled(false);
    Button7.setFocusPainted(false);
    Button7.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        temp = "7";
        isright(temp);
      }
    });
    add(Button7);
    
    Button8.setVisible(false);
    Button8.setBounds(650, 370, 100, 100);
    Button8.setBorderPainted(false);
    Button8.setContentAreaFilled(false);
    Button8.setFocusPainted(false);
    Button8.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        temp = "8";
        isright(temp);
      }
    });
    add(Button8);
    
    Button9.setVisible(false);
    Button9.setBounds(750, 370, 100, 100);
    Button9.setBorderPainted(false);
    Button9.setContentAreaFilled(false);
    Button9.setFocusPainted(false);
    Button9.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        temp = "9";
        isright(temp);
      }
    });
    add(Button9);
    
    Button0.setVisible(false);
    Button0.setBounds(650, 470, 100, 100);
    Button0.setBorderPainted(false);
    Button0.setContentAreaFilled(false);
    Button0.setFocusPainted(false);
    Button0.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        temp = "0";
        isright(temp);
      }
    });
    add(Button0);
    
    ButtonE.setVisible(false);
    ButtonE.setBounds(550, 470, 100, 100);
    ButtonE.setBorderPainted(false);
    ButtonE.setContentAreaFilled(false);
    ButtonE.setFocusPainted(false);
    ButtonE.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        temp = "End";
        isright(temp);
      }
    });
    add(ButtonE);
    
    ButtonB.setVisible(false);
    ButtonB.setBounds(750, 470, 100, 100);
    ButtonB.setBorderPainted(false);
    ButtonB.setContentAreaFilled(false);
    ButtonB.setFocusPainted(false);
    ButtonB.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        temp = "Back";
        isright(temp);
      }
    });
    add(ButtonB);
  }

  public void paint(Graphics g) {
    screenImage = createImage(980, 700);
    screenGraphic = screenImage.getGraphics();
    screenDraw(screenGraphic);
    g.drawImage(screenImage, 0, 0, null);
  }

  public void screenDraw(Graphics g) {
    g.drawImage(Background, 0, 0, null);
    if (isMainScreen) {
        g.drawImage(selectedImage1, 30, 170, null);
        g.drawImage(selectedImage2, 30, 50, null);
    }
    paintComponents(g);
    repaint();
  }

  public void isright(String tmp) { // make string to show
    if (tmp.equals("End")) {
      if (this.UserAnswer.equals(stranswer)) {
        CorrectMessage.setVisible(true);
        CorrectNext.setVisible(true);
      } else {
        WrongMessage.setVisible(true);
        WrongNext.setVisible(true);
      }  
    } else if (tmp.equals("Back")) {
      if (UserAnswer.length()>0) {
        this.UserAnswer = this.UserAnswer.substring(0,this.UserAnswer.length()-1);
        label.setText(this.UserAnswer);
        add(label);
      }
    } else {
      this.UserAnswer += tmp;
      label.setText(this.UserAnswer);
      label.setFont(new Font("�ü�ü",Font.CENTER_BASELINE,60));
      label.setBounds(800,70,200,50);
      add(label);
    }
  }

  public void setOperrendAndOperator() {
    for (int i=0; i< 5; i++) { 
      if (intel >= 12) {
        randomOperrend[i] = random.nextInt(30) - 15; // -15~15 �� ����
      } else if (intel >= 6) {
        randomOperrend[i] = random.nextInt(50) - 25; // -25~25 �� ����
      } else if (intel >= 3) {
        randomOperrend[i] = random.nextInt(100) - 50; // -50~50 �� ����
      } else {
        randomOperrend[i] = random.nextInt(200) - 100; // -100~100 �� ����
      }
    }

    for (int j=0; j < 2; j++) {
      randomOperator[j] = random.nextInt(3); //0 : + || 1 : - || 2 : *
      if (randomOperator[j] == 0) {
        if (intel >= 12) {
          hangulOperator[j] = " (+) ";
        } else {
          int a = random.nextInt(3);
          if (a == 0) {
            hangulOperator[j] = " Plus ";
          } else if (a == 1) {
            hangulOperator[j] = " ���ϱ� ";
          } else if (a == 2) {
            hangulOperator[j] = " ���� ";
          }
        }  
      }

      if (randomOperator[j] == 1) {
        if (intel >= 12) {
          hangulOperator[j] = " (-) ";
        } else { 
          int a = random.nextInt(3);
          if (a == 0) {
            hangulOperator[j] = " Minus ";
          } else if (a == 1) {
            hangulOperator[j] = " ���� ";
          } else if (a == 2) {
            hangulOperator[j] = " ���� ";
          }
        }
      }

      if (randomOperator[j] == 2) {
        if (intel >=12) {
          hangulOperator[j] = " (x) ";
        } else {
          int a = random.nextInt(3);
           if (a == 0) {
            hangulOperator[j] = " Multiply ";
          } else if (a == 1) {
            hangulOperator[j] = " ���ϱ� ";
          } else if (a == 2) {
            hangulOperator[j] = " ���� ";
          }
        }
      }
    }

    for (int j=2; j<4; j++) {
      randomOperator[j] = random.nextInt(2);
      if (randomOperator[j] == 0) {
        if (intel >= 12) {
          hangulOperator[j] = " (+) ";
        } else {
          int a = random.nextInt(3);
          if (a == 0) {
            hangulOperator[j] = " Plus ";
          } else if (a == 1) {
            hangulOperator[j] = " ���ϱ� ";
          } else if (a == 2) {
            hangulOperator[j] = " ���� ";
          }
        }
      }

      if (randomOperator[j] == 1) {
        if (intel >= 12) {
          hangulOperator[j] = "  (-) ";
        } else {
          int a = random.nextInt(3);
          if (a == 0) {
            hangulOperator[j] = " Minus ";
          } else if (a == 1) {
            hangulOperator[j] = " ���� ";
          } else if (a == 2) {
            hangulOperator[j] = " ���� ";
          }
        }
      }
    }
  }

  public int calculate() {
    if (randomOperator[1] == 0) {
      if (randomOperator[0] == 0) {
        sum = randomOperrend[0] + randomOperrend[1] + randomOperrend[2];
      } else if (randomOperator[0] == 1) {
        sum = randomOperrend[0] - randomOperrend[1] + randomOperrend[2];
      } else if (randomOperator[0] == 2) {
        sum = randomOperrend[0] * randomOperrend[1] + randomOperrend[2];
      }
    } else if (randomOperator[1] == 1) {
      if (randomOperator[0] == 0) {
        sum = randomOperrend[0] + randomOperrend[1] - randomOperrend[2];
      } else if (randomOperator[0] == 1) {
        sum = randomOperrend[0] - randomOperrend[1] - randomOperrend[2];
      } else if (randomOperator[0] == 2) {
        sum = randomOperrend[0] * randomOperrend[1] - randomOperrend[2];
      }
    } else if (randomOperator[1] == 2) {
      if (randomOperator[0] == 0) {
        sum = randomOperrend[0] + randomOperrend[1] * randomOperrend[2];
      } else if (randomOperator[0] == 1) {
        sum = randomOperrend[0] - randomOperrend[1] * randomOperrend[2];
      } else if (randomOperator[0] == 2) {
        sum = randomOperrend[0] * randomOperrend[1] * randomOperrend[2];
      }
    }

    if (intel <= 15) {
      if (randomOperator[2] == 0) sum += randomOperrend[3];
      if (randomOperator[2] == 1) sum -= randomOperrend[3];
    }

    if (intel <= 6) {
      if (randomOperator[3] == 0) sum += randomOperrend[4];
      if (randomOperator[3] == 1) sum -= randomOperrend[4];
    }
    return sum;
  }

  public void printGame() {
    if (check_m == 1) {
      label3.setText(" - ");
      label3.setFont(new Font("�ü�ü",Font.CENTER_BASELINE,60));
      label3.setBounds(700,70,200,50);
      add(label3);
      if (intel > 15) {
        label2.setText("[ " + randomOperrend[0] + "" + hangulOperator[0] + "" + 
                       randomOperrend[1] + "" + hangulOperator[1] + "" + randomOperrend[2] + " = ? ]");
        label2.setFont(new Font("����",Font.CENTER_BASELINE,20));
        label2.setForeground(Color.black);
        label2.setBounds(10,300,900,50);
        add(label2);
      } else if (intel > 6) {
        label2.setText("[ " + randomOperrend[0] + "" + hangulOperator[0] + "" + 
                       randomOperrend[1] + "" + hangulOperator[1] + "" + randomOperrend[2] + "" + 
                       hangulOperator[2] + "" + randomOperrend[3] + " = ? ]");
        label2.setFont(new Font("����",Font.CENTER_BASELINE,20));
        label2.setForeground(Color.black);
        label2.setBounds(10,300,900,50);
        add(label2);
      } else {
        label2.setText("[ " + randomOperrend[0] + "" + hangulOperator[0] + "" + 
                       randomOperrend[1] + "" + hangulOperator[1] + "" + randomOperrend[2] + "" +
                       hangulOperator[2] + "" + randomOperrend[3] + "" + hangulOperator[3] + "" + 
                       randomOperrend[4] + " = ? ]");
        label2.setFont(new Font("����",Font.CENTER_BASELINE,20));
        label2.setForeground(Color.black);
        label2.setBounds(10,300,900,50);
        add(label2);
      }
    } else {
      label3.setText("");
      label3.setFont(new Font("�ü�ü",Font.CENTER_BASELINE,60));
      label3.setBounds(700,70,200,50);
      add(label3);
      if (intel > 15) {
        label2.setText("[ " + randomOperrend[0] + "" + hangulOperator[0] + "" + 
                       randomOperrend[1] + "" + hangulOperator[1] + "" + randomOperrend[2] + " = ? ]");
        label2.setFont(new Font("����",Font.CENTER_BASELINE,20));
        label2.setForeground(Color.black);
        label2.setBounds(10,300,900,50);
        add(label2);
      } else if (intel > 6) {
        label2.setText("[ " + randomOperrend[0] + "" + hangulOperator[0] + "" + 
                       randomOperrend[1] + "" + hangulOperator[1] + "" + randomOperrend[2] + "" + 
                       hangulOperator[2] + "" + randomOperrend[3] + " = ? ]");
        label2.setFont(new Font("����",Font.CENTER_BASELINE,20));
        label2.setForeground(Color.black);
        label2.setBounds(10,300,900,50);
        add(label2);
      } else {
        label2.setText("[ " + randomOperrend[0] + "" + hangulOperator[0] + "" + 
                       randomOperrend[1] + "" + hangulOperator[1] + "" + randomOperrend[2] + "" + 
                       hangulOperator[2] + "" + randomOperrend[3] + "" + hangulOperator[3] + "" + 
                       randomOperrend[4] + " = ? ]");
        label2.setFont(new Font("����",Font.CENTER_BASELINE,20));
        label2.setForeground(Color.black);
        label2.setBounds(10,300,900,50);
        add(label2);
      }
    }
  }

  public void gameScreen() {
      startButton.setVisible(false);
      quitButton.setVisible(false);

      Button1.setVisible(true);
      Button2.setVisible(true);
      Button3.setVisible(true);
      Button4.setVisible(true);
      Button5.setVisible(true);
      Button6.setVisible(true);
      Button7.setVisible(true);
      Button8.setVisible(true);
      Button9.setVisible(true);
      Button0.setVisible(true);
      ButtonE.setVisible(true);
      ButtonB.setVisible(true);
      label.setVisible(true);
      label2.setVisible(true);
      label3.setVisible(true);

      Background = new ImageIcon(Main.class.getResource("./img/background.jpg")).getImage();
      isMainScreen = true;
   }

  public void changeGame() {
    stranswer = "";
    UserAnswer = "";
    answer = 0;
    check_m = 0;
    setOperrendAndOperator();
    answer = calculate();
    if (answer < 0) {
      answer = -answer;
      check_m = 1;
    }
    
    this.end = false;
    stranswer = Integer.toString(answer);
    printGame();
  }
  
  public void setend(boolean a) { this.end = a; }
  public boolean getend() { return this.end; }
}