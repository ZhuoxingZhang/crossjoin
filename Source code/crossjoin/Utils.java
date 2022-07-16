package crossjoin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * all tools for experiments
 *
 */

public class Utils {
	/**
	 * databases connections
	 * @return
	 */
	public static Connection connectDB() {
		String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
	    String DB_URL = "jdbc:mysql://localhost:3306/freeman?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
	    		+ "&useServerPrepStmts=true&cachePrepStmts=true&rewriteBatchedStatements=true";
	    String USER = "root";
	    String PASS = "zzxzzx";
	    Connection conn = null;
        try{
            Class.forName(JDBC_DRIVER);
        
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        return conn;

	}
	
	/**
	 * according to relation schema R and parameter arity, generate all candidate ISs
	 * with specific arity
	 * @param R
	 * @param arity
	 * @return	a list of IS
	 */
	public static List<CJ> computeCandidateISs_only_arity_2(List<String> R) {
		List<CJ> IS_list = new ArrayList<CJ>();
		for(int i = 0;i < R.size() - 1;i ++) {
			for(int j = i + 1;j < R.size();j ++) {
				CJ is = new CJ(Arrays.asList(R.get(i)), Arrays.asList(R.get(j)));
				IS_list.add(is);
			}
		}
		
		return IS_list;
	}
	
	public static List<CJ> computeCandidateISs(List<String> R, List<CJ> CANDIDATES) {
		List<CJ> NEW_CANDIDATES = new ArrayList<CJ>();
		for(CJ is : CANDIDATES) {
			List<String> X = is.getX();
			List<String> Y = is.getY();
			List<String> xy_union = union(R,X,Y);
			List<String> exclusion = new ArrayList<String>();
			exclusion.addAll(R);
			exclusion.removeAll(xy_union);
			for(String a : exclusion) {//add one new attribute to left of IS
				List<String> new_X = new ArrayList<String>();
				for(String a1 : R) {
					if(a1.equals(a))
						new_X.add(a);
					if(X.contains(a1))
						new_X.add(a1);
				}
				CJ new_IS_X = new CJ(new_X,Y);
				if(!NEW_CANDIDATES.contains(new_IS_X))
					NEW_CANDIDATES.add(new_IS_X);
			}
			for(String a : exclusion) {//add one new attribute to right of IS
				List<String> new_Y = new ArrayList<String>();
				for(String a1 : R) {
					if(a1.equals(a))
						new_Y.add(a);
					if(Y.contains(a1))
						new_Y.add(a1);
				}
				CJ new_IS_Y = new CJ(X,new_Y);
				if(!NEW_CANDIDATES.contains(new_IS_Y))
					NEW_CANDIDATES.add(new_IS_Y);
			}
		}
		return NEW_CANDIDATES;
	}
	
	public static List<String> union(List<String> R,List<String> e1, List<String> e2){
		List<String> u = new ArrayList<String>();
		for(String att : R) {
			if(e1.contains(att) || e2.contains(att))
				u.add(att);
		}
		return u;
	}
	
	
	/**
	 * 
	 * get a sub relation projection of relation r on attributes sub_R, and remove distinct rows
	 * finally return row count of projection
	 * 
	 * sometimes we will drop rowSs with null markers
	 * 
	 * @param r
	 * @param R
	 * @param sub_R
	 * @return row count of projection
	 */
	public static int getDistictRowCountOfSubRelation(List<List<String>> r, List<String> R, List<String> sub_R) {
		List<List<String>> sub_r = new ArrayList<List<String>>();
		for(List<String> row : r) {
			List<String> sub_row = new ArrayList<String>();
			if(Constant.drop_null_marker_tuple) {
				boolean has_null_marker = false;
				for(String attr : sub_R) {
					String value = row.get(R.indexOf(attr));
					sub_row.add(value);
					if(value.equals(Constant.nullMarker)) {//if a row has null marker, do not add this tuple
						has_null_marker = true;
						break;
					}
				}
				if(!has_null_marker && !sub_r.contains(sub_row)) {
					sub_r.add(sub_row);
				}
			}else {
				for(String attr : sub_R) {
					sub_row.add(row.get(R.indexOf(attr)));
				}
				if(!sub_r.contains(sub_row))
					sub_r.add(sub_row);
			}
			
		}
		return sub_r.size();
	}
	
	/**
	 * check whether existing specific arity of IS in IS set
	 * @param ISs
	 * @param arity
	 * @return
	 */
	public static boolean hasISOfSpecificArity(List<CJ> ISs, int arity) {
		boolean exist = false;
		for(CJ is : ISs) {
			if(is.getArity() == arity) {
				exist = true;
				break;
			}
		}
		return exist;
	}
	
	public static List<CJ> get_all_ISs_with_specific_Ele(List<String> Ele, List<CJ> CANDIDATES){
		List<CJ> res = new ArrayList<CJ>();
		for(CJ is : CANDIDATES) {
			if(Ele.equals(is.getX()) || Ele.equals(is.getY()))
				res.add(is);
		}
		return res;
	}
	
	/**
	 * get union of u1 and u2 in order of R'elements
	 * @param R
	 * @param u1
	 * @param u2
	 * @return
	 */
	public static List<String> union_in_order_of_R(List<String> R,List<String> u1, List<String> u2) {
		List<String> union = new ArrayList<String>();
		for(String att : R) {
			if(u1.contains(att)) {
				union.add(att);
				continue;
			}
			if(u2.contains(att))
				union.add(att);
		}
		return union;
	}
	
	/**
	 * all data sets stored in databases
	 * @param dataset_name
	 * @return r
	 * @throws SQLException 
	 */
	public static List<List<String>> import_dataset_from_database(String dataset_name) throws SQLException{
		List<List<String>> r = new ArrayList<List<String>>();
		Connection conn = connectDB();
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM " + dataset_name;
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			int col_num = rs.getMetaData().getColumnCount();
			List<String> tuple = new ArrayList<String>();
			for(int i = 1;i <= col_num;i ++) {
				tuple.add(rs.getString(i));
			}
			r.add(tuple);
		}
		rs.close();
		stmt.close();
		conn.close();
		return r;
	}
	
