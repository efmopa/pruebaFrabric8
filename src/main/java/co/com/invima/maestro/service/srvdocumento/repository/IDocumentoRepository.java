package co.com.invima.maestro.service.srvdocumento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.com.invima.maestro.modeloTransversal.entities.documento.DocumentoDAO;

@Repository
public interface IDocumentoRepository extends JpaRepository<DocumentoDAO, Integer> {

    @Query("select documento from DocumentoDAO documento where documento.id=:id")
    DocumentoDAO consultarPorID(@Param("id") Integer id);

    @Procedure("dbo.USP_Documento_I")
    String guardarDocumento(String json);

    @Procedure("dbo.USP_DocumentosOAC_S")
    String documentosOAC(String json);

    @Procedure("dbo.USP_Documento_S")
    String consultarArticuloporidDocumento(Integer idDocumento);

    @Query("select documento from DocumentoDAO documento where documento.idTramite=:idTramite")
    List<DocumentoDAO> consultarDocumentoTramite(@Param("idTramite") Integer idTramite);

    @Procedure("dbo.USP_GestionarTareaTramiteDocumentacion_S")
    String consultarTareaTramite();

    @Procedure("dbo.USP_GestionarTareaTramiteDocumentacion_U")
    String actualizarTareaTramiteDocumentacion(String json);

    @Procedure("dbo.USP_DocumentoGenerar_S")
    String consultarDocumentoGenerar(Integer idEtapa, Integer idRol, Integer idConcepto);

    @Procedure("dbo.USP_DocumentacionOAC_S")
    String consultarDocumentacionOAC();

    @Procedure("dbo.USP_DocumentoAsociado_S")
    String consultarDocumentosAsociados(Integer idSolicitud);

    @Procedure("dbo.USP_DocumentoAsociado_U")
    String actualizarDocumentoExpediente(Integer idSolicitud, String json);

    @Procedure("dbo.USP_ConsultarDocumentosAsociados_S")
    String consultarDocumentoAsociadoLegalTecnico(Integer idTramite, Integer idPersona);

    @Procedure("dbo.USP_ConsultarDocumentosAsociadosV1_S")
    String consultarDocumentoAsociadoCoordinador(Integer idTramite);

    @Procedure("dbo.USP_ObservacionDocumentoAsociado_I")
    String guardaAprobacionDocumentoAsociado(String json);

    @Procedure("dbo.USP_DocumentoModeloConfiguracion_I")
    String guardarDocumentoConfiguracion(String json);

    @Procedure("dbo.USP_ConsultarDocumentoGenerado_S")
    String consultarDocumentoGenerados(Integer idTramite);

    @Procedure("Tramites.tramite.USP_ConsultarInformacionDocumentoProducir_S")
    String consultarInformacionDocumentoProducir(Integer idTramite);

    @Procedure("dbo.USP_DevolverDocumentoGenerado")
    String devolverDocumentoGenerado(String json);

    @Procedure("dbo.USP_ConsultarDocumentosFiltro_S")
    String consultarDocumentoFiltro(String json);

    @Procedure("Tramites.tramite.USP_ConsultarInformacionFactura_S")
    String consultarInformacionFactura(Integer idTramite);

    @Procedure("dbo.USP_TipoDocumentalModeloMetadatos_S")
    String generarMetadataSeSuite(String json);

    @Procedure("dbo.USP_ConsultarDocumentos_S")
    String consultarDocumentos(Integer idTramite, String tipo);

    @Procedure("dbo.USP_ActualizarEnviarEnNotificacion_U")
    String actualizarEnviarEnNotificacion(String json);

    @Procedure("dbo.USP_Consecutivos_S")
    String consultaConsecutivo(String codigoConsecutivo);

    @Procedure("Tramites.tramite.USP_ObtenerFactura_S")
    String obtenerInformacionFactura(String json_IN);
    
    @Procedure("dbo.USP_DocumentoModelo_I")
    String crearDocumentoModelo(String json);
    
	@Procedure("dbo.USP_DocumentoModelo_S")
	String consultarDocumentoModelo(String json);



	@Procedure("dbo.USP_DocumentoModelo_Q")
	String consultaDinamicaDocumentoModelo(String json);

	@Procedure("dbo.USP_DocumentoModelo_U")
	String actualizarDocumentoModelo(String json);

	@Procedure("dbo.USP_DocumentoModelo_D")
	String eliminarDocumentoModelo(String json);

    @Procedure("dbo.USP_DocumentoModeloConfiguracion_D")
    String eliminarDocumentoModeloConfiguracion(String json);

	@Procedure("dbo.USP_DocumentoModeloSeccion_I")
	String crearDocumentoModeloSeccion(String json);

	@Procedure("dbo.USP_DocumentoModeloSeccion_IS")
	String crearDocumentoModeloSeccionMultiple(String json);

	@Procedure("dbo.USP_DocumentoModeloSeccion_S")
	String consultarDocumentoModeloSeccion(String json);

	@Procedure("dbo.USP_DocumentoModeloSeccion_U")
	String actualizarDocumentoModeloSeccion(String json);

	@Procedure("dbo.USP_DocumentoModeloSeccion_US")
	String actualizarDocumentoModeloSeccionMultiple(String json);

	@Procedure("dbo.USP_DocumentoModeloSeccion_D")
	String eliminarDocumentoModeloSeccion(String json);

	@Procedure("dbo.USP_DocumentoSeccion_I")
	String crearDocumentoSeccion(String json);

	@Procedure("dbo.USP_DocumentoSeccion_S")
	String consultarDocumentoSeccion(String json);

	@Procedure("dbo.USP_DocumentoSeccion_U")
	String actualizarDocumentoSeccion(String json);

	@Procedure("dbo.USP_DocumentoSeccion_D")
	String eliminarDocumentoSeccion(String json);

    @Procedure("dbo.USP_NumeroFolios_S")
    String consultarNumeroFolios(String json);
    
    @Procedure("dbo.USP_DocumentoModelo_Q")
    String consultarPlantillaGenerada(String json);
}
