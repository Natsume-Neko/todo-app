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
      val todosView = todos.map { case (todo, category) =>
        ViewValueTodoItem.tupled((
          Seq("main.css"),
          Seq("main.js"),
          todo.title,
          todo.body,
          todo.state,
          category.name,
          category.color
        ))
      }
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
