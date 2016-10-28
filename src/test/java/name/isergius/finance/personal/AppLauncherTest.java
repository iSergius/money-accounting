package name.isergius.finance.personal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by isergius on 28.10.16.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class AppLauncherTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAppLauncher() {
        String expected = "pong";
        String actual = this.restTemplate.getForObject("/ping", String.class);
        Assert.assertEquals(expected, actual);
    }
}