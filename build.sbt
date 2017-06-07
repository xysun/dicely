lazy val akkaHttpVersion = "10.0.7"
lazy val akkaVersion    = "2.5.2"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "org.jsun",
      scalaVersion    := "2.11.6"
    )),
    name := "dicely",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.1"         % Test,
      "net.debasishg"     %% "redisclient"          % "3.4",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.7",
      "com.google.guava"  % "guava"                 % "22.0"
    )
  )
