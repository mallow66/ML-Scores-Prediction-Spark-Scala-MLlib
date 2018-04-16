
# Machine learning Score prediction for NBA games


In this repository, I created a machine learning system to predicte Nba games results based on the historical data taken from
[Basketball Reference](https://www.basketball-reference.com)

I treated this problem as a Regression model, in order to predict the score for each team 

### Data Description: 
As I said the data was taken was taken from the the link above as csv files
I grouped the data of each year in a single file, which mean i created four files: 
- 2014-2015.csv 
- 2015-2016.csv
- 2016-2017.csv
- 2017-2018.csv

(It is possible to add more files of course)
However the files must respect the structure bellow: 


| Visitor/Neutral  | Home/Neutral | Date | Start (ET)|PTS(Visitor)|PTS(Home) |
| ------------- | ------------- | ------------- |------------- | ------------- | ------------- | 
Houston Rockets|Los Angeles Lakers|Tue Oct 28 2014|10:30 pm|108|90|
Orlando Magic|New Orleans Pelicans|Tue Oct 28 2014|8:00 pm|84|101|
Dallas Mavericks|San Antonio Spurs|Tue Oct 28 2014|8:00 pm|100|101|

## Features Extraction
As known, each machine learning model need features to be represented as numbers to make it possible to predict the values needed. (PTS Home and PTS Visitor)
the Date and time are easy to be represented as numbers.
so for example we can code the day of weeks (Mon, Tue, Wed, ...) with Integers from 1 to 7 (1, 2, 3, ..., 7)
the same thing for the months (Jan, Feb, Mar,...) we'll code it with Integers from 1 to 12 (1, 2, 3, ..., 12)

##### However the big challenge is how to code the the name of each team ? !
If we code it with same method that we did previously, for sure we gonna have wrong predictions.
When we coded the Days of Week with Integers, it was significant...
Monday was coded with "1": meant that monday is the first day of the week
Tuesday was coded with "2": meant that Tuesday is the second day of the week..etc

If we do the same thing with the teams
Huston Rockets will be coded as 1
Los Angeles will be coded as 2, etc....

But why those teams will have those values and not another random ones ?, it means nothing for our model.
So unfortnantly, this method is not a good one to code our teams.

There are many ways to code Strings.
In our case, I choosed a method called **One hot encoding**
##### One Hot Encoding
This method is also termed as dummy coding. Using this method, dummy columns are created for each class of a categorical attribute. For each dummy attribute, the presence of the class is represented by 1 and its absence is represented by 0.
**Example:** 
Suppose that we have the only 3 teams with the initial data: 

| Visitor/Neutral  | Home/Neutral | PTS (Visitor)|PTS(Home) |
| ------------- | ------------- | ------------- |------------- |
|Team A |Team B | 70 | 83
|Team B | Team C | 90 | 50

It will be transformed as the table bellow: 

| Team A  | Team B | Team C| PTS (Visitor)|PTS(Home) |
| ------------- | ------------- | ------------- |------------- |------------- |
|1 |1 | 0 | 70 | 83
|0 | 1 | 1 | 90|50

Note ! 
we can change the one values to other values that differs to Zero
so for example if we want to code the idea of **Home teams** and **Visitor Teams**
we can give 2 for the visitors , and 1 for Home teams: 

| Team A  | Team B | Team C| PTS (Visitor)|PTS(Home) |
| ------------- | ------------- | ------------- |------------- |------------- |
|2 |1 | 0 | 70 | 83
|0 | 2 | 1 | 90|50

As known the NBA league contains **30 teams**, therefore **30 teams columns will be added**

At the end of the preprocessing part of this project we'll have data represented as follow: 




| Team A  | Team B | Team C|...| Day of week|Month |Day of Month	| Year|	Start (ET) |PTS (Visitor)|PTS(Home) |Pr(Visitor)|Pr(Home)
| ------------- | ------------- | ------------- |------------- |------------- |------------- |------------- | ------------- | ------------- |------------- |------------- |------------- |------------- |
|2 |1 | 0 |...|3|7 |23 | 2 |8.50 | 90 |78| 1 | 0
|0 |2 | 1 |...|4|7 |24 | 2 |10.00 | 80 |102| 0 | 1
|... |...|...|...|... |...|...|...|... |...|...|...|...|

### Requierements
You'll need to **SBT** project manager, Follow the instructions  [here](https://www.scala-sbt.org/1.0/docs/Setup.html)
You'll need also to add Spark libraries to your *Build* file

**Note ! Spark does not support Scala 2.12.**
Here is my build file: 

```scala
name := "Spark-trainnings"

version := "1.0"
scalaVersion := "2.11.12"
resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % "2.2.0",
  "org.apache.spark" % "spark-sql_2.11" % "2.2.0",
  "org.apache.spark" % "spark-mllib_2.11" % "2.2.0",
  "org.apache.spark" % "spark-graphx_2.11" % "2.2.0"
)
```


In the project I created a MLConfig Class to allow to config all the parameters for our predicting model: 
```scala
object MLConfig {

   val NUM_ITERATIONS = 100
   val STEP_SIZE = 0.01

   val HOME_TEAM = 82
   val VISITOR_TEAM = 83


  final val JAN_MONTH = 1
  final val FEB_MONTH = 2
  final val MAR_MONTH = 3
  final val APR_MONTH = 4
  final val MAI_MONTH = 5
  final val JUN_MONTH = 6
  final val JUL_MONTH = 7
  final val AOU_MONTH = 8
  final val SEP_MONTH = 9
  final val OCT_MONTH = 10
  final val NOV_MONTH = 11
  final val DEC_MONTH = 12


  final val MONDAY   = 1
  final val TUESDAY  = 2
  final val WEDNSDAY = 3
  final val THURSDAY = 4
  final val FRIDAY   = 5
  final val SATURDAY = 6
  final val SUNDAY   = 7

  final val YEAR_2014 = 1
  final val YEAR_2015 = 2
  final val YEAR_2016 = 3
  final val YEAR_2017 = 4
  final val YEAR_2018 = 5
  final val YEAR_2019 = 6



}

```




