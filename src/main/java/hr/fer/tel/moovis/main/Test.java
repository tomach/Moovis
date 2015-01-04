package hr.fer.tel.moovis.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Test {
	public static void main(String[] args) throws IOException,
			InterruptedException {

		URL url = new URL("http://checkip.amazonaws.com/");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				url.openStream()));
		System.out.println(br.readLine());

		Connection con = Jsoup.connect("http://www.whatismyip.com/");

		Thread.sleep(5000);

		System.out.println("!!!");
		System.out.println(con.execute().body());

		System.out.println("?????");
		Document doc = Jsoup.parse("http://www.whatismyip.com/");
		System.out.println(doc);
		for (Element el : doc.getAllElements()) {
			System.out.println(el);
		}
	}
}
