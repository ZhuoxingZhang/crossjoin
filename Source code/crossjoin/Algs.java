package crossjoin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * implement 4 algorithms
 *
 */
public class Algs {
	
	public static List<CJ> alg1(List<String> R, List<List<String>> r,int usingAlg,Double indep_ratio_thres) throws IOException {
		List<CJ> MPCOVER = new ArrayList<CJ>();
		Map<Set<String>,Integer> cache = new HashMap<Set<String>,Integer>();//cache size of sub-relation
		
		int arity = 2;
		List<CJ> CANDIDATES = Utils.computeCandidateISs_only_arity_2(R);
		
		
		while(!CANDIDATES.isEmpty()) {
			long start = System.currentTimeMillis();
			List<CJ> CANDIDATES_TEMP = new ArrayList<CJ>();
			CANDIDATES_TEMP.addAll(CANDIDATES);
//			int count = 1;
			for(CJ is : CANDIDATES_TEMP) {
//				System.out.println("current process : "+count ++ +"/"+CANDIDATES_TEMP.size());
				List<String> X = is.getX();
				List<String> Y = is.getY();
				List<String> XY  = new ArrayList<String>();
				for(String attr : R) {
					if(X.contains(attr)) {
						XY.add(attr);
						continue;
					}
					if(Y.contains(attr)) {
						XY.add(attr);
						continue;
					}
				}
				
				//cache set X's |r(X)|
				Set<String> X_set = new HashSet<String>(X);
				Set<String> Y_set = new HashSet<String>(Y);
				Set<String> XY_set = new HashSet<String>(XY);
				int r_X_size = -1;
				int r_Y_size = -1;
				int r_XY_size = -1;
				if(cache.containsKey(X_set)) {
					r_X_size = cache.get(X_set);
				}else {
					r_X_size = Utils.getDistictRowCountOfSubRelation(r, R, X);
					cache.put(X_set, r_X_size);
				}
				if(cache.containsKey(Y_set)) {
					r_Y_size = cache.get(Y_set);
				}else {
					r_Y_size = Utils.getDistictRowCountOfSubRelation(r, R, Y);
					cache.put(Y_set, r_Y_size);
				}
				if(cache.containsKey(XY_set)) {
					r_XY_size = cache.get(XY_set);
				}else {
					r_XY_size = Utils.getDistictRowCountOfSubRelation(r, R, XY);
					cache.put(XY_set, r_XY_size);
				}
				
				double indep_ratio = ((double)r_XY_size)/(r_X_size*r_Y_size);
				double final_thres = 2;
				
				
				if(Constant.approx_indep_exp)
					final_thres = indep_ratio_thres;
				else
					final_thres = 1.0;
				
				if(indep_ratio >= final_thres) {
					is.setIndep_ratio(indep_ratio);//set each pair independent ratio
					if(!MPCOVER.contains(is))
						MPCOVER.add(is);
					for(String A : X) {
						List<String> X_without_A = new ArrayList<String>();
						X_without_A.addAll(X);
						X_without_A.remove(A);
						CJ is1 = new CJ(X_without_A,Y);
						if(MPCOVER.contains(is1))
							MPCOVER.remove(is1);
						
					}
					for(String B : Y) {
						List<String> Y_without_B = new ArrayList<String>();
						Y_without_B.addAll(Y);
						Y_without_B.remove(B);
						CJ is1 = new CJ(X, Y_without_B);
						if(MPCOVER.contains(is1))
							MPCOVER.remove(is1);
					}
				}else {
					CANDIDATES.remove(is);
				}
			}
			
			
			
			if(CANDIDATES.isEmpty() || Utils.hasISOfSpecificArity(CANDIDATES, R.size())) {
				long end = System.currentTimeMillis();
				String info = "arity level : "+arity+" | |MPCOVER| : "+MPCOVER.size()+" | next |CANDIDATES| : "+CANDIDATES.size()+" | cost : "+(end-start)/1000.0+" seconds";
				System.out.println(info);
				Utils.output_exp_result(Constant.log_add, Arrays.asList(info));
				Utils.output_exp_result(Constant.output_add, Arrays.asList(info));
				return MPCOVER;
			}else {
				switch(usingAlg) {
				case 2:
					CANDIDATES = alg2(R, CANDIDATES);
					break;
				case 3:
					CANDIDATES = alg3(R, CANDIDATES);
					break;
				case 4:
					CANDIDATES = alg4(R, CANDIDATES);
					break;
				default:
					CANDIDATES = null;
					break;
				}
				
			}
			
			long end = System.currentTimeMillis();
			String info = "arity level : "+arity+" | |MPCOVER| : "+MPCOVER.size()+" | next |CANDIDATES| : "+CANDIDATES.size()+" | cost : "+(end-start)/1000.0+" seconds";
			System.out.println(info);
			Utils.output_exp_result(Constant.log_add, Arrays.asList(info));
			Utils.output_exp_result(Constant.output_add, Arrays.asList(info));
			
			arity ++;
		}
		return MPCOVER;
	}
	
