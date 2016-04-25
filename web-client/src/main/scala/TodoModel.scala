package client

import japgolly.scalajs.react.{Callback, CallbackTo}
import japgolly.scalajs.react.extra.Broadcaster
import scala.concurrent.ExecutionContext.Implicits.global
import autowire._
import boopickle.Default._
import model._

class TodoModel extends Broadcaster[Seq[Todo]] {
  private object State {
    var todos = Seq.empty[Todo]

    def mod(f: Seq[Todo] => Seq[Todo]): Callback = {
      val newTodos = f(todos)

      for {
        _ <- Callback(todos = newTodos)
        _ <- store(newTodos)
        c <- broadcast(newTodos)
      } yield c
    }

    def modOne(Id: TodoId)(f: Todo => Todo): Callback =
      mod(_.map {
        case existing @ Todo(Id, _, _) => f(existing)
        case other                     => other
      })
  }

  def restorePersisted: Callback =
    Callback.future(AutowireClient[Api].load().call().map(existing => State.mod(_ ++ existing)))

  def store(newTodos: Seq[Todo]): Callback =
    CallbackTo { AutowireClient[Api].store(newTodos).call(); () }

  def addTodo(title: Title): Callback =
    State.mod(_ :+ Todo(TodoId.random, title, isCompleted = false))

  def clearCompleted: Callback =
    State.mod(_.filterNot(_.isCompleted))

  def delete(id: TodoId): Callback =
    State.mod(_.filterNot(_.id == id))

  def todos: Seq[Todo] =
    State.todos

  def toggleAll(checked: Boolean): Callback =
    State.mod(_.map(_.copy(isCompleted = checked)))

  def toggleCompleted(id: TodoId): Callback =
    State.modOne(id)(old => old.copy(isCompleted = !old.isCompleted))

  def update(id: TodoId, text: Title): Callback =
    State.modOne(id)(_.copy(title = text))
}
