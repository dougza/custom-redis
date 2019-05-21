package br.com.customredis.executable;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.customredis.model.ScoredValues;

@SpringBootTest
public class ExecutableRedisTests {
	
	Map<Object, Object> ts = new HashMap<Object, Object>();
	
	Map<String,SortedSet<ScoredValues<String>>> so = new HashMap<String, SortedSet<ScoredValues<String>>>();
	
	@Test
	public void testZADD() throws IOException {

	    String[] args = {"ZADD","myset","1","one"};

	    ExecutableRedis.applyCommand(args, ts, so);
	    
	    assertEquals(1, so.get("myset").size());
	    
	    args = new String[] {"ZADD","myset","2","two"};
	    
	    ExecutableRedis.applyCommand(args, ts, so);
	    
	    assertEquals(2, so.get("myset").size());
	}
	
}
