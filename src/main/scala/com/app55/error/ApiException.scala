package com.app55.error

class ApiException(val message:String, val code:Long, val body:Map[String,Any]) extends Throwable {
  override	
  def toString():String = {
		return message
	}
}

object ApiException {
   	def createException(error:Map[String, Any]): ApiException =
	{

		val tpe:String = error.get("type").asInstanceOf[String]
		val message:String = error.get("message").asInstanceOf[String]
		val codeInt:Int = error.get("code").asInstanceOf[Int]
		val code:Long = if (codeInt != null.asInstanceOf[Long])  codeInt.longValue() else null.asInstanceOf[Long]
		val body:Map[String, Any] = error.get("body").asInstanceOf[Map[String, Any]]

		tpe match {
		  case "request-error" => new RequestException(message, code, body)
		  case "resource-error" => new ResourceException(message, code, body)
		  case "authentication-error" => new AuthenticationException(message, code, body)
		  case "server-error" => new ServerException(message, code, body)
		  case "validation-error" => new ValidationException(message, code, body)
		  case "card-erro" => new CardException(message, code, body)
		  case _ => new ApiException(message, code, body)
		}
	}
   	
   	def createException(message:String, code:Long) = {
   	  new ApiException(message, code, null)
   	}

}

class RequestException(message:String, code:Long, body:Map[String,Any]) extends ApiException(message,code, body)

class ResourceException(message:String, code:Long, body:Map[String,Any]) extends ApiException(message,code, body)

class AuthenticationException(message:String, code:Long, body:Map[String,Any]) extends ApiException(message,code, body)

class ServerException(message:String, code:Long, body:Map[String,Any]) extends ApiException(message,code, body)

class ValidationException(message:String, code:Long, body:Map[String,Any]) extends ApiException(message,code, body)

class CardException(message:String, code:Long, body:Map[String,Any]) extends ApiException(message,code, body)

class InvalidSignatureException() extends ApiException("The response contained an invalid signature.", 0, null)
