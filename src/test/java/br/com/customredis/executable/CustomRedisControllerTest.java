package br.com.customredis.executable;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class CustomRedisControllerTest {
	
	private MockMvc mockMvc;
	
	@Autowired 
	private WebApplicationContext ctx;
    
	@Before  
	public void init() { 
		MockitoAnnotations.initMocks(this);
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx).build();
	} 
	
	@Test
	public void commandTests() throws Exception {
		mockMvc.perform(get("/?cmd=SET mykey cool-value"))
		.andExpect(status().isOk())
		.andReturn()
	    .getResponse()
	    .getContentAsString().equals("OK");
		
		mockMvc.perform(get("/?cmd=GET mykey"))
		.andExpect(status().isOk())
		.andReturn()
	    .getResponse()
	    .getContentAsString().equals("cool-value");
	}
}
