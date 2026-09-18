package controllers

import lib.persistence.TodoRepository
import javax.inject._
import play.api.mvc._
import model.ViewValueTodoList
import model.ViewValueTodoItem

@Singleton
class TodoListController @Inject() (
  val controllerComponents: ControllerComponents,
  todoRepository:           TodoRepository,
)(implicit ec:              scala.concurrent.ExecutionContext) extends BaseController {

  def index() = Action.async { implicit req =>
    todoRepository.getAllJoined().map(todos => {
      val todosView = todos.map(todo =>
        ViewValueTodoItem.tupled((
          Seq("main.css"),
          Seq("main.js"),
          todo._1.title,
          todo._1.body,
          todo._1.state,
          todo._2.name,
          todo._2.color
        ))
      )
      val vv        = ViewValueTodoList(
        title  = "Todo一覧",
        cssSrc = Seq("main.css"),
        jsSrc  = Seq("main.js"),
        todos  = todosView,
      )
      Ok(views.html.TodoList(vv))
    })
  }
}