	public static List<CJ> alg2(List<String> R, List<CJ> CANDIDATES){
		return Utils.computeCandidateISs(R, CANDIDATES);
	}
	
	public static List<CJ> alg3(List<String> R, List<CJ> CANDIDATES){
		List<CJ> NEW_CANDIDATES = new ArrayList<CJ>();
		List<CJ> Temp = Utils.computeCandidateISs(R, CANDIDATES);
		for(CJ is : Temp) {
			List<String> X = is.getX();
			List<String> Y = is.getY();
			boolean IsFalsified = false;
			
			for(String A : X) {
				List<String> X_without_A = new ArrayList<String>();
				X_without_A.addAll(X);
				X_without_A.remove(A);
				CJ is1 = new CJ(X_without_A, Y);
				if(!CANDIDATES.contains(is1) && !X_without_A.isEmpty()) {
					IsFalsified = true;
					break;
				}
			}
			if(IsFalsified == false) {
				for(String B : Y) {
					List<String> Y_without_B = new ArrayList<String>();
					Y_without_B.addAll(Y);
					Y_without_B.remove(B);
					CJ is1 = new CJ(X, Y_without_B);
					if(!CANDIDATES.contains(is1) && !Y_without_B.isEmpty()) {
						IsFalsified = true;
						break;
					}
				}
			}
			
			
			if(IsFalsified == false) {
				if(!NEW_CANDIDATES.contains(is))
					NEW_CANDIDATES.add(is);
			}
		}
		return NEW_CANDIDATES;
	}
	
	public static List<CJ> alg4(List<String> R, List<CJ> CANDIDATES){
		List<CJ> NEW_CANDIDATES = new ArrayList<CJ>();
		for(CJ is : CANDIDATES) {
			List<String> X1 = is.getX();
			List<String> X2 = is.getY();
			List<List<String>> X = new ArrayList<List<String>>();
			X.add(new ArrayList<String>());
			X.add(X1);X.add(X2);
			for(int i = 1;i <= 2;i ++) {
				for(CJ is_specific_ele : Utils.get_all_ISs_with_specific_Ele(X.get(i), CANDIDATES)) {
					List<String> Xi = X.get(i);
					List<String> Y = is_specific_ele.getX().equals(Xi) ? is_specific_ele.getY() : is_specific_ele.getX();
					List<String> X_3_minus_i_Un_Y = Utils.union_in_order_of_R(R, X.get(3-i), Y);
					CJ cand_is = new CJ(Xi, X_3_minus_i_Un_Y);
					
					List<String> Y_without_X_3_minus_i = new ArrayList<String>();
					Y_without_X_3_minus_i.addAll(Y);
					for(String a : X.get(3-i)) {
						Y_without_X_3_minus_i.remove(a);
					}
					
					if(NEW_CANDIDATES.contains(cand_is) || Y_without_X_3_minus_i.size() != 1)
						continue;
					else {
						boolean IsFalsified = false;
						for(String A : X.get(3-i)) {// A \in X_3-i
							List<String> X_3_minus_i_Un_Y_without_A = new ArrayList<String>();
							X_3_minus_i_Un_Y_without_A.addAll(X_3_minus_i_Un_Y);
							X_3_minus_i_Un_Y_without_A.remove(A);
							CJ cand_is_2 = new CJ(Xi,X_3_minus_i_Un_Y_without_A);
							if(!CANDIDATES.contains(cand_is_2) && !X_3_minus_i_Un_Y_without_A.isEmpty()) {
								IsFalsified = true;
								break;
							}
						}
//						for(String A : X.get(i)) {//A \in X_i
//							List<String> X_i_without_A = new ArrayList<String>();
//							X_i_without_A.addAll(X.get(i));
//							X_i_without_A.remove(A);
//							IS cand_is_3 = new IS(X_i_without_A,X_3_minus_i_Un_Y);
//							if(!CANDIDATES.contains(cand_is_3) && !X_i_without_A.isEmpty()) {
//								IsFalsified = true;
//								break;
//							}
//						}
						if(IsFalsified == false) {
							NEW_CANDIDATES.add(cand_is);
						}
					}
				}
			}
		}
		return NEW_CANDIDATES;
	}
	
	

}
