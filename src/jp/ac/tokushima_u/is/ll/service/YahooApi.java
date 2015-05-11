package jp.ac.tokushima_u.is.ll.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.mail.internet.ContentType;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Service
public class YahooApi {
	private final String appid = "dj0yJmk9SjZlZ3ZyQWxqbXRMJmQ9WVdrOVkyVnVhMk5GTm5NbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1hNQ--";
	private final int size = 1;
	private final String url = "http://search.yahooapis.jp/WebSearchService/V2/webSearch";
	private final String charset = "UTF-8";
	private final XPath xpath = XPathFactory.newInstance().newXPath();
	public static String string;
	int count = 0;
/**
	public YahooApi(String parameter, int size){
		this.size = size;
	}
*/
	public YahooApi() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public String main(String parameter){
		YahooApi api = new YahooApi();
		String results = api.searchWikipediaSummary(parameter);
		return results;
	}

	public synchronized String searchWikipediaSummary(/**final*/ String query) {
		List<String> result = new LinkedList<String>();
		string = null;
		try {
			String section = makeQuery(query+"Wikipedia");
			URL url = new URL(section);
			String xml = search(url,query);
			Document doc = xmlToDocument(xml);

			for (int i = 0, size = size(doc, "ResultSet/Result"); i < size; i++) {
				String s = value(doc, "ResultSet/Result[" + (i + 1) + "]/Summary");
				while (s.indexOf("（") != -1 && s.indexOf("）") != -1) // remove rubies
					s = s.substring(0, s.indexOf("（")) + s.substring(s.indexOf("）") + 1, s.length() - 1);
				Scanner scan = new Scanner(s);
				scan.useDelimiter("。");
				String vol = scan.next();
				scan.close();

				doit(appid,vol);
/**
				if (s.indexOf("。") != -1){
					s = s.substring(0, s.indexOf("。") + 1);
				}else if(s.indexOf(".") != -1){
					s = s.substring(0, s.indexOf(".") + 1);
				}
*/				result.add(s.trim().replace(" ", ""));
//				doit(appid,s);
			}
		} catch (Exception e) {
			e.printStackTrace();
//			return searchWikipediaSummary(query); // try again
		}
		return string;
	}

	private String makeQuery(final String sentence)
			throws UnsupportedEncodingException {
		return url + "?appid=" + appid +
			"&query=" + URLEncoder.encode(sentence, charset) + "&results=" + size;
	}

	private int size(final Document doc, final String expression)
			throws XPathExpressionException {
		NodeList list = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		return list.getLength();
	}

	private String value(final Document doc, final String expression)
			throws XPathExpressionException {
		return xpath.evaluate(expression, doc);
	}

	private Document xmlToDocument(final String xml)
			throws IOException, SAXException, ParserConfigurationException {
		StringReader sr = new StringReader(xml);
		InputSource is = new InputSource(sr);
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

		return doc;
	}

	private String search(final URL url,String Key)
			throws IOException, ParseException, javax.mail.internet.ParseException {
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.connect();

		OutputStream os = connection.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(Key);
		bw.flush();
		bw.close();
		
		String ct = connection.getContentType();
		String charset = "UTF-8";
		if(ct != null){
			String cs = new ContentType(ct).getParameter("charset");
			if(cs != null){
				charset = cs;
			}
		}
	      InputStream is = connection.getInputStream();
	      InputStreamReader isr = new InputStreamReader(is, charset);
	      BufferedReader br = new BufferedReader(isr);
	      StringBuffer buf = new StringBuffer();
	      String s;
	        while ((s = br.readLine()) != null) {
	          buf.append(s);
	          buf.append("\r\n");
	        }
	      br.close();
	      connection.disconnect();
		return buf.toString();
	}

