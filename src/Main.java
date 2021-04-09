// 2021 - 4 - 08 / 자바 연습용.
// 웹페이지 데이터 실시간 크롤링 및 배열이용한 랜덤 숫자 생성. 쓰레드활용
// UI 구현 - 자바 swing.awt -구현중 21/04/09
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import javax.swing.*;


public class Main {
    static String[] tar = new  String[5];      //645당첨금액 저장 배열
    static String[] win_ball = new  String[7];  //645당첨번호 저장 배열        // 720은 개별 매서드에 선언


    static void showmenu(){
        /*JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        panel.add(new );
        frame.add(panel);
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(800,600));
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); */

        int type;
        System.out.println("------------로또 정보조회 & 번호생성기------------");
        System.out.println("원하는 복권형태 선택.");
        System.out.println("1. Lotto 6/45");
        System.out.println("2. 연금복권 720+");
        System.out.println("3. 프로그램 종료");

        Scanner scanner = new Scanner(System.in);      // scanner
        int input = scanner.nextInt();
        type = input;

        if(type == 1){                          //시작
            Lotto645_home();

        } else if(type == 2) {
            Lotto720_home();

        } else {
            System.out.println("프로그램 종료.");
            System.exit(0);               //끝끝
        }
    }

    static  void Lotto645_home(){                       // 로또645 메서드
        System.out.println("---------------------------------------------");
        Crawling645();

        System.out.println("---------------------------------------------");
        Crawling645_money();

        System.out.println("---------------------------------------------");
        System.out.println("로또 6/45 랜덤번호를 받으시겠습니까?");
        System.out.println("1 = 예 , 0 = 아니오");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        if(input == 1){
            System.out.println("로또6/45 랜덤번호");
            Lotto645();
            System.out.println("\n");
            System.out.println("잠시후 메인메뉴로 돌아갑니다.");
            try {
                Thread.sleep(2000);                       //delay 걸기
            } catch (InterruptedException e) {                  //인터럽트 예외 처리 - 혹은 인터럽트 값 반환필요
                e.printStackTrace();
            }
            showmenu();
        } else {
            System.out.println("메인메뉴로 돌아갑니다.");
            showmenu();
        }
    }

    static  void Lotto720_home(){           //720 홈
        System.out.println("---------------------------------------------");
        Crawling720();

        System.out.println("---------------------------------------------");
        System.out.println("연금복권 720+ 랜덤번호를 받으시겠습니까?");
        System.out.println("1 = 예 , 0 = 아니오");

        Scanner scanner = new Scanner(System.in);         // 입력
        int input = scanner.nextInt();

        if(input == 1){
            System.out.println("연금복권720+ 랜덤번호");
            Lotto720();
            System.out.println("\n");
            System.out.println("잠시후 메인메뉴로 돌아갑니다.");
            try {
                Thread.sleep(2000);                       //delay 걸기
            } catch (InterruptedException e) {                  //인터럽트 예외 처리 - 혹은 인터럽트 값 반환필요
                e.printStackTrace();
            }
            showmenu();
        } else {
            System.out.println("메인메뉴로 돌아갑니다.");
            showmenu();
        }
    }

    static void Lotto645(){                                     //645 랜덤번호생성기능
        System.out.println("게임 횟수 입력 :");  // 게임횟수 입력
        Scanner scanner = new Scanner(System.in);
        int Repeat = scanner.nextInt();

        for (int b = 0 ; b < Repeat ; b++) {
            int ball[] = new int[45];   // 볼랜덤 숫자 획득

            for (int i = 0; i < ball.length; i++) {
                ball[i] = i + 1;
            }
            int temp = 0;
            int K = 0; //랜덤값

            for (int i = 0; i < 7; i++) {
                K = (int) (Math.random() * 45);
                temp = ball[i];
                ball[i] = ball[K];
                ball[K] = temp;
            }

            for (int i = 0; i < 6; i++) {      //출력부
                System.out.printf("[%d]", ball[i]);
            }
            System.out.printf(" 보너스 번호 = [%d]%n", ball[6]);
        }
    }

