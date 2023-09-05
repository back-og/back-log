package dev.backlog.common;

import dev.backlog.common.config.JpaConfig;
import dev.backlog.common.config.QueryDslConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(value = {QueryDslConfig.class, JpaConfig.class})
public class RepositoryTest {
}