	 private static void doit(String appid, String sentence){
		    try{
		      // リクエストURL
		      String requesturl = "http://jlp.yahooapis.jp/KeyphraseService/V1/extract";

		      // キーフレーズ抽出APIを使う
		      String parameters = getParametersString(appid, sentence);
		      String xmlContent = getContent(new URL(requesturl), parameters);
		      Document doc = getDocument(xmlContent);
		      ResultSet rs = getResultSet(doc);

		      // 結果を出力
		      System.out.println("[解析対象のテキスト]");
		      System.out.println(sentence);
		      System.out.println("[解析結果]");
		      for(int i=0; i<rs.result.length; i++){
		    	  if(Integer.valueOf(rs.result[i].Score) < 0){}else{
		        System.out.println(rs.result[i].Keyphrase + ": " + rs.result[i].Score);
		        if(rs.result[i].Keyphrase == null){}else{
		        	/**ここで結果を表示する文字列の変更*/
		        	if(string == null){
		        		string = rs.result[i].Keyphrase + "\r\n";
		        	}else{
		        		string = string + rs.result[i].Keyphrase + "\r\n";
		        	}
		        }
		    	  }
		      	}
		      System.out.println();
		    }catch(Exception e){
		      e.printStackTrace();
		    }
		  }

	private static String getParametersString(String appid, String sentence) throws UnsupportedEncodingException{
	    String parameters =
	      "appid=" + appid +
	      "&" +
	      "sentence=" + URLEncoder.encode(sentence, "UTF-8");
	    return parameters;
	  }

	private static Document getDocument(String xmlContent) throws IOException, SAXException, ParserConfigurationException {
	      StringReader sr = new StringReader(xmlContent);
	      InputSource is = new InputSource(sr);
	      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
	      return doc;
	    }

	 private static String getContent(URL url, String parameters) throws IOException, ParseException, javax.mail.internet.ParseException {

	      HttpURLConnection con = (HttpURLConnection)url.openConnection();
	      con.setRequestMethod("POST");
	      con.setDoOutput(true);
	      con.connect();

	      OutputStream os = con.getOutputStream();
	      OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
	      BufferedWriter bw = new BufferedWriter(osw);
	      bw.write(parameters);
	      bw.flush();
	      bw.close();

	      String ct = con.getContentType();
	      String charset = "UTF-8";
	      if(ct != null){
	        String cs = new ContentType(ct).getParameter("charset");
	        if(cs != null){
	              charset = cs;
	          }
	      }

	      InputStream is = con.getInputStream();
	      InputStreamReader isr = new InputStreamReader(is, charset);
	      BufferedReader br = new BufferedReader(isr);
	      StringBuffer buf = new StringBuffer();
	      String s;
	        while ((s = br.readLine()) != null) {
	          buf.append(s);
	          buf.append("\r\n");
	        }
	      br.close();
	      con.disconnect();

	      return buf.toString();
	    }

	private static int getLength(XPath xpath, Document doc, String expression)
			throws XPathExpressionException{
		NodeList nodelist = (NodeList)xpath.evaluate(expression,  doc, XPathConstants.NODESET);
		if(nodelist != null){
			return nodelist.getLength();
		}else{
			return 0;
		}
	}

	private static String getString(XPath xpath,Document doc,String expression)
			throws XPathExpressionException{
		return xpath.evaluate(expression,doc);
	}

	private static ResultSet getResultSet(Document doc)
			throws Exception{
		XPath xpath = XPathFactory.newInstance().newXPath();
		int length = getLength(xpath,doc,"ResultSet/Result");
		int flg = 0;
		int count = 0;
		ArrayList<String>types = new ArrayList<String>();
		Result[] r = new Result[length];
		for(int i = 0; i < length; i++){
			flg = types.indexOf(getString(xpath,doc,"ResultSet/Result["+(i+1)+"]/Keyphrase"));
			if(flg != -1){
			}else{
			r[count] = new Result();
			r[count].Keyphrase = getString(xpath,doc,"ResultSet/Result["+(i+1)+"]/Keyphrase");
			r[count].Score = getString(xpath,doc,"ResultSet/Result["+(i+1)+"]/Score");
			types.add(r[count].Keyphrase);
			count++;
			}
		}
		ResultSet rs = new ResultSet();
		rs.result = r;
		return rs;
	}

	public static class ResultSet{
		public Result[] result;
	}

	public static class Result{
		public String Keyphrase;
		public String Score;
	}
}
