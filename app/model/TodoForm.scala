package model

import play.api.data._
import play.api.data.Forms._

case class TodoAddData(title: String, body: String, categoryId: Long)

object TodoForm {
  val create: Form[TodoAddData] = Form(
    mapping(
      "title"      -> nonEmptyText(maxLength = 255),
      "body"       -> text,
      "categoryId" -> longNumber,
    )(TodoAddData.apply)(TodoAddData.unapply)
  )
}
