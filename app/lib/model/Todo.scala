package lib.model

import ixias.model._
import ixias.util.EnumStatus
import java.time.LocalDateTime

case class Todo(
  val id:         Option[Todo.Id],
  val categoryId: TodoCategory.Id,
  val title:      String,
  val body:       String,
  val state:      Todo.State,
  val updatedAt:  LocalDateTime = NOW,
  val createdAt:  LocalDateTime = NOW,
) extends EntityModel[Todo.Id]

object Todo {
  val  Id         = the[Identity[Id]]
  type Id         = Long @@ Todo
  type WithNoId   = Entity.WithNoId[Id, Todo]
  type EmbeddedId = Entity.EmbeddedId[Id, Todo]

  sealed abstract class State(val code: Short) extends EnumStatus
  object State extends EnumStatus.Of[State] {
    case object NotBegin extends State(code = 0)
    case object Doing    extends State(code = 1)
    case object Done     extends State(code = 2)
  }
}
