package s_seguimiento_usuarios.sop_corba;


/**
* s_seguimiento_usuarios/sop_corba/EjercicioDTO.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from gpacientes.idl
* domingo 6 de marzo de 2022 13H51' COT
*/

public final class EjercicioDTO implements org.omg.CORBA.portable.IDLEntity
{
  public String nombreEjercicio = null;
  public int repeticiones = (int)0;
  public int peso = (int)0;

  public EjercicioDTO ()
  {
  } // ctor

  public EjercicioDTO (String _nombreEjercicio, int _repeticiones, int _peso)
  {
    nombreEjercicio = _nombreEjercicio;
    repeticiones = _repeticiones;
    peso = _peso;
  } // ctor

} // class EjercicioDTO
