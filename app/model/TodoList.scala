package model

import lib.model.Todo
import lib.model.TodoCategory
import play.api.data._
import play.api.data.Forms._

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

  private def stateLabel(state: Todo.State): String = state match {
    case Todo.State.NotBegin => "TODO"
    case Todo.State.Doing    => "進行中"
    case Todo.State.Done     => "完了"
  }

  private def colorClass(color: TodoCategory.Color): String = color match {
    case TodoCategory.Color.Blue   => "category--blue"
    case TodoCategory.Color.Green  => "category--green"
    case TodoCategory.Color.Red    => "category--red"
    case TodoCategory.Color.Yellow => "category--yellow"
  }

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
