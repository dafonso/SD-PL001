package client;

import common.Event;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

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

    public static void main(String[] args) throws ParseException {
        serverHostName = args[0];
        serverPortNumber = Integer.valueOf(args[1]);
        try {
            Client client = new Client(serverHostName, serverPortNumber);
            mainLoop(client);
        } catch (java.net.UnknownHostException e) {
            System.err.println("Error connecting to server");
        } catch (java.io.IOException e) {
            System.err.println("Error opening read and write streams");
        }
    }

    private static void mainLoop(Client client) throws ParseException {
        Scanner s = new Scanner(System.in);
        System.out.print("1 - Marcar Evento\n2 - Alterar Evento\n3 - Apagar Evento\n4 - Procurar Evento\n5 - Terminar ");
        //int choice = s.nextInt();
        while (s.hasNext()) {
            switch (s.nextInt()) {
                case 1:
                    client.addEvent(convertData2Event(s));
                    break;
                case 2:
                    client.updateEvent(convertData2Event(s));
                    break;
                case 3:
                    client.deleteEvent(convertData2Event(s));
                    break;
                case 4:
                    client.findEvent(convertData2Event(s));
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

    }

    private static Event convertData2Event(Scanner s) throws ParseException {
        System.out.print("Introduzir dados do evento no seguinte formato? ");
        return new Event(new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z")
                .parse(s.next()), new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z")
                .parse(s.next()), s.next(), s.next());
    }
}