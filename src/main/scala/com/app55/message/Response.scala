package com.app55.message

import scala.collection.mutable
import scala.collection.SortedMap
import scala.collection.immutable.TreeMap
import scala.util.control.Breaks._

import com.app55.util.ReflectionUtil

import com.fasterxml.jackson.annotation.JsonProperty;

class Response() extends Message() {

  @JsonProperty(value = "sig")
  var signature: String = ""
  
  @JsonProperty(value = "ts")
  var timestamp: String = ""

	def isValidSignature(): Boolean = {
		val expectedSig = getExpectedSignature();
		return expectedSig.equals(signature);
	}

	def getExpectedSignature(): String = {
		return toSignature();
	}

    
    private def populateAdditional(hashtable: Map[String, _], prefix: String): Unit = {
    val ht: SortedMap[String, Any] = TreeMap[String, Any]() ++ hashtable
    for (entry <- ht) {
      val key = entry._1
      val value = entry._2

      if (value != null) {
        if (value.isInstanceOf[Map[_, Any]]) {
          populateAdditional(value.asInstanceOf[Map[String, Any]], prefix + key + ".");
        } else if (value.isInstanceOf[List[_]]) {
          var i: Int = 0;
          for (arrayItem <- value.asInstanceOf[List[Any]]) {
            if (arrayItem.isInstanceOf[Map[_, _]]) {
              populateAdditional(arrayItem.asInstanceOf[Map[String, Any]], prefix + key + "." + (i += 1) + ".")
            } else {
              additionalFields.put(prefix + key + "." + (i += 1), arrayItem.toString());
            }
          }
        } else
          additionalFields.put(prefix + key, value.toString());
      }
    }
  }

  def populate(hashtable: Map[String, _]): Unit = {
    populate(hashtable, null)
  }

  private def populate(hashtable: Map[String, _], o: Any): Unit = {
    populate(hashtable, o, "")
  }

  private def populate(hashtable: Map[String, _], o1: Any, prefix: String): Unit = {
    val o = if (o1 == null) this else o1

    var properties = ReflectionUtil.getAllProperties(o);
    for (entry <- hashtable) {
      breakable {
        try {
          val key = entry._1
          val value = entry._2

          if (value == null || !properties.containsKey(key)) {
            if (value != null) {
              if (value.isInstanceOf[Map[_, _]]) {
                populateAdditional(value.asInstanceOf[Map[String,Any]], prefix + key + ".");
              } else if (value.isInstanceOf[List[_]]) {
                var i = 0
                for (arrayItem <- value.asInstanceOf[List[Any]]) {
                  if (arrayItem.isInstanceOf[Map[_, _]]) {
                    populateAdditional(arrayItem.asInstanceOf[Map[String, Any]], prefix + key + "." + (i+=1) +".")
                  } else {
                    additionalFields.put(prefix + key + "." + (i+=1), arrayItem.toString());
                  }
                }
              } else
                additionalFields.put(prefix + key, value.toString());
            }
            break
          }
        } catch {
          case e: Exception => ;
        }
      }
    }
  }

}