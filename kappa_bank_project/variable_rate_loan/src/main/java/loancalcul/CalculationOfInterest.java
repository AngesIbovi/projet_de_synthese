package loancalcul;

import lendingscenario.FloatingRate;
import lendingscenario.GetInformation;
import lendingscenario.Information;

public class CalculationOfInterest {
	
	
	public float repayment(float amount, float rateValue)
	{
		float interest;
	System.out.println("montant"+amount+"\n taux:"+ rateValue);
		interest= (float) (amount*((rateValue*0.01)/12));
		
		return interest;
	}

	
	public int calcul_duration(int date)
	{
		int max,min;
		if(date==1)
		{
			min=1;max=12;
		}
		else {
			min=1;max=date*12;
		}
		
		return max;
	}

}
