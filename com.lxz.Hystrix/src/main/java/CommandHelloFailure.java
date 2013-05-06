import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * Sample {@link HystrixCommand} showing a basic fallback implementation.
 */
public class CommandHelloFailure extends HystrixCommand<String> {
	private static Logger log = LoggerFactory.getLogger(CommandHelloFailure.class);

    private final String name;

    public CommandHelloFailure(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() {
        throw new RuntimeException("this command always fails");
    }

    @Override
    protected String getFallback() {
        return "Hello Failure " + name + "!";
    }
    

   
    public static void main(String[] args) throws Exception{
    	CommandHelloFailure command = new CommandHelloFailure("Jim");
		String s = command.queue().get();
		log.debug("run result is {} in time: {}",s);
    }

}