    static void Lotto720(){                    //720 랜덤번호생성기능
        int Group;  //오류검사용 변수
        for(;;) {
            System.out.println("연금복권 720+ 조 선택 :"); //조 선택
            Scanner scanner = new Scanner(System.in);
            Group = scanner.nextInt();

            if (Group >= 1 && Group <= 5){
            break;}
            else {
                System.out.println("연금복권 720+ 조는 1~5 조입니다.");
            }
        }
        int ball[] = new int[6];  // 0~9 난수 생성

        for (int i = 0; i < ball.length; i++) {
            ball[i] = (int)(Math.random() * 10);
        }

        System.out.printf("조 번호 : [%d] , " , Group); //출력부
        for (int i = 0 ; i < 6 ; i++)
        System.out.printf("[%d]" , ball[i]);
    }

    static void Crawling645(){                      // 로또 645 당첨번호 크롤링 메서드

        String url = "https://dhlottery.co.kr/gameResult.do?method=byWin"; // 크롤링 주소 받기
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (UnknownHostException e1){
            System.out.println("인터넷 연결 오류. 메인메뉴로 돌아갑니다"); // 인터넷연결오류 예외처리
            showmenu();
        } catch (IOException e2) {
            e2.printStackTrace();   // IOE 예외처리
        }
        Elements element = doc.select("div.win_result");  // 크롤링할 DIV class 명
        String title = element.select("h4").text();   // 크롤링할 DIV 내부 css 스타일 명
        System.out.println("[로또6/45 전 회차 당첨번호]  ");
        System.out.println("["+ title + "]");


        for(int i = 0; i < win_ball.length ; i++) {  //당첨번호 추출
            int idx_num = i;
            int array_num = i;
            String str = element.select("span").eq(idx_num).text();
            win_ball[array_num] = str;
        }
        System.out.print("[" + win_ball[0] + "]");
        System.out.print("[" + win_ball[1] + "]");
        System.out.print("[" + win_ball[2] + "]");
        System.out.print("[" + win_ball[3] + "]");
        System.out.print("[" + win_ball[4] + "]");
        System.out.print("[" + win_ball[5] + "]");
        System.out.println("  [보너스 번호][" + win_ball[6] + "]");


    }

    static void Crawling645_money(){                            // 로또 645 당첨금액 크롤링 메서드

        String url = "https://dhlottery.co.kr/gameResult.do?method=byWin"; // 크롤링 주소 받기
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (UnknownHostException e1){
                System.out.println("인터넷 연결 오류.메인메뉴로 돌아갑니다."); // 인터넷연결오류 예외처리
                showmenu();
        } catch (IOException e2) {
            e2.printStackTrace();   // IOE 예외처리
        }
        doc.select("tr");  // 크롤링할 DIV class 명
        for(int i = 1 ; i < (tar.length *2) ; i+=2 ) {  // 당첨금액 추출
            int index_num = i;
            int array_idx = i/2;
            String str = doc.select("td.tar").eq(index_num).text();
            tar[array_idx] = str;
        }
        System.out.println("[로또6/45 전 회차 당첨금액] ");
        System.out.println("[1등] "+tar[0]);
        System.out.println("[2등] "+tar[1]);
        System.out.println("[3등] "+tar[2]);
        System.out.println("[4등] "+tar[3]);
        System.out.println("[5등] "+tar[4]);
    }

    static void Crawling720(){                              // 720 크롤링 매서드
        String[] L720_num = new String[13];                   //720당첨번호 저장할 배열
        String url = "https://dhlottery.co.kr/gameResult.do?method=win720";//720 당첨번호 크롤링
        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();
        } catch (UnknownHostException e1){
            System.out.println("인터넷 연결 오류.메인메뉴로 돌아갑니다.");
            showmenu();
        } catch (IOException e2){
            e2.printStackTrace();
        }


        Elements tar_title = doc.select("div#after720");   // 크롤링할 DIV class 명
        String title = tar_title.select("h4").text();              // 크롤링할 DIV 내부 css 스타일 명
        System.out.println("[연금복권720 전 회차 당첨번호]  "+title);
        System.out.print("\n");

        Elements tar_num = doc.select("div.win720_num");   //당첨번호 추출 후 배열에 저장
        for (int i = 0; i< L720_num.length ; i++){
            int idx_num = i;
            String Win720_num = tar_num.select("span.large").eq(idx_num).text();  //해당 인덱스 해당 요소만 추출
            L720_num[i] = Win720_num;
        }

        System.out.print("[ 1등 월 700만원 X 20년 ]  ");             //1등 출력
        System.out.print("["+L720_num[0] + " 조]  ");
        for (int i = 1; i <7 ; i++){
            System.out.print("["+L720_num[i]+"]");
        }
        System.out.print("\n");

