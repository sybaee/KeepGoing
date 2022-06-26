import javax.swing.ImageIcon;

public class Recreationquiz {
	private String[] RecreationQuiz = new String[] {
    		"�䳢�� �Ϸ翡 18�� ������ �ܴ�.",
    		"���̵� ������ ���� ����� �Ѵ�.",
    		"�����̴� �ܸ��� ���� �� �ִ�.",
    		"�̱��� ����ɰ� �δ������ ���� ���� ������ �� �� ����.",
    		"���� �������� ���ʷ� ���� ����� ���ѹα��̴�.",
    		"���迡�� ���ʷ� �ýð� ������ ���� �����̴�.",
    		"������ ������ �¾翡�� ���� ������ �ܿ￡�� ���� �ִ�.",
    		"������ ������ �㿡 �� ���� �� �ִ�.",
    		"���� �¾��� �������� �¾纸�� �� �Ӵ�.",
    		"���濡�� ���� ���� �� �ǵμ����� ���̴� 969���̾���.",
    		"����, ����, ����, ����, �ٿ��� ��� �������� 12�����̴�.",
    		"���º������� �Ʊ⿹���� ������ �� ����ڻ�� 3���̴�.",
    		"one more question?"
    };
	
	private String[] RecreationChoices = new String[] { "O", "X" };
  
    private int[] RecreationAnswers = new int[] {
    	0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0
    };
    
    private ImageIcon resultQuizImg1 = new ImageIcon(
		Main.class.getResource("/img/resultQuiz1.jpg"));
	private ImageIcon resultQuizImg2 = new ImageIcon(
		Main.class.getResource("/img/resultQuiz2.jpg"));
    
    public String getRecreationQuiz(int n) { return RecreationQuiz[n]; }
    public String getRecreationChoices(int n) { return RecreationChoices[n]; }
    public int getRecreationAnswer(int n) { return RecreationAnswers[n]; }
	
	public ImageIcon getResultQuizImg1() { return resultQuizImg1; }
	public ImageIcon getResultQuizImg2() { return resultQuizImg2; }
}