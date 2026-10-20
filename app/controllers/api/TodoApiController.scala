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
import model.ApiValueTodoItem
import scala.concurrent.Future

@Singleton
class TodoApiController @Inject() (
  val controllerComponents: ControllerComponents,
  todoRepository:           TodoRepository,
  todoCategoryRepository:   TodoCategoryRepository,
)(implicit ec:              scala.concurrent.ExecutionContext)
  extends BaseController with I18nSupport {
  implicit val todoWrites: OWrites[ApiValueTodoItem] = Json.writes[ApiValueTodoItem]

  def list() = Action.async { implicit req =>
    val todosFuture = todoRepository.getAllJoined()
    todosFuture.map(todos => {
      val todosView = todos.map {
        case (todo, category) => ApiValueTodoItem.from(todo.toEmbeddedId, category)
      }
      Ok(Json.toJson(todosView))
    })
  }

  def create() = Action.async { implicit req =>
    val boundForm = TodoForm.create.bindFromRequest()
    boundForm.fold(
      formWithError =>
        Future.successful(BadRequest(formWithError.errorsAsJson)),
      formData => {
        val categoryId = TodoCategory.Id(formData.categoryId)
        todoCategoryRepository.getCategoryById(categoryId).flatMap {
          case Some(_) => {
            val todo = Todo(
              None,
              categoryId,
              formData.title,
              formData.body,
              Todo.State.NotBegin,
            ).toWithNoId
            todoRepository.addTodo(todo).map(newId =>
              Created(Json.obj("id" -> newId.toLong))
            )
          }
          case None    => Future.successful(BadRequest(boundForm.withError(
            "categoryId",
            "error.category.notFound"
          ).errorsAsJson))
        }
      }
    )
  }
}
