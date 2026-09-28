package lib.model

import ixias.model._
import ixias.util.EnumStatus
import java.time.LocalDateTime

case class TodoCategory(
  val id:        Option[TodoCategory.Id],
  val name:      String,
  val slug:      String,
  val color:     TodoCategory.Color,
  val updatedAt: LocalDateTime = NOW,
  val createdAt: LocalDateTime = NOW,
) extends EntityModel[TodoCategory.Id]

object TodoCategory {
  val  Id         = the[Identity[Id]]
  type Id         = Long @@ TodoCategory
  type WithNoId   = Entity.WithNoId[Id, TodoCategory]
  type EmbeddedId = Entity.EmbeddedId[Id, TodoCategory]

  sealed abstract class Color(val code: Short) extends EnumStatus
  object Color extends EnumStatus.Of[Color] {
    case object Blue   extends Color(code = 0)
    case object Green  extends Color(code = 1)
    case object Red    extends Color(code = 2)
    case object Yellow extends Color(code = 3)
  }
}
