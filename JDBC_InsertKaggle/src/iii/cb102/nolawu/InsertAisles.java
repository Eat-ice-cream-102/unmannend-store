package iii.cb102.nolawu;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertAisles {

	public static void main(String[] args) {
		final int batchSize = 100; //setting size
		int count = 0;
		File file=new File("res/aisles.csv");//where is the csv
		Connection conn = null;
		try {     
			String connUrl = "jdbc:mysql://localhost:3306/kaggle?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
			conn = DriverManager.getConnection(connUrl, "root", "pwd"); // MySQL account and password 
			try {
				BufferedReader in=new BufferedReader(new FileReader(file)); //read csv
				StringBuffer sb=new StringBuffer(256);
				String str=null;
				int step=0;
				while((str=in.readLine())!=null) { 
					sb.append(str).append("\n"); 
					String insStmt = "INSERT INTO aisles VALUES (?, ?)";
					PreparedStatement pstmt = conn.prepareStatement(insStmt);
					if(step!=0) { 
						String[] split_line = str.split(","); 
						for(int i=0;i<split_line.length;i++) { 
							pstmt.setString(i+1, split_line[i]);								
						}
						pstmt.addBatch(); 
						if(count%batchSize==0) {
							pstmt.executeBatch();
						}
					}
					step++;
					count++;
					pstmt.executeBatch();
					System.out.println(count+" Success");
					System.out.println(" ---------------------- ");
				}			
				in.close();
			}catch(IOException e) {
				e.printStackTrace();
				System.err.println("Error:  "+count);}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error:  "+count);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch(SQLException e) { 
					e.printStackTrace();
				}
		}
		System.out.print("Success");
	}

}