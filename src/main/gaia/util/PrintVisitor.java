package gaia.util;

import starlib.StarVisitor;
import starlib.formula.Formula;
import starlib.formula.HeapFormula;
import starlib.formula.PureFormula;
import starlib.formula.heap.HeapTerm;
import starlib.formula.heap.PointToTerm;
import starlib.formula.pure.ComparisonTerm;
import starlib.formula.pure.EqNullTerm;
import starlib.formula.pure.EqTerm;
import starlib.formula.pure.NEqNullTerm;
import starlib.formula.pure.NEqTerm;
import starlib.formula.pure.PureTerm;

public class PrintVisitor extends StarVisitor{
	
	@Override
	public void visit(ComparisonTerm term) {
		System.out.println(">>>>> ComparisonTerm: " + term.toString());
	}
	
	@Override
	public void visit(HeapTerm term) {
		System.out.println(">>>>> HeapTerm: " + term.toS2SATString());
	}
	
	@Override
	public void visit(PointToTerm term) {
		System.out.println(">>>>> PointToTerm: " + term.toS2SATString());
	}
	
	@Override
	public void visit(Formula formula) {
		System.out.println(">>>>> Formula: " + formula.toS2SATString());
		formula.getPureFormula().accept(this);
		formula.getHeapFormula().accept(this);
	}
	
	@Override
	public void visit(HeapFormula formula) {
		System.out.println(">>>>> HeapFormula: " + formula.toS2SATString());
		for(HeapTerm term: formula.getHeapTerms()) {
			term.accept(this);
		}
	}
	
	@Override
	public void visit(PureFormula formula) {
		System.out.println(">>>>> PureFormula: " + formula.toS2SATString());
		for(PureTerm term: formula.getPureTerms()) {
			term.accept(this);
		}
	}
	
	// TODO: delete
	
	@Deprecated
	public void visit(NEqTerm term) {
		System.out.println(">>>>> NEqTerm: " + term.toS2SATString());
	}
	
	@Deprecated
	public void visit(EqNullTerm term) {
		System.out.println(">>>>> EqNullTerm: " + term.toS2SATString());
	}
	
	@Deprecated
	public void visit(NEqNullTerm term) {
		System.out.println(">>>>> NEqNullTerm: " + term.toS2SATString());
	}
	
	@Deprecated
	public void visit(EqTerm term) {
		System.out.println(">>>>> EqTerm: " + term.toS2SATString());
	}

}
