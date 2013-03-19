package net.stoerr.grokdiscoverytoo.incremental

import javax.servlet.http.HttpServletRequest
import net.stoerr.grokdiscoverytoo.webframework.WebView
import xml.NodeSeq
import net.stoerr.grokdiscoverytoo.RandomTryLibrary
import net.stoerr.grokdiscoverytoo.webframework.TableMaker._

/**
 * Entry for the start parameters for the incremental construction of grok patterns.
 * @author <a href="http://www.stoerr.net/">Hans-Peter Stoerr</a>
 * @since 01.03.13
 */
class IncrementalConstructionInputView(val request: HttpServletRequest) extends WebView {
  val title: String = "Incremental Construction of Grok Patterns"
  val action: String = IncrementalConstructionStepView.path

  val form = IncrementalConstructionForm(request)

  if (null != request.getParameter("example")) {
    val trial = RandomTryLibrary.example(request.getParameter("example").toInt)
    form.loglines.value = Some(trial.loglines)
    form.multlineRegex.value = trial.multline
    form.multlineNegate.values = List(form.multlineNegate.name)
    form.groklibs.values = List("grok-patterns")
  }

  def inputform: NodeSeq =
    <p>Please enter some loglines you want to construct a grok pattern for and then press
      {submit("Go!")}
      You can also just fill this with a
      {buttonanchor(action + "?randomize", "random example.")}
    </p> ++
      form.loglinesEntry ++
      form.grokpatternEntry ++
      form.multlineEntry

  // missing: extra patterns by hand

  def result: NodeSeq = <span/>

}

object IncrementalConstructionInputView {
  val path = "/web/construction"
}
