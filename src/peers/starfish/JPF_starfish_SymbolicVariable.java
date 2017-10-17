package starfish;

import gov.nasa.jpf.annotation.MJI;
import gov.nasa.jpf.symbc.numeric.IntegerExpression;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.NativePeer;
import starfish.variable.Domain;
import starfish.variable.GlobalVariables;

public class JPF_starfish_SymbolicVariable extends NativePeer {

	@MJI
	public static void setMinMax__III__V(MJIEnv env, int objRef, int var, int min, int max){
		Object [] attrs = env.getArgAttributes();
		IntegerExpression sym_arg = (IntegerExpression)attrs[0];
		if (sym_arg !=null){
			GlobalVariables.domains.put(attrs[0].toString(), new Domain(min,max));
		}
	}
}
