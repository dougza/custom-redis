package br.com.customredis.executable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.com.customredis.commands.CommandsEnum;
import br.com.customredis.model.ScoredValues;

public class ExecutableRedis {
	
	static Map<Object, Object> ts;
	
	static Map<String,SortedSet<ScoredValues<String>>> so;
	static Map<Long, String> expireValues;
	static long currentTime = System.currentTimeMillis();

	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
	    String cf = "";
	    ts = new HashMap<Object, Object>();
	    so = new HashMap<String, SortedSet<ScoredValues<String>>>();
	    expireValues = new HashMap<Long, String>();
	    
	    ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
	    exec.scheduleAtFixedRate(new Runnable() {
	      @Override
	      public void run() {
	    	  expireValues.entrySet()
	    	  			.parallelStream().forEach(x -> {
	    	  				if (x.getKey() < System.currentTimeMillis()) {
	    	  					ts.remove(x.getValue());
	    	  				}
	    	  			});
	      }
	    }, 0, 5, TimeUnit.SECONDS);
	    
	    while (!cf.equals(CommandsEnum.FINISH.toString())) {
	        System.out.print("redis 127.0.0.1:6379> ");
	        if (in.hasNext()) {
	            cf = in.nextLine();
	            
	            String[] arg = cf.trim().split("\\s+");
	            
	            if (CommandsEnum.isMember(arg[0].toUpperCase())) {
	            	applyCommand(arg,ts,so);
	            } else {
	            	System.out.println("Comando invalido! ");
	            }
	        }
	    }
	    in.close();
	}
	
	public static String applyCommand(String[] command, 
								    Map<Object, Object> ts, 
								    Map<String,SortedSet<ScoredValues<String>>> so) {
		
		String retorno = "";
		
		SortedSet<ScoredValues<String>> a = null;

		if (command[0].toUpperCase().equals(CommandsEnum.PING.toString())) {
			retorno = "PONG";
			System.out.println(retorno);
		}
		if (command[0].toUpperCase().equals(CommandsEnum.SET.toString())) {
			ts.put(command[1], command[2]);
			
			if (command.length > 3) {
				expireValues.put((System.currentTimeMillis()+TimeUnit.SECONDS.toMillis(Long.parseLong(command[3].toString()))),command[1]);	
			}	
			
			retorno = "OK";
			System.out.println(retorno);
		}
		if (command[0].toUpperCase().equals(CommandsEnum.GET.toString())) {
			retorno = ts.get(command[1]) == null ? "nil" : ts.get(command[1]).toString();
			System.out.println(retorno);
		}
		if (command[0].toUpperCase().equals(CommandsEnum.DEL.toString())) {
			int count = 0;
			for(int x = 1; x < command.length; x++) {
				if (ts.get(command[x]) != null) {
					count++;
					ts.remove(command[x]);
				}
			}
			retorno = Integer.toString(count);
			System.out.println(retorno);
		}
		if (command[0].toUpperCase().equals(CommandsEnum.DBSIZE.toString())) {
			retorno = Integer.toString(ts.size());
			System.out.println(retorno);
		}
		if (command[0].toUpperCase().equals(CommandsEnum.INCR.toString())) {
			int value = Integer.parseInt(ts.get(command[1]).toString());
			ts.put(command[1], ++value);

			retorno = Integer.toString(value);
			System.out.println(retorno);
		}
		if (command[0].toUpperCase().equals(CommandsEnum.ZADD.toString())) {
			
			if (so.get(command[1]) == null) {
				so.put(command[1].toString(), new TreeSet<>(Collections.reverseOrder()));
			} 
			a = so.get(command[1].toString());
			
			int count = 0;
			for (int j = 2; j < command.length; j++) {
				a.add(new ScoredValues<String>(Integer.parseInt(command[j++].toString()),command[j].toString()));	
				count++;
			}
			
			System.out.println(count);
		}
		if (command[0].toUpperCase().equals(CommandsEnum.ZCARD.toString())) {
			a = so.get(command[1].toString());
			System.out.println(a.size());
		}
		if (command[0].toUpperCase().equals(CommandsEnum.ZRANK.toString())) {
			a = so.get(command[1].toString());
			if (a ==null ) {
				System.out.println("nil");
			} else {
				Iterator<ScoredValues<String>> it =a.iterator();
				int exist = 0;
				while(it.hasNext()) {
					ScoredValues<String> xxx = (ScoredValues<String>)it.next();
					
					if (xxx.getValue().equals(command[2].toString())) {
						exist = 1;
						if (a.tailSet(xxx).size() > a.size()) {
							System.out.println(a.tailSet(xxx).size());	
						} else {
							System.out.println(a.size()-a.tailSet(xxx).size());
						}
						
					}
					
				}
				
				if(exist == 0)
					System.out.println("nil");
			}
			
		}

		if (command[0].toUpperCase().equals(CommandsEnum.ZRANGE.toString())) {
			a = so.get(command[1].toString());
			
			if (a ==null ) {
				System.out.println("nil");
			} else {
				
				Object[] ar = a.toArray();
				
				int start = Integer.parseInt(command[2].toString());
				int stop = Integer.parseInt(command[3].toString());
				boolean withScores = (command.length == 5 
										&& command[4].toString().toUpperCase().equals("WITHSCORES")) ? true : false;
				
				if (start > stop) {
					System.out.println("");
				} else {
					if (start < 0) {
						start = ar.length+start;
					}
					
					if (stop < 0) {
						stop = ar.length+stop;
					}
					
					for (int b = start; b <= stop; b++) {
						
						try {
							ScoredValues<String> sv = (ScoredValues<String>) ar[b];
							System.out.println(sv.getValue());
							if (withScores) {
								System.out.println(sv.getScore());
							}
						} catch (ArrayIndexOutOfBoundsException ex) {
							System.out.print("");
						}
					}
				}
			}
		}
		return retorno;
	}
}
