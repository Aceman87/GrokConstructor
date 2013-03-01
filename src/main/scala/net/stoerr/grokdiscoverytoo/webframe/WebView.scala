package net.stoerr.grokdiscoverytoo.webframe

import javax.servlet.http.HttpServletRequest
import xml.NodeSeq

/**
 * Basis for a View-Class that displays a page.
 * @author <a href="http://www.stoerr.net/">Hans-Peter Stoerr</a>
 * @since 15.02.13
 */
trait WebView {

  val request: HttpServletRequest

  val title: String

  /** action of the form, e.g. "/web/match" */
  val action: String

  def inputform: NodeSeq

  def result: NodeSeq

  def body = <body>
    <h1>
      {title}
    </h1>
    <form action={action} method="post">
      <table>
        {inputform}
      </table>{result}
    </form>
  </body>


}
