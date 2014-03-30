package net.stoerr.grokconstructor

import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}

/**
 * @author <a href="http://www.stoerr.net/">Hans-Peter Stoerr</a>
 * @since 07.02.13
 */
class GrokLibraryServlet extends HttpServlet {

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) {
    val path = request.getPathInfo
    if (path.startsWith("/")) {
      response.setContentType("text/plain")
      response.setStatus(HttpServletResponse.SC_OK)
      response.setDateHeader("Expires", System.currentTimeMillis() + 86400000L)
      GrokPatternLibrary.grokSource(path.substring(1)).getLines().foreach(response.getWriter.println(_))
    }
  }

}
