package org.jsun.dicely.util

import com.netaporter.uri.Uri.parse
import org.apache.commons.validator.routines.UrlValidator

/**
  * Created by jsun on 6/9/2017 AD.
  */
trait UrlEnricher {

  private val urlValidator = new UrlValidator()

  def enrichUrl(url:String):Option[String] = {
    val strippedUrl = url.replace(" ","")
    val s = parse(strippedUrl)
    val enrichedUrl = if (s.protocol.isEmpty) s"http://$strippedUrl" else strippedUrl
    if (urlValidator.isValid(enrichedUrl)) Some (enrichedUrl) else None
  }

}
