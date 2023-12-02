package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * JDBC - DataSource 사용, JdbcUtils 사용
 */
@Slf4j
public class MemberRepositoryV2 {

  private final DataSource dataSource;

  public MemberRepositoryV2(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Member save(Member member) throws SQLException {
    String sql = "insert into member(member_id, money) values (?, ?)";

    Connection conn = null;
    PreparedStatement pstmt = null;

    try {
      conn = getConnection();
      pstmt = conn.prepareStatement(sql);
      // 변수를 통해 SQL 문자열을 구성하면 SQL Injection 공격을 받을 수 있다.
      // 웬만하면 아래와 같이 파라미터 바인딩을 사용하는 것이 좋다.
      pstmt.setString(1, member.getMemberId());
      pstmt.setInt(2, member.getMoney());
      int count = pstmt.executeUpdate(); // 영향받은 rows 개수만큼 count 반환
      return member;
    } catch (SQLException e) {
      log.info("db error", e);
      throw e;
    } finally { // close는 무조건 finally => 잘 관리하지 않으면 리소스 누수로 인해 커넥션이 부족해질 수 있다!
//      pstmt.close(); // Exception이 터지면 밑의 코드가 실행되지 않는다. => try-catch로 묶어줘야 한다.
//      conn.close();
      close(conn, pstmt, null);
    }
  }

  public Member findById(String memberId) throws SQLException {
    String sql = "select * from member where member_id = ?";

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      conn = getConnection();
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, memberId);

      rs = pstmt.executeQuery();
      if (rs.next()) {
        Member member = new Member();
        member.setMemberId(rs.getString("member_Id"));
        member.setMoney(rs.getInt("money"));
        return member;
      } else  {
        throw new NoSuchElementException("member not found memberId=" + memberId);
      }
    } catch (SQLException e) {
      log.info("db error", e);
      throw e;
    } finally {
      close(conn, pstmt, rs);
    }
  }

  public Member findById(Connection conn, String memberId) throws SQLException {
    String sql = "select * from member where member_id = ?";

    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, memberId);

      rs = pstmt.executeQuery();
      if (rs.next()) {
        Member member = new Member();
        member.setMemberId(rs.getString("member_Id"));
        member.setMoney(rs.getInt("money"));
        return member;
      } else  {
        throw new NoSuchElementException("member not found memberId=" + memberId);
      }
    } catch (SQLException e) {
      log.info("db error", e);
      throw e;
    } finally {
      // 트랜잭션을 유지하려면 Connection을 종료하면 안된다.
      JdbcUtils.closeResultSet(rs);
      JdbcUtils.closeStatement(pstmt);
//      JdbcUtils.closeConnection(conn);
    }
  }

  public void update(String memberId, int money) throws SQLException {
    String sql = "update member set money=? where member_id=?";

    Connection conn = null;
    PreparedStatement pstmt = null;

    try {
      conn = getConnection();
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, money);
      pstmt.setString(2, memberId);
      int resultSize = pstmt.executeUpdate();
      log.info("resultSize={}", resultSize);
    } catch (SQLException e) {
      log.info("db error", e);
      throw e;
    } finally {
      close(conn, pstmt, null);
    }
  }

  public void update(Connection conn, String memberId, int money) throws SQLException {
    String sql = "update member set money=? where member_id=?";

    PreparedStatement pstmt = null;

    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, money);
      pstmt.setString(2, memberId);
      int resultSize = pstmt.executeUpdate();
      log.info("resultSize={}", resultSize);
    } catch (SQLException e) {
      log.info("db error", e);
      throw e;
    } finally {
      // 커넥션은 닫지 않는다.
      JdbcUtils.closeStatement(pstmt);
    }
  }

  public void delete(String memberId) throws SQLException {
    String sql = "delete from member where member_id=?";

    Connection conn = null;
    PreparedStatement pstmt = null;

    try {
      conn = getConnection();
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, memberId);
      int resultSize = pstmt.executeUpdate();
      log.info("resultSize={}", resultSize);
    } catch (SQLException e) {
      log.info("db error", e);
      throw e;
    } finally {
      close(conn, pstmt, null);
    }
  }

  private void close(Connection conn, Statement stmt, ResultSet rs) {
    JdbcUtils.closeResultSet(rs);
    JdbcUtils.closeStatement(stmt);
    JdbcUtils.closeConnection(conn);
  }

  private Connection getConnection() throws SQLException {
    Connection conn = dataSource.getConnection();
    log.info("get connection={}, class={}", conn, conn.getClass());
    return conn;
  }
}
