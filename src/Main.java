import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Hello world!");

        //executeStatement("INSERT INTO todos (todo, date_added)\n" +
        //        "VALUES (\"test todo-interactor\", \"2023-10-21 02:48:00\")");

        //fixAutoIncrement("todos", 0);

        System.out.println("What do you want to do?");
        System.out.println();
        System.out.println("1. Create new TODO");
        System.out.println("2. Make existing TODO a child of another TODO");
        System.out.println("3. Finish a TODO");
        System.out.println("4. Reset the AUTO_INCREMENT for a table");
        System.out.println();
        System.out.println("9. DELETE ALL TODOs");

        Scanner scanner = new Scanner(System.in);

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                addTodoPretty();
                break;

            case 2:
                makeChildTodoPretty();
                break;

            case 3:
                finishTodoPretty();
                break;

            case 4:
                System.out.println("Which table do you want to fix?");
                String table = scanner.nextLine();
                System.out.println("To what do you want to reset the AUTO_INCREMENT?");
                int id = scanner.nextInt();
                scanner.nextLine();
                fixAutoIncrement(table, id);
                break;

            case 9:
                System.out.println("I haven't implemented that yet");
                break;

            default:
                System.out.println("Alright, nevermind then.");
        }

    }

    public static boolean executeStatement(String statement) {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            String configFilePath = "src/config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);

            Properties props =new Properties();
            props.load(propsInput);

            String user = props.getProperty("username");
            String password = props.getProperty("password");

            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.128:3306/grouse_lol",user,password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(statement);

        } catch (Exception e) {
            System.out.println(e);
        }

        return false;

    }

    public static ResultSet doQuery(String statement) {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            String configFilePath = "src/config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);

            Properties props =new Properties();
            props.load(propsInput);

            String user = props.getProperty("username");
            String password = props.getProperty("password");

            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.128:3306/grouse_lol",user,password);
            Statement stmt = con.createStatement();
            return stmt.executeQuery(statement);

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;

    }

    public static boolean addTodo(String todo, String DateTime) {
        return executeStatement("INSERT INTO todos (todo, date_added)\n" +
                        "VALUES (\"" + todo + "\", \"" + DateTime + "\")");
    }

    public static boolean giveTodoParent(int id, int parent_id) {
        return executeStatement("INSERT INTO child_todos (id, parent_id)\n" +
                "VALUES (" + id + ", " + parent_id + ")");
    }

    public static boolean finishTodo(int id, String date_finished, boolean is_satisfactory) {
        int satisfactory_int = 0;
        if(is_satisfactory) {
            satisfactory_int = 1;
        }
        return executeStatement("INSERT INTO finished_todos (id, date_finished, is_satisfactory)\n" +
                "VALUES (" + id + ", \"" + date_finished + "\", " + satisfactory_int + ")");
    }

    public static boolean addTodoPretty() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("What is the TODO?");
        String todo = scanner.nextLine();

        System.out.println("When was it added?");
        String date_added = scanner.nextLine();

        addTodo(todo, date_added);

        System.out.println("Is this TODO a child of another TODO? (Y/N)");
        if(scanner.nextLine().equalsIgnoreCase("Y")) {

            if(makeChildTodoPretty()) {
                System.out.println("Childrearing successful");
            } else {
                System.out.println("Might not have childed right, idk.");
            }

        }

        System.out.println("Is this new TODO completed? (Y/N)");
        if(scanner.nextLine().equalsIgnoreCase("Y")) {

            if(finishTodoPretty()) {
                System.out.println("TODO finished successfully");
            } else {
                System.out.println("TODO failed to finish maybe, idk. This is hard, ok?");
            }

        }

        return true;

    }

    public static boolean makeChildTodoPretty() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("What id was THIS TODO just given? (I wasn't paying attention)");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("What is the parent's id?");
        int parent_id = scanner.nextInt();
        scanner.nextLine();

        return giveTodoParent(id, parent_id);

    }

    public static boolean finishTodoPretty() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("What id was it just given? (I wasn't paying attention)");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("When was it completed?");
        String date_finished = scanner.nextLine();

        System.out.println("Was it completed in a satisfactory manner? (Y/N)");
        boolean is_satisfactory = scanner.nextLine().equalsIgnoreCase("Y");

        return finishTodo(id, date_finished, is_satisfactory);

    }



    public static String fixAutoIncrement(String table, int id) throws SQLException {

        executeStatement("ALTER TABLE " + table + " AUTO_INCREMENT=" + id);
        return "hi";

    }

}