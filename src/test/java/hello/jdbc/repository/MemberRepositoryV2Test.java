package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.jdbc.connecntion.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 트랜잭션 - 커넥션 파라미터  전달 방식 동기화   
 */
@Slf4j
class MemberRepositoryV2Test {

   MemberRepositoryV2 repository;

   @BeforeEach
   void beforeEach(){
//       DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
       HikariDataSource dataSource = new HikariDataSource();
       dataSource.setJdbcUrl(URL);
       dataSource.setUsername(USERNAME);
       dataSource.setPassword(PASSWORD);

       repository = new MemberRepositoryV2(dataSource);
   }

    @Test
    @Transactional
    void crud() throws SQLException {
        Member member = new Member("memberV8", 10000);
        repository.save(member);

        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember ={}",findMember);

        assertThat(findMember).isEqualTo(member);

        //update : money : 10000 -> 20000
        repository.update(member.getMemberId(),20000);
        Member updateMember = repository.findById(member.getMemberId());

        assertThat(updateMember.getMoney()).isEqualTo(20000);

        repository.delete(member.getMemberId());
        assertThatThrownBy(()->repository.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);

    }
}