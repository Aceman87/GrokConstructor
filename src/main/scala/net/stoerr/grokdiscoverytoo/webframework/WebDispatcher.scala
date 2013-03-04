package net.stoerr.grokdiscoverytoo.webframework

import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import net.stoerr.grokdiscoverytoo.matcher.MatcherEntryView
import net.stoerr.grokdiscoverytoo.incremental.{IncrementalConstructionStepView, IncrementalConstructionInputView}

/**
 * Servlet that forwards the request to a controller and displays the view.
 * @author <a href="http://www.stoerr.net/">Hans-Peter Stoerr</a>
 * @since 15.02.13
 */
class WebDispatcher extends HttpServlet {

  override def doPost(req: HttpServletRequest, resp: HttpServletResponse) {
    doGet(req, resp)
  }

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    val view: Either[String, WebView] = giveView(req.getPathInfo, req)
    view match {
      case Left(url) =>
        resp.sendRedirect(url)
      case Right(view) =>
        req.setAttribute("title", view.title)
        req.setAttribute("body", view.body)
        getServletContext.getRequestDispatcher("/frame.jsp").forward(req, resp)
    }
  }

  def giveView(path: String, request: HttpServletRequest): Either[String, WebView] = {
    val view = ("/web" + path) match {
      case MatcherEntryView.path => new MatcherEntryView(request)
      case IncrementalConstructionInputView.path => new IncrementalConstructionInputView(request)
      case IncrementalConstructionStepView.path => new IncrementalConstructionStepView(request)
    }
    val forward: Option[Either[String, WebView]] = view.doforward
    forward match {
      case Some(res) => res
      case None => Right(view)
    }
  }
}
