package crossjoin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * do experiments using different algorithms
 *
 */
public class Experiments {
	
	/**
	 * 
	 * @param opt_alg
	 * @param indep_ratio_thres
	 * @return all ISs
	 * @throws IOException
	 */
	public static List<CJ> exe_single_exp(int opt_alg,Double indep_ratio_thres) throws IOException {
		List<String> log = new ArrayList<String>();
		List<String> res = new ArrayList<String>();
		res.add(Constant.file_add);
		res.add("alg 1 + "+opt_alg+" result : ");
		
		List<List<String>> r = null;
		List<String> R = null;
		int constant_col = -1;
		
		if(Constant.remove_constant_col) {
			List<Object> out = Utils.remove_constant_col_from_dataset(Constant.file_add,Constant.hasHeader,Constant.split,Constant.hasLastEmptyStr,Constant.col_num);
			constant_col = (Integer) out.get(0);
			R = (List<String>) out.get(1);
			r = (List<List<String>>) out.get(2);
		}else {
			List<Object> out = Utils.import_dataset_from_file(Constant.file_add,Constant.hasHeader,Constant.split,Constant.hasLastEmptyStr,Constant.col_num);
			R = (List<String>) out.get(0);
			r = (List<List<String>>) out.get(1);
		}
		
		System.out.println(Constant.file_add);
		log.add(Constant.file_add);
		System.out.println("col num : "+R.size()+" | "+"row num : "+r.size());
		log.add("col num : "+R.size()+" | "+"row num : "+r.size());
		System.out.println("alg 1 + "+opt_alg+" : ");
		log.add("alg 1 + "+opt_alg+" : ");
		
		Utils.output_exp_result(Constant.log_add, log);
		log.clear();
		Utils.output_exp_result(Constant.output_add, res);
		res.clear();
		
		long start1 = System.currentTimeMillis();
		List<CJ> alg_res = Algs.alg1(R, r, opt_alg, indep_ratio_thres);
		long end1 = System.currentTimeMillis();
		
		
		alg_res.forEach(is->System.out.println(is.toString()));
		alg_res.forEach(is->log.add(is.toString()));
		
		double cost1 = (end1 - start1)/1000.0;
		System.out.println("\ncost : "+cost1+" seconds");
		System.out.println("########");
		log.add("\ncost : "+cost1+" seconds");
		log.add("########");
		
		System.out.println("\nalg 1 + "+opt_alg+", cost : "+cost1+" | #IS : "+alg_res.size()+" | max arity : "+(alg_res.size() == 0 ? 0 : alg_res.get(alg_res.size()-1).getArity())+" | constant col : "+constant_col);
		log.add("\nalg 1 + "+opt_alg+", cost : "+cost1+" | #IS : "+alg_res.size()+" | max arity : "+(alg_res.size() == 0 ? 0 : alg_res.get(alg_res.size()-1).getArity())+" | constant col : "+constant_col);
		System.out.println("********\n\n");
		log.add("********\n\n");
		
		
		res.add(cost1+","+alg_res.size()+","+(alg_res.size() == 0 ? 0 : alg_res.get(alg_res.size()-1).getArity())+","+constant_col+"\n");
		Utils.output_exp_result(Constant.output_add, res);
		
		Utils.output_exp_result(Constant.log_add, log);
		
		return alg_res;
	}
	
