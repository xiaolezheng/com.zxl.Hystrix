import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public class CommandUsingRequestCache extends HystrixCommand<Boolean> {
	private static Logger log = LoggerFactory.getLogger(CommandUsingRequestCache.class);

    private final int value;

    protected CommandUsingRequestCache(int value) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.value = value;
    }

    @Override
    protected Boolean run() {
        return value == 0 || value % 2 == 0;
    }

    @Override
    protected String getCacheKey() {
        return String.valueOf(value);
    }
    
    public static void main(String[] args){
    	HystrixRequestContext context = HystrixRequestContext.initializeContext();
    	 try {
	    	log.debug("is: {}",new CommandUsingRequestCache(2).execute());
	    	log.debug("is: {}",new CommandUsingRequestCache(1).execute());
	    	log.debug("is: {}",new CommandUsingRequestCache(0).execute());
	    	log.debug("is: {}",new CommandUsingRequestCache(168).execute());
	    	
	    	CommandUsingRequestCache command2a = new CommandUsingRequestCache(2);
            CommandUsingRequestCache command2b = new CommandUsingRequestCache(2);
            log.debug("is: {}",command2a.execute());
            log.debug("is: {}",command2a.isResponseFromCache());
            log.debug("is: {}",command2b.execute());
            log.debug("is: {}",command2b.isResponseFromCache());
    	 }finally{
    		 context.shutdown();
    	 }
    	 
    	 context = HystrixRequestContext.initializeContext();
         try {
             CommandUsingRequestCache command3b = new CommandUsingRequestCache(2);
             log.debug("is: {}",command3b.execute());
             // this is a new request context so this 
             // should not come from cache
             log.debug("is: {}",command3b.isResponseFromCache());
         } finally {
             context.shutdown();
         }
    }
}