package s_gestion_usuarios.sop_corba;


/**
* s_gestion_usuarios/sop_corba/GestionPersonalIntPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from gusuarios.idl
* domingo 6 de marzo de 2022 13H51' COT
*/

public abstract class GestionPersonalIntPOA extends org.omg.PortableServer.Servant
 implements s_gestion_usuarios.sop_corba.GestionPersonalIntOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("registrarPersonal", new java.lang.Integer (0));
    _methods.put ("consultarPersonal", new java.lang.Integer (1));
    _methods.put ("registrarUsuario", new java.lang.Integer (2));
    _methods.put ("consultarUsuario", new java.lang.Integer (3));
    _methods.put ("abrirSesion", new java.lang.Integer (4));
    _methods.put ("registrarCallback", new java.lang.Integer (5));
    _methods.put ("registrarCallbackFap", new java.lang.Integer (6));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // sop_corba/GestionPersonalInt/registrarPersonal
       {
         s_gestion_usuarios.sop_corba.personalDTO objPersonal = s_gestion_usuarios.sop_corba.personalDTOHelper.read (in);
         org.omg.CORBA.BooleanHolder res = new org.omg.CORBA.BooleanHolder ();
         this.registrarPersonal (objPersonal, res);
         out = $rh.createReply();
         out.write_boolean (res.value);
         break;
       }

       case 1:  // sop_corba/GestionPersonalInt/consultarPersonal
       {
         int id = in.read_long ();
         s_gestion_usuarios.sop_corba.personalDTOHolder objPersonal = new s_gestion_usuarios.sop_corba.personalDTOHolder ();
         boolean $result = false;
         $result = this.consultarPersonal (id, objPersonal);
         out = $rh.createReply();
         out.write_boolean ($result);
         s_gestion_usuarios.sop_corba.personalDTOHelper.write (out, objPersonal.value);
         break;
       }

       case 2:  // sop_corba/GestionPersonalInt/registrarUsuario
       {
         s_gestion_usuarios.sop_corba.usuarioDTO objUsuario = s_gestion_usuarios.sop_corba.usuarioDTOHelper.read (in);
         org.omg.CORBA.BooleanHolder res = new org.omg.CORBA.BooleanHolder ();
         this.registrarUsuario (objUsuario, res);
         out = $rh.createReply();
         out.write_boolean (res.value);
         break;
       }

       case 3:  // sop_corba/GestionPersonalInt/consultarUsuario
       {
         int id = in.read_long ();
         s_gestion_usuarios.sop_corba.usuarioDTOHolder objUsuario = new s_gestion_usuarios.sop_corba.usuarioDTOHolder ();
         boolean $result = false;
         $result = this.consultarUsuario (id, objUsuario);
         out = $rh.createReply();
         out.write_boolean ($result);
         s_gestion_usuarios.sop_corba.usuarioDTOHelper.write (out, objUsuario.value);
         break;
       }

       case 4:  // sop_corba/GestionPersonalInt/abrirSesion
       {
         s_gestion_usuarios.sop_corba.credencialDTO objCredencial = s_gestion_usuarios.sop_corba.credencialDTOHelper.read (in);
         boolean $result = false;
         $result = this.abrirSesion (objCredencial);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 5:  // sop_corba/GestionPersonalInt/registrarCallback
       {
         s_gestion_usuarios.sop_corba.AdmCllbckInt objCllbck = s_gestion_usuarios.sop_corba.AdmCllbckIntHelper.read (in);
         this.registrarCallback (objCllbck);
         out = $rh.createReply();
         break;
       }

       case 6:  // sop_corba/GestionPersonalInt/registrarCallbackFap
       {
         s_gestion_usuarios.sop_corba.FapCllbckInt objCllbck = s_gestion_usuarios.sop_corba.FapCllbckIntHelper.read (in);
         this.registrarCallbackFap (objCllbck);
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:sop_corba/GestionPersonalInt:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public GestionPersonalInt _this() 
  {
    return GestionPersonalIntHelper.narrow(
    super._this_object());
  }

  public GestionPersonalInt _this(org.omg.CORBA.ORB orb) 
  {
    return GestionPersonalIntHelper.narrow(
    super._this_object(orb));
  }


} // class GestionPersonalIntPOA
