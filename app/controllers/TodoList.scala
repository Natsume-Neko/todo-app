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
import model.TodoUpdateData

@Singleton
class TodoListController @Inject() (
  val controllerComponents: ControllerComponents,
  todoRepository:           TodoRepository,
)(implicit ec:              scala.concurrent.ExecutionContext)
  extends BaseController with I18nSupport {

  def index(edit: Option[Long] = None) = Action.async { implicit req =>
    renderTodoList(TodoForm.create, edit, None).map(Ok(_))
  }

  def create() = Action.async { implicit req =>
    val boundForm = TodoForm.create.bindFromRequest()
    boundForm.fold(
      formWithError =>
        renderTodoList(
          formWithError,
          None,
          None,
        ).map(BadRequest(_)),
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
          case None    => renderTodoList(
              boundForm.withError(
                "categoryId",
                "error.category.notFound"
              ),
              None,
              None,
            ).map(BadRequest(_))
        }
      }
    )
  }

  def edit(id: Long) = Action.async { implicit req =>
    val editingForm = TodoForm.edit.bindFromRequest()
    editingForm.fold(
      formWithError =>
        renderTodoList(
          TodoForm.create,
          Some(id),
          Some(formWithError),
        ).map(BadRequest(_)),
      formData => {
        val categoryId = TodoCategory.Id(formData.categoryId)
        todoRepository.getCategoryById(categoryId).flatMap {
          case Some(_) => todoRepository.editTodo(
              Todo.Id(id),
              categoryId,
              formData.title,
              formData.body,
              Todo.State(formData.state),
            ).map {
              case 0 => NotFound
              case _ => Redirect(routes.TodoListController.index())
            }

          case None => renderTodoList(
              TodoForm.create,
              Some(id),
              Some(editingForm.withError(
                "categoryId",
                "error.category.notFound"
              )),
            ).map(BadRequest(_))
        }
      }
    )
  }

  def delete(id: Long) = Action.async { implicit req =>
    todoRepository.deleteTodo(Todo.Id(id)).map {
      case 0 => NotFound
      case _ => Redirect(routes.TodoListController.index())
    }
  }

  private def renderTodoList(
    createForm:     Form[TodoAddData],
    editingId:      Option[Long],
    editingFormOpt: Option[Form[TodoUpdateData]],
  )(implicit req:   Request[AnyContent]) = {
    val todosFuture      = todoRepository.getAllJoined()
    val categoriesFuture = todoRepository.getAllCategories()
    for {
      todos      <- todosFuture
      categories <- categoriesFuture
    } yield {
      val todosView = todos.map { case (todo, category) =>
        ViewValueTodoItem.from(todo, category)
      }

      val editingForm = editingFormOpt.getOrElse(
        editingId
          .flatMap(id => todos.find { case (t, _) => t.id.contains(id) })
          .map { case (t, _) =>
            TodoForm.edit.fill(
              TodoUpdateData(t.categoryId, t.title, t.body, t.state.code)
            )
          }.getOrElse(TodoForm.edit)
      )

      val vv = ViewValueTodoList(
        title        = "Todo一覧",
        cssSrc       = Seq("main.css"),
        jsSrc        = Seq("main.js"),
        todos        = todosView,
        createForm   = createForm,
        stateOptions = ViewValueTodoItem.stateOptions,
        categories   =
          categories.flatMap(c => c.id.map(id => (id.toString, c.name))),
        editingId    = editingId,
        editingForm  = editingForm,
      )
      views.html.TodoList(vv)
    }
  }

}
