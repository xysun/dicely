package org.jsun.dicely.db

/**
 * Created by jsun on 6/11/2017 AD.
 */
trait Closeable {

  def using[X <: { def close() }, A](resource: X)(f: X => A) = {
    try {
      f(resource)
    } finally {
      if (resource != null) resource.close()
    }
  }

}
