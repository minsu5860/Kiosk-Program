package a_010_java_after2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.*;

class Product{
	private int 	pdt_id;				// 상품코드 pk
	private String  pdt_id_name;		// 상품명
	private int     pdt_unit_price;		// 단가
	private int     pdt_order_method;	// 주문방법
	
	public int cnt;
	
	public int getPdt_id() {
		return pdt_id;
	}
	public void setPdt_id(int pdt_id) {
		this.pdt_id = pdt_id;
	}
	//
	public String getPdt_id_name() {
		return pdt_id_name;
	}
	public void setPdt_id_name(String pdt_id_name) {
		this.pdt_id_name = pdt_id_name;
	}
	//
	public int getPdt_unit_price() {
		return pdt_unit_price;
	}
	public void setPdt_unit_price(int pdt_unit_price) {
		this.pdt_unit_price = pdt_unit_price;
	}
	//
	public int getPdt_order_method() {
		return pdt_order_method;
	}
	public void setPdt_order_method(int pdt_order_method) {
		this.pdt_order_method = pdt_order_method;
	}
	//
	void printScore() {
        System.out.printf(" %3d %6d %3d %7s \n",
        		pdt_id, pdt_unit_price, pdt_order_method, pdt_id_name);
    }
}
public class Kiosk_product_Insert {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
	       
        System.out.print("상품코드 입력은 몇 건입니까? : ");
        int num = input.nextInt();
       
        Product stu[] = new Product[num];
       
        for(int i=0; i<stu.length; i++) {
           
            stu[i] = new Product();    // 매우 중요!!! 배열은 인덱스 모두에 객체생성 후 참조배열 연계
            System.out.print("상품코드를 입력하세요 : ");
            stu[i].setPdt_id(input.nextInt());
            input.nextLine();
            System.out.print("상품명을 입력하세요 : ");
            stu[i].setPdt_id_name(input.nextLine());
            System.out.print("상품단가를 입력하세요 : ");
            stu[i].setPdt_unit_price(input.nextInt());
            System.out.print("주문방법을 입력하세요 : ");
            stu[i].setPdt_order_method(input.nextInt());
            input.nextLine();
        }
        
        System.out.println("=============상품코드 등록 내용=============");
        System.out.println("상품코드  단가  주문방법  상품명");
        System.out.println("=======================================");
        for (int i=0; i<stu.length; i++) {
            stu[i].printScore();
        }
        System.out.println("=======================================");
        // DB연결 후 입력된 자료 등록
            Connection conn = null;
            PreparedStatement pstmt = null;
            String sql;

            String url = "jdbc:oracle:thin:@localhost:1521:xe";
            String id = "system";
            String pw = "1234";
            try {
	            
                Class.forName("oracle.jdbc.OracleDriver");
                
                conn = DriverManager.getConnection(url, id, pw);
               
                for(int i=0; i<stu.length; i++) {
                    sql = "insert into tbl_product_master values (?, ?, ?, ?)";
                    pstmt = conn.prepareStatement(sql);
                   
                    pstmt.setInt(1, stu[i].getPdt_id());
                    pstmt.setString(2, stu[i].getPdt_id_name());
                    pstmt.setInt(3, stu[i].getPdt_unit_price());
                    pstmt.setInt(4, stu[i].getPdt_order_method());
                    pstmt.executeUpdate();
                }
                System.out.println("위 내용을 저장 시 1번. 그외는 취소합니다.");
	            int save = input.nextInt();
	               
	            if(save != 1) {
	            	for(int i = 0; i < stu.length; i++) {
	            		sql = "delete from tbl_product_master where pdt_id = (?)";
			            pstmt = conn.prepareStatement(sql);
			            pstmt.setInt(1, stu[i].getPdt_id());
			            pstmt.executeUpdate();
			        }
	               	System.out.println("저장실패.");
	            }else {
	            	System.out.println("DB 입력작업 성공:"+stu.length+"건은 정상등록 되었습니다.");
	            }
	            System.out.println("클래스 로딩 성공");
	            System.out.println("DB 접속");
                
                
               
                pstmt.close();
                conn.close();
        }catch(Exception e) {
                e.printStackTrace();
        }
    }
}