        System.out.print("[ 2등 월 100만원 X 10년 ]  ");             //2등
        System.out.print("[각 조]  ");
        for (int i = 1; i <7 ; i++){
            System.out.print("["+L720_num[i]+"]");
        }
        System.out.print("\n");

        System.out.print("[보너스 월 100만원 X 10년 ]  ");            //보너스
        System.out.print("[각 조] ");
        for (int i = 7; i < L720_num.length ; i++){
            System.out.print("["+L720_num[i]+"]");
        }
        System.out.println("\n");
    }

    public static void main(String[] args) {
       // JFrame frame = new JFrame();
       // JPanel panel = new JPanel();


        showmenu();        //시작
        //끝
    }
}

       /* 인터넷 연결확인이 되지않거나 웹페이지 점검등으로 응답이 불가능할떄 예외로발생하는 에러코드.
          이에 대한 예외처리를 각 크롤링 메서드 초반부 예외처리에 구현완료 21.04.09 */
        /*  java.net.UnknownHostException: dhlottery.co.kr
        at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:196)
        at java.net.PlainSocketImpl.connect(PlainSocketImpl.java:162)
        at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:394)
        at java.net.Socket.connect(Socket.java:606)
        at sun.security.ssl.SSLSocketImpl.connect(SSLSocketImpl.java:287)
        at sun.net.NetworkClient.doConnect(NetworkClient.java:175)
        at sun.net.www.http.HttpClient.openServer(HttpClient.java:463)
        at sun.net.www.http.HttpClient.openServer(HttpClient.java:558)
        at sun.net.www.protocol.https.HttpsClient.<init>(HttpsClient.java:292)
        at sun.net.www.protocol.https.HttpsClient.New(HttpsClient.java:395)
        at sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection.getNewHttpClient(AbstractDelegateHttpsURLConnection.java:191)
        at sun.net.www.protocol.http.HttpURLConnection.plainConnect0(HttpURLConnection.java:1162)
        at sun.net.www.protocol.http.HttpURLConnection.plainConnect(HttpURLConnection.java:1056)
        at sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection.connect(AbstractDelegateHttpsURLConnection.java:177)
        at sun.net.www.protocol.https.HttpsURLConnectionImpl.connect(HttpsURLConnectionImpl.java:167)
        at org.jsoup.helper.HttpConnection$Response.execute(HttpConnection.java:732)
        at org.jsoup.helper.HttpConnection$Response.execute(HttpConnection.java:707)
        at org.jsoup.helper.HttpConnection.execute(HttpConnection.java:297)
        at org.jsoup.helper.HttpConnection.get(HttpConnection.java:286)
        at Main.Crawling645(Main.java:156)
        at Main.Lotto645_home(Main.java:40)
        at Main.showmenu(Main.java:27)
        at Main.Crawling720(Main.java:220)
        at Main.Lotto720_home(Main.java:70)
        at Main.showmenu(Main.java:30)
        at Main.Crawling720(Main.java:220)
        at Main.Lotto720_home(Main.java:70)
        at Main.showmenu(Main.java:30)
        at Main.Crawling720(Main.java:220)
        at Main.Lotto720_home(Main.java:70)
        at Main.showmenu(Main.java:30)
        at Main.Crawling720(Main.java:220)
        at Main.Lotto720_home(Main.java:70)
        at Main.showmenu(Main.java:30)
        at Main.main(Main.java:261)
        Exception in thread "main" java.lang.NullPointerException
        at Main.Crawling645(Main.java:160)
        at Main.Lotto645_home(Main.java:40)
        at Main.showmenu(Main.java:27)
        at Main.Crawling720(Main.java:220)
        at Main.Lotto720_home(Main.java:70)
        at Main.showmenu(Main.java:30)
        at Main.Crawling720(Main.java:220)
        at Main.Lotto720_home(Main.java:70)
        at Main.showmenu(Main.java:30)
        at Main.Crawling720(Main.java:220)
        at Main.Lotto720_home(Main.java:70)
        at Main.showmenu(Main.java:30)
        at Main.Crawling720(Main.java:220)
        at Main.Lotto720_home(Main.java:70)
        at Main.showmenu(Main.java:30)
        at Main.main(Main.java:261) */
