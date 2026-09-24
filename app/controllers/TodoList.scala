package controllers

import lib.persistence.TodoRepository
import javax.inject._
import play.api.mvc._
import model.ViewValueTodoList
import model.ViewValueTodoItem
import model.TodoForm
import play.api.data.Form
import model.TodoAddData

@Singleton
class TodoListController @Inject() (
  val controllerComponents: ControllerComponents,
  todoRepository:           TodoRepository,
)(implicit ec:              scala.concurrent.ExecutionContext) extends BaseController {

  def index() = Action.async { implicit req =>
    renderTodoList(TodoForm.create).map(Ok(_))
  }

  def renderTodoList(form: Form[TodoAddData])(implicit req: Request[AnyContent]) = {
    val todosFuture      = todoRepository.getAllJoined()
    val categoriesFuture = todoRepository.getAllCategories()
    for {
      todos      <- todosFuture
      categories <- categoriesFuture
    } yield {
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
        title      = "Todo一覧",
        cssSrc     = Seq("main.css"),
        jsSrc      = Seq("main.js"),
        todos      = todosView,
        form       = form,
        categories = categories.flatMap(c => c.id.map(id => (id.toString, c.name)))
      )
      views.html.TodoList(vv)
    }
  }

}
