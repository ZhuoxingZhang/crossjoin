package crossjoin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CJ means Cross Join in form of {X,Y}
 *
 */
public class CJ {
	private List<String> X;
	private List<String> Y;
	private int arity;//arity = |X U Y|
	private double indep_ratio = 0d;//independent ratio = |r(XY)|/|r(X)|*|r(Y)|
	
	public CJ() {
		X = new ArrayList<String>();
		Y = new ArrayList<String>();
		arity = 0;
		indep_ratio = 0;
	}
	
	public CJ(List<String> X, List<String> Y, int arity) {
		this.X = X;
		this.Y = Y;
		this.arity = arity;
	}
	
	public CJ(List<String> X, List<String> Y, double indep_ratio) {
		this.X = X;
		this.Y = Y;
		this.indep_ratio = indep_ratio;
	}
	
	public CJ(List<String> X, List<String> Y) {
		List<String> Y_temp = new ArrayList<String>();//remove common attributes in X and Y from Y
		Y_temp.addAll(Y);
		for(String attr : Y_temp) {
			if(X.contains(attr))
				Y.remove(attr);
		}
		this.X = X;
		this.Y = Y;
		this.arity = X.size() + Y.size();
	}

	public List<String> getX() {
		return X;
	}

	public void setX(List<String> x) {
		X = x;
	}

	public List<String> getY() {
		return Y;
	}

	public void setY(List<String> y) {
		Y = y;
	}

	public int getArity() {
		return arity;
	}

	public void setArity(int arity) {
		this.arity = arity;
	}
	
	

	public double getIndep_ratio() {
		return indep_ratio;
	}

	public void setIndep_ratio(double indep_ratio) {
		this.indep_ratio = indep_ratio;
	}


	@Override
	public String toString() {
		return "IS [X=" + X + ", Y=" + Y + ", arity=" + arity + ", indep_ratio=" + indep_ratio + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CJ) {
			CJ is1 = (CJ)obj;
//			if((is1.getX().equals(this.X) && is1.getY().equals(this.Y)) || (is1.getX().equals(this.Y) && is1.getY().equals(this.X))) {
			if((is1.getX().containsAll(this.X) && is1.getX().size() == this.X.size() && is1.getY().containsAll(this.Y) && is1.getY().size() == this.Y.size())
					|| (is1.getX().containsAll(this.Y) && is1.getX().size() == this.Y.size() && is1.getY().containsAll(this.X) && is1.getY().size() == this.X.size())) {
				return true;
			}else
				return false;
		}else
			return false;
	}
	

	
	
}
