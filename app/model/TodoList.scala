package model

import lib.model.Todo
import lib.model.TodoCategory
import play.api.data._
import play.api.data.Forms._
import model.Utils.colorClass
import model.Utils.stateLabel

case class ViewValueTodoItem(
  id:           Long,
  categoryId:   Long,
  title:        String,
  body:         String,
  stateLabel:   String, // "TODO" / "進行中" / "完了"
  categoryName: String,
  colorClass:   String, // "category--blue" など
)

object ViewValueTodoItem {
  def from(todo: Todo, category: TodoCategory): ViewValueTodoItem =
    ViewValueTodoItem(
      id           = todo.id.get,
      categoryId   = todo.categoryId,
      title        = todo.title,
      body         = todo.body,
      stateLabel   = stateLabel(todo.state),
      categoryName = category.name,
      colorClass   = colorClass(category.color),
    )

  val stateOptions: Seq[(String, String)] =
    Todo.State.values.map(s => (s.code.toString(), stateLabel(s)))
}

case class ViewValueTodoList(
  title:        String,
  cssSrc:       Seq[String],
  jsSrc:        Seq[String],
  todos:        Seq[ViewValueTodoItem],
  createForm:   Form[TodoAddData],
  categories:   Seq[(String, String)],
  stateOptions: Seq[(String, String)],
  editingId:    Option[Long],
  editingForm:  Form[TodoUpdateData],
) extends ViewValueCommon
