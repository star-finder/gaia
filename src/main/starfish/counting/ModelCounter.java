package starfish.counting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.MethodInfo;
import starfish.variable.Domain;
import starfish.variable.GlobalVariables;
import starlib.formula.Formula;
import starlib.jpf.PathFinderUtils;

public class ModelCounter {
	
	protected Config conf = null;
	protected ClassInfo ci = null;
	protected MethodInfo mi = null;
	
	public ModelCounter(Config conf) {
		this.conf = conf;
	}
	
	public void setClassAndMethoInfo(ClassInfo ci, MethodInfo mi) {
		this.ci = ci;
		this.mi = mi;
	}

	public void countSinglePath(Formula formula) {
		HashMap<String, String> knownTypeVars = PathFinderUtils.initTypeVarMap(ci, mi);
		formula.updateType(knownTypeVars);
		knownTypeVars.forEach((name,type)->{
			if(type.equals("int"))
				System.out.println(name + ":" + type);
		});
		ArrayList<String> vars = new ArrayList<String>();
		for(Entry<String,String> entry : knownTypeVars.entrySet()) {
			if(entry.getValue().equals("int")) { // TODO: generalize to all numeric types ???
				vars.add(entry.getKey());
			}
		}
		System.out.println(createUserProfileString(vars));
	}
	
	public String createUserProfileString(ArrayList<String> vars){
		StringBuilder sb = new StringBuilder();
		sb.append("domain{\n");

		int MIN = conf.getInt("symbolic.min_int", Integer.MIN_VALUE);
		int MAX = conf.getInt("symbolic.max_int", Integer.MAX_VALUE);
		for(String var : vars) {
			// first check if the domain is in the global variable
			if(GlobalVariables.domains.containsKey(var)){
				Domain domain = GlobalVariables.domains.get(var);
				sb.append("\t" + var + " : " + domain.min + "," + domain.max + ";\n");
			} else{
				sb.append("\t" + var + " : " + MIN + "," + MAX + ";\n");
			}
		}

		sb.append("};\n\n");
		sb.append("usageProfile{\n\t");

		int count = 0;
		int size = vars.size();
		for(String var : vars) {
			sb.append(var + "==" + var);
			count++;
			if (count < size)
				sb.append(" && ");
			
		}
		sb.append(" : 100/100;\n};");
		return sb.toString();
	}
}
