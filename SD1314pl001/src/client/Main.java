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
    private static Scanner s;

    public static void main(String[] args) throws ParseException {
        s = new Scanner(System.in);
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
        System.out.println("1 - Ver Agenda\n2- Marcar Evento\n3 - Alterar Evento\n"
                + "4 - Apagar Evento\n5 - Procurar Evento\n6 - Terminar ");
        int task = s.nextInt();
        while (task != 6) {
            switch (task) {
                case 1:
                    client.showAgenda();
                    break;
                case 2:
                    Event e = new Event(convertString2Date(s.next()), convertString2Date(s.next()), s.next(), s.next());
                    client.addEvent(e);
                    client.showAgenda();
                    break;
                case 3:
                    client.showAgenda();
                    System.out.println("Escolha o evento a actualizar?");
                    int id = s.nextInt();
                    System.out.println("A informação a alterar é:\n1 - Data Inicio\n2- Data Inicio\n"
                            + "3 - Titulo\n4 - Descrição");
                    Event event = buildEvent(s.nextInt());
                    event.setId(id);
                    client.updateEvent(event);
                    break;
                case 4:
                    client.showAgenda();
                    System.out.println("Escolha o evento a apagar?");
                    client.deleteEvent(s.nextInt());
                    break;
                case 5:
                    System.out.println("Pesquisar por:\n1 - Data Inicio\n2- Data Inicio\n"
                            + "3 - Titulo\n4 - Descrição");
                    client.findEvent(buildEvent(s.nextInt()));
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
            System.out.println("1 - Ver Agenda\n2- Marcar Evento\n3 - Alterar Evento\n"
                    + "4 - Apagar Evento\n5 - Procurar Evento\n6 - Terminar ");
            task = s.nextInt();
        }
    }

    private static Event buildEvent(int field) throws ParseException {
        Event e = new Event();
        System.out.println("Introduza o texto pretendido");
        switch (field) {
            case 1:
                e.setStart(convertString2Date(s.next()));
                break;
            case 2:
                e.setEnd(convertString2Date(s.next()));
                break;
            case 3: 
                e.setTitle(s.next());
                break;
            case 4:
                e.setDescription(s.next());
                break;

            default:
                System.out.println("Invalid option");
                break;
        }
        return e;
    }

    private static Date convertString2Date(String s) throws ParseException {
        return new SimpleDateFormat("yyyy.mm.dd 'at' hh:mm:ss z").parse(s);

    }
}