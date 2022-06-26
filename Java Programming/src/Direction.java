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

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;

public class Direction extends JFrame {
	int[] status = new int[8];
	String ch;
	private Image screenImage;
	private Graphics screenGraphic;

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

	private Image Background = new ImageIcon(Main.class.getResource("./img/start2.jpg")).getImage();

	private JLabel menuBar = new JLabel(new ImageIcon(Main.class.getResource("./img/bar.png")));
	private JLabel CorrectMessage = new JLabel(new ImageIcon(Main.class.getResource("./img/Correct.png")));
	private JLabel WrongMessage = new JLabel(new ImageIcon(Main.class.getResource("./img/Wrong.png")));

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
	private JButton Next = new JButton(NextBasicImage);

	private int mouseX, mouseY;

	private boolean isMainScreen = false;

	ArrayList<Board> BoardList = new ArrayList<Board>();
	ArrayList<Map> MapList = new ArrayList<Map>();
	private Image selectedImage1, selectedImage2;
	private int startNum = 0, CountNum = 0, answer = 0;
	private String stranswer = "";
	private String UserAnswer = "";
	private String temp = "";

	private String second = "";
	private JLabel label = new JLabel(UserAnswer);
	private JLabel time = new JLabel(second);
	private Random random = new Random();
	private int BoardNum = 0, MapNum = 0;
	private int count = 0;

	static int interval;
	static Timer timer;
	
	File file = new File("C:\\Users\\user\\Desktop\\Status.txt");
	FileReading fored = new FileReading();

