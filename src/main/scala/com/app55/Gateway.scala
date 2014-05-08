package com.app55

import com.app55.domain._
import com.app55.message._


class Gateway(val environment: Environment, val apiKey: String, val apiSecret: String) {

  def createUser(user: User): UserCreateRequest =
	{
	  new UserCreateRequest(this, user)
	}

  def getUser(id: Long): UserGetRequest =
	{
	  new UserGetRequest(this, id)
	}

  def createCard(u:User, c:Card): CardCreateRequest =
	  {
		  new CardCreateRequest(this, u, c)
	  }
  def deleteCard(u:User, c:Card): CardDeleteRequest =
	  {
		  new CardDeleteRequest(this, c, u)
	  }

  def listCards(u:User): CardListRequest =
	  {
		  new CardListRequest(this, u)
	  }
  
  def createTransaction(u:User, c:Card, t:Transaction) : TransactionCreateRequest = 
	  {
	    new TransactionCreateRequest(this, u,c,t)
	  }
   def commitTransaction(t:Transaction) : TransactionCommitRequest = 
   {
     new TransactionCommitRequest(this, t)
   }
}