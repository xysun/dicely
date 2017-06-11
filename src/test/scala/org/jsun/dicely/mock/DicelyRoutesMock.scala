package org.jsun.dicely.mock

import org.jsun.dicely.UrlShortener
import org.jsun.dicely.routes.DicelyRoutes

/**
 * Created by jsun on 6/9/2017 AD.
 */
trait DicelyRoutesMock extends DicelyRoutes with DBMockPool

trait DicelyRoutesExceptionMock extends DicelyRoutes with DBExceptionMockPool
