package com.app55.test

import com.app55.Gateway;
import com.app55.Environment;

object Configuration {
	val API_KEY_DEFAULT		= "cHvG680shFTaPWhp8RHhGCSo5QbHkWxP"
	val API_SECRET_DEFAULT	= "zMHzGPF3QAAQQzTDoTGtGz8f5WFZFjzM"

	val Gateway = new Gateway(Environment.Development, apiKey(), apiSecret());

	def apiKey():String  = 
	{
		val value = System.getenv("APP55_API_KEY");
		return  if (value != null) value else API_KEY_DEFAULT
	}

	def apiSecret():String =
	{
		val value = System.getenv("APP55_API_SECRET");
		return if (value != null) value else API_SECRET_DEFAULT
	}
}