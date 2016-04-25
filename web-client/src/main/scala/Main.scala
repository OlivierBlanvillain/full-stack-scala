package client

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import org.scalajs.dom

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import model._

object Main extends JSApp {
  val baseUrl = BaseUrl(dom.window.location.href.takeWhile(_ != '#'))

  val routerConfig: RouterConfig[TodoFilter] = RouterConfigDsl[TodoFilter].buildConfig { dsl =>
    import dsl._

    // How the application renders the list given a filter
    def filterRoute(s: TodoFilter): Rule = staticRoute("#/" + s.link, s) ~> renderR(CTodoList(model, s))

    val filterRoutes: Rule = TodoFilter.values.map(filterRoute).reduce(_ | _)

    // Build a final RouterConfig with a default page
    filterRoutes.notFound(redirectToPage(TodoFilter.All)(Redirect.Replace))
  }

  // Instantiate model and restore todos
  val model = new TodoModel()

  model.restorePersisted.runNow()

  /** The router is itself a React component, which at this point is not mounted (U-suffix) */
  val router: ReactComponentU[Unit, Resolution[TodoFilter], Any, TopNode] =
    Router(baseUrl, routerConfig.logToConsole)()

  /**
   * Main entry point, which the sbt plugin finds and makes the browser run.
   *
   * Takes the unmounted router component and gives to React,
   *  will render into the first element with `todoapp` class
   */
  @JSExport
  override def main(): Unit = {
    val mounted = ReactDOM.render(router, dom.document.getElementsByClassName("todoapp")(0))
    throw new Exception("Testing source maps");
  }
}
