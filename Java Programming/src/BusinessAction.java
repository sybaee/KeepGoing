import javax.swing.ImageIcon;
//�ǰ�, �ൿ��, ��Ʈ����, ����, �θ�, ��, ���� ��, ���ھ� ����
public class BusinessAction {
	private String[] businessAction = new String[] {
			"��� ����",// �� D �θ� U 0
        	"<html>��� �Ȱ���<br>������ ��� Ÿ��<br><html>", // �θ� D 1
        	"���� �غ��ϱ�", // �� D �θ� U 2
        	"��CC �̳� ������ �ϱ�", // �� D �θ� U 3
        	"<html>��� �����<br>���ϳ� �ٻڴٰ� ����ϱ�<br><html>", // �θ� D 4
        	"������ �� ä�� ����", // �θ� U 5
        	"������ ��ü���", // �θ� D 6
        	"��Ư�� ���� ����", // �θ� U 7
        	"���Ƹ� ����", // �θ� U 8
        	"<html>���Ƹ� ���� �����ϰ�<br>����翡�� ����<br><html>", // �θ� D 9
        	"�� ������ ����",// �θ� U �� D 10
        	"�� ������ �� ����", // �θ� D 11
        	"�Ƹ�����Ʈ �ϱ�", // �� UU �θ� U �ǰ� D 12
        	"<html>�Ƹ�����Ʈ �� ���� <br>©����<br><html>", // �θ� D �� D 13
        	"��������̶� �뷡�� ����" // �� D �θ� U 14
    };
	
	private ImageIcon businessImg1;
	private ImageIcon businessImg2;
	private ImageIcon businessImg3;
	
	public String getBusinessAction(int n) { return businessAction[n]; }
	public int changefrombusiness(int n) {
		if(n==0 || n==2 || n==3 || n==10 || n==14) return 4; // �θ� U �� D
		else if(n==1 || n==4 || n==6 || n==9 || n==11) return 3; // �θ� D
		else if(n==12) return 2; // �� UU �θ� U
		else if(n==13) return 1; // �� D �θ� D
		return 0; // �θ� U
	}
	
	public ImageIcon getBusinessImg1(int n) {
		businessImg1 = new ImageIcon(
			Main.class.getResource("/img/business" + Integer.toString(n) + ".jpg"));
		
		return businessImg1;
	}
	
	public ImageIcon getBusinessImg2(int n) {
		businessImg2 = new ImageIcon(
			Main.class.getResource("/img/business" + Integer.toString(n) + ".jpg"));
		
		return businessImg2;
	}
	
	public ImageIcon getBusinessImg3(int n) {
		businessImg3 = new ImageIcon(
			Main.class.getResource("/img/business" + Integer.toString(n) + ".jpg"));
		
		return businessImg3;
	}
}