	public static void exe_single_exp_with_row_efficiency(int opt_alg, double ratio, Double indep_ratio_thres) throws IOException {
		List<String> log = new ArrayList<String>();
		List<String> res = new ArrayList<String>();
		
		
		List<List<String>> r = null;
		List<String> R = null;
		int constant_col = -1;
		

		List<Object> raw_dataset = null;
		if(Constant.remove_constant_col) {
			List<Object> out = Utils.remove_constant_col_from_dataset(Constant.file_add,Constant.hasHeader,Constant.split,Constant.hasLastEmptyStr,Constant.col_num);
			constant_col = (Integer) out.get(0);
			raw_dataset = (List<Object>) out.remove(0);
		}else {
			raw_dataset = Utils.import_dataset_from_file(Constant.file_add,Constant.hasHeader,Constant.split,Constant.hasLastEmptyStr,Constant.col_num);
		}
		
		List<Object> new_dataset = Utils.generate_fixed_row_num_dataset(raw_dataset, ratio);
		R = (List<String>) new_dataset.get(0);
		r = (List<List<String>>) new_dataset.get(1);
		res.add(Constant.file_add);
		res.add("exp with row efficiency, row num : "+r.size());
		res.add("alg 1 + "+opt_alg+" result : ");
		
		
		System.out.println(Constant.file_add);
		log.add(Constant.file_add);
		System.out.println("exp with row efficiency, ratio : "+ratio);
		log.add("exp with row efficiency, ratio : "+ratio);
		System.out.println("col num : "+R.size()+" | "+"row num : "+r.size());
		log.add("col num : "+R.size()+" | "+"row num : "+r.size());
		System.out.println("alg 1 + "+opt_alg+" : ");
		log.add("alg 1 + "+opt_alg+" : ");
		
		Utils.output_exp_result(Constant.log_add, log);
		log.clear();
		Utils.output_exp_result(Constant.output_add, res);
		res.clear();
		
		long start1 = System.currentTimeMillis();
		List<CJ> alg_res = Algs.alg1(R, r, opt_alg, indep_ratio_thres);
		long end1 = System.currentTimeMillis();
		
		
		alg_res.forEach(is->System.out.println(is.toString()));
		alg_res.forEach(is->log.add(is.toString()));
		
		double cost1 = (end1 - start1)/1000.0;
		System.out.println("\ncost : "+cost1+" seconds");
		System.out.println("########");
		log.add("\ncost : "+cost1+" seconds");
		log.add("########");
		
		System.out.println("\nalg 1 + "+opt_alg+", cost : "+cost1+" | #IS : "+alg_res.size()+" | max arity : "+(alg_res.size() == 0 ? 0 : alg_res.get(alg_res.size()-1).getArity())+" | constant col : "+constant_col);
		log.add("\nalg 1 + "+opt_alg+", cost : "+cost1+" | #IS : "+alg_res.size()+" | max arity : "+(alg_res.size() == 0 ? 0 : alg_res.get(alg_res.size()-1).getArity())+" | constant col : "+constant_col);
		System.out.println("********\n\n");
		log.add("********\n\n");
		
		
		res.add(r.size()+","+cost1+","+alg_res.size()+","+(alg_res.size() == 0 ? 0 : alg_res.get(alg_res.size()-1).getArity())+","+constant_col+"\n");
		Utils.output_exp_result(Constant.output_add, res);
		
		Utils.output_exp_result(Constant.log_add, log);
	}
	
	public static void exe_single_exp_with_col_efficiency(int opt_alg,int new_col_num, Double indep_ratio_thres) throws IOException {
		List<String> log = new ArrayList<String>();
		List<String> res = new ArrayList<String>();
		res.add(Constant.file_add);
		res.add("exp with column efficiency, new col num : "+new_col_num);
		res.add("alg 1 + "+opt_alg+" result : ");
		
		List<List<String>> r = null;
		List<String> R = null;
		int constant_col = -1;
		

		List<Object> raw_dataset = null;
		if(Constant.remove_constant_col) {
			List<Object> out = Utils.remove_constant_col_from_dataset(Constant.file_add,Constant.hasHeader,Constant.split,Constant.hasLastEmptyStr,Constant.col_num);
			constant_col = (Integer) out.get(0);
			raw_dataset = (List<Object>) out.remove(0);
		}else {
			raw_dataset = Utils.import_dataset_from_file(Constant.file_add,Constant.hasHeader,Constant.split,Constant.hasLastEmptyStr,Constant.col_num);
		}
		
		List<Object> new_dataset = Utils.generate_fixed_col_num_dataset(raw_dataset, new_col_num);
		R = (List<String>) new_dataset.get(0);
		r = (List<List<String>>) new_dataset.get(1);
		
		
		System.out.println(Constant.file_add);
		log.add(Constant.file_add);
		System.out.println("exp with column efficiency, new col num : "+new_col_num);
		log.add("exp with column efficiency, new col num : "+new_col_num);
		System.out.println("col num : "+R.size()+" | "+"row num : "+r.size());
		log.add("col num : "+R.size()+" | "+"row num : "+r.size());
		System.out.println("alg 1 + "+opt_alg+" : ");
		log.add("alg 1 + "+opt_alg+" : ");
		
		Utils.output_exp_result(Constant.log_add, log);
		log.clear();
		Utils.output_exp_result(Constant.output_add, res);
		res.clear();
		
		long start1 = System.currentTimeMillis();
		List<CJ> alg_res = Algs.alg1(R, r, opt_alg, indep_ratio_thres);
		long end1 = System.currentTimeMillis();
		
		
		alg_res.forEach(is->System.out.println(is.toString()));
		alg_res.forEach(is->log.add(is.toString()));
		
		double cost1 = (end1 - start1)/1000.0;
		System.out.println("\ncost : "+cost1+" seconds");
		System.out.println("########");
		log.add("\ncost : "+cost1+" seconds");
		log.add("########");
		
		System.out.println("\nalg 1 + "+opt_alg+", cost : "+cost1+" | #IS : "+alg_res.size()+" | max arity : "+(alg_res.size() == 0 ? 0 : alg_res.get(alg_res.size()-1).getArity())+" | constant col : "+constant_col);
		log.add("\nalg 1 + "+opt_alg+", cost : "+cost1+" | #IS : "+alg_res.size()+" | max arity : "+(alg_res.size() == 0 ? 0 : alg_res.get(alg_res.size()-1).getArity())+" | constant col : "+constant_col);
		System.out.println("********\n\n");
		log.add("********\n\n");
		
		
		res.add(new_col_num+","+cost1+","+alg_res.size()+","+(alg_res.size() == 0 ? 0 : alg_res.get(alg_res.size()-1).getArity())+","+constant_col+"\n");
		Utils.output_exp_result(Constant.output_add, res);
		
		Utils.output_exp_result(Constant.log_add, log);
	}
	
