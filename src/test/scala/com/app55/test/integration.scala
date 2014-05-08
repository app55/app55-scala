package com.app55.test

import com.app55.message.{UserCreateResponse, UserGetResponse}
import com.app55.message.{CardCreateResponse, CardListResponse, CardDeleteResponse}
import com.app55.message.{TransactionCreateResponse, TransactionCommitResponse}
import com.app55.test.util.Utils
import com.app55.domain.{User, Card, Transaction, Address}

//ToDO:
// Handle Option[Long] better (and use default params)
// Rmv java reflection Util
// Fix populateAdditional odd logic get properties.
// Use a native scala json converter 

object integration extends App {
  
	def  createUser(email:String="example." + Utils.timestamp() + "@scalatest.com", 
					phone:String = "0123 456 7890", 
					password:String = "pa55word" ): UserCreateResponse =
	{
		println("Creating user %s...".format(email))
		
		val response: UserCreateResponse = Configuration.Gateway.createUser(new User(email, phone, password, password)).send();
		println("DONE (user-id %s)".format(response.user.id))
		response
	}

	def  getUser(u:User): UserGetResponse =
	{
		println("Getting user %s...".format(u.id))
    	val id:Long =u.id.get.toString.toLong	//clunky way round scala type conversion 'bug' 	SI-1448

		val response: UserGetResponse = Configuration.Gateway.getUser(id).send();
		
		println("DONE (user %s)".format(response.user.email))
		response
	}
	
	def  createCard(u:User, number:String = "4111111111111111"): CardCreateResponse =
		{
		println("Creating card ...")
		
		val response: CardCreateResponse = Configuration.Gateway.createCard(new User(u.id.get.toString.toLong), 
		    new Card(holderName= "App55CardHolder",
		    	number=number,
		    	expiry="04/2019",
		    	securityCode = "111",
		    	address = new Address("8 Exchange Quay", null, "Manchester", "M5 3EJ", "GB")
		        )).send();
		
		println("DONE (card-token %s)".format(response.card.token))
		response
		}
	
	def listCards(u:User): CardListResponse =
	{
		println("Getting card list ...")
		
		val response: CardListResponse = Configuration.Gateway.listCards(new User(u.id.get.toString.toLong)).send(); 
		println("DONE (found %s cards)".format(response.cards.length))
		return response
	}
	
	def createTransaction(u:User, c:Card, t:Transaction):TransactionCreateResponse =
	{
	  println("Creating Transaction ...")
	  val response:TransactionCreateResponse = Configuration.Gateway.createTransaction(u, c, t).send()
	  println("DONE (transaction_id %s)".format(response.transaction.id))
	  return response
	}
	
	def commitTransaction(t:Transaction): TransactionCommitResponse = 
	{
	  println("Commiting Transaction ...")
	  val response:TransactionCommitResponse = Configuration.Gateway.commitTransaction(t).send()
	  println("DONE (transaction_id %s)".format(response.transaction.id))
	  return response
	}
	
	def deleteCard(u:User, c:Card): CardDeleteResponse = 
		{
		println("Removing card ...")
		val response:CardDeleteResponse = Configuration.Gateway.deleteCard(u, c).send()
		println("DONE ( %s)".format(response.timestamp))
		return response
		}
	
    override def main(args: Array[String]) = {

		println("app55-scala - Integration Test")
    
	    // Create User
	    val email:String = "example." + Utils.timestamp() + "@scalatest.com"
	    val user = createUser(email).user
	    assert(email == user.email, "UserCreate: Email")
		assert("0123 456 7890" == user.phone, "UserCreate: Phone")
			
	    //Get user just created
		val check = getUser(user).user
		assert(user.email == check.email)
		assert(user.id == check.id)
		
		//create card
		val card = createCard(user).card
		assert(card != null)
		assert(card.token != null)
		
		//get list cards
		//val responseLC = listCards(user)
		//assert(responseLC.cards != null)
//!		assert(responseLC.cards.length > 1)
		//check just created in list
	//!	assert(responseLC.cards.contains(responseCC.card))
		
		//create transaction
		val checkT = createTransaction(user, card, new Transaction(null, "0.10", "GBP", "Scala Transaction", null, null, null)).transaction
		
		//commit transaction
		val commitT = commitTransaction(checkT)
		
		//create and commit
		val t = new Transaction(null, "0.20", "GBP", "Scala Transaction2", null, null, null)
		t.commit = Some(true)
		val checkT2 = createTransaction(user, card, t).transaction
		
		//commit annonymous transaction
		val checkT3 = createTransaction(new User("anon@scalatest.com", null, null, null), 
    		    new Card(holderName= "App55CardHolderAnon",
			    	number="4111111111111111",
			    	expiry="04/2019",
			    	securityCode = "111",
			    	address = new Address("8 Exchange Quay", null, "Manchester", "M5 3EJ", "GB")), 
			    t).transaction

		//delete card
		val delC = deleteCard(user, card)
		//chk deleted?

  }
}