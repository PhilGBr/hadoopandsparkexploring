package philgbr.exploration.spark;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class MainTest {
	
	private Map<String, Integer> methodsCalls = new HashMap<String,Integer>();

    @Test
    public void testBench() throws Exception {
    	
    	// Access regular public virtual method, with variable number of args
    	Main.benchClass(InnerUnderTestClass.class, new String[] {"virtualMethod1"}, new Class[] {Map.class}, methodsCalls);
    	assertTrue(methodsCalls.get(InnerUnderTestClass.CALL_VIRTUAL_METHOD_1).equals(1));

    	Main.benchClass(InnerUnderTestClass.class, new String[] {"virtualMethod1"}, new Class[] {Map.class, String.class}, methodsCalls, "dummy");
       	assertTrue(methodsCalls.get(InnerUnderTestClass.CALL_VIRTUAL_METHOD_1_EXTRA_ARG).equals(1));
    	
    	// Let's call the later one another time .... 
    	Main.benchClass(InnerUnderTestClass.class, new String[] {"virtualMethod1"}, new Class[] {Map.class, String.class}, methodsCalls, "dummy");
    	assertTrue(methodsCalls.get(InnerUnderTestClass.CALL_VIRTUAL_METHOD_1_EXTRA_ARG).equals(2));  // .. and check our counter
    	
    	// Give a try to static method too
    	Main.benchClass(InnerUnderTestClass.class, new String[] {"staticMethod1"}, new Class[] {Map.class}, methodsCalls);
    	assertTrue(methodsCalls.get(InnerUnderTestClass.CALL_STATIC_METHOD_1).equals(1));
    	
  
    	// Now, checks behavior when attempting to invoke a method which is NOT ACCESSIBLE to the caller
    	Main.benchClass(InnerUnderTestClass.class, new String[] {"virtualMethod2"}, new Class[] {Map.class}, methodsCalls);
    	assertNull(methodsCalls.get(InnerUnderTestClass.CALL_VIRTUAL_METHOD_2));  // the method could'nt be invoked
    }
    
    
    public static class InnerUnderTestClass{
    	
    	public static final String CALL_VIRTUAL_METHOD_1 = "callVirtualMethod1";
    	public void virtualMethod1(Map<String, Integer> callCounter) { callCounter.merge(CALL_VIRTUAL_METHOD_1, 1, Integer::sum);}
    	
    	public static final String CALL_VIRTUAL_METHOD_1_EXTRA_ARG = "callVirtualMethod1_extraArg";
    	public void virtualMethod1(Map<String, Integer> callCounter, String extraArg) { callCounter.merge(CALL_VIRTUAL_METHOD_1_EXTRA_ARG, 1, Integer::sum);}
    	
    	public static final String CALL_STATIC_METHOD_1 = "callStaticMethod1";
    	public static void staticMethod1(Map<String, Integer> callCounter) {callCounter.merge(CALL_STATIC_METHOD_1, 1, Integer::sum);}

    	public static final String CALL_VIRTUAL_METHOD_2 = "callVirtualMethod2";
    	@SuppressWarnings("unused")
    	private void virtualMethod2(Map<String, Integer> callCounter) {callCounter.merge(CALL_VIRTUAL_METHOD_2, 1, Integer::sum);}
    }
}
