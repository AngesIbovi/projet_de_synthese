package loancalcul;

import lendingscenario.GetInformation;
import lendingscenario.Information;

public class LoanCalculation {
	
	public float loan( float capital,int nbr_month)
	{
		float rest;
		float payment=0;
		
		payment=(float)capital/(nbr_month);
		return payment;
	}
	
	
	public float restCapital(float capital,int delay){
		float rest;
		float payment=0;
		
		payment=(float)capital/(delay*12);
		
		
		return (capital-payment*12);
		
	}
	

}
