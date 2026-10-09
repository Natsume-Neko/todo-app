package controllers.api

import lib.persistence.TodoRepository
import javax.inject._
import play.api.mvc._
import model.ViewValueTodoList
import model.ViewValueTodoItem
import model.TodoForm
import play.api.data.Form
import model.TodoAddData
import lib.model.Todo
import lib.model.TodoCategory
import play.api.i18n.I18nSupport
import model.TodoUpdateData
import lib.persistence.TodoCategoryRepository
import play.api.libs.json._

@Singleton
class TodoApiController @Inject() (
  val controllerComponents: ControllerComponents,
  todoRepository:           TodoRepository,
)(implicit ec:              scala.concurrent.ExecutionContext)
  extends BaseController {
  implicit val todoWrites: OWrites[ViewValueTodoItem] = Json.writes[ViewValueTodoItem]

  def list() = Action.async { implicit req =>
    val todosFuture = todoRepository.getAllJoined()
    todosFuture.map(todos => {
      val todosView = todos.map {
        case (todo, category) => ViewValueTodoItem.from(todo.toEmbeddedId, category)
      }
      Ok(Json.toJson(todosView))
    })
  }
}
