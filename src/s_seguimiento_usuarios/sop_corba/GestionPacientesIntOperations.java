package s_seguimiento_usuarios.sop_corba;


/**
* s_seguimiento_usuarios/sop_corba/GestionPacientesIntOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from gpacientes.idl
* domingo 6 de marzo de 2022 13H51' COT
*/

public interface GestionPacientesIntOperations 
{
  void registrarValoracion (s_seguimiento_usuarios.sop_corba.ValoracionDTO objValoracion, org.omg.CORBA.BooleanHolder res);
  boolean consultarValoracion (int id, s_seguimiento_usuarios.sop_corba.ValoracionDTOHolder objValoracion);
  void registrarProgramaFisico (s_seguimiento_usuarios.sop_corba.ProgramaFisicoDTO objProgramaFisico, org.omg.CORBA.BooleanHolder res);
  boolean consultarProgramaFisico (int id, s_seguimiento_usuarios.sop_corba.ProgramaFisicoDTOHolder objProgramaFisico);
  void registrarAsistencia (s_seguimiento_usuarios.sop_corba.AsistenciaDTO objAsistencia, org.omg.CORBA.BooleanHolder res);
  boolean consultarAsistencia (int id, s_seguimiento_usuarios.sop_corba.ArrayAsistenciaHolder lstAsistencia);
  void contarFaltas (int id, org.omg.CORBA.IntHolder numFaltas);
  void enviarNotificacion (s_seguimiento_usuarios.sop_corba.notificacionDTO objNotificacion);
} // interface GestionPacientesIntOperations