package a_010_java_after2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

class SalesList{
	public int pdt_id;				//상품코드
	public int ord_buying_count;	//수량
	public int tot_in_money;		//가격
	public String pdt_id_name;		//상품명
	public String tot_system_date;	//판매일자
	
	public int cnt;  				//순번
	
	void printScore() {
	    System.out.printf("%3d %11s %6d  %8d      %6d %8s \n",
	            cnt, tot_system_date, ord_buying_count, tot_in_money, pdt_id, pdt_id_name);
	}
}

public class Kiosk_Product_SalesList {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		do {
			
			Connection conn = null;
	        PreparedStatement pstmt = null;
	        String sql;
	       
	        String url = "jdbc:oracle:thin:@localhost:1521:xe";
	        String id = "system";
	        String pw = "1234";
	        
			int num = 0;
			int count = 0;
			int money = 0;
	        
	        try {
				
				Class.forName("oracle.jdbc.OracleDriver");
	            System.out.println("클래스 로딩 성공");
	            conn = DriverManager.getConnection(url, id, pw);
	            

	            System.out.println("DB 접속");
				
				System.out.print("상품코드를 입력하세요. 상품 리스트를 출력합니다. 전체:1 종료:9 > ");
				int input = sc.nextInt();
				
				if(input == 1) {
					sql = "select to_char(a.tot_system_date, 'yyyy-mm-dd') 판매일자, b.ord_buying_count 수량, "
							+   "a.tot_in_money 금액, c.pdt_id 상품코드, c.pdt_id_name 상품명 "
							+   "from tbl_order_total a, tbl_order_list b, tbl_product_master c "
							+   "where c.pdt_id = b.ord_pdt_id and b.ord_no = a.tot_ord_no "
							+   "order by c.pdt_id, a.tot_system_date ";
					
					pstmt = conn.prepareStatement(sql);
				
				}else if(input == 9) {
					Kiosk_MainMenu.main(args);
				}else if (input != 1 || input != 9) {
	                sql = "select to_char(a.tot_system_date, 'yyyy-mm-dd') 판매일자, b.ord_buying_count 수량, "
							+   "a.tot_in_money 금액, c.pdt_id 상품코드, c.pdt_id_name 상품명 "
							+   "from tbl_order_total a, tbl_order_list b, tbl_product_master c "
							+   "where c.pdt_id = " + input + " and c.pdt_id = b.ord_pdt_id and b.ord_no = a.tot_ord_no "
							+   "order by c.pdt_id, a.tot_system_date ";
	                
	                pstmt = conn.prepareStatement(sql);
	                }else {
	                	continue;
	                }
				
				ResultSet rs = pstmt.executeQuery();
				rs = pstmt.executeQuery();
                

				System.out.println("===================상품별/판매일자별 매출내역서===================");
				System.out.println("순번    판매일자        수량       금액      상품코드     상품명");
				System.out.println("==========================================================");
				
				SalesList sl = new SalesList();
				
				while(rs.next()) {
					
					sl.cnt += num + 1;
					num++;
					
					sl.tot_system_date = rs.getString("판매일자");
					sl.ord_buying_count = rs.getInt("수량");
					sl.tot_in_money = rs.getInt("금액");
					sl.pdt_id = rs.getInt("상품코드");
					sl.pdt_id_name = rs.getString("상품명");
					
					sl.printScore();
					
					money += sl.tot_in_money;
					count += sl.ord_buying_count;
					
				}
				
				System.out.println("=================================================");
				System.out.println("*전체판매수량:        " + count + "    " + money);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}while(true);
	}
}