package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectTest02 {
	// 세미콜론 빼고 넣자 쿼리!!!
	public static void main(String[] args) {
		searchExployees("ko");
	}

	public static void searchExployees(String keyword) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 1. JDBC Driver Class 로딩
			Class.forName("org.mariadb.jdbc.Driver");
			// 2. 연결하기
			String url = "jdbc:mariadb://192.168.0.174:3307/employees?chraset=utf8";
			// getConnection (url, 계정이름, 비밀번호)
			conn = DriverManager.getConnection(url, "hr", "hr");
			System.out.println("연결 성공");
			// 3. sql 준비
			String sql = 
					" select emp_no, first_name, last_name" +
					" from employees" + 
					" where first_name like ?" +
					" and last_name like ?";
			pstmt = conn.prepareStatement(sql);
			
			// 5. binding
			pstmt.setString(1, "%" + keyword + "%");
			pstmt.setString(2, "%" + keyword + "%");
			// 5. sql 실행
			
			rs = pstmt.executeQuery();
			
			// 6. 결과처리
			while(rs.next()) {
				Long empNo = rs.getLong(1);
				String firstName = rs.getString(2);
				String lastName = rs.getString(3);
				
				System.out.println(empNo + " : " +  firstName + lastName);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				// 7. 자원 처리
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}