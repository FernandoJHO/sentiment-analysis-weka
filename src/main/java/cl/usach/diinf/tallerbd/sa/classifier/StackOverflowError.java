package cl.usach.diinf.tallerbd.sa.classifier;

public class StackOverflowError {
		
	void muerte(){
		vida();
	}

	 void vida() {
		muerte();
	}
	
	 public static void main(String[] args) {
		new StackOverflowError().vida();
	}
}

