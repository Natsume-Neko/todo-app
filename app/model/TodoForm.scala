package model

import play.api.data._
import play.api.data.Forms._
import lib.model.Todo

case class TodoAddData(title: String, body: String, categoryId: Long)
case class TodoUpdateData(
  categoryId: Long,
  title:      String,
  body:       String,
  state:      Short,
)

object TodoForm {
  private val titleConstraint = nonEmptyText(maxLength = 255)
    .verifying(
      "error.title.newline",
      title => "\\R".r.findFirstIn(title).isEmpty,
    )
  private val bodyConstraint  = text(maxLength = 21845) // 上限65535 Bytes
  private val stateConstraint = shortNumber
    .verifying(
      "error.state.invalid",
      state => Todo.State.values.exists(_.code == state),
    )

  val create: Form[TodoAddData] = Form(
    mapping(
      "title"      -> titleConstraint,
      "body"       -> bodyConstraint,
      "categoryId" -> longNumber,
    )(TodoAddData.apply)(TodoAddData.unapply)
  )

  val edit: Form[TodoUpdateData] = Form(
    mapping(
      "categoryId" -> longNumber,
      "title"      -> titleConstraint,
      "body"       -> bodyConstraint,
      "state"      -> stateConstraint,
    )(TodoUpdateData.apply)(TodoUpdateData.unapply)
  )
}
