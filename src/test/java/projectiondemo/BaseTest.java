package projectiondemo;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Cepro, 2017-03-26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
abstract public class BaseTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
}
