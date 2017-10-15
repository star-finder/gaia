package starfish.counting;

import starlib.StarVisitor;
import starlib.formula.Formula;
import starlib.formula.PureFormula;
import starlib.formula.heap.HeapTerm;
import starlib.formula.pure.ComparisonTerm;
import starlib.formula.pure.EqTerm;
import starlib.formula.pure.NEqTerm;
import starlib.formula.pure.PureTerm;

/*
 * This class extract Presburger arithmetic constraints
 * from an separation logic formula. It collects the constraints
 * on primitive types
 */
public class ArithmeticExtractor extends StarVisitor {
	
	StringBuilder sb = new StringBuilder();
	
	@Override
	public void visit(Formula formula) {
		formula.getPureFormula().accept(this);
	}

	@Override
	public void visit(PureFormula formula) {

		PureTerm[] pureTerms = formula.getPureTerms();

		if (pureTerms.length == 0)
			sb.append("true");
		else {
			int length = pureTerms.length;
			int count = 0;
			for (int i = 0; i < length - 1; i++) {
				if(isNumericalConstraint(pureTerms[i])){
					sb.append(pureTerms[i] + " &&\n");
					++count;
				}
			}
			if(length > 0) {
				if(isNumericalConstraint(pureTerms[length - 1])){
					sb.append(pureTerms[length - 1]);
				} else if (count > 0) {
					int len = sb.length();
					sb.delete(len - 4, len);
				}
			}
		}
	}
	public boolean isNumericalConstraint(PureTerm term) {
		return term instanceof ComparisonTerm || term instanceof EqTerm
				|| term instanceof NEqTerm;
	}
	
	public String getArithmeticConstraints() {
		return sb.toString();
	}
}