	/**
	 * execute a query from database and return execution time
	 * @param sql
	 * @return execute time (in seconds)
	 * @throws SQLException
	 */
	public static double get_query_time_from_database(String sql) throws SQLException{
		Connection conn = connectDB();
		Statement stmt = conn.createStatement();
		long start = System.currentTimeMillis();
		ResultSet rs = stmt.executeQuery(sql);
		long end = System.currentTimeMillis();
		
		rs.close();
		stmt.close();
		conn.close();
		return (end-start)/1000.0;
	}
	
	/**
	 * execute 3 kinds of query experiments
	 * @param CJ
	 * @param queryID
	 * @return time-costed list in increasing order for each IS in query
	 * @throws IOException
	 * @throws SQLException 
	 */
	public static List<Double> get_query_time_with_IS(CJ is,int queryID) throws IOException, SQLException{
		List<String> log = new ArrayList<String>();//log recorder
		List<Double> time_res = new ArrayList<Double>();//store average number of each query time
		boolean printed = false;
		log.add(is.toString());
		for(int i = 0;i < Constant.query_exp_time;i ++) {
			String query = Utils.getSpecificQuerySql(Constant.dataset_name, is, queryID);
			if(!printed) {
				log.add("query ID : "+queryID+"\n");
				log.add(query+"\n");
				System.out.println("query ID : "+queryID+"\n");
				System.out.println(query+"\n");
				System.out.println("############\n");
				printed = true;
			}
			double query_time = Utils.get_query_time_from_database(query);
			time_res.add(query_time);
		}
		time_res.sort(null);
		log.add("query time:  "+time_res.toString()+"\n");
		log.add("########################\n\n");
		Utils.output_exp_result(Constant.log_add, log);
		return time_res;
	}
	
