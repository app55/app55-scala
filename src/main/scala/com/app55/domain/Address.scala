package com.app55.domain

import scala.reflect.BeanInfo
import scala.annotation.target.field
import scala.annotation.meta.{field, getter, setter, param} //,beanSetter,beanGetter}
import com.fasterxml.jackson.annotation.{JsonCreator, JsonIgnoreProperties, JsonProperty}

@JsonIgnoreProperties(ignoreUnknown = true)
@BeanInfo class Address(val street:String, 
						val street2:String, 
						val city:String, 
						@(JsonProperty @getter @setter @param )("postal_code")
						val postalCode:String, 
						val country:String) {

}