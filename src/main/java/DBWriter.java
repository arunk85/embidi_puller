import org.sqlite.core.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBWriter {

    public DBWriter(){

    }

    public void writeToDB(String dbName , List<Question> questions) {
        String url = "jdbc:sqlite:/home/arun/Documents/db/" + dbName;
        try(Connection conn = DriverManager.getConnection(url)){
            if(conn != null) {
                String metaQuery = "CREATE TABLE \"android_metadata\" (\"locale\" TEXT DEFAULT 'en_US')";
                String insertMeta = "INSERT INTO \"android_metadata\" VALUES ('en_US')";

                String createTableSql = "CREATE TABLE IF NOt EXISTS dbquestion ( id text PRIMARY KEY, question text NOT NULL, " +
                        "options text NOT NULL, answer text, correctAttempts integer, failedAttempts integer, visited text, batch text);";

                Statement stm = conn.createStatement();
                stm.execute(metaQuery);
                stm.execute(insertMeta);
                stm.execute(createTableSql);
                int i = 0;
                for(Question q : questions){
                    i++;
                    String insertQuery = String.format("INSERT INTO dbquestion values (\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"false\",\"tbatch\")", q.getId(), q.getQuestion(),
                            q.getOptionsAsHashSep(), q.getAnswer(), q.getCorrectAttepmts(), q.getFailedAttempts());
                    stm.execute(insertQuery);
                    System.out.println(insertQuery);
                }


            }
        } catch (SQLException sqe){
            System.out.println(sqe);
        }
    }

    public static void main(String args[]){
        DBWriter writer = new DBWriter();
        writer.writeToDB("questionsdatabase", new ArrayList<>());
    }

}
