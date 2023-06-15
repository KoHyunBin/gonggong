package gonggong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Basic1 {

	public static void main(String[] args) throws IOException {
		String gongurl = "https://apis.data.go.kr/9720000/searchservice/basic";
		//URLEncoder.encode : 2바이트 문자열을 UTF-8 형식으로 인코딩
		String search = URLEncoder.encode("자료명,홍길동","UTF-8");
		StringBuilder urlBuilder = new StringBuilder(gongurl);
		urlBuilder.append("?serviceKey=u%2BFpA4hadZBtMCJqRnJq47dbnnHR9Ulymf6XLwUO2o5wKuogVoeo9j5SJxJ2YvDxBCW5t5RE%2BunW%2B7Te%2BPWdbA%3D%3D");
		urlBuilder.append("&displaylines=10&search="+search);
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //공공데이터에 접속
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		System.out.println("Response code :" + conn.getResponseCode());
		BufferedReader br;
		if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) { // 200 이상 300미만인 경우 정상 처리
			br = new BufferedReader(new InputStreamReader(conn.getInputStream())); //공공데이터 전달해준 데이터를 br 대입
		} else {
			br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder sb = new StringBuilder(); //xml 형태
		String line;
		while((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		conn.disconnect();
		System.out.println(sb.toString());
		//sb : 국회 도서자료의 응답 메세지 저장. xml 형태로 저장함 응답형식이 xml 임
		//Jsoup : markup language 파싱기능을 가진 툴 html 분석 xml 분석
		//doc : sb 문자열의 내용(xml 형식)을 DOM tree로 변경. 최상단의 문자노드 저장
		Document doc = Jsoup.parse(sb.toString());
		// recode 태그 들
		Elements recodes = doc.select("recode");
		//r : recodes 태그 한개
		//r.select("item") : recode 태그의 하위 item 태그들
		for(Element r : recodes) {//recode 객체
			// i : item 태그 한개
			for(Element i : r.select("item")) { // recode 객체 안에 item 객체
				String name = i.select("name").html();
				String value = i.select("value").html();
				System.out.println(name+":"+value + "\t");
			}
		}
		System.out.println();
	}

}
