package hello.jdbc.connection;

import static hello.jdbc.connection.ConnectionConst.PASSWORD;
import static hello.jdbc.connection.ConnectionConst.URL;
import static hello.jdbc.connection.ConnectionConst.USERNAME;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Slf4j
public class ConnectionTest {

  @Test
  void driverManager() throws SQLException {
    Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    log.info("connection={}, class={}", con1, con1.getClass());
    log.info("connection={}, class={}", con2, con2.getClass());
  }

  @Test
  void dataSourceDriverManager() throws SQLException {
    DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    useDataSource(dataSource);
  }

  @Test
  void dataSourceConnectionPool() throws SQLException, InterruptedException {
    // 커넥션 풀링
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(URL);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    dataSource.setMaximumPoolSize(10); // default = 10
    dataSource.setPoolName("myPool");

    useDataSource(dataSource);
    Thread.sleep(1000);
  }

  private void useDataSource(DataSource dataSource) throws SQLException {
    Connection con1 = dataSource.getConnection();
    Connection con2 = dataSource.getConnection();
    Connection con3 = dataSource.getConnection();
    Connection con4 = dataSource.getConnection();
    Connection con5 = dataSource.getConnection();
    Connection con6 = dataSource.getConnection();
    Connection con7 = dataSource.getConnection();
    Connection con8 = dataSource.getConnection();
    Connection con9 = dataSource.getConnection();
    Connection con10 = dataSource.getConnection();
    // 만약 풀이 모두 사용 중이라면 풀이 생길 때까지 대기한다. => 그런데 close가 없기 때문에 계속 대기상태가 될 것이다.
//    Connection con11 = dataSource.getConnection();
    log.info("connection={}, class={}", con1, con1.getClass());
    log.info("connection={}, class={}", con2, con2.getClass());
  }
}
