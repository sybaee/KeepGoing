import javax.swing.ImageIcon;

public class StudyAction {	
	private String[] studyAction = new String[] {
    		"<html>�� �ð��� �Ͼ��<br>��������<br><html>", // ���� U 0
    		"�����ϱ�", // ���� D ��Ʈ���� U 1
    		"��ü �ް�", // ��Ʈ���� D ���� D 2
    		"���� �ϱ�", // ��Ʈ���� U ���� U 3
    		"���� °��", // ��Ʈ���� D ���� D �θ� D 4
    		"��ħ �����ϱ�", // ���� U ��Ʈ���� U 5 
    		"<html>��ħ�� �Ͼ��<br>�����ϰڴٰ� �����ϰ�<br>��~ �Ͼ��<br></html>", // ��Ʈ���� U ���� D 6
    		"��� �����ϱ�", // �ǰ� D ���� U ��Ʈ���� U 7
    		"<html>�����ϰڴ� ������ �ϰ�<br>�ްԽǿ��� �߽� �԰�<br>ģ�����̶� ���<br><html>", // ��, ���� D �ǰ� D 8
    		"������ ���� �����ϱ�", // ���� U ��Ʈ���� U 9
    		"<html>������ ���ٰ�<br>�׳� ���ƿ���<html>", // ���� D 10
    		"<html>������ ����<br>�����ٸ�����<br>ġŲ�� �԰� ���ƿ���<br><html>", // ��, ���� D �ǰ� D 11
    		"���͵� �׷� ����", // ���� U ��Ʈ���� U 12
    		"TA ���� ����", // ���� U ��Ʈ���� U 13
    		"���� �Ϸ� �׳� ����" // ��Ʈ���� D ���� D 14
    };
	
	private ImageIcon studyImg1;
	private ImageIcon studyImg2;
	private ImageIcon studyImg3;
	
	public String getStudyAction(int n) { return studyAction[n]; }
	public int changestatus(int n) {
		if (n == 0) return 6; // ���� ���� U
		else if (n == 1 || n == 10) return 5; // ���� ���� D
		else if (n == 2 || n == 6  || n == 14) return 4; // ��Ʈ����, ���� D
		else if (n == 3 || n == 13 || n == 5 || n == 9 || n == 12) return 3; // ��Ʈ����, ���� U
		else if (n == 7) return 2; // �ǰ� D, ���� U ��Ʈ���� U
		else if (n == 4) return 0; // ��Ʈ���� ���� �θ� D

		return 1; // n=8, 11 �� ��� ��, ����, �ǰ� D
	}
	
	public ImageIcon getStudyImg1(int n) {
		studyImg1 = new ImageIcon(
			Main.class.getResource("/img/study" + Integer.toString(n) + ".jpg"));

		return studyImg1;
	}
	
	public ImageIcon getStudyImg2(int n) {
		studyImg2 = new ImageIcon(
			Main.class.getResource("/img/study" + Integer.toString(n) + ".jpg"));
		
		return studyImg2;
	}
	
	public ImageIcon getStudyImg3(int n) {
		studyImg3 = new ImageIcon(
			Main.class.getResource("/img/study" + Integer.toString(n) + ".jpg"));
		
		return studyImg3;
	}
}