	/**
	 * execute query from table
	 * @param sql_id
	 * @return sql clause with id
	 */
	public static String getSpecificQuerySql(String table_name,CJ is,int sql_id) {
		List<String> X = is.getX();
		List<String> Y = is.getY();
		
		String sql = "";
		switch(sql_id) {
		case 1:
			sql += "SELECT ";
			for(int i = 0;i < X.size();i ++) {
				if(i != X.size() - 1)
					sql += "a0.`"+X.get(i)+"`,";
				else
					sql += "a0.`"+X.get(i)+"` ";
			}
			sql += "\nFROM `"+table_name+"` a0 \n";
			sql += "WHERE NOT EXISTS( \n";
			sql += "SELECT *\n";
			sql += "FROM `"+table_name+"` a1 \n";
			sql += "WHERE NOT EXISTS( \n";
			sql += "SELECT * \n";
			sql += "FROM `"+table_name+"` a2 \n";
			sql += "WHERE ";
			for(int i = 0;i < Y.size();i ++) {
				sql += "a2.`"+Y.get(i)+"`=a1.`"+Y.get(i)+"` AND ";
			}
			for(int i = 0;i < X.size();i ++) {
				if(i != X.size() - 1)
					sql += "a2.`"+X.get(i)+"`=a0.`"+X.get(i)+"` AND ";
				else
					sql += "a2.`"+X.get(i)+"`=a0.`"+X.get(i)+"`))";
			}
			break;
		case 2:
			sql += "SELECT ";
			for(int i = 0;i < X.size();i ++) {
				if(i != X.size() - 1)
					sql += "`"+X.get(i)+"`,";
				else
					sql += "`"+X.get(i)+"` \n";
			}
			sql += "FROM `"+table_name+"` \n";
			sql += "GROUP BY ";
			for(int i = 0;i < X.size();i ++) {
				if(i != X.size() - 1)
					sql += "`"+X.get(i)+"`,";
				else
					sql += "`"+X.get(i)+"` \n";
			}
			sql += "HAVING COUNT(*) = (SELECT COUNT(DISTINCT ";
			for(int i = 0;i < Y.size();i ++) {
				if(i != Y.size() - 1)
					sql += "`"+Y.get(i)+"`,";
				else
					sql += "`"+Y.get(i)+"`) ";
			}
			sql += "FROM `"+table_name+"`)";
			break;
		case 3:
			sql += "SELECT ";
			for(int i = 0;i < X.size();i ++) {
				if(i != X.size() - 1)
					sql += "`"+X.get(i)+"`,";
				else
					sql += "`"+X.get(i)+"` \n";
			}
			sql += "FROM `"+table_name+"`";
			break;
		default:
			sql = "";
			break;
		}
//		System.out.println("current sql : \n"+sql+"\n");
		return sql;
	}
	
	/**
	 * header = 0, ... , n-1
	 */
	public static List<String> write_header_for_table(int col_num) {
		List<String> header = new ArrayList<String>();
		for(int i = 0;i < col_num;i ++) {
			header.add(""+i);
		}
		return header;
	}
	
	/**
	 * 
	 * @param dataset_path
	 * @param hasHeader
	 * @param split
	 * @param hasLastEmptyStr
	 * @param col_num
	 * @return	a list. just store two value, first is R, second is r
	 * @throws IOException
	 */
	public static List<Object> import_dataset_from_file(String dataset_path,boolean hasHeader,String split,boolean hasLastEmptyStr,int col_num) throws IOException{
		List<Object> out = new ArrayList<Object>();
		
		out.add(write_header_for_table(col_num));
		
		List<List<String>> r = new ArrayList<List<String>>();
		FileReader fr = new FileReader(dataset_path);
		BufferedReader br = new BufferedReader(fr);
		String tuple;
		if(hasHeader)
			br.readLine();
		while((tuple = br.readLine()) != null) {
			if(Constant.dataset_name.equals("uniprot")) {//special care
				List<String> row = Utils.split(tuple,'"');
				if(row.size() != col_num) {
					System.out.println("split error at "+tuple);
					System.out.println(row.size()+" != "+col_num);
					System.exit(1);
				}
				r.add(row);
			}else {
				String[] row;
				if(hasLastEmptyStr)
					row = tuple.split(split,-1);
				else
					row = tuple.split(split);
				if(row.length != col_num) {
					System.out.println("split error at "+tuple);
					System.out.println(row.length+" != "+col_num);
					System.exit(1);
				}
				List<String> row1 = new ArrayList<String>();
				for(String a : row) {
					row1.add(a);
				}
				r.add(row1);
			}
			
		}
		
		out.add(r);
		br.close();
		fr.close();
		return out;
	}
	
