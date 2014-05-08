package com.app55.util

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

object EncodeUtil {

	def createBasicAuthString(username:String, password: String): String =
	{
		val toEncode = username + ":" + password
		return "Basic " + base64(toEncode.getBytes())
	}

	def sha1(data : String): Array[Byte] = 
	{
		try
		{
			val md = MessageDigest.getInstance("SHA-1")
			return md.digest(data.getBytes("UTF-8"))
		}
		catch 
		{
		  case e:NoSuchAlgorithmException => ; // Will never happen.
		  case e:UnsupportedEncodingException => ; // Will never happen.
		}

		return null;
	}

	def base64(data: Array[Byte]): String =
	{
		return Base64.encodeBytes(data, Base64.DONT_BREAK_LINES | Base64.URL_SAFE)
	}
}
