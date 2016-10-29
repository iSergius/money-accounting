package name.isergius.finance.personal.ui;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by isergius on 29.10.16.
 */
@RestController
public class PingController {

    @RequestMapping(path = "/ping")
    public String ping() {
        return "pong";
    }
}
