lazy val baseSettings: Seq[Setting[_]] = Seq(
  scalaVersion       := "2.12.4",
  scalacOptions     ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:higherKinds",
    "-language:implicitConversions", "-language:existentials",
    "-unchecked",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture"
  ),
  resolvers += Resolver.sonatypeRepo("releases")
)

lazy val `refined-in-practice` = project.in(file("."))
  .settings(moduleName := "refined-in-practice")
  .settings(baseSettings: _*)
  .aggregate(core, slides)
  .dependsOn(core, slides)

lazy val core = project
  .settings(moduleName := "refined-in-practice-core")
  .settings(baseSettings: _*)
  .settings(libraryDependencies ++= Seq(
    // normal deps
    "eu.timepit"         %% "refined"        % "0.8.4",
    "com.typesafe.slick" %% "slick"          % "3.2.1",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1",
    "com.typesafe.play"  %% "play-json"      % "2.6.7",
    "be.venneborg"       %% "slick-refined"  % "0.0.1+7-1c4c3305+20171028-1318",
    // test deps
    "org.scalatest"      %% "scalatest"      % "3.0.4"        % "test",
    "com.h2database"     %  "h2"             % "1.4.196"      % "test"
  ))


lazy val slides = project
  .settings(moduleName := "refined-in-practice-slides")
  .settings(baseSettings: _*)
  .settings(
    tutSourceDirectory := baseDirectory.value / "tut",
    tutTargetDirectory := baseDirectory.value / "../docs",
    watchSources ++= (tutSourceDirectory.value ** "*.html").get
  ).dependsOn(core)
  .enablePlugins(TutPlugin)
