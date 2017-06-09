package org.jsun.dicely.util

import com.netaporter.uri.Uri.parse
import org.apache.commons.validator.routines.UrlValidator

/**
  * Created by jsun on 6/9/2017 AD.
  */
trait UrlEnricher {

  private val urlValidator = new UrlValidator()

  def enrichUrl(url:String):Option[String] = {
    val s = parse(url)
    val enrichedUrl = if (s.protocol.isEmpty) s"http://$url" else url
    if (urlValidator.isValid(enrichedUrl)) Some (enrichedUrl) else None
  }

}
