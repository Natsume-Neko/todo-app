package controllers.api

import lib.persistence.TodoCategoryRepository
import javax.inject._
import play.api.mvc._
import scala.concurrent.ExecutionContext
import model.ViewValueCategoryItem
import model.ViewValueCategories
import model.CategoryForm
import play.api.data.Form
import model.CategoryData
import lib.model.TodoCategory
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.libs.json.OWrites

@Singleton
class TodoCategoryApiController @Inject() (
  val controllerComponents: ControllerComponents,
  todoCategoryRepository:   TodoCategoryRepository,
)(implicit ec:              ExecutionContext)
  extends BaseController {
  implicit val categoryWrites: OWrites[ViewValueCategoryItem] = 
    Json.writes[ViewValueCategoryItem]

  def list() = Action.async { implicit req =>
    val categoriesFuture = todoCategoryRepository.getAllCategories()
    categoriesFuture.map(categories => {
      val categoriesView = categories
        .map(category => ViewValueCategoryItem.from(category))
      Ok(Json.toJson(categoriesView))
    })
  }
}
