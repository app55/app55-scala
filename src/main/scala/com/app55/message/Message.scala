package com.app55.message

import com.app55.Gateway
import java.util.Collection;

import scala.collection.mutable
import scala.collection.immutable.TreeMap
import scala.util.control.Breaks._
import java.beans.PropertyDescriptor
import java.lang.reflect.Method
import java.net.URLEncoder

import com.app55.converter.Converter
import com.app55.util.EncodeUtil
import com.app55.util.ReflectionUtil

//import scala.reflect.runtime.currentMirror
import scala.collection.mutable.HashMap
import scala.collection.mutable.Map
import scala.collection.SortedMap
import scala.collection.JavaConversions._

abstract class Message() {
  var gateway: Gateway = null

  val additionalFields = HashMap[String, String]();

  def this(gateway: Gateway) = {
    this()
    this.gateway = gateway
  }

  def toSignature(): String =
    {
      return toSignature(false);
    }

  protected def toSignature(includeApiKey: Boolean): String =
    {
      val exclude = HashMap[String, Boolean]();
      exclude += ("sig" -> true)
      val formData = toFormData(includeApiKey, exclude);
      println("FormData: " + formData)
      val digest = EncodeUtil.sha1(gateway.apiSecret + formData);
      return EncodeUtil.base64(digest);
    }

  protected def toFormData(includeApiKey: Boolean, exclude: Map[String, Boolean]): String = {
    try {
      var formData = "";
      var description: SortedMap[String, String] = TreeMap[String, String]() ++ describe(this, null, exclude);

      if (includeApiKey) {
        description += ("api_key" -> gateway.apiKey)
        if (exclude == null || !exclude.contains("sig")) {
          description -= "sig"
          description += ("sig" -> toSignature(true))
        }
      }
      additionalFields -= "sig" // exclude sig from signing.
      description ++= additionalFields;

      for (entry <- description)
        formData += "&" + entry._1 + "=" + URLEncoder.encode(entry._2, "UTF-8");

      if (formData.length() > 0)
        formData = formData.substring(1)

      formData = formData.replace("+", "%20")
      formData = formData.replace("+", "%20")

      return formData;
    } catch {
      case e: Exception => return null;
    }
  }
  private def testAnyVal[T: Manifest](t: T) = manifest[T] <:< manifest[AnyVal]

  private def describe(o: Any): Map[String, String] =
    {
      return describe(o, null, null)
    }

  private def describe(o: Any, prefix: String): Map[String, String] =
    {
      return describe(o, prefix, null)
    }

  private def describe(o: Any, pre: String, exclude: Map[String, Boolean]): Map[String, String] = {
    if (o == null)
      return Map[String, String]();

    val prefix = if (pre == null) "" else pre + "."

    var description = Map[String, String]()

    var properties: Map[String, PropertyDescriptor] = ReflectionUtil.getAllProperties(o);

    for (property <- properties) {
      breakable {
        val m: Method = property._2.getReadMethod();

        if (exclude != null && exclude.contains(property._1))
          break

        var value: Any = null;
        try {
          value = m.invoke(o);
        } catch {
          case e: Exception => ;
        }

        value match {
          case null => //break/continue
          case x: Option[_] =>
            description += (prefix + property._1 -> x.get.toString);
          case x: String =>
            description += (prefix + property._1 -> x.toString);
          case x: Number =>
            description += (prefix + property._1 -> x.toString);
          case x: Boolean =>
            description += (prefix + property._1 -> x.toString);
          case s: Collection[_] => {
            var i: Int = 0;

            for (obj <- s) {
              for (entry <- describe(obj, prefix + property._1 + '.' + i).entrySet())
                description.put(entry.getKey(), entry.getValue());
              i += 1;
            }
          }
          case x => {
            for (entry <- describe(x, prefix + property._1).entrySet())
              description.put(entry.getKey(), entry.getValue());
          }
        }
      }
    }

    return description;
  }

}