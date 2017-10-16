package starfish.counting;

import java.util.HashMap;

import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.MethodInfo;
import starlib.formula.Formula;
import starlib.jpf.PathFinderUtils;

public class ModelCounter {

	public static void countSinglePath(Formula formula, ClassInfo ci, MethodInfo mi) {
		HashMap<String, String> knownTypeVars = PathFinderUtils.initTypeVarMap(ci, mi);
		formula.updateType(knownTypeVars);
		knownTypeVars.forEach((name,type)->{
			if(type.equals("int"))
				System.out.println(name + ":" + type);
		});
	}
}
