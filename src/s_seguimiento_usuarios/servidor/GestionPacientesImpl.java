package s_seguimiento_usuarios.servidor;

import java.util.ArrayList;

import org.omg.CORBA.BooleanHolder;
import org.omg.CORBA.IntHolder;
import s_seguimiento_usuarios.sop_corba.*;

public class GestionPacientesImpl extends GestionPacientesIntPOA{

    private ArrayList<ValoracionDTO> lstValoraciones;
    private ArrayList<ProgramaFisicoDTO> lstProgramas;
    private ArrayList<AsistenciaDTO> lstAsistencia;

    public GestionPacientesImpl() {
        super();
        this.lstValoraciones = new ArrayList<ValoracionDTO>();
        this.lstProgramas = new ArrayList<ProgramaFisicoDTO>();
        this.lstAsistencia = new ArrayList<AsistenciaDTO>();
    }

    @Override
    public void registrarValoracion(ValoracionDTO objValoracion, BooleanHolder res) {
        System.out.println("***Entrando a registrarValoracion()...");
        res.value = false;
        if(objValoracion != null){
            objValoracion.estado = generarEstado(objValoracion);
            res.value = lstValoraciones.add(objValoracion);
            if (res.value) {
                System.out.println("Valoracion fisica Registrada! ");
            }else{
                System.out.println("ERROR: No se pudo registrar la valoracion fisica.");
            }
        }
    }

    @Override
    public boolean consultarValoracion(int id, ValoracionDTOHolder objValoracion) {
        System.out.println("***Entrando a consultarValoracion()...");
        ValoracionDTO varObjValoracionDTO = new ValoracionDTO(0, "", 0, 0, 0, 0, 0, 0, 0, "");
        objValoracion.value = varObjValoracionDTO;
        boolean resultado = false;
        int bandera =  buscarValoracion(id);
        if (bandera != -1) {
           objValoracion.value = lstValoraciones.get(bandera);
           resultado = true;
        }else{         
            System.out.println("ERROR: No se pudo se encontro la valoracion.");
        }
        return resultado;
    }

    @Override
    public void registrarProgramaFisico(ProgramaFisicoDTO objProgramaFisico, BooleanHolder res) {
        System.out.println("***Entrando a regProgramaFisico()...");
        res.value = false;
        if (objProgramaFisico != null) {
            res.value = lstProgramas.add(objProgramaFisico);
            if (res.value) {
                System.out.println("Programa fisico Agregado! ");
            } else {
                System.out.println("ERROR: No se pudo registrar el programa fisico.");
            }
        }
    }

    @Override
    public boolean consultarProgramaFisico(int id, ProgramaFisicoDTOHolder objProgramaFisico) {
        System.out.println("***Entrando a consultarProgramaFísico()...");
        ProgramaDTO[] varListaProgramasDefault = {};
        ProgramaFisicoDTO varObjProgramaFisicoDTO = new ProgramaFisicoDTO(0, "", varListaProgramasDefault);
        objProgramaFisico.value = varObjProgramaFisicoDTO;
        boolean resultado = false;
        int bandera = buscarPrograma(id);
        if (bandera != -1) {
            objProgramaFisico.value = lstProgramas.get(bandera);
            resultado = true;
        }else{
            System.out.println("ERROR: No se pudo se encontro el Programa.");
        }
        return resultado;
    }

    @Override
    public void registrarAsistencia(AsistenciaDTO objAsistencia, BooleanHolder res) {
        System.out.println("***En registrarAsistencia()...");
        res.value = false;
        if (objAsistencia != null) {
            res.value = this.lstAsistencia.add(objAsistencia);
            IntHolder faltas = new IntHolder();
            contarFaltas(objAsistencia.idPaciente, faltas);
            if (res.value) {
                System.out.println("Registro exitoso de asistencia.");
            }else{
                System.out.println("Registro de asistencia fallo.");
            }
            if (faltas.value >= 3) {
                System.out.println("Alcanzo el numero maximo de faltas");
                System.out.println("Se borraran valoracion y programa fisico");
                eliminarValoracion(objAsistencia.idPaciente);
                eliminarPrograma(objAsistencia.idPaciente);
            }
        }
    }

