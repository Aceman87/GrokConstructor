package net.stoerr.grokdiscoverytoo.webframework

import xml.NodeSeq

/**
 * Standard frame that consists of a main text, a sidebox with an additional action one can take, an input form and a result
 * @author <a href="http://www.stoerr.net/">Hans-Peter Stoerr</a>
 * @since 21.03.13
 */
abstract class WebViewWithHeaderAndSidebox extends WebView {

  override def inputform: NodeSeq =
    <div class="ym-grid">
      <div class="ym-g75 ym-gl">
        <div class="box info">
          {maintext}
        </div>
      </div>
      <div class="ym-g25 ym-gr">
        <div class="box info">
          {sidebox}
        </div>
      </div>
    </div> ++ formparts

  def maintext: NodeSeq

  def sidebox: NodeSeq

  def formparts: NodeSeq

}
