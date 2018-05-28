import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.net.UnknownHostException;


/**
 * 项目引导入口
 *
 */
@SpringBootApplication
@ComponentScan("com.bee.lw")
public class Application {
	public static void main(String[] args) throws UnknownHostException {
		SpringApplication.run(Application.class, args);
	}
}
