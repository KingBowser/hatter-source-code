package me.hatter.tools.jtop.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JStackService extends Remote {

    String getProcessId() throws RemoteException;

    JMemoryInfo getMemoryInfo() throws RemoteException;

    JThreadInfo[] listThreadInfos() throws RemoteException;
}
