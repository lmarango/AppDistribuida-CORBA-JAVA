package s_seguimiento_usuarios.sop_corba;


/**
* s_seguimiento_usuarios/sop_corba/ArrayEjerciciosHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from gpacientes.idl
* domingo 6 de marzo de 2022 13H51' COT
*/

public final class ArrayEjerciciosHolder implements org.omg.CORBA.portable.Streamable
{
  public s_seguimiento_usuarios.sop_corba.EjercicioDTO value[] = null;

  public ArrayEjerciciosHolder ()
  {
  }

  public ArrayEjerciciosHolder (s_seguimiento_usuarios.sop_corba.EjercicioDTO[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = s_seguimiento_usuarios.sop_corba.ArrayEjerciciosHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    s_seguimiento_usuarios.sop_corba.ArrayEjerciciosHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return s_seguimiento_usuarios.sop_corba.ArrayEjerciciosHelper.type ();
  }

}
