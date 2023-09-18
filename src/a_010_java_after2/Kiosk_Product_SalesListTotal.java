package a_010_java_after2;

import java.sql.*;
import java.util.*;

class Product_BuyList1{
    public int     pdt_id;					//상품코드
    public int     ord_buying_count;		//주문수량
    public int     tot_in_money;			//금액
    public String  pdt_id_name;				//상품명
    
    public int     cnt;						//순서
    
    void printScore() {
        System.out.printf("%3d  %5d  %5d  %7d  %7s \n",
                cnt, pdt_id, ord_buying_count, tot_in_money, pdt_id_name);
    }
}
public class Kiosk_Product_SalesListTotal {

    public static void main(String[] args) {
    	Scanner kpbl = new Scanner(System.in);
        
        do {
        	
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        String sql;
	       
	        String url = "jdbc:oracle:thin:@localhost:1521:xe";
	        String id = "system";
	        String pw = "1234";

	        int list=0;			//입력
	        int num_count =0;	//순번
	        int total=0;		//가격 합계
	        int buy_count=0;	//수량 합계
	        int num = 0;		//등록된 코드 건 수
	        
	        try {
	            Class.forName("oracle.jdbc.OracleDriver");
	            System.out.println("클래스 로딩 성공");
	            conn = DriverManager.getConnection(url, id, pw);
	            
	            System.out.println("DB 접속");
	            
	            sql="select count(*) num from tbl_order_total";
	            
	            pstmt = conn.prepareStatement(sql);
	            ResultSet rs = pstmt.executeQuery();
	            rs.next();
	            num = rs.getInt(1);
	            
	            
	            System.out.println("등록된 상품코드는 건 수:"+ num);
	            System.out.println("주문번호를 입력하세요. 주문 리스트를 출력합니다. 전체:1 종료:9");
	            list=kpbl.nextInt();
	            
	            if(list==1) {	//전체 주문 수량 및 가격
	            	sql="select c.pdt_id, a.ord_buying_count, b.tot_in_money, c.pdt_id_name "
	            			+ "from tbl_order_list a, tbl_order_total b, tbl_product_master c "
	            			+ "where c.pdt_id = a.ord_pdt_id and a.ord_no = tot_ord_no "
	            			+ "order by ord_pdt_id";
	            			
	            }else if(list==9) {	//메인으로
	            	System.out.println("Kiosk_MainMenu로 돌아갑니다");
	            	Kiosk_MainMenu.main(args);
	            	
	            }else if(list!=1||list!=9){	//상품코드별 수량 및 가격 출력
	                sql= "select c.pdt_id, a.ord_buying_count, b.tot_in_money, c.pdt_id_name"
	                		+ " from tbl_order_list a, tbl_order_total b, tbl_product_master c"
	                		+ " where c.pdt_id = a.ord_pdt_id and a.ord_no = tot_ord_no and c.pdt_id=" +list
	                		+ " order by ord_pdt_id";
	            }																								
	                pstmt = conn.prepareStatement(sql);
	                rs = pstmt.executeQuery();
	               
	                num_count = 0;
	                
	                System.out.println("================================================");
	                System.out.println("순번  상품코드     수량      금액     상품명");
	                System.out.println("================================================");

	                Product_BuyList1 p = new Product_BuyList1();

	                while(rs.next()) {
				    	
				        p.cnt = num_count+1;
				        num_count++;
				        
				        p.pdt_id 			= rs.getInt("pdt_id");
				        p.ord_buying_count  = rs.getInt("ord_buying_count");
				        p.tot_in_money 		= rs.getInt("tot_in_money");
				        p.pdt_id_name 		= rs.getString("pdt_id_name");
				       
				        p.printScore();
				        
				        total += p.tot_in_money; 			//전체가격 합계
				        buy_count += p.ord_buying_count;    //주문수량 합계
				    }
				
				    System.out.println("================================================");
				    System.out.println("***주문합계:       " + buy_count + "     " + total);
        
	    }catch(Exception e) {																						
	        e.printStackTrace();
	    	}
    	}while(true);
    }
}