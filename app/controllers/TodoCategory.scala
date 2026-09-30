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

@Singleton
class TodoCategoryController @Inject() (
  val controllerComponents: ControllerComponents,
  todoRepository:           TodoRepository,
)(implicit ec:              ExecutionContext)
  extends BaseController with I18nSupport {

  def index() = Action.async { implicit req =>
    renderCategories(CategoryForm.create).map(Ok(_))
  }

  def create() = Action.async { implicit req =>
    val boundForm = CategoryForm.create.bindFromRequest()
    boundForm.fold(
      formWithError =>
        renderCategories(formWithError).map(BadRequest(_)),
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

  private def renderCategories(
    createForm:   Form[CategoryAddData],
  )(implicit req: Request[AnyContent]) = {
    val categories = todoRepository.getAllCategories()
    categories.map(category => {
      val categoriesView = category.map(ViewValueCategoryItem.from(_))

      val vv = ViewValueCategories(
        title        = "カテゴリ一覧",
        cssSrc       = Seq("main.css"),
        jsSrc        = Seq("main.js"),
        categories   = categoriesView,
        createForm   = createForm,
        colorOptions = ViewValueCategoryItem.colorOptions,
      )
      views.html.Categories(vv)
    })
  }
}