	/**
	 * according raw data set, generate has fixed column number's sub-data set.
	 * we randomly get fixed column number's sub-schema
	 */
	public static List<Object> generate_fixed_col_num_dataset(List<Object> raw_dataset, int new_col_num){
		List<String> raw_R = (List<String>) raw_dataset.get(0);
		List<List<String>> raw_r = (List<List<String>>) raw_dataset.get(1);
		
		if(raw_R.size() == new_col_num)
			return raw_dataset;
		
		List<Object> res = new ArrayList<Object>();//new sub data set with fixed col num
		List<String> new_R = new ArrayList<String>();
		List<List<String>> new_r = new ArrayList<List<String>>();
		
		Random rand = new Random();
		new_R.addAll(raw_R);
		while(true) {
			int remove = rand.nextInt(new_R.size());
			new_R.remove(remove);
			if(new_R.size() == new_col_num)
				break;
		}
		
		for(List<String> raw_tuple : raw_r) {
			List<String> new_tuple = new ArrayList<String>();
			for(String attr : new_R) {
				new_tuple.add(raw_tuple.get(raw_R.indexOf(attr)));
			}
			new_r.add(new_tuple);
		}
		
		res.add(new_R);
		res.add(new_r);
		
		return res;
	}
	
	/**
	 * according raw data set, generate has fixed row number(fixed ratio)'s sub-data set.
	 */
	public static List<Object> generate_fixed_row_num_dataset(List<Object> raw_dataset, double ratio){
		List<String> raw_R = (List<String>) raw_dataset.get(0);
		List<List<String>> raw_r = (List<List<String>>) raw_dataset.get(1);
		
		if(ratio >= 1.0)
			return raw_dataset;
		
		List<Object> res = new ArrayList<Object>();//new sub data set with specific row number
		List<List<String>> new_r = new ArrayList<List<String>>();
		int new_row_num = (int)(raw_r.size()*ratio);
		
		Random rand = new Random();
		new_r.addAll(raw_r);
		while(true) {
			int remove = rand.nextInt(new_r.size());
			new_r.remove(remove);
			if(new_r.size() == new_row_num)
				break;
		}
		
		res.add(raw_R);
		res.add(new_r);
		
		return res;
	}
	
	/**
	 * 
	 * @return a list, just store 3 values. first is constant col num, second is R, last is r
	 * @throws IOException 
	 */
	public static List<Object> remove_constant_col_from_dataset(String dataset_path,boolean hasHeader,String split,boolean hasLastEmptyStr,int col_num) throws IOException {
		List<Object> out = new ArrayList<Object>();
		
		List<Object> out1 = Utils.import_dataset_from_file(dataset_path,hasHeader,split,hasLastEmptyStr,col_num);
		List<String> origin_R = (List<String>) out1.get(0);
		List<List<String>> origin_r = (List<List<String>>) out1.get(1);
		
		List<Set<String>> checker = new ArrayList<Set<String>>();//if constant col then set size is 1 in each col
		for(int i = 0;i < origin_R.size();i ++) {
			checker.add(new HashSet<String>());
		}
		
		for(List<String> t : origin_r) {//check constant column
			for(int i = 0;i < t.size();i ++) {
				checker.get(i).add(t.get(i));
			}
		}
		
		List<Integer> constant_col = new ArrayList<Integer>();
		for(int i = 0;i < origin_R.size();i ++) {
			if(checker.get(i).size() == 1) {//record constant column position
				constant_col.add(i);
			}
		}
		
		List<String> new_R = new ArrayList<String>();
		List<List<String>> new_r = new ArrayList<List<String>>();
		
		for(int i = 0;i < origin_R.size();i ++) {
			if(!constant_col.contains(i))
				new_R.add(origin_R.get(i));
		}
		
		for(List<String> tuple : origin_r) {
			List<String> new_tuple = new ArrayList<String>();
			for(int i = 0;i < tuple.size();i ++) {
				if(!constant_col.contains(i))
					new_tuple.add(tuple.get(i));
			}
			new_r.add(new_tuple);
		}
		
		out.add(constant_col.size());
		out.add(new_R);
		out.add(new_r);
		
		
		return out;
	}
	
