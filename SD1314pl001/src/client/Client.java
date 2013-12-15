/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import common.Agenda;
import common.Event;
import common.properties.CommonProps;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashSet;
import common.RemoteClientProtocol;

/**
 *
 * @author João
 */
public class Client
{

    private String[][] serverPool;
    private RemoteClientProtocol clientStub;
    private Agenda agenda;

    public Client()
    {
	this.serverPool = CommonProps.getServerPool();
	this.agenda = new Agenda();
	System.out.println("A iniciar o Cliente...");// Vamos tentar aceder ao Servidor de Registos para recolher a interface
	try
	{
	    connect2MasterServer();
	} catch (NoSuchServerOn e)
	{
	    System.out.println("Falhou o arranque do Cliente");
	    System.out.println("Não existe nenhum servidor ligado!!");
	    System.exit(0);
	}
    }

    private void connect2MasterServer() throws NoSuchServerOn
    {
	int i = 0;
	boolean masterFound = false;
	while (!masterFound && i < serverPool.length)
	{
	    String getMasterName;
	    Registry registry;
	    try
	    {
		registry = LocateRegistry.getRegistry(serverPool[i][0], Integer.parseInt(serverPool[i][1]));
		RemoteClientProtocol stub = (RemoteClientProtocol) registry.lookup(serverPool[i][3]);
		getMasterName = stub.getMasterServer().getKey();
		this.clientStub = (RemoteClientProtocol) registry.lookup(getMasterName);
		masterFound = true;
	    } catch (RemoteException | NotBoundException ex)
	    {
		System.out.println("Não é possível aceder ao servidor: " + ex);
		i++;
	    }
	}
	if (!masterFound && i >= serverPool.length)
	{
	    throw new NoSuchServerOn();
	}
    }
    
    public FeedBack addEvent(Event e)
    {

	boolean success = true;
	String description = "Evento adicionado com sucesso.";

	try
	{
	    clientStub.create(e);
	} catch (RemoteException ex)
	{
	    try
	    {
		connect2MasterServer();
	    } catch (NoSuchServerOn n)
	    {
		//System.out.println("Não existe nenhum servidor ligado!!");
		success = false;
		description = "Não existe nenhum servidor ligado!!";
	    }
	} catch (Exception exp)	{
	    success = false;
	    description = "Ocorreu um erro ao adicionar o evento.";
	} finally
	{
	    
	    return new FeedBack(success, description);
	}
    }

    public FeedBack updateEvent(Event e)
    {
	boolean success = true;
	String description = "Evento atualizado com sucesso.";
	try
	{
	    if (!clientStub.update(e))
	    {
		success = false;
		description = "Ocorreu um erro e o evento não foi actualizado!";
	    }
	} catch (RemoteException ex)
	{
	    try
	    {
		connect2MasterServer();
	    } catch (NoSuchServerOn n)
	    {
		success = false;
		description = "Não existe nenhum servidor ligado!!";
	    }
	}
	catch (Exception exp)	{
	    success = false;
	    description = "Ocorreu um erro ao adicionar o evento.";
	} finally
	{
	    return new FeedBack(success, description);
	}
    }

    public FeedBack deleteEvent(int id)
    {
	boolean success = true;
	String description = "Evento eliminado com sucesso.";
	try
	{
	    if (!clientStub.delete(id))
	    {
		success = false;
		description = "Ocorreu um erro e o evento não foi apagado!";
	    }
	} catch (RemoteException ex)
	{
	    try
	    {
		connect2MasterServer();
	    } catch (NoSuchServerOn n)
	    {
		success = false;
		description = "Não existe nenhum servidor ligado!!";
	    }
	}catch (Exception exp)	{
	    success = false;
	    description = "Ocorreu um erro ao adicionar o evento.";
	} finally
	{
	    return new FeedBack(success, description);
	}
    }

    public ArrayList<Event> findEvent(Event e)
    {
	ArrayList<Event> eventsList = new ArrayList<>();
	try
	{
	    eventsList = clientStub.find(e);
	} catch (RemoteException ex)
	{
	    try
	    {
		connect2MasterServer();
	    } catch (NoSuchServerOn n)
	    {
		System.out.println("Não existe nenhum servidor ligado!!");
	    }
	} finally
	{
	    return eventsList;
	}
    }

    public Event getEvent(int id)
    {
	Event event = null;
	for (Event e : agenda.getEvents())
	{
	    if (e.getId() == id)
	    {
		event = e;
		break;
	    }
	}
	return event;
    }

    public void setAgenda()
    {
	agenda.setEvents(new HashSet((ArrayList<Event>) findEvent(null)));
    }

    public String showAgenda()
    {
	StringBuilder str = new StringBuilder();
	for (Event e : agenda.getEvents())
	{
	    str.append(e.toString());
	    str.append("\n");
	}
	return str.toString();
    }
}
