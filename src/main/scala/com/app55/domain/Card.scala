package com.app55.domain

import scala.reflect.BeanInfo
import scala.annotation.target.field
import scala.annotation.meta.{field, getter, setter, param} //,beanSetter,beanGetter}
import com.fasterxml.jackson.annotation.{JsonCreator, JsonIgnoreProperties, JsonProperty}

@JsonIgnoreProperties(ignoreUnknown = true)
@BeanInfo class Card(val token:String = null,
			@(JsonProperty @getter @setter @param )("holder_name")
			val holderName:String,
			val number:String,
			val expiry:String = null,
			@(JsonProperty @getter @setter @param )("expiry_month")
			val expiryMonth:String = null,
			@(JsonProperty @getter @setter @param )("expiry_year")
			val expiryYear:String = null,
			@(JsonProperty @getter @setter @param )("security_code")
			val securityCode:String,
			val address:Address = null,
			@(JsonProperty @getter @setter @param )("type")
			val typ:String = null,
			val description:String = null,
			val issue:String = null) {
  
	val id : Option[Long] = null
}