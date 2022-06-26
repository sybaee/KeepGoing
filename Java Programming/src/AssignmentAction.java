import javax.swing.ImageIcon;

public class AssignmentAction {	
	private String[] assignmentAction = new String[] {
    		"���� �ٷ� �ϱ�",
    		"�翬�ϰ� �ϴ� �̷��"
    };
		
	private ImageIcon assignmentImg1 = new ImageIcon(
		Main.class.getResource("/img/assignment0.jpg"));
	private ImageIcon assignmentImg2 = new ImageIcon(
		Main.class.getResource("/img/assignment1.jpg"));
	
	public String getAssignmentAction(int n) { return assignmentAction[n]; }
	
	public ImageIcon getAssignmentImg1() { return assignmentImg1; }
	public ImageIcon getAssignmentImg2() { return assignmentImg2; }
}
