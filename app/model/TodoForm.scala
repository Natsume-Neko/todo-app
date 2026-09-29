package model

import play.api.data._
import play.api.data.Forms._

case class TodoAddData(title: String, body: String, categoryId: Long)

object TodoForm {
  val create: Form[TodoAddData] = Form(
    mapping(
      "title"      -> nonEmptyText(maxLength = 255)
        .verifying(
          "error.title.newline",
          title => "\\R".r.findFirstIn(title).isEmpty,
        ),
      "body"       -> text(maxLength = 21845), // 上限65535 Bytes
      "categoryId" -> longNumber,
    )(TodoAddData.apply)(TodoAddData.unapply)
  )
}
