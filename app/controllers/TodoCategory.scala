package controllers

import lib.persistence.TodoRepository
import javax.inject._
import play.api.mvc._
import scala.concurrent.ExecutionContext
import model.ViewValueCategoryItem
import model.ViewValueCategories

@Singleton
class TodoCategoryController @Inject() (
  val controllerComponents: ControllerComponents,
  todoRepository:           TodoRepository,
)(implicit ec:              ExecutionContext)
  extends BaseController {

  def index() = Action.async { implicit req =>
    val categories = todoRepository.getAllCategories()
    categories.map(category => {
      val categoriesView = category.map(ViewValueCategoryItem.from(_))

      val vv = ViewValueCategories(
        title      = "カテゴリ一覧",
        cssSrc     = Seq("main.css"),
        jsSrc      = Seq("main.js"),
        categories = categoriesView,
      )
      Ok(views.html.Categories(vv))
    })
  }
}
