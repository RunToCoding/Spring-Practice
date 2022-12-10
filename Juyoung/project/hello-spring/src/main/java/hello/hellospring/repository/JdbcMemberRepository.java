package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {
	//Java에서 DB접근을 위한 접속정보 - DataSource 
	private final DataSource dataSource;

	public JdbcMemberRepository(DataSource dataSource) { //DataSource dataSource : Spring으로부터 주입받음
		this.dataSource = dataSource;
	}

	@Override
	public Member save(Member member) {
		String sql = "insert into member(name) values(?)"; //sql 선언
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;	//결과 받는 곳
		
		try {
			conn = getConnection();													//connection DB
			
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);	//SQL 준비 : RETURN_GENERATED_KEYS -> INSERT한 후 ID 값 얻는 옵션(KEY값)
			
			pstmt.setString(1, member.getName());									//SQL 에 파라미터 주입
			pstmt.executeUpdate();													// DB 실제 쿼리 실행
			
			rs = pstmt.getGeneratedKeys();											//KEY값 얻음 : 여기서는 ID값
			
			if (rs.next()) {	// rs.next() : 값이 있으면
				member.setId(rs.getLong(1));	// 값 꺼냄
			} else {
				throw new SQLException("id 조회 실패");
			}
			return member;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			close(conn, pstmt, rs);	// 리소스 반환
		}
	}

	@Override
	public Optional<Member> findById(Long id) {
		String sql = "select * from member where id = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			
			rs = pstmt.executeQuery();		// DB 실제 쿼리 실행
			
			if (rs.next()) {
				Member member = new Member();
				member.setId(rs.getLong("id"));
				member.setName(rs.getString("name"));
				
				return Optional.of(member);
			} else {
				return Optional.empty();
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			close(conn, pstmt, rs);
		}
	}

	@Override
	public List<Member> findAll() {
		String sql = "select * from member";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			List<Member> members = new ArrayList<>();
			
			while (rs.next()) {
				Member member = new Member();
				member.setId(rs.getLong("id"));
				member.setName(rs.getString("name"));
				members.add(member);
			}
			
			return members;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			close(conn, pstmt, rs);
		}
	}

	@Override
	public Optional<Member> findByName(String name) {
		String sql = "select * from member where name = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				Member member = new Member();
				member.setId(rs.getLong("id"));
				member.setName(rs.getString("name"));
				
				return Optional.of(member);
			}
			return Optional.empty();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			close(conn, pstmt, rs);
		}
	}

	private Connection getConnection() {
		return DataSourceUtils.getConnection(dataSource);
	}

	private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null) {
				close(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void close(Connection conn) throws SQLException {
		DataSourceUtils.releaseConnection(conn, dataSource);
	}
}