	public static void output_exp_result(String output_add, List<String> result) throws IOException {
		File f = new File(output_add);
		if(!f.exists())
			f.createNewFile();
		
		FileWriter fw = new FileWriter(output_add,true);
		BufferedWriter bw = new BufferedWriter(fw);
		result.forEach(line->{
			try {
				bw.write(line+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		bw.close();
		fw.close();
	}
	
	/**
	 * input string "a","","v", then closure = " , finally we return list ["a" "" "v"]
	 * @param raw_string
	 * @param closure
	 * @return
	 */
	public static List<String> split(String raw_string, char closure){
		List<String> res = new ArrayList<String>();
		char[] chars = raw_string.toCharArray();
		boolean start = false;//when start is true,we start to get a sub-string with closure of param 'closure'
		String sub_string = "";
		for(char c : chars) {
			if(c == closure && !start) {
				start = true;
				sub_string += c;
				continue;
			}
			if(start)
				sub_string += c;
			if(c == closure && start) {
				start = false;
				res.add(sub_string);
				sub_string = "";
			}
		}
		return res;
	}
	
	public static int getDistinctRowCountFromCSV(String path,String split,int col_num) throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		String line;
		List<List<String>> r = new ArrayList<List<String>>();
		while((line = br.readLine()) != null) {
			String[] data = line.split(split);
			if(data.length != col_num) {
				System.out.println("split error!");
				System.out.println(line);
			}
			List<String> tuple = new ArrayList<String>();
			for(String a : data) {
				tuple.add(a);
			}
			if(!r.contains(tuple))
				r.add(tuple);
		}
		br.close();
		fr.close();
		return r.size();
	}
	
	public static void main(String[] args) throws IOException {
		List<Object> out = Utils.remove_constant_col_from_dataset(Constant.file_add,Constant.hasHeader,Constant.split,Constant.hasLastEmptyStr,Constant.col_num);
		int constant_col = (Integer) out.get(0);
		List<String> R = (List<String>) out.get(1);
		List<List<String>> r = (List<List<String>>) out.get(2);
		System.out.println("dataset : "+Constant.dataset_name);
		System.out.println("R : "+R.toString());
		System.out.println("r size : "+r.size());
		
		List<String> X = Arrays.asList("a0","a1","a8");
		List<String> Y = Arrays.asList("a6");
		List<String> XY  = Arrays.asList("a0","a1","a6","a8");
		
		int r_X_size = Utils.getDistictRowCountOfSubRelation(r, R, X);
		int r_Y_size = Utils.getDistictRowCountOfSubRelation(r, R, Y);
		int r_XY_size = Utils.getDistictRowCountOfSubRelation(r, R, XY);
		System.out.println("XY : "+XY.toString()+" - X : "+X.toString()+" Y : "+Y.toString());
		System.out.println("|r(XY)| : "+r_XY_size+" - |r(X)| : "+r_X_size+" |r(Y)| : "+r_Y_size);
		
		System.out.println("indep ratio : "+((double)r_XY_size)/(r_X_size*r_Y_size));
//		System.out.println("check |r(XY)| from nursery(a0,a8).csv : "+Utils.getDistinctRowCountFromCSV("C:\\Users\\wang\\Desktop\\phdÂÛÎÄ\\TKDE\\nursery(a0,a8).csv", ",", 2));
//		System.out.println("check |r(X)| from nursery(a0).csv : "+Utils.getDistinctRowCountFromCSV("C:\\Users\\wang\\Desktop\\phdÂÛÎÄ\\TKDE\\nursery(a0).csv", ",", 1));
//		System.out.println("check |r(Y)| from nursery(a8).csv : "+Utils.getDistinctRowCountFromCSV("C:\\Users\\wang\\Desktop\\phdÂÛÎÄ\\TKDE\\nursery(a8).csv", ",", 1));
	}
}
