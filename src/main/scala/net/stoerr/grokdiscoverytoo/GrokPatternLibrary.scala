package net.stoerr.grokdiscoverytoo

import util.matching.Regex
import scala.io.{BufferedSource, Source}

/**
 * @author <a href="http://www.stoerr.net/">Hans-Peter Stoerr</a>
 * @since 17.02.13
 */
object GrokPatternLibrary {

  val grokpatternnames = List("firewalls", "grok-patterns", "haproxy", "java", "linux-syslog", "nagios", "ruby")
  val grokpatternKeys = grokpatternnames.map(p => (p -> p)).toMap

  val extrapatternnames = List("extras")
  val extrapatternKeys = extrapatternnames.map(p => (p -> p)).toMap

  def mergePatternLibraries(libraries: List[String], extrapatterns: Option[String]): Map[String, String] = {
    val extrapatternlines: Iterator[String] = extrapatterns.map(Source.fromString(_).getLines()).getOrElse(Iterator())
    val grokPatternSources = for (grokfile <- libraries) yield grokSource(grokfile).getLines()
    val allPatternLines = grokPatternSources.fold(extrapatternlines)(_ ++ _)
    readGrokPatterns(allPatternLines)
  }

  def grokSource(location: String): Source = {
    if (!location.matches("^[a-z-]+$")) throw new IllegalArgumentException("Invalid path " + location)
    val stream: BufferedSource = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("/grok/" + location))
    if (null == stream) throw new IllegalArgumentException("Could not find " + location)
    return stream
  }

  /** Reads the patterns from a source */
  def readGrokPatterns(src: Iterator[String]): Map[String, String] = {
    val cleanedupLines = src.filterNot(_.trim.isEmpty).filterNot(_.startsWith("#"))
    val grokLine = "(\\w+) (.*)".r
    cleanedupLines.map {
      case grokLine(name, grokregex) => (name -> grokregex)
    }.toMap
  }

  /** We replace patterns like %{BLA:name} with the definition of bla. This is done
    * (arbitrarily) 10 times to allow recursions but to not allow infinite loops. */
  def replacePatterns(grokregex: String, grokMap: Map[String, String]): String = {
    var substituted = grokregex
    val grokReference = """%\{(\w+)(:(\w+))?\}""".r
    0 until 10 foreach {
      _ =>
        substituted = grokReference replaceAllIn(substituted, {
          m =>
            "(?" + Option(m.group(3)).map(Regex.quoteReplacement).map("<" + _ + ">").getOrElse(":") +
              Regex.quoteReplacement(grokMap(m.group(1))) + ")"
        })
    }
    substituted
  }

}
