import mill._
import mill.scalalib._
import publish._
import ammonite.ops._
import mill.modules.Jvm.createJar

trait DgraphPublishModule extends PublishModule{
  def artifactName = T {"mill-dgraph"}
  def publishVersion = "0.2.0"

  def pomSettings = PomSettings(
    description = artifactName(),
    organization = "com.github.ajrnz",
    url = "https://github.com/ajrnz/mill-dgraph",
    licenses = Seq(
      License("MIT license", "http://www.opensource.org/licenses/mit-license.php")
    ),
    scm = SCM(
      "git://github.com/ajrnz/mill-dgraph.git",
      "scm:git://github.com/ajrnz/mill-dgraph.git"
    ),
    developers = Seq(
      Developer("ajrnz", "Andrew Richards", "https://github.com/ajrnz")
    )
  )
}

object dgraph extends ScalaModule with DgraphPublishModule {
  def scalaVersion = T {"2.12.4"}

  def dagre_d3 = T{ "https://cdnjs.cloudflare.com/ajax/libs/dagre-d3/0.6.1/dagre-d3.min.js" }
  def d3 = T{ "https://cdnjs.cloudflare.com/ajax/libs/d3/4.13.0/d3.min.js" }

  // disable docs generation
  def docJar = T{
    val javadocDir = T.ctx().dest / 'javadoc
    mkdir(javadocDir)
    createJar(Agg())(javadocDir)
  }

  def generatedResources = T{
    mill.modules.Util.download(dagre_d3(), "dagre-d3.js")
    mill.modules.Util.download(d3(), "d3.js")
    PathRef(T.ctx().dest)
  }

  def resources = T.sources{ super.resources() :+ generatedResources() }

  val millVersion = "0.2.0"

  def compileIvyDeps = Agg(
    ivy"com.lihaoyi::mill-scalalib:$millVersion",
    ivy"com.lihaoyi::geny:0.1.2",
  )

  def ivyDeps = Agg(
  	ivy"com.lihaoyi::scalatags:0.6.7",
  )

}
