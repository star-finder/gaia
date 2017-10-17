package starfish.counting;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.MethodInfo;
import modelcounting.analysis.SequentialAnalyzerBarvinok;
import modelcounting.domain.ProblemSetting;
import modelcounting.utils.Configuration;
import starfish.util.FileUtils;
import starlib.formula.Formula;
import starlib.jpf.PathFinderUtils;

public class ModelCounter {
	
	protected Config conf = null;
	protected ClassInfo ci = null;
	protected MethodInfo mi = null;
	protected ProblemSetting problemSettings;
	protected Configuration configuration; 
	
	public ModelCounter(Config conf) {
		this.conf = conf;
		configuration = new Configuration();
		configuration.setTemporaryDirectory(conf
				.getProperty("star.tmpDir"));
		configuration.setOmegaExectutablePath(conf
				.getProperty("star.omegaPath"));
		configuration.setLatteExecutablePath(conf
				.getProperty("star.lattePath"));
		configuration.setIsccExecutablePath(conf
				.getProperty("star.barvinokPath"));

		problemSettings = null;
	}
	
	public void setClassAndMethoInfo(ClassInfo ci, MethodInfo mi) {
		this.ci = ci;
		this.mi = mi;
	}

	public BigInteger countSinglePath(Formula formula) {
		BigInteger result = new BigInteger("-1");
		HashMap<String, String> knownTypeVars = PathFinderUtils.initTypeVarMap(ci, mi);
		
		// Collect all symbolic variables
		formula.updateType(knownTypeVars);
		ArrayList<String> vars = new ArrayList<String>();
		for(Entry<String,String> entry : knownTypeVars.entrySet()) {
			if(entry.getValue().equals("int")) { // TODO: generalize to all numeric types ???
				vars.add(entry.getKey());
			}
		}
		
		// Get the numeric part of the path condition
		ArithmeticExtractor extractor = new ArithmeticExtractor();
		formula.accept(extractor);
		String pc = extractor.getArithmeticConstraints();
		
		// Create user profile, then count
		try {
			File problem = FileUtils.writeToFile(createUserProfileString(vars));
			problemSettings = ProblemSetting.loadFromFile(problem.getAbsolutePath());
			SequentialAnalyzerBarvinok	analyzerBarvinok = new SequentialAnalyzerBarvinok(configuration,
					problemSettings.getDomain(),
					problemSettings.getUsageProfile(), 1);
			result = analyzerBarvinok.countPointsOfPC(pc).bigIntegerValue();
			analyzerBarvinok.terminate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String createUserProfileString(ArrayList<String> vars){
		StringBuilder sb = new StringBuilder();
		sb.append("domain{\n");

		int MIN = conf.getInt("symbolic.min_int", Integer.MIN_VALUE);
		int MAX = conf.getInt("symbolic.max_int", Integer.MAX_VALUE);
		for(String var : vars) {
			/* first check if the domain is in the global variable
			if(GlobalVariables.domains.containsKey(var)){
				Domain domain = GlobalVariables.domains.get(var);
				sb.append("\t" + var + " : " + domain.min + "," + domain.max + ";\n");
			} else{
				sb.append("\t" + var + " : " + MIN + "," + MAX + ";\n");
			}
			//*/
			// Sang: setting domains for dynamically allocated variables is tricky
			sb.append("\t" + var + " : " + MIN + "," + MAX + ";\n");
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
	
	public BigInteger countSinglePathBarvinok(String pc) {
		BigInteger result = new BigInteger("-1");

		try {
			SequentialAnalyzerBarvinok	analyzerBarvinok = new SequentialAnalyzerBarvinok(configuration,
					problemSettings.getDomain(),
					problemSettings.getUsageProfile(), 1);
			result = analyzerBarvinok.countPointsOfPC(pc).bigIntegerValue();
			analyzerBarvinok.terminate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
