package co.com.invima.maestro.service.srvdocumento.web.api.rest.v1;


import co.com.invima.canonico.modeloCanonico.dto.generic.GenericRequestDTO;
import co.com.invima.canonico.modeloCanonico.dto.generic.GenericResponseDTO;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface IDocumentoController {

    ResponseEntity<GenericResponseDTO> consultarPorID(@PathVariable Integer id);

    ResponseEntity<GenericResponseDTO> crear(@RequestBody GenericRequestDTO genericRequestDTO);

    ResponseEntity<GenericResponseDTO> actualizar(@RequestBody GenericRequestDTO genericRequestDTO);

    ResponseEntity<GenericResponseDTO> consultarDocumento();

    ResponseEntity<GenericResponseDTO> eliminadoLogicoPorId(@PathVariable Integer id);

    ResponseEntity<GenericResponseDTO> documentosAsociadosTramite(@PathVariable Integer idTramite);

    ResponseEntity<GenericResponseDTO> guardarDocumento(@RequestBody String genericRequestDTO);

    ResponseEntity<GenericResponseDTO> consultarArticuloporidDocumento(Integer idDocumento);

    ResponseEntity<GenericResponseDTO> consultarTareaTramite();

    ResponseEntity<GenericResponseDTO> actualizarTareaTramiteDocumentacion(@RequestBody String genericRequestDTO);

    ResponseEntity<GenericResponseDTO> consultarDocumentoGenerar(@PathVariable Integer idEtapa, @PathVariable Integer idRol, @PathVariable Integer idConcepto);

    ResponseEntity<GenericResponseDTO> consultarDocumentacionOAC();

    ResponseEntity<GenericResponseDTO> consultarDocumentosAsociados(@PathVariable Integer idSolicitud);

    ResponseEntity<GenericResponseDTO> actualizarDocumentosAsociados(@PathVariable Integer idSolicitud, @RequestBody String genericRequestDTO);

    ResponseEntity<GenericResponseDTO> descargarPlantillaCSV(@PathVariable Integer autorizacion);

    ResponseEntity<GenericResponseDTO> archivocsv(@RequestParam("file") MultipartFile file, @PathVariable Integer autorizacion) throws Exception;

    ResponseEntity<GenericResponseDTO> archivocsvAutorizacionMuestrasNacionales(@RequestParam("file") MultipartFile file) throws Exception;

    ResponseEntity<GenericResponseDTO> consultarDocumentoAsociadoLegalTecnico(@PathVariable Integer idTramite, @PathVariable Integer idPersona);

    ResponseEntity<GenericResponseDTO> consultarDocumentoAsociadoCoordinador(@PathVariable Integer idTramite);

    ResponseEntity<GenericResponseDTO> documentosOAC(@RequestBody String genericRequestDTO);

    ResponseEntity<GenericResponseDTO> guardarAprobacionDocumento(@RequestBody String genericRequestDTO);

    ResponseEntity<GenericResponseDTO> consultarDocumentoGeneradosTramite(@PathVariable Integer idTramite);

    ResponseEntity<GenericResponseDTO> consultarInformacionDocumentoProducirTramite(@PathVariable Integer idTramite);

    ResponseEntity<GenericResponseDTO> devolverDocumentoProducir(@RequestBody String genericRequestDTO);

    ResponseEntity<GenericResponseDTO> consultarDocumentosTramiteFiltro(@RequestBody String genericRequestDTO);

    //ResponseEntity<GenericResponseDTO> consultarFactura(@PathVariable Integer idSolicitud);

    ResponseEntity<GenericResponseDTO> consultarFactura(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> generarMetadataSeSuite(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> consultarDocumentosTramite(@PathVariable Integer idTramite, @PathVariable String tipo);

    ResponseEntity<GenericResponseDTO> actualizarEnviarEnNotificacion(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> crearDocumentoModelo(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> crearDocumentoModeloConfiguracion(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> consultarDocumentoModelo(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> consultarDocumentoModeloTipoDocumental(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> consultaDinamicaDocumentoModelo(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> actualizarDocumentoModelo(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> consultarDocumentoModeloConfiguracion(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> eliminarDocumentoModelo(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> eliminarDocumentoModeloConfiguracion(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> crearDocumentoModeloSeccion(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> crearDocumentoModeloSeccionMultiple(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> consultarDocumentoModeloSeccion(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> actualizarDocumentoModeloSeccion(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> actualizarDocumentoModeloSeccionMultiple(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> eliminarDocumentoModeloSeccion(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> crearDocumentoSeccion(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> consultarDocumentoSeccion(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> actualizarDocumentoSeccion(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> eliminarDocumentoSeccion(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> generarDocumento(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> consultarNumeroFolios(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> consultarDocGenFirmaYNotifica(@RequestBody String json);

    ResponseEntity<GenericResponseDTO> consultarDocumentoAutomatico(@RequestBody String json) throws ParseException;
}
