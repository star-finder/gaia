package starfish;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.JVMReturnInstruction;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;
import star.StarChoiceGenerator;
import star.bytecode.BytecodeUtils;
import starfish.counting.ArithmeticExtractor;
import starfish.counting.ModelCounter;
import starlib.formula.Formula;

public class StarFishListener extends PropertyListenerAdapter {

	private boolean first = true;
	private boolean DEBUG = true;
	private ModelCounter mc;
	
	public StarFishListener(Config conf, JPF jpf) {
		jpf.getReporter().getPublishers().clear();
		conf.setProperty("search.multiple_errors","true");
		mc = new ModelCounter(conf);
	}
	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction,
			Instruction executedInstruction) {
		if (!vm.getSystemState().isIgnored()) {
			Config conf = vm.getConfig();
			if (first) {
				MethodInfo mi = executedInstruction.getMethodInfo();
				ClassInfo ci = mi.getClassInfo();
				if (ci != null) {
					String className = ci.getName();
					String methodName = mi.getName();
					int numberOfArgs = mi.getNumberOfArguments();

					boolean isClassSymbolic = BytecodeUtils.isClassSymbolic(conf, className, mi, methodName);
					boolean isMethodSymbolic = BytecodeUtils.isMethodSymbolic(conf, mi.getFullName(), numberOfArgs, null);
					if (isClassSymbolic || isMethodSymbolic) {
						mc.setClassAndMethoInfo(ci, mi);
						first = false;
					}
				}
			} else if (executedInstruction instanceof JVMReturnInstruction) {
				MethodInfo mi = executedInstruction.getMethodInfo();
				ClassInfo ci = mi.getClassInfo();
				if (ci != null) {
					String className = ci.getName();
					String methodName = mi.getName();
					int numberOfArgs = mi.getNumberOfArguments();

					if (((BytecodeUtils.isClassSymbolic(conf, className, mi, methodName))
							|| BytecodeUtils.isMethodSymbolic(conf, mi.getFullName(), numberOfArgs, null))) {

						ChoiceGenerator<?> cg = vm.getChoiceGenerator();
						if (!(cg instanceof StarChoiceGenerator)) {
							ChoiceGenerator<?> prevCG = cg.getPreviousChoiceGenerator();
							while (!((prevCG == null) || (prevCG instanceof StarChoiceGenerator))) {
								prevCG = prevCG.getPreviousChoiceGenerator();
							}
							cg = prevCG;
						}

						if (cg != null && cg instanceof StarChoiceGenerator
								&& ((StarChoiceGenerator) cg).getCurrentPCStar() != null) {
							// String model = Solver.getModel();
							if (DEBUG) {
								System.out.println("\n\n==========================================");
								System.out.println(((StarChoiceGenerator) cg).getCurrentPCStar());
								Formula formula = ((StarChoiceGenerator) cg).getCurrentPCStar();
								ArithmeticExtractor extractor = new ArithmeticExtractor();
								System.out.println("-------------------");
								formula.accept(extractor);
								System.out.println(extractor.getArithmeticConstraints());
								System.out.println("Number of models is " + mc.countSinglePath(formula));
								System.out.println("------------------------------------------");

							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void propertyViolated (Search search){

		VM vm = search.getVM();

			ChoiceGenerator <?>cg = vm.getChoiceGenerator();
			if (!(cg instanceof PCChoiceGenerator)){
				ChoiceGenerator <?> prev_cg = cg.getPreviousChoiceGenerator();
				while (!((prev_cg == null) || (prev_cg instanceof StarChoiceGenerator))) {
					prev_cg = prev_cg.getPreviousChoiceGenerator();
				}
				cg = prev_cg;
			}
			if ((cg instanceof PCChoiceGenerator) &&
				      ((StarChoiceGenerator) cg).getCurrentPC() != null){
				if (DEBUG) {
					System.out.println("\n\n==========================================");
					System.out.println(search.getLastError().getDetails());
					System.out.println(((StarChoiceGenerator) cg).getCurrentPCStar());
					Formula formula = ((StarChoiceGenerator) cg).getCurrentPCStar();
					ArithmeticExtractor extractor = new ArithmeticExtractor();
					System.out.println("-------------------");
					formula.accept(extractor);
					System.out.println(extractor.getArithmeticConstraints());
					System.out.println("Number of models is" + mc.countSinglePath(formula));
					System.out.println("------------------------------------------");
				}
			}
		//}
	}

	@Override
	public void searchFinished(Search search) {
		//TODO
	}
}
