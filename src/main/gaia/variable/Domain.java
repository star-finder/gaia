package gaia.variable;

public class Domain {
	public int min;
	public int max;
	
	public Domain(int min, int max){
		if(min > max){
			//TODO: better message handling
			assert false;
		}
		this.min = min;
		this.max = max;
	}
}
