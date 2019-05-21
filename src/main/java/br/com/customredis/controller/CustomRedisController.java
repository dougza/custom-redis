package br.com.customredis.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.customredis.executable.ExecutableRedis;
import br.com.customredis.model.ScoredValues;

@RestController
@RequestMapping(path = "/")
public class CustomRedisController {
	
	Map<Object, Object> ts = new HashMap<Object, Object>();
	Map<String,SortedSet<ScoredValues<String>>> so = new HashMap<String, SortedSet<ScoredValues<String>>>();
	
	@GetMapping
	public String commandSET(@RequestParam String cmd) {
		String[] commands = cmd.split("\\s+");
		
		String retorno = ExecutableRedis.applyCommand(commands, ts, so);
		return retorno;
	}
}
