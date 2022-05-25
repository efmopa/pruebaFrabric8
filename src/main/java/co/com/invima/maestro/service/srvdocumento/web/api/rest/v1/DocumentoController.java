package co.com.invima.maestro.service.srvdocumento.web.api.rest.v1;

import co.com.invima.canonico.modeloCanonico.dto.generic.GenericRequestDTO;
import co.com.invima.canonico.modeloCanonico.dto.generic.GenericResponseDTO;
import co.com.invima.maestro.service.srvdocumento.service.IDocumentoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/Documento")
@CrossOrigin({"*"})
public class DocumentoController implements IDocumentoController {

    private final IDocumentoService service;

    @Autowired
    public DocumentoController(IDocumentoService service) {
        this.service = service;

    }

    @Override
    @GetMapping("/idDocumento/{id}")
    @ApiOperation(value = "Consulta Por ID", notes = "Consulta Por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarPorID(@ApiParam(type = "integer", value = ""
            + "el parametro usuario debe ser un json con la siguiente estructura:" + "<br>" + "{ <br> request:"
            + " {<br>" + "     \"IdDocumento\": \"\",<br>" + "}<br>"

            + "      }<br>", example = "1", required = true) @PathVariable Integer id) {
        return service.consultarPorID(id);
    }

    @Override
    @PostMapping("/")
    @ApiOperation(value = "Crea un nuevo Documento", notes = " Crea  un nuevo  Documento")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> crear(@ApiParam(value = ""
            + "el parametro usuario debe ser un json con la siguiente estructura:" + "<br>" + "{ <br> request:"
            + " {<br>" + "     \"FechaCreacionDocumento\": \"\",<br>" + "     \"FechaDocumento\": \"\",<br>"
            + "     \"IdEcm\": \"\"<br>" + "     \"Tipo\": \"\"<br>" + "     \"NumeroFolios\": \"\"<br>"
            + "     \"IdExpediente\": \"\"<br>" + "     \"TipoDocumento\": \"\"<br>" + "     \"IdTramite\": \"\"<br>"
            + "     \"IdPruebaConcepto\": \"\"<br>" + "     \"FechaCreacion\": \"\"<br>"
            + "     \"UsuarioCrea\": \"\"<br>" + "     \"FechaModifica\": \"\"<br>"
            + "     \"UsuarioModifica\": \"\"<br>" + "}<br>"

            + "      }<br>", required = true) GenericRequestDTO genericRequestDTO) {
        return service.crear(genericRequestDTO);
    }

    @Override
    @GetMapping("/consulta")
    @ApiOperation(value = "Consulta  Documento", notes = "Consulta Documento")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumento() {
        return service.consultarDocumento();
    }

    @Override
    @PutMapping("/")
    @ApiOperation(value = "Actualiza Documento", notes = "Actualiza Documento")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> actualizar(@ApiParam(type = "integer", value = ""
            + "el parametro usuario debe ser un json con la siguiente estructura:" + "<br>" + "{ <br> request:"
            + " {<br>" + "     \"IdDocumento\": \"\",<br>" + "     \"FechaCreacionDocumento\": \"\",<br>"
            + "     \"FechaDocumento\": \"\",<br>" + "     \"IdEcm\": \"\"<br>" + "     \"Tipo\": \"\"<br>"
            + "     \"NumeroFolios\": \"\"<br>" + "     \"IdExpediente\": \"\"<br>" + "     \"TipoDocumento\": \"\"<br>"
            + "     \"IdTramite\": \"\"<br>" + "     \"IdPruebaConcepto\": \"\"<br>"
            + "     \"FechaCreacion\": \"\"<br>" + "     \"UsuarioCrea\": \"\"<br>" + "     \"FechaModifica\": \"\"<br>"
            + "     \"UsuarioModifica\": \"\"<br>" + "}<br>"

            + "      }<br>", example = "1", required = true) GenericRequestDTO genericRequestDTO) {
        return service.actualizar(genericRequestDTO);
    }

    @Override
    @DeleteMapping("/eliminarId/{id}")
    @ApiOperation(value = "Eliminado logico de Documento por id", notes = "Eliminado logico de Documento por id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> eliminadoLogicoPorId(@ApiParam(type = "integer", value = ""
            + "el parametro usuario debe ser un json con la siguiente estructura:" + "<br>" + "{ <br> request:"
            + " {<br>" + "     \"IdDocumento\": \"\",<br>" + "}<br>"

            + "      }<br>", example = "1", required = true) @PathVariable Integer id) {
        return service.eliminadoLogicoPorId(id);
    }

    @Override
    @PostMapping("/guardarDocumento")
    @ApiOperation(value = "guardar Documento", notes = "guardar   Documento")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> guardarDocumento(
            @ApiParam(type = "Json", value = "el parametro usuario debe ser un json con la siguiente estructura:" +

                    "  <br>  { \"Documento\": {<br>" + "        \"nombre\":\"Documento\",<br>"
                    + "        \"detalle\": \"prueba\",<br>" + "        \"numeroFolios\": 3,<br>"
                    + "        \"fechaCreacionDocumento\": \"2021-07-14 12:16:00.000\",<br>"
                    + "}<br>}", required = true) String genericRequestDTO) {
        return service.guardarDocumento(genericRequestDTO);
    }

    @Override
    @GetMapping("/consultarArticuloporidDocumento/{idDocumento}")
    @ApiOperation(value = " consultar Articulo", notes = "metodo para buscar   Articulo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarArticuloporidDocumento(
            @ApiParam(type = "se debe enviar el nombre del Documento para consultar", value = "el parametro usuario debe ser un json con la siguiente estructura:"
                    + "<br>" + "{ <br> request:" + " {<br>" + "     \"idDocumento\": \"\"<br>" + "}<br>"

                    + "      }<br>", example = "1", required = true) @PathVariable Integer idDocumento) {
        return service.consultarArticuloporidDocumento(idDocumento);
    }

    @Override
    @GetMapping("/consultarDocumentoTramite/{idTramite}")
    @ApiOperation(value = "Consulta los documentos por el id del tramite", notes = "Consulta los documentos por el id del tramite")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> documentosAsociadosTramite(
            @ApiParam(type = "integer", value = "Identificador del tramite", example = "3", required = true) @PathVariable("idTramite") Integer idTramite) {
        return service.documentosAsociadosTramite(idTramite);
    }

    @Override
    @GetMapping("/consultarTareaTramite")
    @ApiOperation(value = "Consulta Tarea  Tramite", notes = "Consulta los datos Tarea  Tramite")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarTareaTramite() {
        return service.consultarTareaTramite();
    }

    @Override
    @PutMapping("/actualizarTareaTramiteDocumentacion")
    @ApiOperation(value = "Se Actualiza  Tarea Tramite Documentacion", notes = "Se Actualiza  Tarea Tramite Documentacion")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> actualizarTareaTramiteDocumentacion(
            @ApiParam(type = "Json", value = "el parametro usuario debe ser un json con la siguiente estructura:" +

                    "   <br>{" + "        \"aprobacion\":\"True\",<br>" + "        \"idDocumento\": 1,<br>"
                    + "        \"aprobacion\": \"True\",<br>" + "        \"idDocumento\": 2,<br>" +

                    "}"

                    , required = true) String genericRequestDTO) {
        return service.actualizarTareaTramiteDocumentacion(genericRequestDTO);
    }

    @Override
    @GetMapping("/consultarDocumentoGenerar/{idEtapa}/{idRol}/{idConcepto}")
    @ApiOperation(value = "Consulta empresa por id", notes = "Consulta empresa por id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumentoGenerar(
            @ApiParam(type = "Integer" /*
										 * value = "los parametros de entrada son los siguentes:" +
										 * "    \"idEtapa\": 1;\n" + "    \"idRol\": 9;\n" + "    \"idConcepto\": 1;\n"
										 * + "<br>",
										 */, required = true) @PathVariable Integer idEtapa,
            @PathVariable Integer idRol, @PathVariable Integer idConcepto) {
        return service.consultarDocumentoGenerar(idEtapa, idRol, idConcepto);
    }

    @Override
    @GetMapping("/consultarDocumentacionOAC")
    @ApiOperation(value = "Consulta DocumentacionOAC", notes = "Consulta los datos DocumentacionOAC")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumentacionOAC() {
        return service.consultarDocumentacionOAC();
    }

    @Override
    @GetMapping("/consultarDocumentosAsociados/{idSolicitud}")
    @ApiOperation(value = "Consulta documento por solicitud", notes = "Consulta documento por solicitud")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumentosAsociados(
            @ApiParam(type = "Integer" /*
										 * value = "los parametros de entrada son los siguentes:" +
										 * "    \"idSolicitud\": 1;\n" + "<br>",
										 */, required = true) @PathVariable Integer idSolicitud) {
        return service.consultarDocumentosAsociados(idSolicitud);
    }

    @Override
    @PutMapping("/actualizarDocumentosAsociados/{idSolicitud}")
    @ApiOperation(value = "Se Actualiza  cumplimientos asociados de Tramite", notes = "Se Actualiza Documentos asociados del tramite")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> actualizarDocumentosAsociados(
            @ApiParam(type = "Json", value = "el parametro usuario debe ser un json con la siguiente estructura:"
                    + "<br>{<br>" + "\"DocumentoAsociado\": [{<br>" + "\"idDocumento\":1,<br>"
                    + "\"cumplimiento\":\"true\"<br>" + "},{  <br>" + "\"idDocumento\":2,<br>"
                    + "\"cumplimiento\":\"true\"<br>" + "}]<br>" + "}",

                    example = "1", required = true) @PathVariable Integer idSolicitud,
            String genericRequestDTO) {
        return service.actualizarDocumentosAsociados(idSolicitud, genericRequestDTO);
    }

    @Override
    @GetMapping("/descargarPlantillaCSV/{autorizacion}")
    @ApiOperation(value = "Descargar Plantilla CSV", notes = "Descargar la Plantilla CSV para Carga de Ingredientes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Se envio la Plantilla en Base64", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request. Existen cambios no Controlado en la Plantilla", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> descargarPlantillaCSV(@ApiParam(type = "Integer", required = true) @PathVariable Integer autorizacion) {
        return service.plantillaCsv(autorizacion);
    }

    @Override
    @PostMapping("/archivocsv/{autorizacion}")
    @ApiOperation(value = "Archivo", notes = "Archivo CSV a Convertir")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. se retorna el json", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Error en los registros", response = String.class),
            @ApiResponse(code = 418, message = "Error al validar el pais", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> archivocsv(@ApiParam(type = "MultipartFile", required = true) MultipartFile file,
                                                         @ApiParam(type = "Integer", required = true) @PathVariable Integer autorizacion) throws Exception {
        return service.convertirCsv(file.getInputStream(), autorizacion);
    }

    @Override
    @PostMapping("/archivoAutorizacionMuestraNacionalescsv")
    @ApiOperation(value = "Archivo", notes = "Archivo CSV a Convertir")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. se retorna el json", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Error en los registros", response = String.class),
            @ApiResponse(code = 418, message = "Error al validar el pais", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> archivocsvAutorizacionMuestrasNacionales(@ApiParam(type = "MultipartFile", required = true) MultipartFile file) throws Exception {
        return service.convertirCsvParaAutorizacionMuestrasNacionales(file.getInputStream());
    }

    @Override
    @GetMapping("/consultarDocumentoAsociadoLegalTecnico/{idTramite}/{idPersona}")
    @ApiOperation(value = "Se consulta los documentos asociados por tramite y id Persona", notes = "Se consulta los documentos asociados por tramite y id Persona")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumentoAsociadoLegalTecnico(
            @ApiParam(type = "Integer", required = true) @PathVariable Integer idTramite, @ApiParam(type = "Integer", required = true) @PathVariable Integer idPersona) {
        return service.consultarDocumentoAsociadoLegalTecnico(idTramite, idPersona);
    }


    @Override
    @GetMapping("/consultarDocumentoAsociadoCoordinador/{idTramite}")
    @ApiOperation(value = "Se consulta los documentos asociados al coordinador", notes = "Se consulta los documentos asociados al coordinador")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumentoAsociadoCoordinador(
            @ApiParam(type = "Integer", required = true) @PathVariable Integer idTramite) {
        return service.consultarDocumentoAsociadoCoordinador(idTramite);
    }

    @Override
    @PostMapping("/documentosOAC")
    @ApiOperation(value = "Se consultan documentos asociados", notes = "Se consultan documentos asociados")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> documentosOAC(
            @ApiParam(type = "String", value = "el parametro usuario debe ser un json con la siguiente estructura:" +

                    "   <br>{" + "        \"idTramite\":1,<br>" + "        \"idSolicitud\": 1,<br>"
                    + "        \"idExpediente\": 1,<br>" + "        \"idSala\": 2<br>" +

                    "}"

                    , required = true) String genericRequestDTO) {
        return service.documentosOAC(genericRequestDTO);
    }


    @Override
    @PostMapping("/guardaAprobacionDocumentoAsociado")
    @ApiOperation(value = "guardar Aprobación para el  Documento Asociado", notes = "guardar Aprobación para el  Documento Asociado")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> guardarAprobacionDocumento(
            @ApiParam(type = "String", value = "el parametro usuario debe ser un json con la siguiente estructura:" +

                    "<br> " +
                    "{ <br> \"AprobacionDocumentoRol\": { <br>" +
                    "\t \"idAprobacionDocumentoRol\" : null -> nuevo | id -> update , <br> " +
                    "\t \"idDocumento\" : 24472 , <br>" +
                    "\t \"observacion\" : \"observacion\", <br>" +
                    "\t \"aprobacion\"  : true or false , <br>" +
                    "\t \"idPersona\"   : 24 , <br>" +
                    "\t \"idRolPersona\": 8 , <br>" +
                    "\t \"activo\" : 1 Activo or 0 Inactivo , <br>" +
                    "\t \"usuarioCrea\" : \"usuario\" , <br>" +
                    "\t \"usuarioModifica\" : \"usuario para update \" <br>"
                    + "}<br>" +
                    "}", required = true) String genericRequestDTO) {
        return service.crearAprobacionDocumentoAsociado(genericRequestDTO);
    }


    @Override
    @GetMapping("/consultarDocumentosGenerados/{idTramite}")
    @ApiOperation(value = "Se consulta los documentos generados en el tramite", notes = "Se consulta los documentos generados en el tramite")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumentoGeneradosTramite(
            @ApiParam(type = "Integer", required = true) @PathVariable Integer idTramite) {
        return service.consultarDocumentosGenerados(idTramite);
    }

    @Override
    @GetMapping("/consultarInformacionDocumentosProducir/{idTramite}")
    @ApiOperation(value = "Se consulta informacion documento a producir", notes = "Se consulta informacion documento a producir")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarInformacionDocumentoProducirTramite(
            @ApiParam(type = "Integer", required = true) @PathVariable Integer idTramite) {
        return service.consultarInformacionDocumentoProducir(idTramite);
    }

    @Override
    @PostMapping("/devolverDocumentoProducir")
    @ApiOperation(value = "devolver Documento", notes = "devolver   Documento")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> devolverDocumentoProducir(
            @ApiParam(type = "String", value = "el parametro usuario debe ser un json con la siguiente estructura:" +

                    "  <br>  { \"devolver\":  {<br>"
                    + "        \"idTramite \": 153,<br>"
                    + "        \"idPersona\":\" example 24\",<br>"
                    + "        \"idTipoDevolucion\": \" example 1 \",<br>"
                    + "        \"idRolPersona \": example 3,<br>"
                    + "}<br>}", required = true) String genericRequestDTO) {
        return service.devolverDocumentoGenerado(genericRequestDTO);
    }

    @Override
    @PostMapping("/consultarFactura")
    @ApiOperation(value = "Devuelve la factura en Base 64", notes = "Devuelve la factura en Base 64")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request. No se puede entregar una respuesta", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarFactura(@ApiParam(value = "el JSON debe contar con la siguiente estructura \n" +
            "{\n" +
            "\"objAuditoria\":{\n" +
            "\"usuario\":\"jhsanchez\",\n" +
            "\"ip\":\"192.123.123.1\"\n" +
            "},\n" +
            "\"objOperacion\":{\n" +
            "\"idSolicitud\":34645\n" +
            "}\n" +
            "}", required = true, type = "String") String json) {
        return service.devolverFactura(json);
    }

    @Override
    @PostMapping("/generarMetadataSeSuite")
    @ApiOperation(value = "Genera la MetaData para SeSuite",
            notes = "Genera la MetaData para SeSuite")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> generarMetadataSeSuite(@ApiParam(
            value = "el JSON debe contar con la siguiente estructura " +
                    "{\n" +
                    "\"objAuditoria\":{\n" +
                    "   \"usuario\":\"jhsanchez\",\n" +
                    "   \"ip\":\"192.123.123.1\"\n" +
                    "},\n" +
                    "\"objOperacion\":{\n" +
                    "	\"idDocumento\":20782,\n" +
                    "	\"idSolicitud\":1,\n" +
                    "	\"idTramite\":1\n" +
                    "}\n" +
                    "}"
            , required = true,
            type = "String") String json) {

        return service.generarMetadataSeSuite(json);
    }

    @Override
    @GetMapping("/consultarDocumentosTramite/{idTramite}/{tipo}")
    @ApiOperation(value = "Se consulta los documentos del tramite para los tipos ('A' -> Asociado, 'G' -> Generado, 'S' -> Soporte, 'T'-> Todos)", notes = "Se consulta los documentos del tramite para los tipos ('A' -> Asociado, 'G' -> Generado, 'S' -> Soporte, 'T'-> Todos)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumentosTramite(
            @ApiParam(type = "Integer", required = true) @PathVariable Integer idTramite, @ApiParam(type = "String", required = true) @PathVariable String tipo) {
        return service.consultarDocumentos(idTramite, tipo);
    }

    @Override
    @PostMapping("/consultarDocumentosTramiteFiltro/")
    @ApiOperation(value = "devolver Documento", notes = "devolver   Documento")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumentosTramiteFiltro(
            @ApiParam(type = "String", value = "el parametro usuario debe ser un json con la siguiente estructura:" +

                    "  <br>  { \"idTramite\":  153,<br>"
                    + "        \"enviarEnNotificacion\": 1,<br>"
                    + "        \"tipo\":\" A\",<br>"
                    + "}", required = true) String genericRequestDTO) {
        return service.consultarDocumentosFiltro(genericRequestDTO);
    }


    @Override
    @PostMapping("/actualizarEnviarEnNotificacion")
    @ApiOperation(value = "Actualiza la columna enviar en notificacion en el documento", notes = "Actualiza la columna enviar en notificacion en el documento")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> actualizarEnviarEnNotificacion(@ApiParam(
            value = "el JSON debe contar con la siguiente estructura \n" +
                    "{\n" +
                    "   \"idTramite\":1,\n" +
                    "   \"documentos\":[\n" +
                    "      {\n" +
                    "         \"idDocumento\":1\n" +
                    "      },\n" +
                    "      {\n" +
                    "         \"idDocumento\":2\n" +
                    "      }\n" +
                    "   ]\n" +
                    "}"
            , required = true,
            type = "String") String json) {
        return service.actualizarEnviarEnNotificacion(json);
    }

    @Override
    @PostMapping("/crearDocumentoModelo")
    @ApiOperation(value = "Crea el documento modelo", notes = "Crea el documento modelo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se crea correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> crearDocumentoModelo(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"jhsanchez\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"tipoDocumentoModelo\":\"DOCX\",\r\n"
            + "\"codigoDocumentoModelo\":\"RES_APR\",\r\n"
            + "\"nombreDocumentoModelo\":\"RESOLUCION APROBACION\",\r\n"
            + "\"descripcionDocumentoModelo\":\"Descripcion Resolucion Aprobacion\",\r\n"
            + "\"documentoModelo\":1,\r\n"
            + "\"documentoModeloConfiguracion\":[\r\n"
            + "{\r\n"
            //+ "\"documentoModelo\":19,\r\n"
            + "\"codigoTipoProducto\":\"3\",\r\n"
            + "\"codigoGrupoProducto\":\"N/A\",\r\n"
            + "\"codigoSubGrupo\":\"0\",\r\n"
            + "\"codigoCategoria\":\"0\",\r\n"
            + "\"codigoSubCategoria\":\"0\",\r\n"
            + "\"codigoTipoTramite\":\"TT0\",\r\n"
            + "\"codigoSubTipoTramite\":\"0\",\r\n"
            + "\"codigoCategoriaAlimento\":\"0\",\r\n"
            + "\"codigoSubCategoriaAlimento\":\"0\",\r\n"
            + "\"codigoModalidad\":\"MOD0\",\r\n"
            + "\"documentoModelo\":1,\r\n"
            + "\"descripcion\":\"aaaa\"\r\n"
            + "}\r\n"
            + "]\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.crearDocumentoModelo(json);
    }

    @Override
    @PostMapping("/crearDocumentoModeloConfiguracion")
    @ApiOperation(value = "Crea el documento modelo configuracion", notes = "Crea el documento modelo configuracion")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se crea correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> crearDocumentoModeloConfiguracion(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"ccamilo\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"documentoModeloConfiguracion\":[\r\n"
            + "{\r\n"
            + "\"documentoModelo\":19,\r\n"
            + "\"codigoTipoProducto\":\"3\",\r\n"
            + "\"codigoGrupoProducto\":\"COSME\",\r\n"
            + "\"codigoCategoria\":\"15\",\r\n"
            + "\"codigoTipoTramite\":\"TT0\",\r\n"
            + "\"codigoModalidad\":\"MOD0\",\r\n"
            + "\"tipoDocumental\":1\r\n"
            + "}\r\n"
            + "]\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.guardarDocumentoConfiguracion(json);
    }

    @Override
    @PostMapping("/consultarDocumentoModelo")
    @ApiOperation(value = "Consulta el documento modelo", notes = "Consulta el documento modelo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se consulta correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumentoModelo(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"jhsanchez\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"idDocumentoModelo\":2\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.consultarDocumentoModelo(json);
    }

    @Override
    @PostMapping("/consultarDocumentoModeloConfiguracion")
    @ApiOperation(value = "Consulta el documento modelo", notes = "Consulta el documento modelo configuracion")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se consulta correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumentoModeloConfiguracion(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"ccamilo\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"idDocumentoModelo\":2\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.consultarDocumentoModeloConfiguracion(json);
    }

    @Override
    @PostMapping("/consultarDocumentoModeloTipoDocumental")
    @ApiOperation(value = "Consulta el tipo documental", notes = "Consulta el tipo documental")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se consulta correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumentoModeloTipoDocumental(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"ccamilo\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.consultarDocumentoModeloTipoDocumental(json);
    }

    @Override
    @PostMapping("/consultaDinamicaDocumentoModelo")
    @ApiOperation(value = "Consulta el documento modelo por campos dinamicos", notes = "Consulta el documento modelo por campos dinamicos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se consulta correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultaDinamicaDocumentoModelo(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"jhsanchez\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"idTipoProducto\":3,\r\n"
            + "\"idGrupoProducto\":38,\r\n"
            + "\"idSubGrupo\":null,\r\n"
            + "\"idCategoria\":7,\r\n"
            + "\"idSubCategoria\":null,\r\n"
            + "\"idTipoTramite\":692,\r\n"
            + "\"idSubTipoTramite\":null,\r\n"
            + "\"idCategoriaAlimento\":null,\r\n"
            + "\"idSubCategoriaAlimento\":null,\r\n"
            + "\"idModalidad\":null,\r\n"
            + "\"tipoDocumentoModelo\":\"DOCX\"\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.consultaDinamicaDocumentoModelo(json);
    }

    @Override
    @PutMapping("/actualizarDocumentoModelo")
    @ApiOperation(value = "Actualiza el documento modelo", notes = "Actualiza el documento modelo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se actualiza correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> actualizarDocumentoModelo(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"jhsanchez\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"idDocumentoModelo\":2,\r\n"
            + "\"tipoDocumentoModelo\":\"DOCX\",\r\n"
            + "\"codigoDocumentoModelo\":\"RES_APR\",\r\n"
            + "\"nombreDocumentoModelo\":\"RESOLUCION APROBACION\",\r\n"
            + "\"descripcionDocumentoModelo\":\"Descripcion Resolucion Aprobacion\",\r\n"
            + "\"documentoModelo\":1,\r\n" + "\"documentoModeloConfiguracion\":[\r\n"
            + "{\r\n"
            + "\"codigoTipoProducto\":\"3\",\r\n"
            + "\"codigoGrupoProducto\":\"N/A\",\r\n"
            + "\"codigoSubGrupo\":\"N/A\",\r\n"
            + "\"codigoCategoria\":\"15\",\r\n"
            + "\"codigoSubCategoria\":\"15\",\r\n"
            + "\"codigoTipoTramite\":\"TT0\",\r\n"
            + "\"codigoSubTipoTramite\":\"TT0\",\r\n"
            + "\"codigoCategoriaAlimento\":\"0\",\r\n"
            + "\"codigoSubCategoriaAlimento\":\"0\",\r\n"
            + "\"codigoModalidad\":\"MOD0\",\r\n"
            + "\"documentoModelo\":1,\r\n"
            + "\"descripcion\":\"aaaa\"\r\n"
            + "}\r\n"
            + "]\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.actualizarDocumentoModelo(json);
    }

    @Override
    @PostMapping("/eliminarDocumentoModelo")
    @ApiOperation(value = "Eliminado logico del documento modelo", notes = "Eliminado logico del documento modelo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se elimina correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> eliminarDocumentoModelo(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"jhsanchez\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"idDocumentoModelo\":2\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.eliminarDocumentoModelo(json);
    }

    @Override
    @PostMapping("/eliminarDocumentoModeloConfiguracion")
    @ApiOperation(value = "Eliminado logico del documento modelo", notes = "Eliminado logico del documento modelo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se elimina correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> eliminarDocumentoModeloConfiguracion(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"ccamilo\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"idDocumentoModelo\":2\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.eliminarDocumentoModeloConfiguracion(json);
    }

    @Override
    @PostMapping("/crearDocumentoModeloSeccion")
    @ApiOperation(value = "Creación del documento modelo por sección", notes = "Creación del documento modelo por sección")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se crea correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> crearDocumentoModeloSeccion(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"jhsanchez\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"idDocumentoModelo\":3,\r\n"
            + "\"idDocumentoSeccion\":2,\r\n"
            + "\"tipo\":null,\r\n"
            + "\"orden\":null\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.crearDocumentoModeloSeccion(json);
    }

    @Override
    @PostMapping("/crearDocumentoModeloSeccionMultiple")
    @ApiOperation(value = "Creación de multiples documentos modelo por sección", notes = "Creación de multiples documentos modelo por sección")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se crea correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> crearDocumentoModeloSeccionMultiple(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\": {\r\n"
            + "\"ip\": \"192.123.123.1\",\r\n"
            + "\"usuario\": \"jhsanchez\"\r\n"
            + "},\r\n"
            + "\"objOperacion\": [{\r\n"
            + "\"idDocumentoModelo\": 2,\r\n"
            + "\"idDocumentoSeccion\": 2,\r\n"
            + "\"orden\": null,\r\n"
            + "\"tipo\": null\r\n"
            + "}, {\r\n"
            + "\"idDocumentoModelo\": 2,\r\n"
            + "\"idDocumentoSeccion\": 3,\r\n"
            + "\"orden\": null,\r\n"
            + "\"tipo\": null\r\n"
            + "}\r\n"
            + "]\r\n"
            + "}\r\n"
            + "", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.crearDocumentoModeloSeccionMultiple(json);
    }

    @Override
    @PostMapping("/consultarDocumentoModeloSeccion")
    @ApiOperation(value = "Consulta del documento modelo por sección", notes = "Consulta del documento modelo por sección")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se consulta correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumentoModeloSeccion(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"jhsanchez\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"idDocumentoModelo\":1,\r\n"
            + "\"idDocumentoSeccion\":1,\r\n"
            + "\"idDocumentoModeloSeccion\":2\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.consultarDocumentoModeloSeccion(json);
    }

    @Override
    @PutMapping("/actualizarDocumentoModeloSeccion")
    @ApiOperation(value = "Actualiza el documento modelo por sección", notes = "Actualiza el documento modelo por sección")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se actualiza correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> actualizarDocumentoModeloSeccion(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"jhsanchez\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"idDocumentoModeloSeccion\":1,\r\n"
            + "\"idDocumentoModelo\":3,\r\n"
            + "\"idDocumentoSeccion\":2,\r\n"
            + "\"tipo\":null,\r\n"
            + "\"orden\":null\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.actualizarDocumentoModeloSeccion(json);
    }

    @Override
    @PutMapping("/actualizarDocumentoModeloSeccionMultiple")
    @ApiOperation(value = "Actualiza multiples documentos modelo por sección", notes = "Actualiza multiples documentos modelo por sección")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se actualiza correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> actualizarDocumentoModeloSeccionMultiple(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\": {\r\n"
            + "\"usuario\": \"jhsanchez\",\r\n"
            + "\"ip\": \"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\": [{\r\n"
            + "\"idDocumentoModeloSeccion\":1,\r\n"
            + "\"idDocumentoModelo\": 2,\r\n"
            + "\"idDocumentoSeccion\": 2,\r\n"
            + "\"tipo\": null,\r\n"
            + "\"orden\": null\r\n"
            + "},\r\n"
            + "{\r\n"
            + "\"idDocumentoModeloSeccion\":2,\r\n"
            + "\"idDocumentoModelo\": 2,\r\n"
            + "\"idDocumentoSeccion\": 3,\r\n"
            + "\"tipo\": null,\r\n"
            + "\"orden\": null\r\n"
            + "}\r\n"
            + "]\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.actualizarDocumentoModeloSeccionMultiple(json);
    }

    @Override
    @PostMapping("/eliminarDocumentoModeloSeccion")
    @ApiOperation(value = "Eliminado logico del documento modelo por sección", notes = "Eliminado logico del documento modelo por sección")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se elimina correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> eliminarDocumentoModeloSeccion(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"jhsanchez\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"idDocumentoModeloSeccion\":2\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.eliminarDocumentoModeloSeccion(json);
    }

    @Override
    @PostMapping("/crearDocumentoSeccion")
    @ApiOperation(value = "Creación del documento por sección", notes = "Creación del documento por sección")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se crea correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> crearDocumentoSeccion(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"jhsanchez\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"tipoDocumentoSeccion\":\"0-ENCABEZADO\",\r\n"
            + "\"codigoDocumentoSeccion\":\"ENCA000\",\r\n"
            + "\"descripcionDocumentoSeccion\":\"Encabezado para Documentos Estandar\",\r\n"
            + "\"documentoSeccion\":\"ClJlcMO6YmxpY2EgZGUgQ29sb21iaWEKTWluaXN0ZXJpbyBkZSBTYWx1ZCB5IFByb3RlY2Npw7NuIFNvY2lhbApJbnN0aXR1dG8gTmFjaW9uYWwgZGUgVmlnaWxhbmNpYSBkZSBNZWRpY2FtZW50b3MgeSBBbGltZW50b3Mg4oCTIElOVklNQQpSRVNPTFVDScOTTiBOby4gICAgREUKUG9yIGxhIGN1YWwgc2UgY29uY2VkZSB1biBSZWdpc3RybyBTYW5pdGFyaW8K\",\r\n"
            + "\"tipoIntegracion\":\"0-NINGUNA\",\r\n"
            + "\"integracion\":\"\"\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.crearDocumentoSeccion(json);
    }

    @Override
    @PostMapping("/consultarDocumentoSeccion")
    @ApiOperation(value = "Consulta del documento por sección", notes = "Consulta del documento por sección")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se consulta correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumentoSeccion(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "    \"objAuditoria\": {\r\n"
            + "        \"usuario\": \"jhsanchez\",\r\n"
            + "        \"ip\": \"192.123.123.1\"\r\n"
            + "    },\r\n"
            + "    \"objOperacion\": {\r\n"
            + "        \"idDocumentoSeccion\": 2\r\n"
            + "    }\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.consultarDocumentoSeccion(json);
    }

    @Override
    @PutMapping("/actualizarDocumentoSeccion")
    @ApiOperation(value = "Actualiza el documento por sección", notes = "Actualiza el documento por sección")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se actualiza correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> actualizarDocumentoSeccion(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"jhsanchez\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"idDocumentoSeccion\": 2,\r\n"
            + "\"tipoDocumentoSeccion\":\"0-ENCABEZADO\",\r\n"
            + "\"codigoDocumentoSeccion\":\"ENCA000\",\r\n"
            + "\"descripcionDocumentoSeccion\":\"Encabezado para Documentos Estandar\",\r\n"
            + "\"documentoSeccion\":\"ClJlcMO6YmxpY2EgZGUgQ29sb21iaWEKTWluaXN0ZXJpbyBkZSBTYWx1ZCB5IFByb3RlY2Npw7NuIFNvY2lhbApJbnN0aXR1dG8gTmFjaW9uYWwgZGUgVmlnaWxhbmNpYSBkZSBNZWRpY2FtZW50b3MgeSBBbGltZW50b3Mg4oCTIElOVklNQQpSRVNPTFVDScOTTiBOby4gICAgREUKUG9yIGxhIGN1YWwgc2UgY29uY2VkZSB1biBSZWdpc3RybyBTYW5pdGFyaW8K\",\r\n"
            + "\"tipoIntegracion\":\"0-NINGUNA\",\r\n"
            + "\"integracion\":\"\"\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.actualizarDocumentoSeccion(json);
    }

    @Override
    @PostMapping("/eliminarDocumentoSeccion")
    @ApiOperation(value = "Eliminar el documento por sección", notes = "Eliminar el documento por sección")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se elimina correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> eliminarDocumentoSeccion(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "    \"objAuditoria\": {\r\n"
            + "        \"usuario\": \"jhsanchez\",\r\n"
            + "        \"ip\": \"192.123.123.1\"\r\n"
            + "    },\r\n"
            + "    \"objOperacion\": {\r\n"
            + "        \"idDocumentoSeccion\": 1\r\n"
            + "    }\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) {
        return service.eliminarDocumentoSeccion(json);
    }

    @Override
    @PostMapping("/generarDocumento")
    @ApiOperation(value = "Devuelve la Documento DOCX en Base 64", notes = "Devuelve la Documento DOCX en Base 64")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request. No se puede entregar una respuesta", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> generarDocumento(@ApiParam(value = "el JSON debe contar con la siguiente estructura \n" +
            "{\n" +
            "\"Auditoria\": {\n" +
            "   \"IP\":\"192.168.1.1\",\n" +
            "   \"Usuario\": \"jhsanchez@soaint.com\"\n" +
            "   },\n" +
            "\"objOperacion\": {\n" +
            "   \"idTipoProducto\": \"\",\n" +
            "   \"idGrupoProducto\": \"\",\n" +
            "   \"idSubGrupo\": \"\",\n" +
            "   \"idCategoria\": \"\",\n" +
            "   \"idSubCategoria\": \"\",\n" +
            "   \"idTipoTramite\": \"\",\n" +
            "   \"idSubTipoTramite\": \"\",\n" +
            "   \"idCategoriaAlimento\": \"\",\n" +
            "   \"idSubCategoriaAlimento\":  \"\",\n" +
            "   \"idModalidad\": \"\",\n" +
            "   \"tipoDocumentoModelo\": \"\",\n" +
            "   \"idTramite\": \"\",\n" +
            "   \"idSolicitud\": \"\",\n" +
            "   \"numeroSolicitud\": \"\",\n" +
            "   \"idDocumentoModelo\": \"\",\n" +
            "   \"idTipoDocumental\": \"\",\n" +
            "   \"codigoDocumentoModelo\": \"\"\n" +
            "   }\n" +
            "}\n", required = true, type = "String") String json) {
        return service.generarDocumento(json);
    }

    @Override
    @PostMapping("/consultarNumeroFolios")
    @ApiOperation(value = "Consulta número de folios de los documentos asociados a una solicitud",
            notes = "Consulta número de folios de los documentos asociados a una solicitud")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarNumeroFolios(@ApiParam(
            value = "el JSON debe contar con la siguiente estructura " +
                    "{\n" +
                    "\"objAuditoria\":{\n" +
                    "   \"usuario\":\"jhsanchez\",\n" +
                    "   \"ip\":\"192.123.123.1\"\n" +
                    "},\n" +
                    "\"objOperacion\":{\n" +
                    "	\"idEmpresa\":20782,\n" +
                    "	\"idSolicitud\":1,\n" +
                    "	\"idPantalla\":1\n" +
                    "	\"apoderado\":1\n" +
                    "	\"directorTecnico\":1\n" +
                    "	\"idTipoRolEmpresa\":1\n" +
                    "	\"certificado\":1\n" +
                    "	\"idTipoDocumental\":1\n" +
                    "}\n" +
                    "}"
            , required = true,
            type = "String") String json) {

        return service.consultarNumeroFolios(json);
    }

    @Override
    @PostMapping("/consultarDocGenFirmaYNotifica")
    @ApiOperation(value = "Consulta los documentos generados asociados al tramite, los convierte a pdf , firma y envía a RPA",
            notes = "Consulta los documentos generados asociados al tramite, los convierte a pdf , firma y envía a RPA")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocGenFirmaYNotifica(@ApiParam(
            value = "el JSON debe contar con la siguiente estructura " +
                    "{\n" +
                    "\"idTramite\":10278,\n" +
                    "\"usuarioRed\":\"naguilar@soaint.com\"\n" +
                    "}"
            , required = true,
            type = "String") String json) {
        return service.consultarDocGenFirmaYNotifica(json);
    }

    @Override
    @PostMapping("/consultarDocumentoAutomatico")
    @ApiOperation(value = "Consulta el documento modelo", notes = "Consulta el documento modelo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se consulta correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarDocumentoAutomatico(@ApiParam(value = "El JSON debe contar con la siguiente estructura \n"
            + "{\r\n"
            + "\"objAuditoria\":{\r\n"
            + "\"usuario\":\"jhsanchez\",\r\n"
            + "\"ip\":\"192.123.123.1\"\r\n"
            + "},\r\n"
            + "\"objOperacion\":{\r\n"
            + "\"idDocumentoModelo\":2\r\n"
            + "}\r\n"
            + "}", required = true, type = "Json") @RequestBody(required = true) String json) throws ParseException {
        return service.consultarDocumentoAutomatico(json);
    }
}
