/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.gprinter.aidl;
public interface GpService extends android.os.IInterface
{
  /** Default implementation for GpService. */
  public static class Default implements com.gprinter.aidl.GpService
  {
    @Override public int openPort(int PrinterId, int PortType, java.lang.String DeviceName, int PortNumber) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void closePort(int PrinterId) throws android.os.RemoteException
    {
    }
    @Override public int getPrinterConnectStatus(int PrinterId) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public int printeTestPage(int PrinterId) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void queryPrinterStatus(int PrinterId, int Timesout, int requestCode) throws android.os.RemoteException
    {
    }
    @Override public int getPrinterCommandType(int PrinterId) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public int sendEscCommand(int PrinterId, java.lang.String b64) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public int sendLabelCommand(int PrinterId, java.lang.String b64) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void isUserExperience(boolean userExperience) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String getClientID() throws android.os.RemoteException
    {
      return null;
    }
    @Override public int setServerIP(java.lang.String ip, int port) throws android.os.RemoteException
    {
      return 0;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.gprinter.aidl.GpService
  {
    private static final java.lang.String DESCRIPTOR = "com.gprinter.aidl.GpService";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.gprinter.aidl.GpService interface,
     * generating a proxy if needed.
     */
    public static com.gprinter.aidl.GpService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.gprinter.aidl.GpService))) {
        return ((com.gprinter.aidl.GpService)iin);
      }
      return new com.gprinter.aidl.GpService.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_openPort:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          int _arg3;
          _arg3 = data.readInt();
          int _result = this.openPort(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_closePort:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          this.closePort(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getPrinterConnectStatus:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _result = this.getPrinterConnectStatus(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_printeTestPage:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _result = this.printeTestPage(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_queryPrinterStatus:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          this.queryPrinterStatus(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getPrinterCommandType:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _result = this.getPrinterCommandType(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_sendEscCommand:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _result = this.sendEscCommand(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_sendLabelCommand:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _result = this.sendLabelCommand(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_isUserExperience:
        {
          data.enforceInterface(descriptor);
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.isUserExperience(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getClientID:
        {
          data.enforceInterface(descriptor);
          java.lang.String _result = this.getClientID();
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        case TRANSACTION_setServerIP:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int _result = this.setServerIP(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.gprinter.aidl.GpService
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public int openPort(int PrinterId, int PortType, java.lang.String DeviceName, int PortNumber) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(PrinterId);
          _data.writeInt(PortType);
          _data.writeString(DeviceName);
          _data.writeInt(PortNumber);
          boolean _status = mRemote.transact(Stub.TRANSACTION_openPort, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().openPort(PrinterId, PortType, DeviceName, PortNumber);
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void closePort(int PrinterId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(PrinterId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_closePort, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().closePort(PrinterId);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public int getPrinterConnectStatus(int PrinterId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(PrinterId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPrinterConnectStatus, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getPrinterConnectStatus(PrinterId);
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int printeTestPage(int PrinterId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(PrinterId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_printeTestPage, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().printeTestPage(PrinterId);
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void queryPrinterStatus(int PrinterId, int Timesout, int requestCode) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(PrinterId);
          _data.writeInt(Timesout);
          _data.writeInt(requestCode);
          boolean _status = mRemote.transact(Stub.TRANSACTION_queryPrinterStatus, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().queryPrinterStatus(PrinterId, Timesout, requestCode);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public int getPrinterCommandType(int PrinterId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(PrinterId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPrinterCommandType, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getPrinterCommandType(PrinterId);
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int sendEscCommand(int PrinterId, java.lang.String b64) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(PrinterId);
          _data.writeString(b64);
          boolean _status = mRemote.transact(Stub.TRANSACTION_sendEscCommand, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().sendEscCommand(PrinterId, b64);
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int sendLabelCommand(int PrinterId, java.lang.String b64) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(PrinterId);
          _data.writeString(b64);
          boolean _status = mRemote.transact(Stub.TRANSACTION_sendLabelCommand, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().sendLabelCommand(PrinterId, b64);
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void isUserExperience(boolean userExperience) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((userExperience)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_isUserExperience, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().isUserExperience(userExperience);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String getClientID() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getClientID, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getClientID();
          }
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int setServerIP(java.lang.String ip, int port) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ip);
          _data.writeInt(port);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setServerIP, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().setServerIP(ip, port);
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static com.gprinter.aidl.GpService sDefaultImpl;
    }
    static final int TRANSACTION_openPort = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_closePort = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_getPrinterConnectStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_printeTestPage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_queryPrinterStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_getPrinterCommandType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_sendEscCommand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_sendLabelCommand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_isUserExperience = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_getClientID = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_setServerIP = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    public static boolean setDefaultImpl(com.gprinter.aidl.GpService impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static com.gprinter.aidl.GpService getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public int openPort(int PrinterId, int PortType, java.lang.String DeviceName, int PortNumber) throws android.os.RemoteException;
  public void closePort(int PrinterId) throws android.os.RemoteException;
  public int getPrinterConnectStatus(int PrinterId) throws android.os.RemoteException;
  public int printeTestPage(int PrinterId) throws android.os.RemoteException;
  public void queryPrinterStatus(int PrinterId, int Timesout, int requestCode) throws android.os.RemoteException;
  public int getPrinterCommandType(int PrinterId) throws android.os.RemoteException;
  public int sendEscCommand(int PrinterId, java.lang.String b64) throws android.os.RemoteException;
  public int sendLabelCommand(int PrinterId, java.lang.String b64) throws android.os.RemoteException;
  public void isUserExperience(boolean userExperience) throws android.os.RemoteException;
  public java.lang.String getClientID() throws android.os.RemoteException;
  public int setServerIP(java.lang.String ip, int port) throws android.os.RemoteException;
}
