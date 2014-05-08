package com.app55.test.util

import java.text.SimpleDateFormat;
import java.util.Calendar;

object Utils {

  def timestamp(format: String, daysInFuture:Int = null.asInstanceOf[Int]): String = {
      var c = Calendar.getInstance()
      if (daysInFuture != null)
        c.add(Calendar.DAY_OF_YEAR, daysInFuture)
      return new SimpleDateFormat(format).format(c.getTime())
    }

  def timestamp () : String =  {
      return timestamp("yyyyMMddHHmmss")
    }

}