    @Override
    public boolean consultarAsistencia(int id, ArrayAsistenciaHolder lstAsistencia) {
        System.out.println("***En consultarAsistencia()...");
        AsistenciaDTO varAsistenciaDTO = new AsistenciaDTO(0, "", "");
        AsistenciaDTO[] lstAsistenciaDefault = {varAsistenciaDTO};
        lstAsistencia.value = lstAsistenciaDefault;
        boolean resultado = false;
        if (this.lstAsistencia.isEmpty()) {
           System.out.println("La lista de valoraciones esta vacia"); 
        } else {
            ArrayList<AsistenciaDTO> lstAsistenciasTemp = new ArrayList<AsistenciaDTO>();
            for (int i = 0; i < this.lstAsistencia.size(); i++) {
                if (this.lstAsistencia.get(i).idPaciente == id) { 
                    lstAsistenciasTemp.add(this.lstAsistencia.get(i));
                }
            }
            if (lstAsistenciasTemp.size() > 0) {
                lstAsistenciaDefault = new AsistenciaDTO[lstAsistenciasTemp.size()];
                lstAsistencia.value = lstAsistenciaDefault;
                for (int j = 0; j < lstAsistenciasTemp.size(); j++) {
                    lstAsistencia.value[j] = lstAsistenciasTemp.get(j);
                }
                resultado = true;
            }
        }
        return resultado;
    }

    @Override
    public void contarFaltas(int id, IntHolder numFaltas) {
        System.out.println("***En contarFaltas()...");
        numFaltas.value = 0;
        for (int i = 0; i < this.lstAsistencia.size(); i++) {
            if (this.lstAsistencia.get(i).idPaciente == id) {
                if(this.lstAsistencia.get(i).observacion.equals("No asiste")){
                    numFaltas.value = numFaltas.value + 1;
                } 
            }
        }
    }

    @Override
    public void enviarNotificacion(notificacionDTO objNotificacion){
        System.out.println("***En enviarNotificacion()...");
        System.out.println("El personal [" + objNotificacion.nombreCompleto + "] y ocupacion ["
        + objNotificacion.ocupacion + "] esta autorizado para ingresar al sistema.");
    }

    //Metodos Auxiliares
    public int buscarValoracion(int id){
        int resultado =  -1;
        for (int i = 0; i < lstValoraciones.size(); i++) {
            if (this.lstValoraciones.get(i).idPaciente == id) {
                resultado = i;
                break;
            }
        }
        return resultado;
    }
    public int buscarPrograma(int id){
        int resultado = -1;
        for (int i = 0; i < lstProgramas.size(); i++) {
            if (this.lstProgramas.get(i).idPaciente == id) {
                resultado = i;
                break;
            }
        }
        return resultado;
    }   
    public boolean eliminarValoracion(int id){
        boolean resultado = false;
        for (int i = 0; i < this.lstValoraciones.size(); i++) {
            if(this.lstValoraciones.get(i).idPaciente ==  id){
                this.lstValoraciones.remove(i);
                resultado = true;
                break;
            }
        }
        return resultado;
    }
    public boolean eliminarPrograma(int id){
        boolean resultado = false;
        for (int i = 0; i < this.lstProgramas.size(); i++) {
            if (this.lstProgramas.get(i).idPaciente == id) {
                this.lstProgramas.remove(i);
                resultado = true;
                break;
            }
        }
        return resultado;
    }
    public String generarEstado(ValoracionDTO objValoracion){
        String estado = "";
        int FCR = objValoracion.fecCardiacaReposo;
        if (FCR >= 86 || FCR <= 50) {
            estado = "Enfermo";
        }
        if (FCR >= 75 && FCR < 86) {
            estado = "Regular";
        }
        if (FCR > 50 && FCR < 75) {
            estado = "Sano";
        }
        return estado;
    }    
}
