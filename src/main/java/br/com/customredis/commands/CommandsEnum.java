package br.com.customredis.commands;

public enum CommandsEnum {
	
	SET,
	GET,
	DEL,
	DBSIZE,
	INCR,
	ZADD,
	ZCARD,
	ZRANK,
	ZRANGE,
	FINISH,
	PING;
	
	static public boolean isMember(String aName) {
		CommandsEnum[] aFruits = CommandsEnum.values();
	       for (CommandsEnum aFruit : aFruits)
	           if (aFruit.toString().equals(aName))
	               return true;
	       return false;
	   }

}
