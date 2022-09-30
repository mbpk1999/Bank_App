package bank_pack;
import java.io.*;
import java.util.Scanner;

public class Bank_class {

	public static void main(String[] args) throws Exception {
		System.out.println("------------------Welcome to Bank_withPostreSQL-------------------------");
		int ch1 = 0;
		do
		{
			System.out.println("1) Create a new Account.");
			System.out.println("2) Login existing Customer.");
			System.out.println("3) Admin Login.");
			System.out.println("4) Exit.");
			System.out.println("\n Enter your choice : ");
			Scanner sc = new Scanner(System.in);
			ch1 = sc.nextInt();
			switch(ch1)
			{
			case 1:
			{
				Bank_methods bm = new Bank_methods();
				Long acc_no = bm.Create_account();
				System.out.println("Account created successfully.... Account number :"+acc_no);
				break;
			}
			case 2:
			{
				System.out.println("Welcome To The Bank...");
				Bank_methods bm = new Bank_methods();
				bm.ExistingUser();
				break;
			}
			case 4:
			{
				System.out.println("Have a nyc Day...BYE.....");
				break;
			}
			default:
			{
				System.out.println("Inalid input....Try Again...");
				break;
			}
			}
		}while(ch1!=4);

	}

}
