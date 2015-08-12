name := "texas"

version := "1.0"

scalaVersion := "2.11.6"
dependencyOverrides += "org.scala-lang" % "scala-compiler" % scalaVersion.value
    
libraryDependencies ++= Seq(
	("org.sorm-framework" % "sorm" % "0.3.18"),
	"org.specs2" %% "specs2-core" % "3.6.2" % "test",
	"org.specs2" %% "specs2-mock" % "3.6.2" % "test",
	"org.specs2" %% "specs2-junit" % "3.6.2" % "test",
	"com.googlecode.kiama" %% "kiama" % "1.8.0",
	"com.github.tototoshi" %% "scala-csv" % "1.2.2",
  "com.typesafe.slick" %% "slick" % "3.0.0",
	"com.twitter" %% "chill" % "0.7.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.skinny-framework" %% "skinny-orm"      % "1.3.19",
  "com.h2database"       %  "h2"              % "1.4.+",
  "ch.qos.logback"       %  "logback-classic" % "1.1.+",
	"com.github.aselab" %% "scala-activerecord" % "0.3.1",
	"com.github.aselab" %% "scala-activerecord-specs" % "0.3.1" % "test",
	"org.slf4j" % "slf4j-nop" % "1.7.10",
	"mysql" % "mysql-connector-java" % "5.1.34",
	"org.scala-lang.modules" %% "scala-pickling" % "0.10.1"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
scalacOptions in Test ++= Seq("-Yrangepos")

initialCommands := """
import com.andbutso.poker.cards._
import com.andbutso.poker.cards.Rank._
import com.andbutso.poker.handhistory._
import com.andbutso.poker.handhistory.pokerstars._
import com.andbutso.poker.handhistory.pokerstars.parser._
//import com.github.aselab.activerecord._
//import com.github.aselab.activerecord.dsl._
import com.andbutso.poker.handhistory.model.Implicits._
//model.Tables.initialize
"""
