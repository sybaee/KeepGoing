import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Game extends JFrame {
	// Opening Variable
	int[] status = new int[8];
	String ch;
	String[] temp = new String[9];
	
	private Clip clip;	

	private Image screenImage;
	private Image introImage;
	private Image statusImage;
	private Graphics screenGraphic;
	private ImageIcon icon = new ImageIcon(Main.class.getResource("/img/menubar.jpg"));
	private JLabel menubar = new JLabel(icon);

	private JButton start = new JButton("GAME START");
	private JButton exit = new JButton("EXIT");
	private JButton barexit = new JButton(new ImageIcon(Main.class.getResource("/img/exit.jpg")));
	private JButton showStatus = new JButton("<html>Show<br>Status<br><html>");
	private JButton closeStatus = new JButton("<html>Close<br>Status<br><html>");
	private JLabel bar = new JLabel("Survival in Handong���� ���� �� �г��� �������ּ���.");
	private JButton Fresh = new JButton("�� �� ��");
	private JButton Senior = new JButton("�� �� ��");
	private JButton Graduate = new JButton("�� �� �� �� ��");
	private JLabel bar2 = new JLabel("���ϴ� ���ָ� �������ּ���.");
	private JButton DT = new JButton("�� �� ��");
	private JButton Freedom = new JButton("�� �� �� ��");
	private JLabel Health = new JLabel();
	private JLabel Movement = new JLabel();
	private JLabel Stress = new JLabel();
	private JLabel Int = new JLabel();
	private JLabel Communication = new JLabel();
	private JLabel Money = new JLabel();
	private JLabel Chingho = new JLabel();
	private JButton click = new JButton("Ŭ��");

	private JLabel label = new JLabel("Survival in Handong");
	private int mouseX;
	private int mouseY;

	// Week and Area Variable
	private int week = 1;
	private int[] num = new int[3];
	private JLabel weekSection = new JLabel();
	private JLabel areaSection = new JLabel();
	private int noassignment = 0;

	// StudyArea Variable
	private JLabel studyImgLbl1 = new JLabel();
	private JLabel studyImgLbl2 = new JLabel();
	private JLabel studyImgLbl3 = new JLabel();

	private ImageIcon studyImg1;
	private ImageIcon studyImg2;
	private ImageIcon studyImg3;

	private JButton studyAction1 = new JButton();
	private JButton studyAction2 = new JButton();
	private JButton studyAction3 = new JButton();

	// AssignmentArea Variable
	private JLabel assignmentImgLbl1 = new JLabel();
	private JLabel assignmentImgLbl2 = new JLabel();

	private ImageIcon assignmentImg1;
	private ImageIcon assignmentImg2;

	private JButton assignmentAction1 = new JButton();
	private JButton assignmentAction2 = new JButton();

	// HandongTMIquiz Variable
	private int quiz1 = 0;
	private JLabel TMIquiz = new JLabel();

	private JButton TMIquizChoice1 = new JButton();
	private JButton TMIquizChoice2 = new JButton();
	private JButton TMIquizChoice3 = new JButton();
	private JButton TMIquizChoice4 = new JButton();

	// Quiz result Variable
	private JLabel quizResult = new JLabel();

	private JButton next = new JButton();
	private JButton nextQuiz = new JButton();

	// Recreation quiz Variable
	private int trial = 0;
	private int quiz2 = 0;
	private JLabel RECquiz = new JLabel();

	private JButton RECquizChoice1 = new JButton();
	private JButton RECquizChoice2 = new JButton();

	// LeisureArea Variable
	private JLabel leisureImgLbl1 = new JLabel();
	private JLabel leisureImgLbl2 = new JLabel();
	private JLabel leisureImgLbl3 = new JLabel();

	private ImageIcon leisureImg1;
	private ImageIcon leisureImg2;
	private ImageIcon leisureImg3;

	private JButton leisureAction1 = new JButton();
	private JButton leisureAction2 = new JButton();
	private JButton leisureAction3 = new JButton();

	// BusinessArea Variable
	private JLabel businessImgLbl1 = new JLabel();
	private JLabel businessImgLbl2 = new JLabel();
	private JLabel businessImgLbl3 = new JLabel();

	private ImageIcon businessImg1;
	private ImageIcon businessImg2;
	private ImageIcon businessImg3;

	private JButton businessAction1 = new JButton();
	private JButton businessAction2 = new JButton();
	private JButton businessAction3 = new JButton();

	private JButton nextweek = new JButton("Next");

	// Ending Variable
	private JLabel usergrade = new JLabel("����� �̹� �б� ������?");
	private JLabel noHealth = new JLabel();
	private JLabel fullStress = new JLabel();
	private JLabel lowgrade = new JLabel();

	private JLabel hgubrain = new JLabel();
	private JLabel hguinsider = new JLabel();
	private JLabel worldtravel = new JLabel();
	private JLabel hgulove = new JLabel();
	private JLabel reasonofed = new JLabel();

	File file = new File("C:\\Users\\user\\Desktop\\Status.txt");
	
	private boolean isplus = false;
	private boolean isminigame = false;
	int isshow = 1;
	int i = 0;
	static Random ran = new Random(System.currentTimeMillis());

	// Game Flow
	public Game() {
		StudyAction study = new StudyAction();
		AssignmentAction assign = new AssignmentAction();
		HandongTMIquiz tmi = new HandongTMIquiz();
		LeisureAction leisure = new LeisureAction();
		BusinessAction business = new BusinessAction();
		Recreationquiz rec = new Recreationquiz();
		
		mainSound("C:\\Users\\user\\Desktop\\florida.wav");
		
		setUndecorated(true);
		setTitle("Survival in Handong");
		setSize(1000, 1000);
		setResizable(false);
		setLocationRelativeTo(null);
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);

		setVisible(true);

		barexit.setBounds(980, 0, 20, 20);
		barexit.setBorderPainted(false);
		barexit.setContentAreaFilled(false);
		barexit.setFocusPainted(false);
		barexit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		add(barexit);

		menubar.setBounds(0, 0, 1000, 20);
		menubar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		menubar.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x-mouseX, y-mouseY);
			}
		});
		add(menubar);

		label.setFont(new Font("����", Font.CENTER_BASELINE, 60));
		label.setBounds(230, 100, 600, 65);
		add(label);

		start.setFont(new Font("����", Font.PLAIN, 40));
		start.setBorderPainted(false);
		start.setContentAreaFilled(false);
		start.setFocusPainted(false);
		start.setBounds(250, 240, 500, 40);
		start.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				label.setVisible(false);
				start.setVisible(false);
				exit.setVisible(false);
				bar2.setVisible(true);
				DT.setVisible(true);
				Freedom.setVisible(true);
			}
		});
		add(start);

		exit.setFont(new Font("����", Font.PLAIN, 40));
		exit.setBorderPainted(false);
		exit.setContentAreaFilled(false);
		exit.setFocusPainted(false);
		exit.setBounds(240, 310, 500, 40);
		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		add(exit);

		bar2.setVisible(false);
		bar2.setFont(new Font("����", Font.CENTER_BASELINE, 30));
		bar2.setBounds(300, 100, 900, 30);
		add(bar2);

		DT.setVisible(false);
		DT.setFont(new Font("����", Font.CENTER_BASELINE, 20));
		DT.setContentAreaFilled(false);
		DT.setFocusPainted(false);
		DT.setBounds(250, 210, 500, 40);
		DT.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				bar2.setVisible(false);
				DT.setVisible(false);
				Freedom.setVisible(false);
				bar.setVisible(true);
				Fresh.setVisible(true);
				Senior.setVisible(true);
				Graduate.setVisible(true);
			}
		});
		add(DT);

		Freedom.setVisible(false);
		Freedom.setFont(new Font("����", Font.CENTER_BASELINE, 20));
		Freedom.setContentAreaFilled(false);
		Freedom.setFocusPainted(false);
		Freedom.setBounds(250, 280, 500, 40);
		Freedom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ch = "��ŷ�";
				bar2.setVisible(false);
				DT.setVisible(false);
				Freedom.setVisible(false);
				bar.setVisible(true);
				Fresh.setVisible(true);
				Senior.setVisible(true);
				Graduate.setVisible(true);
			}
		});
		add(Freedom);

		bar.setVisible(false);
		bar.setFont(new Font("����", Font.CENTER_BASELINE, 30));
		bar.setHorizontalAlignment(JLabel.CENTER);
		bar.setBounds(54, 100, 884, 80);
		add(bar);

		Fresh.setVisible(false);
		Fresh.setFont(new Font("����", Font.CENTER_BASELINE, 20));
		Fresh.setContentAreaFilled(false);
		Fresh.setFocusPainted(false);
		Fresh.setBounds(250, 240, 500, 40);
		Fresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				status[0] = 10;
				status[1] = 0;
				status[2] = 0;
				status[3] = 0;
				status[4] = 1;
				status[5] = 4;
				status[6] = 1;
				status[7] = 0;
				ch = "�������";
				try {
					FileWriter fw = new FileWriter(file);
					fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
							 status[3] + " " + status[4] + " " + status[5] + " " + 
							 status[6] + " " + status[7] + " " + ch + "\r\n");
					fw.close();
				} catch (IOException f) {
					f.printStackTrace();
				}

				bar.setVisible(false);
				Fresh.setVisible(false);
				Senior.setVisible(false);
				Graduate.setVisible(false);
				click.setVisible(true);
				showStatus.setVisible(true);
				show(isshow);

				System.out.println("week " + week);
				weekSection.setText("Week " + week);
				weekSection.setVisible(true);
				areaSection.setVisible(true);
				randomNumber();
				studyImg1 = study.getStudyImg1(num[0]);
				studyImg2 = study.getStudyImg1(num[1]);
				studyImg3 = study.getStudyImg1(num[2]);
				
				studyImgLbl1.setIcon(studyImg1);
				studyImgLbl2.setIcon(studyImg2);
				studyImgLbl3.setIcon(studyImg3);
				
				studyImgLbl1.setVisible(true);
				studyImgLbl2.setVisible(true);
				studyImgLbl3.setVisible(true);
				
				studyAction1.setText(study.getStudyAction(num[0]));
				studyAction2.setText(study.getStudyAction(num[1]));
				studyAction3.setText(study.getStudyAction(num[2]));
				
				studyAction1.setVisible(true);
				studyAction2.setVisible(true);
				studyAction3.setVisible(true);
			}
		});
		add(Fresh);

		Senior.setVisible(false);
		Senior.setFont(new Font("����", Font.CENTER_BASELINE, 20));
		Senior.setContentAreaFilled(false);
		Senior.setFocusPainted(false);
		Senior.setBounds(250, 310, 500, 40);
		Senior.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				status[0] = 10;
				status[1] = 0;
				status[2] = 0;
				status[3] = 3;
				status[4] = 2;
				status[5] = 4;
				status[6] = 2;
				status[7] = 0;
				ch = "�峻��";
				try {
					FileWriter fw = new FileWriter(file);
					fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
						     status[3] + " " + status[4] + " " + status[5] + " " + 
							 status[6] + " " + status[7] + " " + ch + "\r\n");
					fw.close();
				} catch (IOException f) {
					f.printStackTrace();
				}

				bar.setVisible(false);
				Fresh.setVisible(false);
				Senior.setVisible(false);
				Graduate.setVisible(false);
				click.setVisible(true);
				showStatus.setVisible(true);
				show(isshow);

				System.out.println("week " + week);
				weekSection.setText("Week " + week);
				weekSection.setVisible(true);
				areaSection.setVisible(true);
				randomNumber();
				studyImg1 = study.getStudyImg1(num[0]);
				studyImg2 = study.getStudyImg1(num[1]);
				studyImg3 = study.getStudyImg1(num[2]);
				
				studyImgLbl1.setIcon(studyImg1);
				studyImgLbl2.setIcon(studyImg2);
				studyImgLbl3.setIcon(studyImg3);
				
				studyImgLbl1.setVisible(true);
				studyImgLbl2.setVisible(true);
				studyImgLbl3.setVisible(true);
				
				studyAction1.setText(study.getStudyAction(num[0]));
				studyAction2.setText(study.getStudyAction(num[1]));
				studyAction3.setText(study.getStudyAction(num[2]));
				
				studyAction1.setVisible(true);
				studyAction2.setVisible(true);
				studyAction3.setVisible(true);
			}
		});
		add(Senior);

		Graduate.setVisible(false);
		Graduate.setFont(new Font("����", Font.CENTER_BASELINE, 20));
		Graduate.setContentAreaFilled(false);
		Graduate.setFocusPainted(false);
		Graduate.setBounds(250, 380, 500, 40);
		Graduate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				status[0] = 10;
				status[1] = 0;
				status[2] = 5;
				status[3] = 5;
				status[4] = 3;
				status[5] = 4;
				status[6] = 1;
				status[7] = 0;
				ch = "����������";
				try {
					FileWriter fw = new FileWriter(file);
					fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
							 status[3] + " " + status[4] + " " + status[5] + " " + 
							 status[6] + " " + status[7] + " " + ch + "\r\n");
					fw.close();
				} catch (IOException f) {
					f.printStackTrace();
				}

				bar.setVisible(false);
				Fresh.setVisible(false);
				Senior.setVisible(false);
				Graduate.setVisible(false);
				click.setVisible(true);
				showStatus.setVisible(true);
				show(isshow);

				System.out.println("week " + week);
				weekSection.setText("Week " + week);
				weekSection.setVisible(true);
				areaSection.setVisible(true);
				randomNumber();
				studyImg1 = study.getStudyImg1(num[0]);
				studyImg2 = study.getStudyImg1(num[1]);
				studyImg3 = study.getStudyImg1(num[2]);
				
				studyImgLbl1.setIcon(studyImg1);
				studyImgLbl2.setIcon(studyImg2);
				studyImgLbl3.setIcon(studyImg3);
				
				studyImgLbl1.setVisible(true);
				studyImgLbl2.setVisible(true);
				studyImgLbl3.setVisible(true);
				
				studyAction1.setText(study.getStudyAction(num[0]));
				studyAction2.setText(study.getStudyAction(num[1]));
				studyAction3.setText(study.getStudyAction(num[2]));
				
				studyAction1.setVisible(true);
				studyAction2.setVisible(true);
				studyAction3.setVisible(true);
			}
		});
		add(Graduate);

		// Study Area
				weekSection.setVisible(false);
				weekSection.setBounds(54, 80, 884, 40);
				weekSection.setFont(new Font("��������", Font.BOLD, 40));
				weekSection.setHorizontalAlignment(JLabel.CENTER);
				add(weekSection);
				
				areaSection.setVisible(false);
				areaSection.setBounds(54, 140, 884, 40);
				areaSection.setText("Study Area");
				areaSection.setFont(new Font("��������", Font.BOLD, 30));
				areaSection.setHorizontalAlignment(JLabel.CENTER);
				add(areaSection);
						
				studyImgLbl1.setVisible(false);
				studyImgLbl1.setBounds(54, 230, 256, 421);
				studyImgLbl1.setHorizontalAlignment(JLabel.CENTER);
				add(studyImgLbl1);
				
				studyImgLbl2.setVisible(false);
				studyImgLbl2.setBounds(364, 230, 256, 421);
				studyImgLbl2.setHorizontalAlignment(JLabel.CENTER);
				add(studyImgLbl2);
				
				studyImgLbl3.setVisible(false);
		        studyImgLbl3.setBounds(674, 230, 256, 421);
				studyImgLbl3.setHorizontalAlignment(JLabel.CENTER);
				add(studyImgLbl3);
				
				studyAction1.setVisible(false);
				studyAction1.setBounds(54, 701, 256, 160);
				studyAction1.setFont(new Font("��������", Font.BOLD, 18));
				studyAction1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						FileReading fored = new FileReading();
						status = fored.getstatus();
						if (study.changestatus(num[0]) == 6) status[3]++;
						else if (study.changestatus(num[0]) == 5) status[3]--;
						else if (study.changestatus(num[0]) == 4) { status[2]--; status[3]--; }
						else if (study.changestatus(num[0]) == 3) { status[2]++; status[3]++; }
						else if (study.changestatus(num[0]) == 2) { status[0]--; status[3]++; status[2]++; }
						else if (study.changestatus(num[0]) == 0) { status[2]--; status[3]--; status[4]--; }
						else { status[3]--; status[5]--; status[0]--; }

						try {
							if (status[2] <0) status[2] = 0;
							if (status[3] <0) status[3] = 0;
							if (status[4] <0) status[4] = 0;
							FileWriter fw = new FileWriter(file, true);
							fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
									 status[3] + " " + status[4] + " " + status[5] + " " + 
									 status[6] + " " + status[7] + " " + ch + "\r\n");
							fw.close();
						} catch (IOException f) {
							f.printStackTrace();
						}

						weekSection.setText("Week " + week);
						areaSection.setText("Assignment Area");
						studyImgLbl1.setVisible(false);
						studyImgLbl2.setVisible(false);
						studyImgLbl3.setVisible(false);
						studyAction1.setVisible(false);
						studyAction2.setVisible(false);
						studyAction3.setVisible(false);
						 
						assignmentImg1 = assign.getAssignmentImg1();
						assignmentImg2 = assign.getAssignmentImg2();
						
						assignmentImgLbl1.setIcon(assignmentImg1);
						assignmentImgLbl2.setIcon(assignmentImg2);
						
						assignmentImgLbl1.setVisible(true);
						assignmentImgLbl2.setVisible(true);

						assignmentAction1.setText(assign.getAssignmentAction(0));
						assignmentAction2.setText(assign.getAssignmentAction(1));
						
						assignmentAction2.setVisible(true);
						assignmentAction1.setVisible(true);
					}
				});
				add(studyAction1);
				
				studyAction2.setVisible(false);
				studyAction2.setBounds(364, 701, 256, 160);
				studyAction2.setFont(new Font("��������", Font.BOLD, 18));
				studyAction2.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						FileReading fored = new FileReading();
						status = fored.getstatus();
						if (study.changestatus(num[1]) == 6) status[3]++;
						else if (study.changestatus(num[1]) == 5) status[3]--;
						else if (study.changestatus(num[1]) == 4) { status[2]--; status[3]--; }
						else if (study.changestatus(num[1]) == 3) { status[2]++; status[3]++; }
						else if (study.changestatus(num[1]) == 2) { status[0]--; status[3]++; status[2]++; }
						else if (study.changestatus(num[1]) == 0) { status[2]--; status[3]--; status[4]--; }
						else { status[3]--; status[5]--; status[0]--; }

						try {
							if(status[2] <0) status[2] = 0;
							if(status[3] <0) status[3] = 0;
							if(status[4] <0) status[4] = 0;
							FileWriter fw = new FileWriter(file, true);
							fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
									 status[3] + " " + status[4] + " " + status[5] + " " + 
									 status[6] + " " + status[7] + " " + ch + "\r\n");
							fw.close();
						} catch (IOException f) {
							f.printStackTrace();
						}

						weekSection.setText("Week " + week);
						areaSection.setText("Assignment Area");
						studyImgLbl1.setVisible(false);
						studyImgLbl2.setVisible(false);
						studyImgLbl3.setVisible(false);
						studyAction1.setVisible(false);
						studyAction2.setVisible(false);
						studyAction3.setVisible(false);
						 
						assignmentImg1 = assign.getAssignmentImg1();
						assignmentImg2 = assign.getAssignmentImg2();
						
						assignmentImgLbl1.setIcon(assignmentImg1);
						assignmentImgLbl2.setIcon(assignmentImg2);
						
						assignmentImgLbl1.setVisible(true);
						assignmentImgLbl2.setVisible(true);

						assignmentAction1.setText(assign.getAssignmentAction(0));
						assignmentAction2.setText(assign.getAssignmentAction(1));
						
						assignmentAction2.setVisible(true);
						assignmentAction1.setVisible(true);
					}
				});
				add(studyAction2);
				
				studyAction3.setVisible(false);
				studyAction3.setBounds(674, 701, 256, 160);
				studyAction3.setFont(new Font("��������", Font.BOLD, 18));
				studyAction3.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						FileReading fored = new FileReading();
						status = fored.getstatus();
						if (study.changestatus(num[2]) == 6) status[3]++;
						else if (study.changestatus(num[2]) == 5) status[3]--;
						else if (study.changestatus(num[2]) == 4) { status[2]--; status[3]--; }
						else if (study.changestatus(num[2]) == 3) { status[2]++; status[3]++; }
						else if (study.changestatus(num[2]) == 2) { status[0]--; status[3]++; status[2]++; }
						else if (study.changestatus(num[2]) == 0) { status[2]--; status[3]--; status[4]--; }
						else { status[3]--; status[5]--; status[0]--; }

						try {
							if(status[2] <0) status[2] = 0;
							if(status[3] <0) status[3] = 0;
							if(status[4] <0) status[4] = 0;
							FileWriter fw = new FileWriter(file, true);
							fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
									 status[3] + " " + status[4] + " " + status[5] + " " + 
									 status[6] + " " + status[7] + " " + ch + "\r\n");
							fw.close();
						} catch (IOException f) {
							f.printStackTrace();
						}

						weekSection.setText("Week " + week);
						areaSection.setText("Assignment Area");
						studyImgLbl1.setVisible(false);
						studyImgLbl2.setVisible(false);
						studyImgLbl3.setVisible(false);
						studyAction1.setVisible(false);
						studyAction2.setVisible(false);
						studyAction3.setVisible(false);
						 
						assignmentImg1 = assign.getAssignmentImg1();
						assignmentImg2 = assign.getAssignmentImg2();
						
						assignmentImgLbl1.setIcon(assignmentImg1);
						assignmentImgLbl2.setIcon(assignmentImg2);
						
						assignmentImgLbl1.setVisible(true);
						assignmentImgLbl2.setVisible(true);

						assignmentAction1.setText(assign.getAssignmentAction(0));
						assignmentAction2.setText(assign.getAssignmentAction(1));
						
						assignmentAction2.setVisible(true);
						assignmentAction1.setVisible(true);
					}
				});
				add(studyAction3);
				
		        // Assignment Area
				assignmentImgLbl1.setVisible(false);
				assignmentImgLbl1.setBounds(54, 230, 417, 421);
				assignmentImgLbl1.setHorizontalAlignment(JLabel.CENTER);
				add(assignmentImgLbl1);
				
				assignmentImgLbl2.setVisible(false);
				assignmentImgLbl2.setBounds(519, 230, 417, 421);
				assignmentImgLbl2.setHorizontalAlignment(JLabel.CENTER);
				add(assignmentImgLbl2);
					
				assignmentAction1.setVisible(false);
				assignmentAction1.setBounds(54, 701, 417, 160);
				assignmentAction1.setFont(new Font("��������", Font.BOLD, 18));
				assignmentAction1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						weekSection.setText("Week " + week);
						areaSection.setText("Handong TMI Quiz");
						assignmentImgLbl1.setVisible(false);
						assignmentImgLbl2.setVisible(false);
						assignmentAction1.setVisible(false);
						assignmentAction2.setVisible(false);
						
						TMIquiz.setText(tmi.getTMIquiz(quiz1));
						TMIquizChoice1.setText(tmi.getTMIquizChoices(quiz1)[0]);
						TMIquizChoice2.setText(tmi.getTMIquizChoices(quiz1)[1]);
						TMIquizChoice3.setText(tmi.getTMIquizChoices(quiz1)[2]);
						TMIquizChoice4.setText(tmi.getTMIquizChoices(quiz1)[3]);
						
						TMIquiz.setVisible(true);
						TMIquizChoice1.setVisible(true);
						TMIquizChoice2.setVisible(true);
						TMIquizChoice3.setVisible(true);
						TMIquizChoice4.setVisible(true);
					}
				});
				add(assignmentAction1);
				
				assignmentAction2.setVisible(false);
				assignmentAction2.setBounds(519, 701, 417, 160);
				assignmentAction2.setFont(new Font("��������", Font.BOLD, 18));
				assignmentAction2.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						++noassignment;
						if (noassignment >=5 ) {
							assignmentImgLbl1.setVisible(false);
							assignmentImgLbl2.setVisible(false);
							assignmentAction1.setVisible(false);
							assignmentAction2.setVisible(false);
							weekSection.setVisible(false);
							areaSection.setVisible(false);
							gameED(0);
						} else {
							FileReading fored = new FileReading();
							status = fored.getstatus();
							status[3]--;
							try {
								if (status[3] < 0) status[3] = 0;
								FileWriter fw = new FileWriter(file, true);
								fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
										 status[3] + " " + status[4] + " " + status[5] + " " + 
										 status[6] + " " + status[7] + " " + ch + "\r\n");
								fw.close();
							} catch (IOException f) {
								f.printStackTrace();
							}

							weekSection.setText("Week " + week);
							areaSection.setText("Leisure Area");
							assignmentImgLbl1.setVisible(false);
							assignmentImgLbl2.setVisible(false);
							assignmentAction1.setVisible(false);
							assignmentAction2.setVisible(false);
							
							randomNumber();
							leisureImg1 = leisure.getLeisureImg1(num[0]);
							leisureImg2 = leisure.getLeisureImg2(num[1]);
							leisureImg3 = leisure.getLeisureImg3(num[2]);
							
							leisureImgLbl1.setIcon(leisureImg1);
							leisureImgLbl2.setIcon(leisureImg2);
							leisureImgLbl3.setIcon(leisureImg3);
							
							leisureImgLbl1.setVisible(true);
							leisureImgLbl2.setVisible(true);
							leisureImgLbl3.setVisible(true);
							
							leisureAction1.setText(leisure.getLeisureAction(num[0]));
							leisureAction2.setText(leisure.getLeisureAction(num[1]));
							leisureAction3.setText(leisure.getLeisureAction(num[2]));
							
							leisureAction1.setVisible(true);
							leisureAction2.setVisible(true);
							leisureAction3.setVisible(true);
						}
					}
				});
				add(assignmentAction2);
					
				// HandongTMIquiz Section
				TMIquiz.setVisible(false);
				TMIquiz.setBounds(50, 230, 950, 281);
				TMIquiz.setFont(new Font("��������", Font.BOLD, 30));
				TMIquiz.setHorizontalAlignment(JLabel.CENTER);
				add(TMIquiz);
				
				TMIquizChoice1.setVisible(false);
				TMIquizChoice1.setBounds(54, 561, 180, 300);
				TMIquizChoice1.setFont(new Font("��������", Font.BOLD, 18));
				TMIquizChoice1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						TMIquiz.setVisible(false);
						TMIquizChoice1.setVisible(false);
						TMIquizChoice2.setVisible(false);
						TMIquizChoice3.setVisible(false);
						TMIquizChoice4.setVisible(false);
						
						if (tmi.getTMIquizAnswer(quiz1++) == 0) {
							FileReading fored = new FileReading();
							status = fored.getstatus();
							status[3]++;
							try {
								FileWriter fw = new FileWriter(file, true);
								fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
										 status[3] + " " + status[4] + " " + status[5] + " " + 
										 status[6] + " " + status[7] + " " + ch + "\r\n");
								fw.close();
							} catch (IOException f) {
								f.printStackTrace();
							}

							System.out.println("You've got answer!!");
							quizResult.setIcon(tmi.getResultQuizImg1());
						} else {
							FileReading fored = new FileReading();
							status = fored.getstatus();
							status[3]--;
							try {
								if (status[3] < 0) status[3] = 0;
								FileWriter fw = new FileWriter(file, true);
								fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
										 status[3] + " " + status[4] + " " + status[5] + " " + 
										 status[6] + " " + status[7] + " " + ch + "\r\n");
								fw.close();
							} catch (IOException f) {
								f.printStackTrace();
							}

							System.out.println("It's wrong answer!!");
							quizResult.setIcon(tmi.getResultQuizImg2());
						}
						quizResult.setVisible(true);
						next.setVisible(true);				
					}
				});
				add(TMIquizChoice1);
				
				TMIquizChoice2.setVisible(false);
				TMIquizChoice2.setBounds(286, 561, 180, 300);
				TMIquizChoice2.setFont(new Font("��������", Font.BOLD, 18));
				TMIquizChoice2.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						TMIquiz.setVisible(false);
						TMIquizChoice1.setVisible(false);
						TMIquizChoice2.setVisible(false);
						TMIquizChoice3.setVisible(false);
						TMIquizChoice4.setVisible(false);
						
						if (tmi.getTMIquizAnswer(quiz1++) == 1) {
							FileReading fored = new FileReading();
							status = fored.getstatus();
							status[3]++;
							try {
								FileWriter fw = new FileWriter(file, false);
								fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
									     status[3] + " " + status[4] + " " + status[5] + " " + 
										 status[6] + " " + status[7] + " " + ch + "\r\n");
								fw.close();
							} catch (IOException f) {
								f.printStackTrace();
							}

							System.out.println("You've got answer!!");
							quizResult.setIcon(tmi.getResultQuizImg1());
						} else { 
							FileReading fored = new FileReading();
							status = fored.getstatus();
							status[3]--;
							try {
								if (status[3] < 0) status[3] = 0;
								FileWriter fw = new FileWriter(file, false);
								fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
										 status[3] + " " + status[4] + " " + status[5] + " " + 
										 status[6] + " " + status[7] + " " + ch + "\r\n");
								fw.close();
							} catch (IOException f) {
								f.printStackTrace();
							}

							System.out.println("It's wrong answer!!");
							quizResult.setIcon(tmi.getResultQuizImg2());
						}
						quizResult.setVisible(true);
						next.setVisible(true);		
					}
				});
				add(TMIquizChoice2);
				
				TMIquizChoice3.setVisible(false);
				TMIquizChoice3.setBounds(518, 561, 180, 300);
				TMIquizChoice3.setFont(new Font("��������", Font.BOLD, 18));
				TMIquizChoice3.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						TMIquiz.setVisible(false);
						TMIquizChoice1.setVisible(false);
						TMIquizChoice2.setVisible(false);
						TMIquizChoice3.setVisible(false);
						TMIquizChoice4.setVisible(false);
						
						if (tmi.getTMIquizAnswer(quiz1++) == 2) {
							FileReading fored = new FileReading();
							status = fored.getstatus();
							status[3]++;
							try {
								FileWriter fw = new FileWriter(file, true);
								fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
										 status[3] + " " + status[4] + " " + status[5] + " " + 
										 status[6] + " " + status[7] + " " + ch + "\r\n");
								fw.close();
							} catch (IOException f) {
								f.printStackTrace();
							}

							System.out.println("You've got answer!!");
							quizResult.setIcon(tmi.getResultQuizImg1());
						} else {
							FileReading fored = new FileReading();
							status = fored.getstatus();
							status[3]--;
							try {
								if (status[3] < 0) status[3] = 0;
								FileWriter fw = new FileWriter(file, true);
								fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
									     status[3] + " " + status[4] + " " + status[5] + " " + 
										 status[6] + " " + status[7] + " " + ch + "\r\n");
								fw.close();
							} catch (IOException f) {
								f.printStackTrace();
							}

							System.out.println("It's wrong answer!!");
							quizResult.setIcon(tmi.getResultQuizImg2());
						}
						quizResult.setVisible(true);
						next.setVisible(true);		
					}
				});
				add(TMIquizChoice3);
				
				TMIquizChoice4.setVisible(false);
				TMIquizChoice4.setBounds(750, 561, 180, 300);
				TMIquizChoice4.setFont(new Font("��������", Font.BOLD, 18));
				TMIquizChoice4.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						TMIquiz.setVisible(false);
						TMIquizChoice1.setVisible(false);
						TMIquizChoice2.setVisible(false);
						TMIquizChoice3.setVisible(false);
						TMIquizChoice4.setVisible(false);
						
						if (tmi.getTMIquizAnswer(quiz1++) == 3) {
							FileReading fored = new FileReading();
							status = fored.getstatus();
							status[3]++;
							try {
								FileWriter fw = new FileWriter(file, true);
								fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
									     status[3] + " " + status[4] + " " + status[5] + " " + 
										 status[6] + " " + status[7] + " " + ch + "\r\n");
								fw.close();
							} catch (IOException f) {
								f.printStackTrace();
							}

							System.out.println("You've got answer!!");
							quizResult.setIcon(tmi.getResultQuizImg1());
						} else {
							FileReading fored = new FileReading();
							status = fored.getstatus();
							status[3]--;
							try {
								if (status[3] < 0) status[3] = 0;
								FileWriter fw = new FileWriter(file, true);
								fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
										 status[3] + " " + status[4] + " " + status[5] + " " + 
										 status[6] + " " + status[7] + " " + ch+"\r\n");
								fw.close();
							} catch (IOException f) {
								f.printStackTrace();
							}

							System.out.println("It's wrong answer!!");
							quizResult.setIcon(tmi.getResultQuizImg2());
						}
						quizResult.setVisible(true);
						next.setVisible(true);		
					}
				});
				add(TMIquizChoice4);
				
				// Quiz result Section
				quizResult.setVisible(false);
				quizResult.setBounds(242, 230, 500, 500);
				quizResult.setHorizontalAlignment(JLabel.CENTER);
				add(quizResult);

				next.setVisible(false);
				next.setBounds(342, 780, 300, 131);
				next.setText("NEXT");
				next.setFont(new Font("��������", Font.BOLD, 30));
				next.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						quizResult.setVisible(false);
						next.setVisible(false);
						
						weekSection.setText("Week " + week);
						areaSection.setText("Leisure Area");

						randomNumber();
						leisureImg1 = leisure.getLeisureImg1(num[0]);
						leisureImg2 = leisure.getLeisureImg2(num[1]);
						leisureImg3 = leisure.getLeisureImg3(num[2]);

						leisureImgLbl1.setIcon(leisureImg1);
						leisureImgLbl2.setIcon(leisureImg2);
						leisureImgLbl3.setIcon(leisureImg3);

						leisureImgLbl1.setVisible(true);
						leisureImgLbl2.setVisible(true);
						leisureImgLbl3.setVisible(true);

						leisureAction1.setText(leisure.getLeisureAction(num[0]));
						leisureAction2.setText(leisure.getLeisureAction(num[1]));
						leisureAction3.setText(leisure.getLeisureAction(num[2]));

						leisureAction1.setVisible(true);
						leisureAction2.setVisible(true);
						leisureAction3.setVisible(true);
					}
				});
				add(next);	
						
				// Leisure Area
				leisureImgLbl1.setVisible(false);
				leisureImgLbl1.setBounds(54, 230, 256, 421);
				leisureImgLbl1.setHorizontalAlignment(JLabel.CENTER);
				add(leisureImgLbl1);
				
				leisureImgLbl2.setVisible(false);
				leisureImgLbl2.setBounds(364, 230, 256, 421);
				leisureImgLbl2.setHorizontalAlignment(JLabel.CENTER);
				add(leisureImgLbl2);
				
				leisureImgLbl3.setVisible(false);
				leisureImgLbl3.setBounds(674, 230, 256, 421);
				leisureImgLbl3.setHorizontalAlignment(JLabel.CENTER);
				add(leisureImgLbl3);
				
				leisureAction1.setVisible(false);
				leisureAction1.setBounds(54, 701, 256, 160);
				leisureAction1.setFont(new Font("��������", Font.BOLD, 18));
				leisureAction1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						FileReading fored = new FileReading();
						status = fored.getstatus();
						if (leisure.changefromleisure(num[0]) == 5) {
							if (status[5] <= 0) {
								warnframe();
								status[2]++;
							} else {
								status[5]--; // money down
								status[2]--; // stress down
							}
						} else if (leisure.changefromleisure(num[0]) == 4) {
							status[0]++; status[2]--;
						} else if (leisure.changefromleisure(num[0]) == 3) {
							status[2]--; status[4]++;
						} else if (leisure.changefromleisure(num[0]) == 2) {
							status[2] += 0;
						} else if (leisure.changefromleisure(num[0]) == 0) {
							status[0]--; status[2]--; status[5]--;
						} else {
							status[2]--; ch = "HandongLover";	
						}
						
						try {
							if (status[2]<0) status[2] = 0;
							if (status[3]<0) status[3] = 0;
							if (status[4]<0) status[4] = 0;
							if (status[5]<0) status[5] = 0;
							FileWriter fw = new FileWriter(file, true);
							fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
								     status[3] + " " + status[4] + " " + status[5] + " " + 
									 status[6] + " " + status[7] + " " + ch+"\r\n");
							fw.close();
						} catch (IOException f) {
							f.printStackTrace();
						}

						weekSection.setText("Week " + week);
						areaSection.setText("Business Area");
						leisureImgLbl1.setVisible(false);
						leisureImgLbl2.setVisible(false);
						leisureImgLbl3.setVisible(false);
						leisureAction1.setVisible(false);
						leisureAction2.setVisible(false);
						leisureAction3.setVisible(false);
						 
						randomNumber();
						businessImg1 = business.getBusinessImg1(num[0]);
						businessImg2 = business.getBusinessImg2(num[1]);
						businessImg3 = business.getBusinessImg3(num[2]);
						
						businessImgLbl1.setIcon(businessImg1);
						businessImgLbl2.setIcon(businessImg2);
						businessImgLbl3.setIcon(businessImg3);
						
						businessImgLbl1.setVisible(true);
						businessImgLbl2.setVisible(true);
						businessImgLbl3.setVisible(true);
						
						businessAction1.setText(business.getBusinessAction(num[0]));
						businessAction2.setText(business.getBusinessAction(num[1]));
						businessAction3.setText(business.getBusinessAction(num[2]));
						
						businessAction1.setVisible(true);
						businessAction2.setVisible(true);
						businessAction3.setVisible(true);
					}
				});
				add(leisureAction1);
				
				leisureAction2.setVisible(false);
				leisureAction2.setBounds(364, 701, 256, 160);
				leisureAction2.setFont(new Font("��������", Font.BOLD, 18));
				leisureAction2.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						FileReading fored = new FileReading();
						status = fored.getstatus();
						if (leisure.changefromleisure(num[1]) == 5) {
							if (status[5] <= 0) {
								warnframe();
								status[2]++;
							} else {
								status[5]--; // money down
								status[2]--; // stress down
								int prob = ran.nextInt(100);
								if(prob <= 20) status[4]--;
							}
						} else if(leisure.changefromleisure(num[1]) == 4) {
							status[0]++; status[2]--;	
						} else if(leisure.changefromleisure(num[1]) == 3) {
							status[2]--; status[4]++;
						} else if(leisure.changefromleisure(num[1]) == 2) {
							status[2] += 0;
						} else if(leisure.changefromleisure(num[1]) == 0) {
							status[0]--; status[2]--; status[5]--;
						} else {
							status[2]--; ch = "HandongLover";	
						}

						try {
							if (status[2] < 0) status[2] = 0;
							if (status[3] < 0) status[3] = 0;
							if (status[4] < 0) status[4] = 0;
							if (status[5] < 0) status[5] = 0;
							FileWriter fw = new FileWriter(file, true);
							fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
									 status[3] + " " + status[4] + " " + status[5] + " " + 
									 status[6] + " " + status[7] + " " + ch+"\r\n");
							fw.close();
						} catch (IOException f) {
							f.printStackTrace();
						}

						weekSection.setText("Week " + week);
						areaSection.setText("Business Area");
						leisureImgLbl1.setVisible(false);
						leisureImgLbl2.setVisible(false);
						leisureImgLbl3.setVisible(false);
						leisureAction1.setVisible(false);
						leisureAction2.setVisible(false);
						leisureAction3.setVisible(false);
						 
						randomNumber();
						businessImg1 = business.getBusinessImg1(num[0]);
						businessImg2 = business.getBusinessImg2(num[1]);
						businessImg3 = business.getBusinessImg3(num[2]);
						
						businessImgLbl1.setIcon(businessImg1);
						businessImgLbl2.setIcon(businessImg2);
						businessImgLbl3.setIcon(businessImg3);
						
						businessImgLbl1.setVisible(true);
						businessImgLbl2.setVisible(true);
						businessImgLbl3.setVisible(true);
						
						businessAction1.setText(business.getBusinessAction(num[0]));
						businessAction2.setText(business.getBusinessAction(num[1]));
						businessAction3.setText(business.getBusinessAction(num[2]));
						
						businessAction1.setVisible(true);
						businessAction2.setVisible(true);
						businessAction3.setVisible(true);
					}
				});
				add(leisureAction2);
				
				leisureAction3.setVisible(false);
				leisureAction3.setBounds(674, 701, 256, 160);
				leisureAction3.setFont(new Font("��������", Font.BOLD, 18));
				leisureAction3.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						FileReading fored = new FileReading();
						status = fored.getstatus();
						if (leisure.changefromleisure(num[2]) == 5) {
							if (status[5] <= 0) {
								status[2]++;
								warnframe();
							} else {
								status[5]--; // money down
								status[2]--; // stress down
							}
						} else if(leisure.changefromleisure(num[2]) == 4) {
							status[0]++; status[2]--;	
						} else if(leisure.changefromleisure(num[2]) == 3) {
							status[2]--; status[4]++;
						} else if(leisure.changefromleisure(num[2]) == 2) {
							status[2] += 0;
						} else if(leisure.changefromleisure(num[2]) == 0) {
							status[0]--; status[2]--; status[5]--;
						} else {
							status[2]--; ch = "HandongLover";	
						}

						try {
							if (status[2] < 0) status[2] = 0;
							if (status[3] < 0) status[3] = 0;
							if (status[4] < 0) status[4] = 0;
							if (status[5] < 0) status[5] = 0;
							FileWriter fw = new FileWriter(file, true);
							fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
								     status[3] + " " + status[4] + " " + status[5] + " " + 
									 status[6] + " " + status[7] + " " + ch+"\r\n");
							fw.close();
						} catch (IOException f) {
							f.printStackTrace();
						}

						weekSection.setText("Week " + week);
						areaSection.setText("Business Area");
						leisureImgLbl1.setVisible(false);
						leisureImgLbl2.setVisible(false);
						leisureImgLbl3.setVisible(false);
						leisureAction1.setVisible(false);
						leisureAction2.setVisible(false);
						leisureAction3.setVisible(false);
						 
						randomNumber();
						businessImg1 = business.getBusinessImg1(num[0]);
						businessImg2 = business.getBusinessImg2(num[1]);
						businessImg3 = business.getBusinessImg3(num[2]);
						
						businessImgLbl1.setIcon(businessImg1);
						businessImgLbl2.setIcon(businessImg2);
						businessImgLbl3.setIcon(businessImg3);
						
						businessImgLbl1.setVisible(true);
						businessImgLbl2.setVisible(true);
						businessImgLbl3.setVisible(true);
						
						businessAction1.setText(business.getBusinessAction(num[0]));
						businessAction2.setText(business.getBusinessAction(num[1]));
						businessAction3.setText(business.getBusinessAction(num[2]));
						
						businessAction1.setVisible(true);
						businessAction2.setVisible(true);
						businessAction3.setVisible(true);
					}
				});
				add(leisureAction3);
				
				reasonofed.setVisible(false);
				reasonofed.setBounds(54, 200, 884, 40);
				reasonofed.setText("������ ������?");
				reasonofed.setFont(new Font("��������", Font.BOLD, 30));
				reasonofed.setHorizontalAlignment(JLabel.CENTER);
				add(reasonofed);
				
		        // Business Area
				nextweek.setVisible(false);
				nextweek.setBounds(342, 780, 300, 131);
				nextweek.setText("NEXT");
				nextweek.setFont(new Font("��������", Font.BOLD, 30));
				nextweek.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						FileReading fored = new FileReading();
						status = fored.getstatus();
						ch = fored.getch();
						nextweek.setVisible(false);
						if (week == 17) {
							weekSection.setText("End");
							areaSection.setText("�����̵�ƾƾƾƾƾƾƾ�!!!!!!!!!!!!");
							if (ch.equals("HandongLover")) gameED(6);
							if (status[3] >= 35) gameED(4);
							if (status[5] >= 20) gameED(7);
							if (status[4] >= 15) gameED(5);
							else gameED(3);
						} else if (status[2] >= 5) {
							weekSection.setText("��  ��  ��  ��  ��");
							areaSection.setText("��                ��");
							reasonofed.setText("������ ������?");
							reasonofed.setVisible(true);
							gameED(2);
						} else if(status[0] <= 6) {
							weekSection.setText("��  ��  ��  ��  ��");
							areaSection.setText("��                ��");
							gameED(1);
						} else if (status[1] >= 3) {
							weekSection.setText("��  ��  ��  ��  ��");
							areaSection.setText("��                ��");
							reasonofed.setText("������ ������?");
							reasonofed.setVisible(true);
							gameED(0);
						} else {
							try {
								FileWriter fw = new FileWriter(file, true);
								fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
										 status[3] + " " + status[4] + " " + status[5] + " " + 
										 status[6] + " " + status[7] + " " + ch+"\r\n");
								fw.close();
							} catch (IOException f) {
								f.printStackTrace();
							}

							System.out.println(ch);
							System.out.println("week " + (week));
							weekSection.setText("Week " + (week));
							areaSection.setText("Study Area");
							randomNumber();
							studyImg1 = study.getStudyImg1(num[0]);
							studyImg2 = study.getStudyImg1(num[1]);
							studyImg3 = study.getStudyImg1(num[2]);
							
							studyImgLbl1.setIcon(studyImg1);
							studyImgLbl2.setIcon(studyImg2);
							studyImgLbl3.setIcon(studyImg3);
							
							studyImgLbl1.setVisible(true);
							studyImgLbl2.setVisible(true);
							studyImgLbl3.setVisible(true);
							
							studyAction1.setText(study.getStudyAction(num[0]));
							studyAction2.setText(study.getStudyAction(num[1]));
							studyAction3.setText(study.getStudyAction(num[2]));
							
							studyAction1.setVisible(true);
							studyAction2.setVisible(true);
							studyAction3.setVisible(true);
						}
					}
				});
				add(nextweek);
				
				businessImgLbl1.setVisible(false);
				businessImgLbl1.setBounds(54, 230, 256, 421);
				businessImgLbl1.setHorizontalAlignment(JLabel.CENTER);
				add(businessImgLbl1);
				
				businessImgLbl2.setVisible(false);
				businessImgLbl2.setBounds(364, 230, 256, 421);
				businessImgLbl2.setHorizontalAlignment(JLabel.CENTER);
				add(businessImgLbl2);
				
				businessImgLbl3.setVisible(false);
				businessImgLbl3.setBounds(674, 230, 256, 421);
				businessImgLbl3.setHorizontalAlignment(JLabel.CENTER);
				add(businessImgLbl3);
						
				businessAction1.setVisible(false);
				businessAction1.setBounds(54, 701, 256, 160);
				businessAction1.setFont(new Font("��������", Font.BOLD, 18));
				businessAction1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						FileReading fored = new FileReading();
						status = fored.getstatus();
						if (business.changefrombusiness(num[0]) == 4) {
							if (status[5] <= 0) {
								status[2]++; // stress up
								warnframe();
							} else {
								status[4]++;
								status[5]--; // money down
							}
						} else if(business.changefrombusiness(num[0]) == 3) {
							status[4]--;
						} else if(business.changefrombusiness(num[0]) == 2) {
							status[4]++; status[5] += 2; status[0]--;
						} else if(business.changefrombusiness(num[0]) == 1) {
							if (status[5] <= 0) {
								status[2]++; // stress up
								warnframe();
							} else {
								status[4]--;
								status[5]--; // money down
							}
						} else {
							status[4]++;
						}

						try {
							if (status[2] < 0) status[2] = 0;
							if (status[3] < 0) status[3] = 0;
							if (status[4] < 0) status[4] = 0;
							if (status[5] < 0) status[5] = 0;

							status[5] += 2;
							FileWriter fw = new FileWriter(file, true);
							fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
									 status[3] + " " + status[4] + " " + status[5] + " " + 
									 status[6] + " " + status[7] + " " + ch+"\r\n");
							fw.close();
						} catch (IOException f) {
							f.printStackTrace();
						}
						businessImgLbl1.setVisible(false);
						businessImgLbl2.setVisible(false);
						businessImgLbl3.setVisible(false);
						businessAction1.setVisible(false);
						businessAction2.setVisible(false);
						businessAction3.setVisible(false);
						
						if (week == 1 || week == 2 || week == 9 || week == 14) {
							switch (week) {
							case 1:
								weekSection.setText("Week 1: ��MT");
								break;
							case 2:
								weekSection.setText("Week 2: ���Ƹ�MT");
								break;
							case 9:
								weekSection.setText("Week 9: ����");
								break;
							case 14:
								weekSection.setText("Week 14: ��Ư��");
								break;
							}
							
							areaSection.setText("Recreation Quiz");
							
							RECquiz.setText(rec.getRecreationQuiz(quiz2));
							RECquizChoice1.setText(rec.getRecreationChoices(0));
							RECquizChoice2.setText(rec.getRecreationChoices(1));
							
							RECquiz.setVisible(true);
							RECquizChoice1.setVisible(true);
							RECquizChoice2.setVisible(true);
						} else if (++week <= 16) {
							FileReading eva = new FileReading();
							int[] eva2 = eva.getstatus();
							
							if (week == 5 || week == 9 ) {
								new MiniGameUI();
									System.out.println(fored.getch());
									weekSection.setText("Week " + (week-1));
									if (week == 5) areaSection.setText("���� 1");
									if (week == 9) areaSection.setText("�߰�����");
									nextweek.setVisible(true);
							} else if (week == 13) {
								new Direction();
									System.out.println(fored.getch());
									weekSection.setText("Week " + (week-1));
									areaSection.setText("���� 2");
									nextweek.setVisible(true);
							} else {
								if (eva2[2] >= 5) {
									weekSection.setText("��  ��  ��  ��  ��");
									areaSection.setText("��                ��");
									reasonofed.setText("������ ������?");
									reasonofed.setVisible(true);
									gameED(2);
								} else if(eva2[0] <= 6) {
									weekSection.setText("��  ��  ��  ��  ��");
									areaSection.setText("��                ��");
									gameED(1);
								} else if(eva2[1] >= 3) {
									weekSection.setText("��  ��  ��  ��  ��");
									areaSection.setText("��                ��");
									gameED(0);
								} else {
									System.out.println("week " + week);
									weekSection.setText("Week " + week);
									areaSection.setText("Study Area");
									randomNumber();
									studyImg1 = study.getStudyImg1(num[0]);
									studyImg2 = study.getStudyImg1(num[1]);
									studyImg3 = study.getStudyImg1(num[2]);
									
									studyImgLbl1.setIcon(studyImg1);
									studyImgLbl2.setIcon(studyImg2);
									studyImgLbl3.setIcon(studyImg3);
									
									studyImgLbl1.setVisible(true);
									studyImgLbl2.setVisible(true);
									studyImgLbl3.setVisible(true);
									
									studyAction1.setText(study.getStudyAction(num[0]));
									studyAction2.setText(study.getStudyAction(num[1]));
									studyAction3.setText(study.getStudyAction(num[2]));
									
									studyAction1.setVisible(true);
									studyAction2.setVisible(true);
									studyAction3.setVisible(true);
								}
							}
						} else {
							new Direction();
								System.out.println(fored.getch());
								weekSection.setText("Week " + (week-1));
								areaSection.setText("�⸻����");
								nextweek.setVisible(true);
						}
					}
				});
				add(businessAction1);
				
				businessAction2.setVisible(false);
				businessAction2.setBounds(364, 701, 256, 160);
				businessAction2.setFont(new Font("��������", Font.BOLD, 18));
				businessAction2.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						FileReading fored = new FileReading();
						status = fored.getstatus();
						if (business.changefrombusiness(num[1]) == 4) {
							if (status[5] <= 0) {
								warnframe();
								status[2]++;
							} else {
								status[4]++;
								status[5]--; // money down
							}
						} else if(business.changefrombusiness(num[1]) == 3) {
							status[4]--;
						} else if(business.changefrombusiness(num[1]) == 2) {
							status[4]++; status[5] += 2; status[0]--;
						} else if(business.changefrombusiness(num[1]) == 1) {
							if (status[5] <= 0) {
								status[2]++; // stress up
								warnframe();
							} else {
								status[4]--;
								status[5]--; // money down
							}
						} else {
							status[4]++;
						}

						try {
							if (status[2] < 0) status[2] = 0;
							if (status[3] < 0) status[3] = 0;
							if (status[4] < 0) status[4] = 0;
							if (status[5] < 0) status[5] = 0;
							status[5] += 2;
							FileWriter fw = new FileWriter(file, true);
							fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
								     status[3] + " " + status[4] + " " + status[5] + " " + 
									 status[6] + " " + status[7] + " " + ch + "\r\n");
							fw.close();
						} catch (IOException f) {
							f.printStackTrace();
						}

						businessImgLbl1.setVisible(false);
						businessImgLbl2.setVisible(false);
						businessImgLbl3.setVisible(false);
						businessAction1.setVisible(false);
						businessAction2.setVisible(false);
						businessAction3.setVisible(false);
						
						if (week == 1 || week == 2 || week == 9 || week == 14) {
							switch (week) {
							case 1:
								weekSection.setText("Week 1: ��MT");
								break;
							case 2:
								weekSection.setText("Week 2: ���Ƹ�MT");
								break;
							case 9:
								weekSection.setText("Week 9: ����");
								break;
							case 14:
								weekSection.setText("Week 14: ��Ư��");
								break;
							}
							
							areaSection.setText("Recreation Quiz");
							
							RECquiz.setText(rec.getRecreationQuiz(quiz2));
							RECquizChoice1.setText(rec.getRecreationChoices(0));
							RECquizChoice2.setText(rec.getRecreationChoices(1));
							
							RECquiz.setVisible(true);
							RECquizChoice1.setVisible(true);
							RECquizChoice2.setVisible(true);
						} else if (++week <= 16) {
							FileReading eva = new FileReading();
							int[] eva2 = eva.getstatus();
							if (week == 5 || week == 9 ) {
								new MiniGameUI();
									System.out.println(fored.getch());
									weekSection.setText("Week " + (week-1));
									if (week == 5) areaSection.setText("���� 1");
									if (week == 9) areaSection.setText("�߰�����");
									nextweek.setVisible(true);
							} else if (week == 13) {
								new Direction();
									System.out.println(fored.getch());
									weekSection.setText("Week " + (week-1));
									areaSection.setText("���� 2");
									nextweek.setVisible(true);
							} else {
								if (eva2[2] >= 5) {
									weekSection.setText("��  ��  ��  ��  ��");
									areaSection.setText("��                ��");
									reasonofed.setText("������ ������?");
									reasonofed.setVisible(true);
									gameED(2);
								} else if (eva2[0] <= 6) {
									weekSection.setText("��  ��  ��  ��  ��");
									areaSection.setText("��                ��");
									gameED(1);
								} else if (eva2[1] >= 3) {
									weekSection.setText("��  ��  ��  ��  ��");
									areaSection.setText("��                ��");
									gameED(0);
								} else {
									System.out.println("week " + week);
									weekSection.setText("Week " + week);
									areaSection.setText("Study Area");
									randomNumber();
									studyImg1 = study.getStudyImg1(num[0]);
									studyImg2 = study.getStudyImg1(num[1]);
									studyImg3 = study.getStudyImg1(num[2]);
									
									studyImgLbl1.setIcon(studyImg1);
									studyImgLbl2.setIcon(studyImg2);
									studyImgLbl3.setIcon(studyImg3);
									
									studyImgLbl1.setVisible(true);
									studyImgLbl2.setVisible(true);
									studyImgLbl3.setVisible(true);
									
									studyAction1.setText(study.getStudyAction(num[0]));
									studyAction2.setText(study.getStudyAction(num[1]));
									studyAction3.setText(study.getStudyAction(num[2]));
									
									studyAction1.setVisible(true);
									studyAction2.setVisible(true);
									studyAction3.setVisible(true);
								}
							}
						} else {
							new Direction();
								System.out.println(fored.getch());
								weekSection.setText("Week " + (week-1));
								areaSection.setText("�⸻����");
								nextweek.setVisible(true);
						}
					}
				});
				add(businessAction2);
				
				businessAction3.setVisible(false);
				businessAction3.setBounds(674, 701, 256, 160);
				businessAction3.setFont(new Font("��������", Font.BOLD, 18));
				businessAction3.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						FileReading fored = new FileReading();
						status = fored.getstatus();
						ch = fored.getch();
						if (business.changefrombusiness(num[2]) == 4) {
							if (status[5] <= 0) {
								status[2]++; // stress up
								warnframe();
							} else {
								status[4]++;
								status[5]--; // money down
							}
						} else if (business.changefrombusiness(num[2]) == 3) {
							status[4]--;
						} else if (business.changefrombusiness(num[2]) == 2) {
							status[4]++; status[5] += 2; status[0]--;
						} else if (business.changefrombusiness(num[2]) == 1) {
							if (status[5] <= 0) {
								status[2]++; // stress up
								warnframe();
							} else {
								status[4]--;
								status[5]--; // money down
							}
						} else {
							status[4]++;
						}

						try {
							if (status[2] < 0) status[2] = 0;
							if (status[3] < 0) status[3] = 0;
							if (status[4] < 0) status[4] = 0;
							if (status[5] < 0) status[5] = 0;
							status[5] += 2;
							FileWriter fw = new FileWriter(file, true);
							fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
									 status[3] + " " + status[4] + " " + status[5] + " " + 
									 status[6] + " " + status[7] + " " + ch + "\r\n");
							fw.close();
						} catch (IOException f) {
							f.printStackTrace();
						}

						businessImgLbl1.setVisible(false);
						businessImgLbl2.setVisible(false);
						businessImgLbl3.setVisible(false);
						businessAction1.setVisible(false);
						businessAction2.setVisible(false);
						businessAction3.setVisible(false);
						
						if (week == 1 || week == 2 || week == 9 || week == 14) {					
							switch (week) {
							case 1:
								weekSection.setText("Week 1: ��MT");
								break;
							case 2:
								weekSection.setText("Week 2: ���Ƹ�MT");
								break;
							case 9:
								weekSection.setText("Week 9: ����");
								break;
							case 14:
								weekSection.setText("Week 14: ��Ư��");
								break;
							}		
							
							areaSection.setText("Recreation Quiz");
							
							RECquiz.setText(rec.getRecreationQuiz(quiz2));
							RECquizChoice1.setText(rec.getRecreationChoices(0));
							RECquizChoice2.setText(rec.getRecreationChoices(1));
							
							RECquiz.setVisible(true);
							RECquizChoice1.setVisible(true);
							RECquizChoice2.setVisible(true);
						} else if (++week <= 16) {
							FileReading eva = new FileReading();
							int[] eva2 = eva.getstatus();
							if (week == 5 || week == 9 ) {
								new MiniGameUI();
									System.out.println(fored.getch());
									weekSection.setText("Week " + (week-1));
									if (week == 5) areaSection.setText("���� 1");
									if (week == 9) areaSection.setText("�߰�����");
									nextweek.setVisible(true);
							} else if (week == 13) {
								new Direction();
									System.out.println(fored.getch());
									weekSection.setText("Week " + (week-1));
									areaSection.setText("���� 2");
									nextweek.setVisible(true);
							} else {
								if (eva2[2] >= 5) {
									weekSection.setText("��  ��  ��  ��  ��");
									areaSection.setText("��                ��");
									reasonofed.setText("������ ������?");
									reasonofed.setVisible(true);
									gameED(2);
								} else if (eva2[0] <= 6) {
									weekSection.setText("��  ��  ��  ��  ��");
									areaSection.setText("��                ��");
									gameED(1);
								} else if (eva2[1] >= 3) {
									weekSection.setText("��  ��  ��  ��  ��");
									areaSection.setText("��                ��");
									gameED(0);
								} else {
									System.out.println("week " + week);
									weekSection.setText("Week " + week);
									areaSection.setText("Study Area");
									randomNumber();
									studyImg1 = study.getStudyImg1(num[0]);
									studyImg2 = study.getStudyImg1(num[1]);
									studyImg3 = study.getStudyImg1(num[2]);
									
									studyImgLbl1.setIcon(studyImg1);
									studyImgLbl2.setIcon(studyImg2);
									studyImgLbl3.setIcon(studyImg3);
									
									studyImgLbl1.setVisible(true);
									studyImgLbl2.setVisible(true);
									studyImgLbl3.setVisible(true);
									
									studyAction1.setText(study.getStudyAction(num[0]));
									studyAction2.setText(study.getStudyAction(num[1]));
									studyAction3.setText(study.getStudyAction(num[2]));
									
									studyAction1.setVisible(true);
									studyAction2.setVisible(true);
									studyAction3.setVisible(true);
								}
							}
						} else {
							new Direction();
								System.out.println(fored.getch());
								weekSection.setText("Week " + (week-1));
								areaSection.setText("�⸻����");
								nextweek.setVisible(true);
						}
					}
				});
				add(businessAction3);
				
				// Recreation quiz Section Area
				RECquiz.setVisible(false);
				RECquiz.setBounds(54, 230, 884, 281);
				RECquiz.setFont(new Font("��������", Font.BOLD, 30));
				RECquiz.setHorizontalAlignment(JLabel.CENTER);
				add(RECquiz);
				
				RECquizChoice1.setVisible(false);
				RECquizChoice1.setBounds(108, 661, 330, 100);
				RECquizChoice1.setFont(new Font("��������", Font.BOLD, 30));
				RECquizChoice1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (rec.getRecreationAnswer(quiz2) == 0) {
							FileReading fored = new FileReading();
							status = fored.getstatus();
							status[3]++;
							try {
								FileWriter fw = new FileWriter(file, true);
								fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
										 status[3] + " " + status[4] + " " + status[5] + " " + 
										 status[6] + " " + status[7] + " " + ch + "\r\n");
								fw.close();
							} catch (IOException f) {
								f.printStackTrace();
							}

							System.out.println("You've got answer!!");
							quizResult.setIcon(rec.getResultQuizImg1());			
						} else {
							FileReading fored = new FileReading();
							status = fored.getstatus();
							status[3]--;
							try {
								if (status[3] < 0) status[3] = 0;
								FileWriter fw = new FileWriter(file, true);
								fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
										 status[3] + " " + status[4] + " " + status[5] + " " + 
										 status[6] + " " + status[7] + " " + ch + "\r\n");
								fw.close();
							} catch (IOException f) {
								f.printStackTrace();
							}

							System.out.println("It's wrong answer!!");
							quizResult.setIcon(rec.getResultQuizImg2());
						}	
						
						RECquiz.setText(rec.getRecreationQuiz(++quiz2));
						RECquiz.setVisible(false);
						RECquizChoice1.setVisible(false);
						RECquizChoice2.setVisible(false);
						
						quizResult.setVisible(true);
						nextQuiz.setVisible(true);
					}
				});
				add(RECquizChoice1);
				
				RECquizChoice2.setVisible(false);
				RECquizChoice2.setBounds(546, 661, 330, 100);
				RECquizChoice2.setFont(new Font("��������", Font.BOLD, 30));
				RECquizChoice2.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (rec.getRecreationAnswer(quiz2) == 1) {
							FileReading fored = new FileReading();
							status = fored.getstatus();
							status[3]++;
							try {
								FileWriter fw = new FileWriter(file, true);
								fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
										 status[3] + " " + status[4] + " " + status[5] + " " + 
										 status[6] + " " + status[7] + " " + ch + "\r\n");
								fw.close();
							} catch (IOException f) {
								f.printStackTrace();
							}

							System.out.println("You've got answer!!");
							quizResult.setIcon(rec.getResultQuizImg1());			
						} else {
							FileReading fored = new FileReading();
							status = fored.getstatus();
							status[3]--;
							try {
								if (status[3] < 0) status[3] = 0;
								FileWriter fw = new FileWriter(file, true);
								fw.write(status[0] + " " + status[1] + " " + status[2] + " " + 
										 status[3] + " " + status[4] + " " + status[5] + " " + 
										 status[6] + " " + status[7] + " " + ch + "\r\n");
								fw.close();
							} catch (IOException f) {
								f.printStackTrace();
							}

							System.out.println("It's wrong answer!!");
							quizResult.setIcon(rec.getResultQuizImg2());
						}
						
						RECquiz.setText(rec.getRecreationQuiz(++quiz2));
						RECquiz.setVisible(false);
						RECquizChoice1.setVisible(false);
						RECquizChoice2.setVisible(false);
						
						quizResult.setVisible(true);
						nextQuiz.setVisible(true);
					}
				});
				add(RECquizChoice2);
				
				nextQuiz.setVisible(false);
				nextQuiz.setBounds(342, 780, 300, 131);
				nextQuiz.setText("NEXT");
				nextQuiz.setFont(new Font("��������", Font.BOLD, 30));
				nextQuiz.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						quizResult.setVisible(false);
						nextQuiz.setVisible(false);
						
						if (trial++ < 2) {
							RECquiz.setVisible(true);
							RECquizChoice1.setVisible(true);
							RECquizChoice2.setVisible(true);
						} else {
							trial = 0;
							
							FileReading fored = new FileReading();
							status = fored.getstatus();
							ch = fored.getch();
							nextweek.setVisible(false);
							if (status[2] >= 5) {
								weekSection.setText("��  ��  ��  ��  ��");
								areaSection.setText("��                ��");
								reasonofed.setVisible(true);
								gameED(2);
							} else if (status[0] <= 6) {
								weekSection.setText("��  ��  ��  ��  ��");
								areaSection.setText("��                ��");
								gameED(1);
							} else if (status[1] >= 3) {
								weekSection.setText("��  ��  ��  ��  ��");
								areaSection.setText("��                ��");
								gameED(0);
							} else{
								week++;
								System.out.println("week " + week);
								weekSection.setText("Week " + week);
								areaSection.setText("Study Area");
								randomNumber();
								studyImg1 = study.getStudyImg1(num[0]);
								studyImg2 = study.getStudyImg1(num[1]);
								studyImg3 = study.getStudyImg1(num[2]);
								
								studyImgLbl1.setIcon(studyImg1);
								studyImgLbl2.setIcon(studyImg2);
								studyImgLbl3.setIcon(studyImg3);
								
								studyImgLbl1.setVisible(true);
								studyImgLbl2.setVisible(true);
								studyImgLbl3.setVisible(true);
								
								studyAction1.setText(study.getStudyAction(num[0]));
								studyAction2.setText(study.getStudyAction(num[1]));
								studyAction3.setText(study.getStudyAction(num[2]));
								
								studyAction1.setVisible(true);
								studyAction2.setVisible(true);
								studyAction3.setVisible(true);
							}
						}
					}
				});
				add(nextQuiz);	

		// Status
		showStatus.setVisible(false);
		showStatus.setBounds(700, 50, 300, 100);
		showStatus.setFont(new Font("����", Font.PLAIN, 30));
		showStatus.setBorderPainted(false);
		showStatus.setContentAreaFilled(false);
		showStatus.setFocusPainted(false);
		showStatus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showStatus.setVisible(false);
				closeStatus.setVisible(true);
				isshow = 0;
				show(isshow);
			}
		});
		add(showStatus);

		closeStatus.setVisible(false);
		closeStatus.setBounds(700, 50, 300, 100);
		closeStatus.setBorderPainted(false);
		closeStatus.setFont(new Font("����", Font.PLAIN, 30));
		closeStatus.setContentAreaFilled(false);
		closeStatus.setFocusPainted(false);
		closeStatus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				closeStatus.setVisible(false);
				showStatus.setVisible(true);
				isshow = 1;
				show(isshow);
			}
		});
		add(closeStatus);

		introImage = new ImageIcon(Main.class.getResource("/img/First.jpg")).getImage();
		statusImage = new ImageIcon(Main.class.getResource("/img/Status.jpg")).getImage();
	}
	
	public void mainSound(String file) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(
				new BufferedInputStream(new FileInputStream(file)));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			clip.loop(-1);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public void randomNumber() {
		Random r = new Random();

		for (int i=0; i < 3; i++) {
			this.num[i] = r.nextInt(15);
			for (int j=0; j < i; j++)
				if (this.num[i] == this.num[j])
					i--;
		}

		//System.out.println("num[0]: " + this.num[0]);
		//System.out.println("num[1]: " + this.num[1]);
		//System.out.println("num[2]: " + this.num[2]);
	}

	public void paint(Graphics g) {
		screenImage = createImage(1500, 1000);
		screenGraphic = screenImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
	}

	public void screenDraw(Graphics g) {
		if (isplus == false) {
			setSize(1000, 1000);
			g.drawImage(introImage, 0, 0, null);
		} else if (isplus == true) {
			setSize(1500, 1000);
			g.drawImage(introImage, 0, 0, null);
			g.drawImage(statusImage, 1000, 0, null);
		} else if (isminigame == true) {
			setSize(1500, 1000);
		}
		paintComponents(g);
		this.repaint();
	}

	public void show(int a) {
		if (a == 0) {
			isplus = true;
			FileReading fored = new FileReading();
			status = fored.getstatus();
			ch = fored.getch();

			Health.setText("�ǰ� " + status[0]);
			Movement.setText("�ൿ�� " + status[1]);
			Stress.setText("��Ʈ���� " + status[2]);
			Int.setText("���� " + status[3]);
			Communication.setText("�θ� " + status[4]);
			Money.setText("�� " + status[5]);
			Chingho.setText("Īȣ " + ch);

			// Health, Movement, Stress, Int, Communication, Money, Chingho
			Chingho.setVisible(false);
			Chingho.setFont(new Font("����", Font.CENTER_BASELINE, 30));
			Chingho.setForeground(Color.white);
			Chingho.setBounds(1100, 220, 900, 30);
			add(Chingho);

			Health.setVisible(false);
			Health.setFont(new Font("����", Font.CENTER_BASELINE, 30));
			Health.setForeground(Color.white);
			Health.setBounds(1100, 350, 900, 30);
			add(Health);

			Stress.setVisible(false);
			Stress.setFont(new Font("����", Font.CENTER_BASELINE, 30));
			Stress.setForeground(Color.white);
			Stress.setBounds(1100, 450, 900, 30);
			add(Stress);

			Int.setVisible(false);
			Int.setFont(new Font("����", Font.CENTER_BASELINE, 30));
			Int.setForeground(Color.white);
			Int.setBounds(1100, 550, 900, 30);
			add(Int);

			Communication.setVisible(false);
			Communication.setFont(new Font("����", Font.CENTER_BASELINE, 30));
			Communication.setForeground(Color.white);
			Communication.setBounds(1100, 650, 900, 30);
			add(Communication);

			Money.setVisible(false);
			Money.setFont(new Font("����", Font.CENTER_BASELINE, 30));
			Money.setForeground(Color.white);
			Money.setBounds(1100, 750, 900, 30);
			add(Money);

			Chingho.setVisible(true);
			Health.setVisible(true);
			Stress.setVisible(true);
			Int.setVisible(true);
			Communication.setVisible(true);
			Money.setVisible(true);
		} else {
			isplus = false;
		}
	}

	public void warnframe() {
		JFrame warn = new JFrame();
		warn.setTitle("Warning!");
		warn.setSize(600, 600);
		warn.setResizable(false);
		warn.setLocationRelativeTo(null);
		warn.setBackground(new Color(0, 0, 0, 0));
		warn.setLayout(null);

		warn.setVisible(true);
		
		JButton warning = new JButton("���� ��� ��Ʈ���� ����..");
		warning.setFont(new Font("����", Font.PLAIN, 20));
//		start.setBorderPainted(false);
//		start.setContentAreaFilled(false);
//		start.setFocusPainted(false);
		warning.setBounds(0, 200, 600, 40);
		warning.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				warn.dispose();
			}
		});
		add(warning);
	}

	public void gameED(int a) {
		usergrade.setVisible(false);
		usergrade.setFont(new Font("����", Font.CENTER_BASELINE, 30));
		usergrade.setBounds(300, 300, 800, 80);
		add(usergrade);

		noHealth.setVisible(false);
		noHealth.setFont(new Font("����", Font.CENTER_BASELINE, 30));
		noHealth.setBounds(300, 300, 800, 80);
		add(noHealth);

		fullStress.setVisible(false);
		fullStress.setFont(new Font("����", Font.CENTER_BASELINE, 30));
		fullStress.setBounds(300, 300, 800, 80);
		add(fullStress);

		lowgrade.setVisible(false);
		lowgrade.setFont(new Font("����", Font.CENTER_BASELINE, 30));
		lowgrade.setBounds(300, 300, 800, 80);
		add(lowgrade);

		hgubrain.setVisible(false);
		hgubrain.setFont(new Font("����", Font.CENTER_BASELINE, 30));
		hgubrain.setBounds(300, 300, 800, 80);
		add(hgubrain);

		hguinsider.setVisible(false);
		hguinsider.setFont(new Font("����", Font.CENTER_BASELINE, 30));
		hguinsider.setBounds(300, 300, 800, 80);
		add(hguinsider);

		worldtravel.setVisible(false);
		worldtravel.setFont(new Font("����", Font.CENTER_BASELINE, 30));
		worldtravel.setBounds(300, 300, 800, 80);
		add(worldtravel);

		hgulove.setVisible(false);
		hgulove.setFont(new Font("����", Font.CENTER_BASELINE, 30));
		hgulove.setBounds(300, 300, 800, 80);
		add(hgulove);
		
		if (a == 0) {
			lowgrade.setText("<html>����� �л����� �Ծ <br>�б� ���߿� �����ϰ� �Ǿ����ϴ�.<br></html>");
			lowgrade.setVisible(true);
		}

		if (a == 1) {
			noHealth.setText("<html>����� �ڷγ��� �ɷ��� <br>�б� ���߿� �����ϰ� �Ǿ����ϴ�.<br><html>");
			noHealth.setVisible(true);
		}

		if (a == 2) {
			fullStress.setText("<html>����� ��Ʈ������ �����Ͽ� <br>ȭ������ �����ϰ� �Ǿ����ϴ�.<br><html>");
			fullStress.setVisible(true);
		}

		if (a == 3) {
			FileReading fored = new FileReading();
			status = fored.getstatus();
			if (status[7] == 4) usergrade.setText("����� ������ A+�Դϴ�.");
			else if (status[7] == 3) usergrade.setText("����� ������ A0�Դϴ�.");
			else if (status[7] == 2) usergrade.setText("����� ������ B+�Դϴ�.");
			else if (status[7] == 1) usergrade.setText("����� ������ B0�Դϴ�.");
			else usergrade.setText("����� ������ C+�Դϴ�.");
			usergrade.setVisible(true);
		}

		if (a == 4) {
			hgubrain.setText("����� �ѵ��� �극���� �Ǿ� �б⸦ �������մϴ�.");
			hgubrain.setVisible(true);
		}

		if (a == 5) {
			hguinsider.setText("����� �ѵ��� ���νΰ� �Ǿ� �б⸦ �������մϴ�.");
			hguinsider.setVisible(true);
		}

		if (a == 6) {
			hgulove.setText("����� ������ �Ǿ� �б⸦ �������մϴ�.");
			hgulove.setVisible(true);
		}

		if (a == 7) {
			worldtravel.setText("����� �̹��б� ���� �� ��Ƽ� �ؿܿ����� ���� �˴ϴ�.");
			worldtravel.setVisible(true);
		}

		setSize(1000, 1000);
	}
}