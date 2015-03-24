package cl.usach.diinf.tallerbd.sa.classifier;

interface hola{
	void hola();
}

abstract class hola1{
	 void saludo(){}
}

public  class  Main extends hola1 implements hola{
	
	static Coffee co;
	
	
	boolean [] a [];
	public static void main(String[] args) {
		
		new Main().saludo();
		new Main().hola();
		
		System.out.println(co.EXPRESSO.value);
	}
	@Override
	 void hola() {
		// TODO Auto-generated method stub
		
	}

}
