package controllers

import lib.persistence.TodoRepository
import javax.inject._
import play.api.mvc._
import scala.concurrent.ExecutionContext
import model.ViewValueCategoryItem
import model.ViewValueCategories
import model.CategoryForm
import play.api.data.Form
import model.CategoryAddData
import lib.model.TodoCategory
import play.api.i18n.I18nSupport
import model.CategoryEditData

@Singleton
class TodoCategoryController @Inject() (
  val controllerComponents: ControllerComponents,
  todoRepository:           TodoRepository,
)(implicit ec:              ExecutionContext)
  extends BaseController with I18nSupport {

  def index(edit: Option[Long] = None) = Action.async { implicit req =>
    renderCategories(CategoryForm.create, edit, None).map(Ok(_))
  }

  def create() = Action.async { implicit req =>
    val boundForm = CategoryForm.create.bindFromRequest()
    boundForm.fold(
      formWithError =>
        renderCategories(formWithError, None, None).map(BadRequest(_)),
      formData => {
        val category = TodoCategory(
          None,
          formData.name,
          formData.slug,
          TodoCategory.Color(formData.color),
        ).toWithNoId

        todoRepository.addCategory(category).map(_ => Redirect(routes.TodoCategoryController.index()))
      }
    )
  }

  def edit(id: Long) = Action.async { implicit req =>
    val boundForm = CategoryForm.edit.bindFromRequest()
    boundForm.fold(
      formWithError =>
        renderCategories(
          CategoryForm.create,
          Some(id),
          Some(formWithError)
        ).map(BadRequest(_)),
      formData => {
        todoRepository.editCategory(
          TodoCategory.Id(id),
          formData.name,
          formData.slug,
          TodoCategory.Color(formData.color),
        ).map {
          case 0 => NotFound
          case _ => Redirect(routes.TodoCategoryController.index())
        }
      }
    )
  }

  def delete(id: Long) = Action.async { implicit req =>
    todoRepository.deleteCategoryWithTodos(TodoCategory.Id(id)).map {
      case 0 => NotFound
      case _ => Redirect(routes.TodoCategoryController.index())
    }
  }

  private def renderCategories(
    createForm:     Form[CategoryAddData],
    editingId:      Option[Long],
    editingFormOpt: Option[Form[CategoryEditData]],
  )(implicit req:   Request[AnyContent]) = {
    val categoriesFuture = todoRepository.getAllCategories()
    categoriesFuture.map(categories => {
      val categoriesView = categories.map(ViewValueCategoryItem.from(_))
      val editingForm    = editingFormOpt.getOrElse(
        editingId
          .flatMap(id => categories.find(_.id.contains(id)))
          .map(c =>
            CategoryForm.edit.fill(
              CategoryEditData(c.name, c.slug, c.color.code)
            )
          ).getOrElse(CategoryForm.edit)
      )

      val vv = ViewValueCategories(
        title        = "カテゴリ一覧",
        cssSrc       = Seq("main.css"),
        jsSrc        = Seq("main.js"),
        categories   = categoriesView,
        createForm   = createForm,
        colorOptions = ViewValueCategoryItem.colorOptions,
        editingId    = editingId,
        editingForm  = editingForm,
      )
      views.html.Categories(vv)
    })
  }
}
