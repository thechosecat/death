package cat.death;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Mysql {

    private String host = "192.168.0.44";
    private String port = "3306";
    private String DataBase = "test";
    private String UserName = "cat";
    private String Passworld = "Joshua1222";

    private Connection connection;

    public boolean isConnect(){
        return (connection == null ? false : true );
    }
    public void connect() throws ClassNotFoundException, SQLException {



        if(!isConnect()){

            connection = DriverManager.getConnection("jdbc:mysql://" + host +
                            ":" + port + "/" + DataBase + "?useSSL=false", UserName, Passworld);
        }

    }
    public void disconnect(){
        if(isConnect()){
            try {
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    public Connection getConnection(){
        return connection;
    }


}
