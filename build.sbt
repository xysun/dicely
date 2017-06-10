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
      "com.typesafe.akka"          %% "akka-http"                   % akkaHttpVersion,
      "com.typesafe.akka"          %% "akka-http-xml"               % akkaHttpVersion,
      "com.typesafe.akka"          %% "akka-stream"                 % akkaVersion,
      "com.typesafe.akka"          %% "akka-http-testkit"           % akkaHttpVersion % Test,
      "org.scalatest"              %% "scalatest"                   % "3.0.1"         % Test,
      "redis.clients"              %  "jedis"                       % "2.9.0",
      "com.typesafe.akka"          %% "akka-http-spray-json"        % "10.0.7",
      "com.google.guava"           %  "guava"                       % "22.0",
      "io.lemonlabs"               %% "scala-uri"                   % "0.4.16",
      "commons-validator"          %  "commons-validator"           % "1.4.0",
      "com.iheart"                 %% "ficus"                       % "1.4.1",
      "com.typesafe.scala-logging" %% "scala-logging"               % "3.5.0",
      "ch.qos.logback"             %  "logback-classic"             % "1.1.7"
    )
  )
