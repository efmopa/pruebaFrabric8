package co.com.invima.maestro.service.srvdocumento.service;

import co.com.invima.canonico.modeloCanonico.dto.generic.GenericRequestDTO;
import co.com.invima.canonico.modeloCanonico.dto.generic.GenericResponseDTO;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;


public interface IDocumentoService {

    ResponseEntity<GenericResponseDTO> consultarPorID(Integer id);

    ResponseEntity<GenericResponseDTO> crear(GenericRequestDTO genericRequestDTO);

    ResponseEntity<GenericResponseDTO> actualizar(GenericRequestDTO genericRequestDTO);

    ResponseEntity<GenericResponseDTO> consultarDocumento();

    ResponseEntity<GenericResponseDTO> eliminadoLogicoPorId(Integer id);

    ResponseEntity<GenericResponseDTO> documentosAsociadosTramite(Integer idTramite);

    ResponseEntity<GenericResponseDTO> guardarDocumento(String genericRequestDTO);

    ResponseEntity<GenericResponseDTO> consultarArticuloporidDocumento(Integer idDocumento);

    ResponseEntity<GenericResponseDTO> consultarTareaTramite();

    ResponseEntity<GenericResponseDTO> actualizarTareaTramiteDocumentacion(String genericRequestDTO);

    ResponseEntity<GenericResponseDTO> consultarDocumentoGenerar(Integer idEtapa, Integer idRol, Integer idConcepto);

    ResponseEntity<GenericResponseDTO> consultarDocumentacionOAC();

    ResponseEntity<GenericResponseDTO> consultarDocumentosAsociados(Integer idSolicitud);

    ResponseEntity<GenericResponseDTO> actualizarDocumentosAsociados(Integer idSolicitud, String genericRequestDTO);

    ResponseEntity<GenericResponseDTO> plantillaCsv(Integer autorizacion);

    ResponseEntity<GenericResponseDTO> convertirCsv(InputStream is, Integer autorizacion);

    ResponseEntity<GenericResponseDTO> convertirCsvParaAutorizacionMuestrasNacionales(InputStream is);

    ResponseEntity<GenericResponseDTO> consultarDocumentoAsociadoLegalTecnico(Integer idTramite, Integer idPersona);

    ResponseEntity<GenericResponseDTO> consultarDocumentoAsociadoCoordinador(Integer idTramite);

    ResponseEntity<GenericResponseDTO> documentosOAC(String genericRequestDTO);

    ResponseEntity<GenericResponseDTO> crearAprobacionDocumentoAsociado(String genericRequestDTO);

    ResponseEntity<GenericResponseDTO> consultarDocumentosGenerados(Integer idTramite);

    ResponseEntity<GenericResponseDTO> consultarInformacionDocumentoProducir(Integer idTramite);

    ResponseEntity<GenericResponseDTO> guardarDocumentoConfiguracion(String json);

    ResponseEntity<GenericResponseDTO> devolverDocumentoGenerado(String genericRequestDTO);

    ResponseEntity<GenericResponseDTO> consultarDocumentosFiltro(String genericRequestDTO);

    ResponseEntity<GenericResponseDTO> devolverFactura(String json);

    ResponseEntity<GenericResponseDTO> generarMetadataSeSuite(String pJson);

    ResponseEntity<GenericResponseDTO> consultarDocumentos(Integer idTramite, String tipo);

    ResponseEntity<GenericResponseDTO> actualizarEnviarEnNotificacion(String json);

    ResponseEntity<GenericResponseDTO> crearDocumentoModelo(String json);

    ResponseEntity<GenericResponseDTO> consultarDocumentoModelo(String json);

    ResponseEntity<GenericResponseDTO> consultarDocumentoModeloConfiguracion(String json);

    ResponseEntity<GenericResponseDTO> consultarDocumentoModeloTipoDocumental(String json);

    ResponseEntity<GenericResponseDTO> consultaDinamicaDocumentoModelo(String json);

    ResponseEntity<GenericResponseDTO> actualizarDocumentoModelo(String json);



    ResponseEntity<GenericResponseDTO> eliminarDocumentoModelo(String json);

    ResponseEntity<GenericResponseDTO> eliminarDocumentoModeloConfiguracion(String json);

    ResponseEntity<GenericResponseDTO> crearDocumentoModeloSeccion(String json);

    ResponseEntity<GenericResponseDTO> crearDocumentoModeloSeccionMultiple(String json);

    ResponseEntity<GenericResponseDTO> consultarDocumentoModeloSeccion(String json);

    ResponseEntity<GenericResponseDTO> actualizarDocumentoModeloSeccion(String json);

    ResponseEntity<GenericResponseDTO> actualizarDocumentoModeloSeccionMultiple(String json);

    ResponseEntity<GenericResponseDTO> eliminarDocumentoModeloSeccion(String json);

    ResponseEntity<GenericResponseDTO> crearDocumentoSeccion(String json);

    ResponseEntity<GenericResponseDTO> consultarDocumentoSeccion(String json);

    ResponseEntity<GenericResponseDTO> actualizarDocumentoSeccion(String json);

    ResponseEntity<GenericResponseDTO> eliminarDocumentoSeccion(String json);

    ResponseEntity<GenericResponseDTO> generarDocumento(String json);

    ResponseEntity<GenericResponseDTO> consultarNumeroFolios(String json);

    ResponseEntity<GenericResponseDTO> consultarDocGenFirmaYNotifica(String json);

    ResponseEntity<GenericResponseDTO> consultarDocumentoAutomatico(String json) throws ParseException;
}
