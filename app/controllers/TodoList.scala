package controllers

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

@Singleton
class TodoListController @Inject() (
  val controllerComponents: ControllerComponents,
  todoRepository:           TodoRepository,
)(implicit ec:              scala.concurrent.ExecutionContext)
  extends BaseController with I18nSupport {

  def index() = Action.async { implicit req =>
    renderTodoList(TodoForm.create).map(Ok(_))
  }

  def create() = Action.async { implicit req =>
    val boundForm = TodoForm.create.bindFromRequest()
    boundForm.fold(
      formWithError => renderTodoList(formWithError).map(BadRequest(_)),
      formData => {
        val categoryId = TodoCategory.Id(formData.categoryId)
        todoRepository.getCategoryById(categoryId).flatMap {
          case Some(_) => {
            val todo = Todo(
              None,
              categoryId,
              formData.title,
              formData.body,
              Todo.State.NotBegin,
            ).toWithNoId
            todoRepository.addTodo(todo).map(_ =>
              Redirect(routes.TodoListController.index())
            )
          }
          case None    => renderTodoList(boundForm.withError(
              "categoryId",
              "error.category.notFound"
            )).map(BadRequest(_))
        }
      }
    )
  }

  private def renderTodoList(form: Form[TodoAddData])(implicit
    req:                           Request[AnyContent]
  ) = {
    val todosFuture      = todoRepository.getAllJoined()
    val categoriesFuture = todoRepository.getAllCategories()
    for {
      todos      <- todosFuture
      categories <- categoriesFuture
    } yield {
      val todosView = todos.map { case (todo, category) =>
        ViewValueTodoItem.from(todo, category)
      }
      val vv        = ViewValueTodoList(
        title       = "Todo一覧",
        cssSrc      = Seq("main.css"),
        jsSrc       = Seq("main.js"),
        todos       = todosView,
        createForm  = form,
        categories  =
          categories.flatMap(c => c.id.map(id => (id.toString, c.name))),
        editingId   = None,
        editingForm = TodoForm.edit,
      )
      views.html.TodoList(vv)
    }
  }

}
