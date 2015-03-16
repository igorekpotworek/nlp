package pl.edu.agh.nlp.spark;

import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

public class SparkHdfsTest {
	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName("Hdfs Test").setMaster("local[1]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new SQLContext(sc);

		JavaRDD<Person> people = sc.textFile("hdfs://localhost:19000/femaleNames.txt").map(line -> {
			String[] parts = line.split(" ");

			Person person = new Person();
			person.setFirstname(parts[0]);
			person.setLastname(parts[1]);

			return person;
		});

		DataFrame schemaPeople = sqlContext.createDataFrame(people, Person.class);
		schemaPeople.registerTempTable("people");

		DataFrame teenagers = sqlContext.sql("SELECT firstname FROM people WHERE lastname=\"Wilkerson\"");
		teenagers.show();

	}

	public static class Person implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String firstname;
		private String lastname;

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public String getLastname() {
			return lastname;
		}

		public void setLastname(String lastname) {
			this.lastname = lastname;
		}

	}
}