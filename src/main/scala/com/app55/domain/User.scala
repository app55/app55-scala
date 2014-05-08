package com.app55.domain

import scala.reflect.BeanInfo
import scala.annotation.target.field
import scala.annotation.meta.{field, getter, setter, param} //,beanSetter,beanGetter}
import com.fasterxml.jackson.annotation.{JsonCreator, JsonIgnoreProperties, JsonProperty}

@JsonIgnoreProperties(ignoreUnknown = true)
@BeanInfo class User (val email:String, 
					  val phone:String, 
					  val password:String, 
					  @(JsonProperty @getter @setter @param )("password_confirm") 
					  val confirmPassword:String) {
	
    var id : Option[Long] = null
	
	def this(id:Long) = { this(null, null, null, null); this.id=Some(id) }
}