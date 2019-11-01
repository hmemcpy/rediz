ThisBuild / scalaVersion := "2.13.1"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.hmemcpy"

lazy val root = (project in file("."))
  .settings(
    name := "rediz",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core"        % "2.0.0",
      "org.scodec"    %% "scodec-core"      % "1.11.4",
      "org.scodec"    %% "scodec-cats"      % "1.0.0",
      "org.scodec"    %% "scodec-stream"    % "2.0.0",
      "com.beachape"  %% "enumeratum"       % "1.5.13",
      "org.typelevel" %% "cats-core"        % "2.0.0",
      "co.fs2"        %% "fs2-core"         % "2.0.0",
      "co.fs2"        %% "fs2-io"           % "2.0.0",
      "dev.zio"       %% "zio"              % "1.0.0-RC14",
      "dev.zio"       %% "zio-streams"      % "1.0.0-RC14",
      "dev.zio"       %% "zio-interop-cats" % "[1.0,)",
      "com.chuusai"   %% "shapeless"        % "2.3.3",
      "org.specs2"    %% "specs2-core"      % "4.6.0" % "test"
    )
  )

scalacOptions in Test ++= Seq("-Yrangepos")
