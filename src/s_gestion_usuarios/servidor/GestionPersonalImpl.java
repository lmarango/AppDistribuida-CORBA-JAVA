package s_gestion_usuarios.servidor;

import java.util.ArrayList;

import org.omg.CORBA.BooleanHolder;
import org.omg.CosNaming.NamingContextExt;

import s_gestion_usuarios.sop_corba.*;
import s_seguimiento_usuarios.sop_corba.GestionPacientesInt;
import s_seguimiento_usuarios.sop_corba.GestionPacientesIntHelper;
import s_seguimiento_usuarios.sop_corba.notificacionDTO;

public class GestionPersonalImpl extends GestionPersonalIntPOA{
    //Lista del personal registrado
    private ArrayList<personalDTO> lstPersonal;
    //Lista de los usuarios(pacientes) registrados
    private ArrayList<usuarioDTO> lstUsuarios;

    GestionPacientesInt ref;
    //Referencia al callback Admin
    private AdmCllbckInt admCllbckInt;
    //Referencia al callback Admin
    private FapCllbckInt fapCllbckInt;  
    //Atributos del administrador
    String admNombre = "Administrador";
    String admtTipoID = "CC";
    int admID = 0;
    String admOcup = "Admin";
    String admUser = "Admin";
    String admPsw = "12345";
    personalDTO admin = new personalDTO(admtTipoID,admID,admNombre, admOcup, admUser, admPsw);
    public GestionPersonalImpl(){
        super();
        lstPersonal = new ArrayList<personalDTO>();
        lstUsuarios = new ArrayList<usuarioDTO>();
        lstPersonal.add(admin);
        admCllbckInt = null;
        fapCllbckInt = null;
        ref = null;
    }
  
    @Override
    public void registrarPersonal(personalDTO objPersonal, BooleanHolder res) {
        System.out.println("*** En registrarPersonal()...");
        res.value = false;
        if (lstPersonal.size() < 3) {
            if(!existeRegistroPersonal(objPersonal)){
                if (!existeID(objPersonal.id)) {
                    if(lstPersonal.add(objPersonal)){
                        res.value = true;
                        System.out.println("El personal " + objPersonal.nombreCompleto + " y ocupacion " + objPersonal.ocupacion + " fue registrado con exito");
                    }
                }else{
                    System.out.println("No se puede registrar un usuario con un id existente en el sistema.");
                }
            }else{
                System.out.println("ya exite un personal con la ocupacion " + objPersonal.ocupacion);
            }
        }else{
            System.out.println("Personal No registrado, se alcanzo la cantidad maxima de personas a registrar.");
        }
    }

    @Override
    public void registrarUsuario(usuarioDTO objUsuario, BooleanHolder res) {
        System.out.println("*** En registrarUsuario()...");
        res.value = false;
        if (!existeID(objUsuario.id)) {
            if(lstUsuarios.add(objUsuario)){
                res.value = true;
                System.out.println("El Usuario " + objUsuario.nombreCompleto + " fue registrado con exito");
                fapCllbckInt.informarIngreso(objUsuario.nombreCompleto, objUsuario.id);
            }
        }else{
            System.out.println("No se puede registrar un usuario con un id existente en el sistema");
        }
    }

    @Override
    public boolean consultarPersonal(int id, personalDTOHolder objPersonal) {
        System.out.println("*** En consultarPersonal()...");
        personalDTO varObjPersonal = new personalDTO("", 0, "", "", "", ""); 
        objPersonal.value = varObjPersonal;
        boolean resultado = false;
        try {
            if (!lstPersonal.isEmpty()) {
                for (int i = 0; i < lstPersonal.size(); i++) {
                    if (lstPersonal.get(i).id == id) {
                        objPersonal.value = lstPersonal.get(i);
                        resultado = true;
                        System.out.println("*** Se encontro al usuario con id " + lstPersonal.get(i).id);
                        break;
                    }
                }
            }else{
                System.out.println("No hay personal registrado en el sistema");
            }
        } catch (Exception e) {
            System.out.println("NO se pudo recuperar la consulta");
        }
        return resultado;
    }

