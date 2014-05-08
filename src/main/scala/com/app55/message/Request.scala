package com.app55.message

import com.app55.Gateway
import com.app55.util._
import com.app55.error._

import com.app55.domain.User


import scala.collection.mutable.HashMap
import scala.collection.JavaConverters._

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.io.DataOutputStream;


abstract class Request[T <: Response : Manifest](gateway:Gateway) extends Message(gateway) {
 
	def this() {
	  this(null)
	}
	
	def getHttpEndpoint() : String
	
	def name[T](implicit m: scala.reflect.Manifest[T]) = m.toString

  	def getHttpMethod(): String = 
	{
		return "GET";
	}

  	def send() :T = {
  		println("Sending for: " + name[T])
  		
		val exclude = HashMap[String, Boolean]();
		exclude.put("id", true);
		exclude.put("sig", true);
		exclude.put("ts", true);
		val qs = toFormData(false, exclude);

		println("FormData: " + qs)
		println("Authent: " + EncodeUtil.createBasicAuthString(gateway.apiKey, gateway.apiSecret))
		println("To: " + getHttpEndpoint())
		
		var url: URL = null
		var con:HttpURLConnection = null

		try
		{		
			if ("GET".equals(getHttpMethod()))
			{
				val url = new URL(getHttpEndpoint() + "?" + qs)
				con = url.openConnection().asInstanceOf[HttpURLConnection];
				con.setRequestMethod(getHttpMethod());

				con.setRequestProperty("Authorization", EncodeUtil.createBasicAuthString(gateway.apiKey, gateway.apiSecret));
			}
			else if ("DELETE".equals(getHttpMethod()))
			{
				val url = new URL(getHttpEndpoint())
				con = url.openConnection().asInstanceOf[HttpURLConnection];
				con.setRequestMethod(getHttpMethod());
				
				con.setRequestProperty("Authorization", EncodeUtil.createBasicAuthString(gateway.apiKey, gateway.apiSecret));
			}
			else
			{
				val data = qs.getBytes("UTF-8")

				url = new URL(getHttpEndpoint());
				con = url.openConnection().asInstanceOf[HttpURLConnection]
				con.setRequestMethod(getHttpMethod());

				con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				con.setRequestProperty("Authorization", EncodeUtil.createBasicAuthString(gateway.apiKey, gateway.apiSecret));
				con.setFixedLengthStreamingMode(data.length);
				con.setDoOutput(true);

				var wr = new DataOutputStream(con.getOutputStream());
				wr.write(data);
				wr.flush();
				wr.close();
			}

			var s:Scanner = null;
			if (con.getResponseCode() != 200)
			{
				throw new RequestException("Http Error " + con.getResponseCode(), con.getResponseCode(), null);
			}
			else
			{
				s = new Scanner(con.getInputStream());
			}
			s.useDelimiter("\\Z");
			var json = s.next();

			var ht = JsonUtil.toMap[Any](json)   //JsonUtil.map(json)
			println("json: " + json)
			println("map: " + ht)
			//var ht:scala.collection.mutable.Map[String, _] = JsonUtil.map(json).asScala

			if (ht.contains("error"))
				throw ApiException.createException(ht("error").asInstanceOf[Map[String,Any]]);

			//!var x:UserCreateResponse = JsonUtil.fromJson[UserCreateResponse](json)
			
			var r:T = JsonUtil.fromJson[T](json)  //JsonUtil.objct(json, responseClass);
			//var r = JacksonWrapper.deserialize[T](json)  //JsonUtil.objct(json, responseClass);
			println("r: " + r)
			r.populate(ht.toMap);
			r.gateway = gateway

			if (!r.isValidSignature())
				throw new InvalidSignatureException();

			return r;
		} catch {
		  case a:ApiException => throw a;		// This just gets rethrown
		  case e:Exception => ApiException.createException(e.getMessage(), -1L)
		  case _ :Throwable => println("Unknown Exception")
		}
		
	return null.asInstanceOf[T]
  }

}