	public Direction() {
		setUndecorated(true);
		setTitle("Where is README file?");
		setSize(960, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);

		BoardList.add(new Board("Board1.png"));
		BoardList.add(new Board("Board2.png"));
		BoardList.add(new Board("Board3.png"));
		BoardList.add(new Board("Board4.png"));

		MapList.add(new Map("Direction1.png"));
		MapList.add(new Map("Direction2.png"));
		MapList.add(new Map("Direction3.png"));
		MapList.add(new Map("Direction4.png"));
		MapList.add(new Map("Direction5.png"));
		MapList.add(new Map("Direction6.png"));
		MapList.add(new Map("Direction7.png"));

		exitButton.setBounds(880, 0, 20, 20);
		exitButton.setBorderPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setFocusPainted(false);
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				status = fored.getstatus();
	            status[1]++;
	            try {
	            	FileWriter fw = new FileWriter(file, true);
					fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
							 status[3] + " " + status[4] + " " + status[5] + " " + 
							 status[6] + " " + status[7] + " " + ch+"\r\n");
					fw.close();
	            } catch (IOException f) {
					f.printStackTrace();
				}
	            dispose();
			}
		});
		add(exitButton);

		menuBar.setBounds(0, 0, 900, 20);
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

		Next.setVisible(false);
		Next.setBounds(400, 300, 100, 100);
		Next.setBorderPainted(false);
		Next.setContentAreaFilled(false);
		Next.setFocusPainted(false);
		Next.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Next.setIcon(NextEnteredImage);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				Next.setIcon(NextBasicImage);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				WrongMessage.setVisible(false);
				CorrectMessage.setVisible(false);
				Next.setVisible(false);
				label.setText("");
				count++;
				if (count == 3) {
					status = fored.getstatus();
					status[7]++; status[3]++;
					System.out.println(status[7]);
					try {
		            	FileWriter fw = new FileWriter(file, true);
		  				fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
								 status[3] + " " + status[4] + " " + status[5] + " " + 
								 status[6] + " " + status[7] + " " + ch+"\r\n");
		  				fw.close();
		            } catch (IOException f) {
		  				f.printStackTrace();
		  			}
					dispose();
				}
					
				changeGame();
				gameScreen();
				temp = "";
				time.setVisible(true);
				int sec = 10;
				TimerFunction(sec);
			}
		});
		add(Next);

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
				changeGame();
				gameScreen();
				time.setVisible(true);
				int sec = 10;
				TimerFunction(sec);
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
				status = fored.getstatus();
				status[1]++;
	            try {
	            	FileWriter fw = new FileWriter(file, true);
					fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
						     status[3] + " " + status[4] + " " + status[5] + " " + 
							 status[6] + " " + status[7] + " " + ch+"\r\n");
					fw.close();
	            } catch (IOException f) {
					f.printStackTrace();
				}
	            dispose();
			}
		});
		add(quitButton);

		Button1.setVisible(false);
		Button1.setBounds(450, 170, 80, 100);
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
		Button2.setBounds(550, 170, 100, 100);
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
		Button3.setBounds(650, 170, 100, 100);
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
		Button4.setBounds(450, 270, 100, 100);
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
		Button5.setBounds(550, 270, 100, 100);
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
		Button6.setBounds(650, 270, 100, 100);
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
		Button7.setBounds(450, 370, 100, 100);
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
		Button8.setBounds(550, 370, 100, 100);
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
		Button9.setBounds(650, 370, 100, 100);
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
		Button0.setBounds(550, 470, 100, 100);
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
		ButtonE.setBounds(450, 470, 100, 100);
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
		ButtonB.setBounds(650, 470, 100, 100);
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
		screenImage = createImage(960, 600);
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
		this.repaint();
	}

	public void selectBoard(int nowSelected) {
		selectedImage1 = new ImageIcon(
			Main.class.getResource("./img/" + BoardList.get(nowSelected).getBoardImage())).getImage();
	}

	public void selectMap(int nowSelected) {
		selectedImage2 = new ImageIcon(
			Main.class.getResource("./img/" + MapList.get(nowSelected).getMapImage())).getImage();
	}

	public void isright(String tmp) { // make string to show

		if (tmp.equals("End")) {
			if (this.UserAnswer.equals(stranswer)) {
				CorrectMessage.setVisible(true);
				Next.setVisible(true);
			} else {
				WrongMessage.setVisible(true);
				Next.setVisible(true);
			}
		} else if (tmp.equals("Back")) {
			if (UserAnswer.length() > 0) {
				this.UserAnswer = this.UserAnswer.substring(0, this.UserAnswer.length()-1);
				label.setText(this.UserAnswer);
				add(label);
			}
		} else {
			this.UserAnswer += tmp;
			label.setText(this.UserAnswer);
			label.setFont(new Font("�ü�ü", Font.CENTER_BASELINE, 60));
			label.setForeground(Color.white);
			label.setBounds(700, 70, 200, 50);
			add(label);
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

		selectBoard(BoardNum);
		selectMap(MapNum);
		Background = new ImageIcon(Main.class.getResource("./img/background2.jpg")).getImage();
		isMainScreen = true;
	}

	public void changeGame() {
		stranswer = "";
		UserAnswer = "";
		answer = 0;
		startNum = 0;
		CountNum = 0;
		BoardNum = random.nextInt(3);
		MapNum = random.nextInt(6);

		switch (BoardNum) {
		case 0:
			startNum = 8;
			break;
		case 1:
			startNum = 12;
			break;
		case 2:
			startNum = 13;
			break;
		case 3:
			startNum = 18;
			break;
		}

		switch (MapNum) {
		case 0:
			CountNum = -4;
			break;
		case 1:
			CountNum = 5;
			break;
		case 2:
			CountNum = 1;
			break;
		case 3:
			CountNum = 6;
			break;
		case 4:
			CountNum = -4;
			break;
		case 5:
			CountNum = -1;
			break;
		case 6:
			CountNum = -4;
			break;
		}

		answer = startNum + CountNum;
		stranswer = Integer.toString(answer);
	}

	public void TimerFunction(int sec) {
		int delay = 1500;
		int period = 1000;

		timer = new Timer();
		interval = sec+1;

		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				String IntervalString = Integer.toString(setInterval());
				time.setText(IntervalString);
				time.setFont(new Font("�ü�ü", Font.CENTER_BASELINE, 30));
				time.setForeground(Color.white);
				time.setBounds(850, 70, 50, 50);
				add(time);

				if (temp.equals("End")) {
					timer.cancel();
					if (UserAnswer.equals(stranswer)) {
						CorrectMessage.setVisible(true);
						Next.setVisible(true);
					} else {
						WrongMessage.setVisible(true);
						Next.setVisible(true);
					}
				}
			}
		}, delay, period);
	}

	private int setInterval() {
		if (interval == 1) {
			if (UserAnswer.equals(stranswer)) {
				CorrectMessage.setVisible(true);
				Next.setVisible(true);
			} else {
				WrongMessage.setVisible(true);
				Next.setVisible(true);
			}			
			timer.cancel();
		}
		return --interval;
	}
}