    @Override
    public boolean consultarUsuario(int id, usuarioDTOHolder objUsuario) {
        System.out.println("*** En consultarUsuario()...");
        usuarioDTO vUsuarioDTO = new usuarioDTO(0, "", "", "", "", "", "", "");
        objUsuario.value = vUsuarioDTO;
        boolean resultado = false;
        try {
            if (!lstUsuarios.isEmpty()) {
                for (int i = 0; i < lstUsuarios.size(); i++) {
                    if (lstUsuarios.get(i).id == id) {
                        objUsuario.value = lstUsuarios.get(i);
                        resultado = true;
                        System.out.println("*** Se encontro al usuario con id " + id);
                        break;
                    }
                }
            }else{
                System.out.println("No hay usuarios registrados en el sistema");
            }
        } catch (Exception e) {
            System.out.println("NO se pudo recuperar la consulta");
        }
        return resultado;
    }
    
    @Override
    public boolean abrirSesion(credencialDTO objCredencial) {
        System.out.println("***En abrirSesion()...");
        boolean resultado = false;
        personalDTO varPersonal = null;
        for (int i = 0; i < lstPersonal.size(); i++) {
            if (objCredencial.usuario.equals(lstPersonal.get(i).usuario) &&
                objCredencial.clave.equals(lstPersonal.get(i).clave) && 
                objCredencial.id == lstPersonal.get(i).id) {
                    resultado = true;
                    varPersonal = lstPersonal.get(i);
                    break;
            }
        }
        usuarioDTO varUsuario = null;
        for (int j = 0; j < lstUsuarios.size(); j++) {
            if (objCredencial.usuario.equals(lstUsuarios.get(j).usuario) &&
                objCredencial.clave.equals(lstUsuarios.get(j).clave) &&
                objCredencial.id == lstUsuarios.get(j).id) {
                    resultado = true;
                    varUsuario = lstUsuarios.get(j);
                    break;
            }
        }
        if (varUsuario != null){
            System.out.println("El usuario " + varUsuario.usuario + " ingreso al Sistema");
        }
        if (varPersonal !=null) {
            String nombreCompleto = varPersonal.nombreCompleto;
            String ocupacion = varPersonal.ocupacion;

            switch (ocupacion) {
                case "Admin":
                    System.out.println("Admin ingreso al Sistema");
                    break;
                case "SEC":
                    System.out.println("La secretaria " + nombreCompleto + " ingreso al Sistema");
                    break;
                case "PAF":
                    this.admCllbckInt.notificar(nombreCompleto, varPersonal.id);
                    System.out.println("El PAF " + nombreCompleto + " ininicio Sesion");
                    System.out.println("Enviando notificacion()...");
                    notificacionDTO varNotificacion = new notificacionDTO();
                    varNotificacion.nombreCompleto = nombreCompleto;
                    varNotificacion.ocupacion = ocupacion;    
                    ref.enviarNotificacion(varNotificacion);
                    break;
            }   
        }
        return resultado;
    }

    @Override
    public void registrarCallback(AdmCllbckInt objCllbck) {
        System.out.println("*** Desde registrarCallback() ***");
        this.admCllbckInt = objCllbck;
    }

    @Override
    public void registrarCallbackFap(FapCllbckInt objCllbck) {
        System.out.println("*** Desde registrarCallbackFap() ***");
        this.fapCllbckInt = objCllbck;
    }

    private boolean existeID(int id){
        System.out.println("En existeID");
        boolean resultado = false;
        for (int i = 0; i < lstPersonal.size(); i++) {
            if (lstPersonal.get(i).id == id) {
                resultado = true;
            }
        }

        for (int j = 0; j < lstUsuarios.size(); j++) {
            if (lstUsuarios.get(j).id == id) {
                resultado = true;
            }
        }
        return resultado;
    }

    public void consultarReferenciaRemota(NamingContextExt nce, String servicio){
        System.out.println("En consultarReferenciaRemota");
        try{
            this.ref = GestionPacientesIntHelper.narrow(nce.resolve_str(servicio));
            System.out.println("Obtenido el manejador sobre el servidor de objetos: ");
        }catch(Exception ex){
            System.out.println("Error: "+ ex.getMessage());
        }  	
    }

    private boolean existeRegistroPersonal(personalDTO prmObjPersonalDTO){
        System.out.println("En existeRegistroPersonal()");
        boolean resultado = false;
        for (int i = 0; i < lstPersonal.size(); i++) {
            if (lstPersonal.get(i).ocupacion.equals(prmObjPersonalDTO.ocupacion)) {
                resultado = true;
            }
        }
        return resultado;
    }
}
