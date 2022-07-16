package crossjoin;

import java.util.Arrays;
import java.util.List;

public class Constant {
	public final static String root_path = "C:\\Users";
	public final static String output_add = root_path + "\\output\\exp_res.txt";
	public final static String log_add = root_path + "\\output\\exp_log.txt";
	public final static boolean hasHeader = false;
	
	public final static boolean remove_constant_col = false;//experiments : remove columns with constant values
	public final static boolean drop_null_marker_tuple = false;//experiments : drop tuples with null marker when computing |r(X)|
	public final static List<Integer> opt_alg = Arrays.asList(4);//start algorithm 1 with subroutine 2, 3, 4
	
	public final static boolean approx_indep_exp = false;//experiments : Approximate Independence experiments
	public final static List<Double> indep_ratio_thres_list = Arrays.asList(0.95);//experiments : Approximate Independence Ratio Threshold
	
	public final static boolean col_efficiency_exp = false;//experiments : execute column efficiency experiments
	
	public final static boolean row_efficiency_exp = false;//experiments : execute row efficiency experiments
	public final static int row_divide = 26;//just used when execute row efficiency experiment 1/26, 2/26,..., 1
	
	public final static boolean query_exp = true;//experiments : execute query experiments only using alg 1+4 and independent ratio=1
	public final static List<Integer> query_ID_list = Arrays.asList(1,2,3);
	public final static int query_exp_time = 5;//execution time for query experiments
	

	
//	public final static String dataset_name = "iris";
//	public final static int col_num = 5;
//	public final static String split = ",";
//	public final static String nullMarker = null;
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
	public final static String dataset_name = "balance";
	public final static int col_num = 5;
	public final static String split = ",";
	public final static String nullMarker = null;
	public final static boolean hasLastEmptyStr = false;
	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
//	public final static String dataset_name = "chess";
//	public final static int col_num = 7;
//	public final static String split = ",";
//	public final static String nullMarker = null;
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
//	public final static String dataset_name = "abalone";
//	public final static int col_num = 9;
//	public final static String split = ",";
//	public final static String nullMarker = null;
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
//	public final static String dataset_name = "nursery";
//	public final static int col_num = 9;
//	public final static String split = ",";
//	public final static String nullMarker = null;
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
//	public final static String dataset_name = "breast";
//	public final static int col_num = 11;
//	public final static String split = ",";
//	public final static String nullMarker = "?";
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
//	public final static String dataset_name = "bridges";
//	public final static int col_num = 13;
//	public final static String split = ",";
//	public final static String nullMarker = "?";
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
//	public final static String dataset_name = "echo";
//	public final static int col_num = 13;
//	public final static String split = ",";
//	public final static String nullMarker = "?";
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
//	public final static String dataset_name = "adult";
//	public final static int col_num = 15;
//	public final static String split = ";";
//	public final static String nullMarker = null;
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
//	public final static String dataset_name = "letter";
//	public final static int col_num = 17;
//	public final static String split = ",";
//	public final static String nullMarker = null;
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
//	public final static String dataset_name = "ncvoter";
//	public final static int col_num = 19;
//	public final static String split = ",";
//	public final static String nullMarker = "\"\"";
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
//	public final static String dataset_name = "hepatitis";
//	public final static int col_num = 20;
//	public final static String split = ",";
//	public final static String nullMarker = "?";
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
//	public final static String dataset_name = "horse";
//	public final static int col_num = 28;
//	public final static String split = ";";
//	public final static String nullMarker = "?";
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
//	public final static String dataset_name = "fd-red";
//	public final static int col_num = 30;
//	public final static String split = ",";
//	public final static String nullMarker = null;
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
//	
//	public final static String dataset_name = "plista";
//	public final static int col_num = 63;
//	public final static String split = ";";
//	public final static String nullMarker = "";
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
//	public final static String dataset_name = "flight";
//	public final static int col_num = 109;
//	public final static String split = ";";
//	public final static String nullMarker = "\"\"";
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";
	
//	public final static String dataset_name = "uniprot";
//	public final static int col_num = 223;
//	public final static String split = ",";
//	public final static String nullMarker = "\"\"";
//	public final static boolean hasLastEmptyStr = false;
//	public final static String file_add = root_path + "\\all_datasets\\"+dataset_name+".csv";

}
