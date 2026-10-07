package model

import lib.model.TodoCategory
import model.ViewLabels.colorClass
import play.api.data._
import play.api.data.Forms._

case class ViewValueCategoryItem(
  id:         Long,
  name:       String,
  slug:       String,
  colorClass: String,
)

object ViewValueCategoryItem {
  def from(category: TodoCategory): ViewValueCategoryItem =
    ViewValueCategoryItem(
      id         = category.id.get,
      name       = category.name,
      slug       = category.slug,
      colorClass = colorClass(category.color),
    )

  val colorOptions = TodoCategory.Color.values.map(c => (c.code.toString(), colorClass(c)))
}

case class ViewValueCategories(
  title:        String,
  cssSrc:       Seq[String],
  jsSrc:        Seq[String],
  categories:   Seq[ViewValueCategoryItem],
  createForm:   Form[CategoryAddData],
  colorOptions: Seq[(String, String)],
  editingId:    Option[Long],
  editingForm:  Form[CategoryEditData],
) extends ViewValueCommon
