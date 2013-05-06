import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class CommandHelloWorld extends HystrixCommand<String> {
	private static Logger log = LoggerFactory
			.getLogger(CommandHelloWorld.class);

	private final String name;

	public CommandHelloWorld(String name) {
		super(
				Setter.withGroupKey(
						HystrixCommandGroupKey.Factory.asKey("CommandHelloWorld"))
						.andCommandPropertiesDefaults(
						// we default to a 100ms timeout for secondary
								HystrixCommandProperties
										.Setter()
										.withExecutionIsolationThreadTimeoutInMilliseconds(
												300)));
		this.name = name;
	}

	@Override
	protected String run() {
		try {
			Random r = new Random();
			Thread.sleep(r.nextInt(200));
		} catch (Exception e) {
			log.error("", e);
		}
		return "Hello " + name + "!";
	}

	public static void main(String[] args) throws Exception {

		CommandHelloWorld command = new CommandHelloWorld("Jim");
		String s = command.queue().get();
		log.debug("run result is {} in time: {}", s);

		CommandHelloWorld command2 = new CommandHelloWorld("Tom");
		String s2 = command2.queue().get();
		log.debug("run2 result is {} in time: {}", s2);

		log.debug(command.getClass().getSimpleName());
	}
}