	/**
	 * execute 3 kinds of query experiments
	 * @param CJ
	 * @param opt_alg
	 * @param indep_ratio_thres
	 * @param queryID
	 * @return time-costed list in increasing order for each IS in query
	 * @throws IOException
	 * @throws SQLException 
	 */
	public static void exe_single_exp_with_query() throws IOException, SQLException{
		List<CJ> IS_set = Experiments.exe_single_exp(4, 1.0);//only using alg 1+4
		List<String> log = new ArrayList<String>();//log recorder
		List<String> res = new ArrayList<String>();//result recorder
		System.out.println("\n\nquery stats:\n");
		log.add("\n\nquery stats:\n");
		res.add("\n\nquery stats:\n");
		int count = 0;
		for(CJ is : IS_set) {
			System.out.println("num : "+ count +" | IS : "+is.toString());
			log.add("num : "+ count +" | IS : "+is.toString());
			res.add("num : "+ count +" | IS : "+is.toString());
			count ++;
			List<Double> res_median = new ArrayList<Double>();
			List<Double> res_ave = new ArrayList<Double>();
			log.add("Q1,Q2,Q3");
			res.add("Q1,Q2,Q3");
			for(Integer query_id : Constant.query_ID_list) {
				List<Double> time_list_in_order = Utils.get_query_time_with_IS(is, query_id);
				double median_time = time_list_in_order.get(time_list_in_order.size()/2);
				double ave_time = 0d;
				for(Double t : time_list_in_order) {
					ave_time += t;
				}
				ave_time /= (double)time_list_in_order.size();
				res_median.add(median_time);
				res_ave.add(ave_time);
			}
			String median_opt = "";
			String ave_opt = "";
			for(int i = 0;i < res_median.size();i ++) {
				if(i != res_median.size() - 1)
					median_opt += String.format("%.4f", res_median.get(i))+",";
				else
					median_opt += String.format("%.4f", res_median.get(i));
			}
			for(int i = 0;i < res_ave.size();i ++) {
				if(i != res_ave.size() - 1)
					ave_opt += String.format("%.4f", res_ave.get(i))+",";
				else
					ave_opt += String.format("%.4f", res_ave.get(i));
			}
			log.add("median result : "+median_opt);
			log.add("average result : "+ave_opt);
			log.add("#####################\n");
			res.add("median,"+median_opt);
			res.add("average,"+ave_opt);
			res.add("#####################\n");
		}
		Utils.output_exp_result(Constant.log_add, log);
		Utils.output_exp_result(Constant.output_add, res);
	}
	
	public static void main(String[] args) throws SQLException, IOException {
		if(Constant.approx_indep_exp) {
			for(double indep_ratio_thres : Constant.indep_ratio_thres_list) {
				for(int opt_alg : Constant.opt_alg) {
					Experiments.exe_single_exp(opt_alg, indep_ratio_thres);
				}
			}
		}else if(Constant.row_efficiency_exp) {
			for(int opt_alg : Constant.opt_alg) {
				for(int i = 1;i <= Constant.row_divide;i ++) {
					double ratio = ((double)i)/Constant.row_divide;
					Experiments.exe_single_exp_with_row_efficiency(opt_alg, ratio, 1.0);
				}
			}
			
		}else if(Constant.col_efficiency_exp) {
			for(int opt_alg : Constant.opt_alg) {
				for(int i = 2;i <= Constant.col_num;i ++) {
					Experiments.exe_single_exp_with_col_efficiency(opt_alg, i, 1.0);
				}
			}
		}else if(Constant.query_exp) {
			Experiments.exe_single_exp_with_query();
		}else {
			for(int opt_alg : Constant.opt_alg) {
				Experiments.exe_single_exp(opt_alg, 1.0);
			}
		}
		
		
	}
}
