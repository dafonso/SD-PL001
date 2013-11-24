package client;

import common.Event;
import common.Message;
import common.properties.CommonProps;
import java.net.InetAddress;
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
    //static private String hostName;
    /**
     * Port number where the server is listening.
     */
    //static private int portNumber;
    private static Scanner s;

    public static void main(String[] args) throws ParseException {
        s = new Scanner(System.in);
        
        try {
            //hostName = InetAddress.getLocalHost().getHostAddress();
            //portNumber = CommonProps.getServerPort();
            //Client client = new Client(hostName, portNumber);
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
        Message m = new Message();
        int task = s.nextInt();
        while (task != 6) {
            switch (task) {
                case 1:
                    client.setAgenda();
                    System.out.println(client.showAgenda());
                    break;
                case 2:
                    Event e = new Event(convertString2Date(s.next()), convertString2Date(s.next()), s.next(), s.next());
                    m = client.addEvent(e);
                    client.processMessage(m);
                    client.setAgenda();
                    System.out.println(client.showAgenda());
                    break;
                case 3:
                    System.out.println(client.showAgenda());
                    System.out.println("Escolha o evento a actualizar?");
                    int id = s.nextInt();
                    System.out.println("A informação a alterar é:\n1 - Data Inicio\n2 - Data Fim\n"
                            + "3 - Titulo\n4 - Descrição");
                    Event event = buildEvent(s.nextInt());
                    event.setId(id);
                    m = client.updateEvent(event);
                    client.processMessage(m);
                    client.setAgenda();
                    break;
                case 4:
                    System.out.println(client.showAgenda());
                    System.out.println("Escolha o evento a apagar?");
                    m = client.deleteEvent(s.nextInt());
                    client.processMessage(m);
                    client.setAgenda();
                    break;
                case 5:
                    System.out.println("Pesquisar por:\n1 - Data Inicio\n2 - Data Fim\n"
                            + "3 - Titulo\n4 - Descrição");
                    m = client.findEvent(buildEvent(s.nextInt()));
                    client.processMessage(m);
                    System.out.println(client.showAgenda());
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

        //return new SimpleDateFormat("yyyy.mm.dd 'at' hh:mm:ss z").parse(s);
        return new SimpleDateFormat("dd/MM/yyyy").parse(s);

    }
}
