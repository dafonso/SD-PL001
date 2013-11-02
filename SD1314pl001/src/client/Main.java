package client;

import common.Event;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emanuel
 */
public class Main {

    /**
     * Host name of the server.
     */
    static private String serverHostName;
    /**
     * Port number where the server is listening.
     */
    static private int serverPortNumber;
    private static Scanner s; 

    public static void main(String[] args) throws ParseException {
        s = new Scanner(System.in);
        serverHostName = args[0];
        serverPortNumber = Integer.valueOf(args[1]);
        try {
            Client client = new Client(serverHostName, serverPortNumber);
            mainLoop(client);
        } 
        catch (java.net.UnknownHostException e) {
            System.err.println("Error connecting to server");
        } 
        catch (java.io.IOException e) {
            System.err.println("Error opening read and write streams");
        }
    }

    private static void mainLoop(Client client) throws ParseException {
        System.out.print("\n1 - Ver Agenda\n2- Marcar Evento\n3 - Alterar Evento\n"
                + "4 - Apagar Evento\n5 - Procurar Evento\n6 - Terminar ");
        int task = s.nextInt();
        while (task != 6) {
            switch (task) {
                case 1:
                    client.showAgenda();
                    break;
                case 2:
                    client.addEvent(new Event(convertString2Date(s.next()),convertString2Date(s.next()),
                            s.next(),s.next()));
                    client.showAgenda();
                    break;
                case 3:
                    client.showAgenda();
                    System.out.println("Escolha o evento a actualizar?");
                    int id = s.nextInt();
                    System.out.println("Escolha o evento a actualizar?");
                    client.updateEvent(s.nextInt());                    
                    break;
                case 4:
                    client.showAgenda();
                    System.out.println("Escolha o evento a apagar?");
                    client.deleteEvent(s.nextInt());
                    break;
                case 5:
                    //client.findEvent(convertData2Event);
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
            System.out.print("\n1 - Ver Agenda\n2- Marcar Evento\n3 - Alterar Evento\n"
                + "4 - Apagar Evento\n5 - Procurar Evento\n6 - Terminar ");
            task = s.nextInt();
        }
    }
    private static Date convertString2Date(String s) throws ParseException {
        return new SimpleDateFormat("yyyy.mm.dd 'at' hh:mm:ss z").parse(s);

    }
}