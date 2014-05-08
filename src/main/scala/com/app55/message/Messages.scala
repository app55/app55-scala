package com.app55.message

import com.app55.domain.{User, Card, Transaction, Address}
import com.app55.Gateway

import scala.reflect.BeanInfo

@BeanInfo class UserCreateRequest(gateway:Gateway, var user:User) extends Request[UserCreateResponse](gateway) {
  	def this(user:User) = this(null, user)

	def getHttpEndpoint(): String = {
		return gateway.environment.baseUrl() + "/user";
	}

	override
	def getHttpMethod(): String = {
		return "POST";
	}
}
@BeanInfo class UserCreateResponse(var user:User) extends Response()

@BeanInfo class UserGetRequest(gateway:Gateway, id:Long) extends Request[UserGetResponse](gateway) {
  	def this(id:Long) = this(null, id)

	def getHttpEndpoint(): String = {
		return gateway.environment.baseUrl() + "/user/" + id;
	}
}
@BeanInfo class UserGetResponse(var user:User) extends Response()

@BeanInfo class CardCreateRequest(gateway:Gateway, var user:User, var card:Card) extends Request[CardCreateResponse](gateway) {
	val ipAddress:String = null
  
	def this(user:User, card:Card) = this(null, user, card)
			
	def getHttpEndpoint(): String = {
			return gateway.environment.baseUrl() + "/card"
	}
	
	override
	def getHttpMethod(): String = {
			return "POST";
	}
}
@BeanInfo class CardCreateResponse(var card:Card) extends Response()

@BeanInfo class CardListRequest(gateway:Gateway, var user:User) extends Request[CardListResponse](gateway){
	def this(user:User) = this(null, user)

	override
	def getHttpEndpoint(): String = {
			return gateway.environment.baseUrl() + "/card"
	}
}
@BeanInfo class CardListResponse(var cards:List[Card]) extends Response

@BeanInfo class CardDeleteRequest(gateway:Gateway, var card:Card, var user:User) extends Request[CardDeleteResponse](gateway){
	def this(card:Card, user:User) = this(null, card, user)
			
			override
			def getHttpEndpoint(): String = {
					return gateway.environment.baseUrl() + "/card/" + card.token
			}
	
	override
	def getHttpMethod(): String = {
			return "DELETE";
	}
}
@BeanInfo class CardDeleteResponse() extends Response

@BeanInfo class TransactionCreateRequest(gateway:Gateway, var user:User, var card:Card, var transaction:Transaction) extends Request[TransactionCreateResponse](gateway) {
	val ipAddress:String = null

	def this(user:User, card:Card, transaction:Transaction) = this(null, user, card, transaction)
  
 	def getHttpEndpoint(): String = {
			return gateway.environment.baseUrl() + "/transaction"
	}
	
	override
	def getHttpMethod(): String = {
			return "POST";
	}
}
@BeanInfo class TransactionCreateResponse(var transaction:Transaction) extends Response

@BeanInfo class TransactionCommitRequest(gateway:Gateway, var transaction:Transaction) extends Request[TransactionCommitResponse](gateway) {

	def this(transaction:Transaction) = this(null, transaction)
  
 	def getHttpEndpoint(): String = {
			return gateway.environment.baseUrl() + "/transaction/" + transaction.id
	}
	
	override
	def getHttpMethod(): String = {
			return "POST";
	}
}
@BeanInfo class TransactionCommitResponse(var transaction:Transaction) extends Response

