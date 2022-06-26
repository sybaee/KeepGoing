import javax.swing.ImageIcon;

public class LeisureAction {
	private String[] leisureAction = new String[] {
    		"�ｺ�� ����", // �ǰ� U ��Ʈ���� D 0
    		"SNS�ϱ�", // ��Ʈ���� D �θ� U 1
    		"<html>���� �����ڸ���<br>����� ���� ���� �ڱ�<br><html>", // ��Ʈ���� D 2
    		"<html>�ްԽǿ���<br>ȥ�� �� �����<br><html>", // ��Ʈ���� D �� D 3
    		"���Ÿ� �����", // ��Ʈ���� D �� D 4
    		"���ϴ� ���� ���", // ��Ʈ���� D �� D 5
    		"������ ���ֱ�", // ��Ʈ���� D �� D 6
    		"���� ����", // ��Ʈ���� D �� D 7
    		"�κ긮�� ����", // ��Ʈ���� D �� D 8
    		"���� ����� ����", // ��Ʈ���� D �� D 9
    		"�� ���÷� ��� ����", // ��Ʈ���� D �� D �ǰ� D 10
    		"��ȸ ����", // ��Ʈ���� D 11
    		"�Ǿ��� ����", // ��Ʈ���� D �� D 12
    		"�̼��̶� ���å �ϱ�", // ��Ʈ���� D Īȣ ȹ�� 13
    		"�ƹ� �͵� ���ϰ� ����" // ��Ʈ���� D 14
    };
	
	private ImageIcon leisureImg1;
	private ImageIcon leisureImg2;
	private ImageIcon leisureImg3;
	
	public String getLeisureAction(int n) { return leisureAction[n]; }
	public int changefromleisure(int n) {
		if (n == 3 || n == 4 || n == 5 || n == 6 || n == 7 || n == 8 || n == 9 || n == 12) return 5; 
		else if (n == 0) return 4;
		else if (n == 1) return 3;
		else if (n == 2 || n == 11 || n == 14) return 2;
		else if (n == 10) return 0;

		return 1;
	}
	
	public ImageIcon getLeisureImg1(int n) {
		leisureImg1 = new ImageIcon(
			Main.class.getResource("/img/leisure" + Integer.toString(n) + ".jpg"));
		
		return leisureImg1;
	}
	
	public ImageIcon getLeisureImg2(int n) {
		leisureImg2 = new ImageIcon(
			Main.class.getResource("/img/leisure" + Integer.toString(n) + ".jpg"));
		
		return leisureImg2;
	}
	
	public ImageIcon getLeisureImg3(int n) {
		leisureImg3 = new ImageIcon(
			Main.class.getResource("/img/leisure" + Integer.toString(n) + ".jpg"));
		
		return leisureImg3;
	}
}
