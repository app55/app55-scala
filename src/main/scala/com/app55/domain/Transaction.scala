package com.app55.domain

import scala.reflect.BeanInfo
import scala.annotation.target.field
import scala.annotation.meta.{field, getter, setter, param} //,beanSetter,beanGetter}
import com.fasterxml.jackson.annotation.{JsonCreator, JsonIgnore, JsonIgnoreProperties, JsonProperty}

//@JsonIgnoreProperties(ignoreUnknown = true)
@(JsonCreator)
@BeanInfo class Transaction(val id:String,
				 	val amount:String,
				 	val currency:String,
				 	val description:String,
				 	val code:String,
				 	@(JsonProperty @getter @setter @param )("auth_code")
				 	val authCode:String,
				 	@(JsonProperty @getter @setter @param )("type")
				 	val typ:String) {
	var commit:Option[Boolean] = null
}