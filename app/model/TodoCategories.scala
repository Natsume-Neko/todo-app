package model

import lib.model.TodoCategory
import model.Utils.colorClass

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
}

case class ViewValueCategories(
  title:      String,
  cssSrc:     Seq[String],
  jsSrc:      Seq[String],
  categories: Seq[ViewValueCategoryItem],
) extends ViewValueCommon
