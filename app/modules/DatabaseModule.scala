package modules

import javax.inject.{ Inject, Provider, Singleton }
import scala.concurrent.Future
import com.google.inject.name.Names
import com.google.inject.AbstractModule
import play.api.inject.ApplicationLifecycle
import ixias.slick.model.DataSourceName
import ixias.slick.builder._
import ixias.slick.jdbc.MySQLProfile.api.Database
import com.zaxxer.hikari.HikariDataSource

@Singleton
class MasterDatabaseProvider @Inject() (
  lifecycle: ApplicationLifecycle
) extends Provider[Database] {

  private val hikariConfigBuilder = HikariConfigBuilder.default(DataSourceName("ixias.db.mysql://master/to_do"))
  private val hikariConfig        = hikariConfigBuilder.build()
  hikariConfig.validate()

  private val dataSource = new HikariDataSource(hikariConfig)

  lifecycle.addStopHook { () =>
    Future.successful(dataSource.close())
  }

  override def get(): Database = DatabaseBuilder.fromHikariDataSource(dataSource)
}

@Singleton
class SlaveDatabaseProvider @Inject() (
  lifecycle: ApplicationLifecycle
) extends Provider[Database] {

  private val hikariConfigBuilder = HikariConfigBuilder.default(DataSourceName("ixias.db.mysql://slave/to_do"))
  private val hikariConfig        = hikariConfigBuilder.build()
  hikariConfig.validate()

  private val dataSource = new HikariDataSource(hikariConfig)

  lifecycle.addStopHook { () =>
    Future.successful(dataSource.close())
  }

  override def get(): Database = DatabaseBuilder.fromHikariDataSource(dataSource)
}

class DatabaseModule extends AbstractModule {
  // 以下のサイトを参考に実装してみましょう
  // https://nextbeat-dev.github.io/ixias/03-Slick.html
  override def configure(): Unit = {
    bind(classOf[Database])
      .annotatedWith(Names.named("master"))
      .toProvider(classOf[MasterDatabaseProvider])
      .asEagerSingleton()
    bind(classOf[Database])
      .annotatedWith(Names.named("slave"))
      .toProvider(classOf[SlaveDatabaseProvider])
      .asEagerSingleton()
  }
}
