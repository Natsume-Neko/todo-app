package model

import play.api.data._
import play.api.data.Forms._
import lib.model.TodoCategory

case class CategoryAddData(name: String, slug: String, color: Short)
case class CategoryEditData(name: String, slug: String, color: Short)

object CategoryForm {
  private val nameConstraint = nonEmptyText(maxLength = 255)
    .verifying(
      "error.name.newline",
      name => "\\R".r.findFirstIn(name).isEmpty,
    )

  private val slugConstraint = nonEmptyText(maxLength = 64)
    .verifying(
      "error.slug.alphanumeric",
      slug => slug.matches("[a-zA-Z0-9]+"),
    )

  private val colorConstraint = shortNumber
    .verifying(
      "error.color.invalid",
      color => TodoCategory.Color.values.exists(_.code == color),
    )

  val create: Form[CategoryAddData] = Form(
    mapping(
      "name"  -> nameConstraint,
      "slug"  -> slugConstraint,
      "color" -> colorConstraint,
    )(CategoryAddData.apply)(CategoryAddData.unapply)
  )

  val edit: Form[CategoryEditData] = Form(
    mapping(
      "name"  -> nameConstraint,
      "slug"  -> slugConstraint,
      "color" -> colorConstraint,
    )(CategoryEditData.apply)(CategoryEditData.unapply)
  )
}
