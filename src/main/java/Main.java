import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws SQLException {

        if (!DatabaseConnection.isConnection()) {
            System.out.println("Chyba pri spojeni s databazi, zkontrolujte internetove pripojeni.");
            return;
        }

        int setOfFunctionsPICKER = 6;


        if (setOfFunctionsPICKER == 0) {
            System.out.println("\nPro prehlednost jsem rozdelil vypis funkci do bloku (tak jak jsou v projektu),\n" +
                    "zmenou promenne \"setOfFunctionsPICKER\" na hodnoty 1 - 6 se vypise pokazde dany blok funkci.");
        }


    }
    }