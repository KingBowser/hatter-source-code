package me.hatter.tools.jtop.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JStackService extends Remote {

    String[] listThreadInfos() throws RemoteException;
}
