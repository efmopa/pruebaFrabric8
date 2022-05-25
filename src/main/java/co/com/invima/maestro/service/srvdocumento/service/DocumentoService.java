package co.com.invima.maestro.service.srvdocumento.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.AltChunkType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import co.com.invima.canonico.modeloCanonico.dto.generic.GenericRequestDTO;
import co.com.invima.canonico.modeloCanonico.dto.generic.GenericResponseDTO;
import co.com.invima.maestro.modeloTransversal.dto.documento.DocumentoDTO;
import co.com.invima.maestro.modeloTransversal.entities.documento.DocumentoDAO;
import co.com.invima.maestro.service.srvdocumento.commons.Utils;
import co.com.invima.maestro.service.srvdocumento.commons.converter.DocumentoConverter;
import co.com.invima.maestro.service.srvdocumento.repository.IDocumentoRepository;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentoService implements IDocumentoService {

    private final IDocumentoRepository documentoRepository;

    private final ModelMapper modelMapper;

    private final DocumentoConverter documentoConverter;


    @Value("${generacion_documento.path}")
    private String urlDocuments;

    public static String TYPE = "text/csv";
    static String[] HEADERs = {"Id", "nombreGenerico", "nombreQuimico", "concentracion", "tipoIngrediente", "paisActivo"};
    static String[] HEADERsAutorizacion = {"Id", "nombreGenerico", "concentracion"};
    private String driver = "spring.datasource.driver-class-name";
    private String username = "spring.datasource.username";
    private String password = "spring.datasource.password";
    private String url = "spring.datasource.url";
    private String schema = "spring.jpa.properties.hibernate.default_schema";
    private String jsonout = "json_OUT";
    private String jsonin = "json_IN";

    @Autowired
    public DocumentoService(DocumentoConverter documentoConverter, ModelMapper modelMapper, IDocumentoRepository iDocumentoRepository) {

        this.documentoConverter = documentoConverter;
        this.modelMapper = modelMapper;
        this.documentoRepository = iDocumentoRepository;
    }

    @Autowired
    public Environment env;

    @Override
    public ResponseEntity<GenericResponseDTO> consultarPorID(Integer id) {

        try {
            DocumentoDAO documentoDAO = documentoRepository.consultarPorID(id);
            DocumentoDTO documentoDTO = documentoConverter.documentoDAOtoDTO(documentoDAO, modelMapper);

            return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta por ID   documento : " + id)
                    .objectResponse(documentoDTO).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error consultando  por ID  documento:  " + e.getMessage())
                    .objectResponse(null).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<GenericResponseDTO> crear(GenericRequestDTO genericRequestDTO) {
        try {
            DocumentoDTO documentoDTO = new DocumentoDTO();
            modelMapper.map(genericRequestDTO.getRequest(), documentoDTO);
            DocumentoDAO documentoDAO = documentoConverter.documentoDTOtoDAO(documentoDTO, modelMapper);
            documentoDAO.setActivo(true);
            documentoRepository.save(documentoDAO);

            return new ResponseEntity<>(GenericResponseDTO.builder().message("Se crea Documento")
                    .objectResponse(true).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error creando  Documento:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumento() {
        try {
            List<DocumentoDTO> documentoDTOS = new ArrayList<>();
            List<DocumentoDAO> list = documentoRepository.findAll();
            for (DocumentoDAO tipo : list) {

                documentoDTOS.add(documentoConverter.documentoDAOtoDTO(tipo, modelMapper));
            }

            return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consultan Documento")
                    .objectResponse(documentoDTOS).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error consultando Documento\":  " + e.getMessage())
                    .objectResponse(null).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> actualizar(GenericRequestDTO genericRequestDTO) {
        try {
            DocumentoDTO documentoDTO = new DocumentoDTO();

            modelMapper.map(genericRequestDTO.getRequest(), documentoDTO);
            DocumentoDAO documentoDAO = documentoConverter.documentoDTOtoDAO(documentoDTO, modelMapper);
            documentoDAO.setActivo(true);
            documentoRepository.save(documentoDAO);

            return new ResponseEntity<>(GenericResponseDTO.builder().message("Se actualiza  Documento")
                    .objectResponse(true).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error actualizando  Documento:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<GenericResponseDTO> eliminadoLogicoPorId(Integer id) {


        Optional<DocumentoDAO> documentoDAO = documentoRepository.findById(id);

        if (documentoDAO.isEmpty())
            return new ResponseEntity<>(GenericResponseDTO.builder()
                    .message("El Documento  a eliminar no existe")
                    .objectResponse(false)
                    .statusCode(HttpStatus.CONFLICT.value())
                    .build(), HttpStatus.OK);

        documentoDAO.get().setActivo(false);
        documentoRepository.save(documentoDAO.get());

        return new ResponseEntity<>(GenericResponseDTO.builder()
                .message(" Documento deleted")
                .objectResponse(true)
                .statusCode(HttpStatus.OK.value())
                .build(), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<GenericResponseDTO> documentosAsociadosTramite(Integer idTramite) {
        try {

            List<DocumentoDAO> documentos = documentoRepository.consultarDocumentoTramite(idTramite);
            List<DocumentoDTO> documentosDTO = new ArrayList<>();

            if (documentos != null && documentos.size() > 0) {
                documentos.parallelStream().forEach(d -> {

                    try {
                        DocumentoDTO documentoDTO = documentoConverter.documentoDAOtoDTO(d, modelMapper);
                        documentosDTO.add(documentoDTO);
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }

                });

                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consultan los doscumetnos asociados al tramite")
                        .objectResponse(documentosDTO).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            }
            return new ResponseEntity<>(GenericResponseDTO.builder().message("No se encontraron documentos asociados al tramite: " + idTramite)
                    .objectResponse(documentosDTO).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);


        } catch (Exception e) {
            log.error("Error consultando los documentos asociados al tramite: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error consultando los documentos asociados al tramite: " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<GenericResponseDTO> guardarDocumento(String genericRequestDTO) {
        try {

            String respuestaTramite = documentoRepository.guardarDocumento(genericRequestDTO);
            org.json.simple.parser.JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(respuestaTramite);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) json.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se guarda el Documento ").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error actualizando el Documento :  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> consultarArticuloporidDocumento(Integer idDocumento) {

        String procedimiento = "dbo.USP_Documento_S(?,?)";
        try {
            Utils.cargarDriver(env.getProperty(driver));
            String respuestaTramite;
            try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                    env.getProperty(url), env.getProperty(schema));
                 CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                String datoEntrada = "idDocumento_IN";
                cStmt.setInt(datoEntrada, idDocumento);
                cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);


                cStmt.execute();

                respuestaTramite = cStmt.getString(jsonout);
            }

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(respuestaTramite);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {

                if (json.get("mensaje").toString().length() > 1) {
                    return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta la informacion de documento: ")
                            .objectResponse(json.get("mensaje"))
                            .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(GenericResponseDTO.builder().message("la empresa no tiene informacion de documento").objectResponse(json.get("mensaje"))
                            .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
                }
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error conusltando documento:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<GenericResponseDTO> consultarTareaTramite() {
        JSONParser parse = new JSONParser();

        try {

            String procedimiento = "dbo.USP_GestionarTareaTramiteDocumentacion_S(?)";
            Utils.cargarDriver(env.getProperty(driver));
            String respuesta;
            try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                    env.getProperty(url), env.getProperty(schema));
                 CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);
                cStmt.execute();
                respuesta = cStmt.getString(jsonout);
            }

            JSONObject json = (JSONObject) parse.parse(respuesta);
            JSONArray mensaje = (JSONArray) json.get("mensaje");


            return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta Tarea Tramite")
                    .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error  Director  Técnico:  " + e.getMessage())

                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<GenericResponseDTO> actualizarTareaTramiteDocumentacion(String genericRequestDTO) {
        try {

            String actualizarTareaTramiteDocumentacion = documentoRepository.actualizarTareaTramiteDocumentacion(genericRequestDTO);
            org.json.simple.parser.JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(actualizarTareaTramiteDocumentacion);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) json.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se actualiza la Tarea Tramite Documentacion").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error actualizando la Tarea Tramite Documentacion :  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumentoGenerar(Integer idEtapa, Integer idRol, Integer idConcepto) {
        JSONParser parse = new JSONParser();

        try {

            String nacional = documentoRepository.consultarDocumentoGenerar(idEtapa, idRol, idConcepto);
            JSONObject json = (JSONObject) parse.parse(nacional);

            return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta por sede y rol")
                    .objectResponse(json).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error  consultando  por sede y rol:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumentacionOAC() {
        JSONParser parse = new JSONParser();

        try {

            String procedimiento = "dbo.USP_DocumentacionOAC_S(?)";
            Utils.cargarDriver(env.getProperty(driver));
            String respuesta;
            try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                    env.getProperty(url), env.getProperty(schema));
                 CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);
                cStmt.execute();
                respuesta = cStmt.getString(jsonout);
            }

            JSONObject json = (JSONObject) parse.parse(respuesta);
            JSONArray mensaje = (JSONArray) json.get("mensaje");


            return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta Director  Técnico")
                    .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error  Director  Técnico:  " + e.getMessage())

                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumentosAsociados(Integer idSolicitud) {
        JSONParser parse = new JSONParser();

        try {

            String procedimiento = "dbo.USP_DocumentoAsociado_S(?, ?)";
            Utils.cargarDriver(env.getProperty(driver));
            String respuesta;
            try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                    env.getProperty(url), env.getProperty(schema));
                 CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                String datoEntrada = "idSolicitud_IN";
                cStmt.setInt(datoEntrada, idSolicitud);
                cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);
                cStmt.execute();
                respuesta = cStmt.getString(jsonout);
            }

            JSONObject json = (JSONObject) parse.parse(respuesta);
            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) json.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta documentos por solicitud").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {

                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);

            }


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error  consultando  por concepto y rol:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> actualizarDocumentosAsociados(Integer idSolicitud, String genericRequestDTO) {
        try {

            String actualizarDocumentosAsociados = documentoRepository.actualizarDocumentoExpediente(idSolicitud, genericRequestDTO);
            org.json.simple.parser.JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(actualizarDocumentosAsociados);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");
            //correoVerificacion(correo);
            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) json.get("mensaje");

                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se actualiza los documentos asociados").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error actualizando los documentos asociados :  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * @param
     * @return ResponseEntity<GenericResponseDTO>
     * servicio que recibe
     * @author jhsanchez
     */
    @Override
    public ResponseEntity<GenericResponseDTO> plantillaCsv(Integer autorizacion) {
        try {
            String plantillaCSV = null;
            if (autorizacion == 1) {
                plantillaCSV = Utils.filetoBase64(env.getProperty("pathPlantilla"));
            } else if (autorizacion == 2) {
                plantillaCSV = Utils.filetoBase64(env.getProperty("pathPlantillaMuestrasNacionales"));
            }

            return new ResponseEntity<>(GenericResponseDTO.builder().message("Se envio la Plantilla en Base64").objectResponse(plantillaCSV)
                    .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);


        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(GenericResponseDTO.builder().message("error")
                    .objectResponse(e.getMessage()).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param is
     * @return ResponseEntity<GenericResponseDTO>
     * servicio que recibe
     * @author geortiz
     */
    @Override
    public ResponseEntity<GenericResponseDTO> convertirCsv(InputStream is, Integer autorizacion) {
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            JSONArray listaExcel = new JSONArray();

            if (autorizacion == 1) {
                CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader(HEADERs).withIgnoreHeaderCase().withTrim());
                Iterable<CSVRecord> csvRecords = csvParser.getRecords();

                for (CSVRecord csvRecord : csvRecords) {
                    JSONObject registro = new JSONObject();
                    try {

                        registro.put("id", csvRecord.get("Id") != null ? csvRecord.get("Id").replace("\"", "") : "");
                        registro.put("nombreGenerico", csvRecord.get("nombreGenerico") != null ? csvRecord.get("nombreGenerico").replace("\"", "") : "");
                        registro.put("nombreQuimico", csvRecord.get("nombreQuimico") != null ? csvRecord.get("nombreQuimico").replace("\"", "") : "");
                        registro.put("concentracion", csvRecord.get("concentracion") != null ? csvRecord.get("concentracion").replace("\"", "") : "");
                        registro.put("tipoIngrediente", csvRecord.get("tipoIngrediente") != null ? csvRecord.get("tipoIngrediente").replace("\"", "") : "");
                        registro.put("paisActivo", csvRecord.get("paisActivo") != null ? csvRecord.get("paisActivo").replace("\"", "") : "");

                        listaExcel.add(registro);
                    } catch (Exception e) {
                        //errores.add("error en el registro con id #" + csvRecord.get("id"));
                        return new ResponseEntity<>(GenericResponseDTO.builder().message("error al convertir a json")
                                .objectResponse("error en el registro con id #" + csvRecord.get("id")).statusCode(HttpStatus.I_AM_A_TEAPOT.value()).build(),
                                HttpStatus.I_AM_A_TEAPOT);
                    }
                }


            } else if (autorizacion == 2) {
                CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader(HEADERsAutorizacion).withIgnoreHeaderCase().withTrim());
                Iterable<CSVRecord> csvRecordsAutorizacion = csvParser.getRecords();

                for (CSVRecord csvRecord : csvRecordsAutorizacion) {
                    JSONObject registro = new JSONObject();
                    try {

                        registro.put("id", csvRecord.get("Id") != null ? csvRecord.get("Id").replace("\"", "") : "");
                        registro.put("nombreGenerico", csvRecord.get("nombreGenerico") != null ? csvRecord.get("nombreGenerico").replace("\"", "") : "");
                        registro.put("concentracion", csvRecord.get("concentracion") != null ? csvRecord.get("concentracion").replace("\"", "") : "");

                        listaExcel.add(registro);
                    } catch (Exception e) {
                        //errores.add("error en el registro con id #" + csvRecord.get("id"));
                        return new ResponseEntity<>(GenericResponseDTO.builder().message("error al convertir a json")
                                .objectResponse("error en el registro con id #" + csvRecord.get("id")).statusCode(HttpStatus.I_AM_A_TEAPOT.value()).build(),
                                HttpStatus.I_AM_A_TEAPOT);
                    }
                }

            }

            List<String> errores = new ArrayList<>();
            JSONParser jsonParser = new JSONParser();


            //validaciones de campos

            //consulta paises

            RestTemplate restTemplate = new RestTemplate();

            //--------------------------------------------------
            if (autorizacion == 1) {
                for (Object registro : listaExcel) {
                    JSONObject jsonRegistro = (JSONObject) jsonParser.parse(registro.toString());
                    String ingrediente = jsonRegistro.get("tipoIngrediente").toString();
                    if (!"Activo".equals(ingrediente)) {
                        if (!"Secundario".equals(ingrediente)) {
                            errores.add("error en el campo tipo ingrediente en el registro con id #" + jsonRegistro.get("id"));
                        }
                    }

                    if (!errores.isEmpty()) {
                        return new ResponseEntity<>(GenericResponseDTO.builder().message("error en los registros")
                                .objectResponse(errores).statusCode(HttpStatus.I_AM_A_TEAPOT.value()).build(),
                                HttpStatus.I_AM_A_TEAPOT);
                    }


                    try {

                        ResponseEntity<GenericResponseDTO> responseActionManagement = restTemplate
                                .getForEntity(env.getProperty("maestra_pais_host") + jsonRegistro.get("paisActivo"),
                                        GenericResponseDTO.class);
                        if (responseActionManagement.getBody().getObjectResponse().toString().length() <= 2) {
                            errores.add("error en el campo pais en el registro con id #" + jsonRegistro.get("id"));
                        }

                    } catch (Exception e) {
                        log.error(e.getMessage());
                        e.printStackTrace();
                        return new ResponseEntity<>(GenericResponseDTO.builder().message("error al validar el pais")
                                .objectResponse(e.getMessage()).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                                HttpStatus.BAD_REQUEST);
                    }
                }

            }
            if (errores.isEmpty()) {
                return new ResponseEntity<>(GenericResponseDTO.builder().message("se retorna el json")
                        .objectResponse(listaExcel).statusCode(HttpStatus.OK.value()).build(),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(GenericResponseDTO.builder().message("error en los registros")
                        .objectResponse(errores).statusCode(HttpStatus.I_AM_A_TEAPOT.value()).build(),
                        HttpStatus.I_AM_A_TEAPOT);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(GenericResponseDTO.builder().message("error")
                    .objectResponse(e.getMessage()).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> convertirCsvParaAutorizacionMuestrasNacionales(InputStream is) {
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader(HEADERs).withIgnoreHeaderCase().withTrim());

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            List<String> errores = new ArrayList<>();
            JSONArray listaExcel = new JSONArray();
            JSONParser jsonParser = new JSONParser();

            for (CSVRecord csvRecord : csvRecords) {
                JSONObject registro = new JSONObject();
                try {
                    System.out.println("mensaje" + csvRecord.get("nombreGenerico"));
                    registro.put("id", csvRecord.get("Id") != null ? csvRecord.get("Id").replace("\"", "") : "");
                    registro.put("nombreGenerico", csvRecord.get("nombreGenerico") != null ? csvRecord.get("nombreGenerico").replace("\"", "") : "");
                    registro.put("concentracion", csvRecord.get("concentracion") != null ? csvRecord.get("concentracion").replace("\"", "") : "");


                    listaExcel.add(registro);
                } catch (Exception e) {
                    //errores.add("error en el registro con id #" + csvRecord.get("id"));
                    return new ResponseEntity<>(GenericResponseDTO.builder().message("error al convertir a json")
                            .objectResponse("error en el registro con id #" + csvRecord.get("id")).statusCode(HttpStatus.I_AM_A_TEAPOT.value()).build(),
                            HttpStatus.I_AM_A_TEAPOT);
                }
            }
            //validaciones de campos

            //consulta paises

            RestTemplate restTemplate = new RestTemplate();

            //--------------------------------------------------


            if (errores.isEmpty()) {
                return new ResponseEntity<>(GenericResponseDTO.builder().message("se retorna el json")
                        .objectResponse(listaExcel).statusCode(HttpStatus.OK.value()).build(),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(GenericResponseDTO.builder().message("error en los registros")
                        .objectResponse(errores).statusCode(HttpStatus.I_AM_A_TEAPOT.value()).build(),
                        HttpStatus.I_AM_A_TEAPOT);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(GenericResponseDTO.builder().message("error")
                    .objectResponse(e.getMessage()).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumentoAsociadoLegalTecnico(Integer idTramite, Integer idPersona) {
        JSONParser parse = new JSONParser();

        try {
            String documentoAsociadoTramite = documentoRepository.consultarDocumentoAsociadoLegalTecnico(idTramite, idPersona);
            JSONObject json = (JSONObject) parse.parse(documentoAsociadoTramite);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) json.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta los documentos asociados por tramite").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {

                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);

            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error  consultando los documentos asociados  por tramite:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumentoAsociadoCoordinador(Integer idTramite) {
        JSONParser parse = new JSONParser();

        try {
            String documentoAsociadoCoordinador = documentoRepository.consultarDocumentoAsociadoCoordinador(idTramite);
            JSONObject json = (JSONObject) parse.parse(documentoAsociadoCoordinador);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) json.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta los documentos asociados del coordinador").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {

                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);

            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error  consultando los documentos asociados  por tramite:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> documentosOAC(String genericRequestDTO) {

        String procedimiento = "Maestra.dbo.USP_DocumentosOAC_S(?,?)";

        try {

            Utils.cargarDriver(env.getProperty(driver));
            String respuesta;
            try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                    env.getProperty(url), env.getProperty(schema));
                 CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                String datoEntrada = "json_in";
                cStmt.setString(datoEntrada, genericRequestDTO);
                cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);
                cStmt.execute();
                respuesta = cStmt.getString(jsonout);
            }

            JSONParser parser = new JSONParser();
            JSONObject jsonRespuesta = (JSONObject) parser.parse(respuesta);
            JSONObject respuestGenerica = (JSONObject) jsonRespuesta.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) jsonRespuesta.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consultan documentos asociados").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error consultando documentos asociados :  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<GenericResponseDTO> crearAprobacionDocumentoAsociado(String genericRequestDTO) {
        try {

            String respuestaObservacion = documentoRepository.guardaAprobacionDocumentoAsociado(genericRequestDTO);
            org.json.simple.parser.JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(respuestaObservacion);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) json.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se guarda o actualiza la Aprobacion del Documento Asociado ").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error Se guarda o actualiza la Aprobacion del Documento Asociado :  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> guardarDocumentoConfiguracion(String json) {
        try {

            String respuestaObservacion = documentoRepository.guardarDocumentoConfiguracion(json);
            org.json.simple.parser.JSONParser parser = new JSONParser();
            JSONObject pJson = (JSONObject) parser.parse(respuestaObservacion);

            JSONObject respuestGenerica = (JSONObject) pJson.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) pJson.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se guarda el documento configuracion ").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error guardando el docuento de configuracion :  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumentosGenerados(Integer idTramite) {
        JSONParser parse = new JSONParser();

        try {
            String documentoAsociadoCoordinador = documentoRepository.consultarDocumentoGenerados(idTramite);
            JSONObject json = (JSONObject) parse.parse(documentoAsociadoCoordinador);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) json.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta los documentos generados al tramite").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {

                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);

            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error  consultando los documentos generados en el tramite:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<GenericResponseDTO> consultarInformacionDocumentoProducir(Integer idTramite) {


        String procedimiento = "Tramites.tramite.USP_ConsultarInformacionDocumentoProducir_S(?,?)";
        try {
            Utils.cargarDriver(env.getProperty(driver));
            String respuestaTramite;
            try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                    env.getProperty("spring.datasource.url.tramite"), env.getProperty("spring.jpa.properties.hibernate.default_schema.tramite"));
                 CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                String datoEntrada = "idTramite_IN";
                cStmt.setInt(datoEntrada, idTramite);
                cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);


                cStmt.execute();

                respuestaTramite = cStmt.getString(jsonout);
            }

            JSONParser parse = new JSONParser();
            JSONObject json = (JSONObject) parse.parse(respuestaTramite);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) json.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta Informacion General Documento Producir").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {

                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);

            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error  consultando los documentos generados en el tramite:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> devolverDocumentoGenerado(String genericRequestDTO) {
        JSONParser parse = new JSONParser();

        try {
            String documentoAsociadoCoordinador = documentoRepository.devolverDocumentoGenerado(genericRequestDTO);
            JSONObject json = (JSONObject) parse.parse(documentoAsociadoCoordinador);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) json.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta Informacion General Documento Producir").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {

                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error  al devolver documento:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumentosFiltro(String genericRequestDTO) {
        JSONParser parse = new JSONParser();

        try {
            String consultarDocumentoFiltro = documentoRepository.consultarDocumentoFiltro(genericRequestDTO);
            JSONObject json = (JSONObject) parse.parse(consultarDocumentoFiltro);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) json.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta documento filtro").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {

                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error  al devolver documento:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<GenericResponseDTO> devolverFactura(String json) {
        JSONParser parse = new JSONParser();

        try {
            String factura = documentoRepository.obtenerInformacionFactura(json);
            JSONObject jsonFactura = (JSONObject) parse.parse(factura);

            JSONObject respuestGenerica = (JSONObject) jsonFactura.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) jsonFactura.get("mensaje");
                if (mensaje != null && !mensaje.isEmpty()) {

                    JSONObject facturas = (JSONObject) mensaje.get(0);
                    JSONObject JsonFactura = (JSONObject) facturas.get("factura");
                    NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
                    DecimalFormat df = (DecimalFormat) nf;
                    Long valorTarifa = (Long) JsonFactura.get("valorTarifa");
                    Long montoPagado = (Long) JsonFactura.get("montoPagado");
                    JsonFactura.put("valorTexto", Utils.cantidadConLetra(String.valueOf(valorTarifa), "PESOS"));
                    JsonFactura.replace("valorTarifa", df.format(valorTarifa));
                    JsonFactura.replace("montoPagado", df.format(montoPagado));
                    JsonFactura.put("Nro", 1);
                    JsonFactura.put("FechaGen", LocalDate.now());
                    String fileBase64 = Utils.filetoBase64(env.getProperty("pathFactura"));
                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    JSONObject documentJsonObject = new JSONObject();
                    documentJsonObject.put("tags", JsonFactura);
                    documentJsonObject.put("base64", fileBase64);
                    HttpEntity<String> request = new HttpEntity<>(documentJsonObject.toString(), headers);
                    String document = restTemplate.postForObject(urlDocuments, request, String.class);
                    JSONObject jsonDoc = (JSONObject) parse.parse(document);
                    String message = (String) jsonDoc.get("message");
                    if (message.equals("OK") || message.equals("Los tags han sido remplazados correctamente")) {
                        return transformWordToPDF(jsonDoc);
                    } else {
                        return new ResponseEntity<>(GenericResponseDTO.builder().message((String) jsonDoc.get("mensaje"))
                                .objectResponse(new JSONObject()).statusCode(HttpStatus.OK.value()).build(),
                                HttpStatus.CONFLICT);
                    }
                } else {
                    return new ResponseEntity<>(GenericResponseDTO.builder().message((String) respuestGenerica.get("mensaje"))
                            .objectResponse(new JSONObject()).statusCode(HttpStatus.OK.value()).build(),
                            HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(GenericResponseDTO.builder().message((String) respuestGenerica.get("mensaje"))
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.OK.value()).build(),
                        HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error  al devolver documento:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param jsonDoc
     * @return
     */
    private ResponseEntity<GenericResponseDTO> transformWordToPDF(JSONObject jsonDoc) {
        String pathFacturaPDF = String.format(env.getProperty("pathFacturaPDF"), Math.random());
        File outputFile = new File(pathFacturaPDF);
        try (InputStream docxInputStream = Utils.base64toFile((String) jsonDoc.get("objectResponse"));
             OutputStream outputStream = new FileOutputStream(outputFile);) {

            XWPFDocument document = new XWPFDocument(docxInputStream);
            PdfConverter.getInstance().convert(document, outputStream, PdfOptions.getDefault());
            String fileBase64PDF = Utils.filetoBase64(pathFacturaPDF);

            return new ResponseEntity<>(GenericResponseDTO.builder().message("Se genera la factura")
                    .objectResponse(fileBase64PDF).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder().message("Error  al devolver documento:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        } finally {
            outputFile.delete();
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> generarMetadataSeSuite(String pJson) {
        JSONParser parse = new JSONParser();

        try {

            String generarMetadataSeSuite = documentoRepository.generarMetadataSeSuite(pJson);
            JSONObject json = (JSONObject) parse.parse(generarMetadataSeSuite);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) json.get("mensaje");
                JSONObject metadata = (JSONObject) mensaje.get(0);

                DecimalFormat format = new DecimalFormat("0.#");

                String metadataDocumento = "";

                for (Object key : metadata.keySet()) {
                    String keyStr = (String) key;
                    Object keyvalue = metadata.get(keyStr);
                    if (keyStr.equals("folio_gd") & keyvalue != null) {
                        keyvalue = format.format(Double.parseDouble(metadata.get(keyStr).toString()));
                    }
                    metadataDocumento = metadataDocumento + (String) key + "=" + keyvalue + ";";
                }
                System.out.println("LA METADATA: " + metadataDocumento);
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se genera la metaData para SeSuite").objectResponse(metadataDocumento)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {

                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);

            }


        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error generando la metaData para seSuite:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumentos(Integer idTramite, String tipo) {

        String procedimiento = "Maestra.dbo.USP_ConsultarDocumentos_S(?,?,?)";

        try {

            Utils.cargarDriver(env.getProperty(driver));
            String respuesta;
            try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                    env.getProperty(url), env.getProperty(schema));
                 CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                String idTramite_in = "idTramite_IN";
                String tipo_in = "tipo";
                cStmt.setInt(idTramite_in, idTramite);
                cStmt.setString(tipo_in, tipo);
                cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);
                cStmt.execute();
                respuesta = cStmt.getString(jsonout);
            }

            JSONParser parser = new JSONParser();
            JSONObject jsonRespuesta = (JSONObject) parser.parse(respuesta);
            JSONObject respuestGenerica = (JSONObject) jsonRespuesta.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) jsonRespuesta.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consultan documentos").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error  consultando los documentos del tramite:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<GenericResponseDTO> actualizarEnviarEnNotificacion(String json1) {
        JSONParser parse = new JSONParser();
        String mensaje = "Se actualiza el envio de notificacion a los documentos enviados";
        try {
            String respuesta = documentoRepository.actualizarEnviarEnNotificacion(json1);
            JSONObject json = (JSONObject) parse.parse(respuesta);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {

                return new ResponseEntity<>(GenericResponseDTO.builder().message(mensaje)
                        .objectResponse(true)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                mensaje = descripcion;

                return new ResponseEntity<>(GenericResponseDTO.builder().message(mensaje)
                        .objectResponse(false).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);

            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error  actualizando los documentos a enviar notificacion:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> crearDocumentoModelo(String json) {
        JSONParser parse = new JSONParser();

        try {
            String documentosTramite = documentoRepository.crearDocumentoModelo(json);
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se crea documento modelo")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error creando documento modelo:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumentoModelo(String json) {
        JSONParser parse = new JSONParser();

        String procedimiento = "dbo.USP_DocumentoModelo_S(?,?)";
        try {
            Utils.cargarDriver(env.getProperty(driver));
            String documentosTramite;
            try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                    env.getProperty(url), env.getProperty(schema));
                 CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                String datoEntrada = jsonin;
                cStmt.setString(datoEntrada, json);
                cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);
                cStmt.execute();
                documentosTramite = cStmt.getString(jsonout);
            }
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta el documento modelo")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error  consultando el documento modelo:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumentoModeloConfiguracion(String json) {
        JSONParser parse = new JSONParser();

        String procedimiento = "dbo.USP_DocumentoModeloConfiguracion_S(?,?)";
        try {
            Utils.cargarDriver(env.getProperty(driver));
            String documentosConfiguracion;
            try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                    env.getProperty(url), env.getProperty(schema));
                 CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                String datoEntrada = jsonin;
                cStmt.setString(datoEntrada, json);
                cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);
                cStmt.execute();
                documentosConfiguracion = cStmt.getString(jsonout);
            }
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosConfiguracion);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta el documento modelo configuracion")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error  consultando el documento modelo configuracion:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumentoModeloTipoDocumental(String json) {
        JSONParser parse = new JSONParser();

        String procedimiento = "USP_DocumentoTipoDocumental_S(?,?)";
        try {
            Utils.cargarDriver(env.getProperty(driver));
            String documentosTramite;
            try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                    env.getProperty(url), env.getProperty(schema));
                 CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                String datoEntrada = jsonin;
                cStmt.setString(datoEntrada, json);
                cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);
                cStmt.execute();
                documentosTramite = cStmt.getString(jsonout);
            }
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta el tipo documental")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error  consultando el tipo documental:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> consultaDinamicaDocumentoModelo(String json) {
        JSONParser parse = new JSONParser();

        try {
            String documentosTramite = documentoRepository.consultaDinamicaDocumentoModelo(json);
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta el documento modelo")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error consultando el documento modelo:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> actualizarDocumentoModelo(String json) {
        JSONParser parse = new JSONParser();

        try {
            String documentosTramite = documentoRepository.actualizarDocumentoModelo(json);
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se actualiza el documento modelo")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error actualizando el documento modelo:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> eliminarDocumentoModelo(String json) {
        JSONParser parse = new JSONParser();

        try {
            String documentosTramite = documentoRepository.eliminarDocumentoModelo(json);
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se elimina el documento modelo")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error eliminando el documento modelo:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> eliminarDocumentoModeloConfiguracion(String json) {
        JSONParser parse = new JSONParser();

        try {
            String documentosTramite = documentoRepository.eliminarDocumentoModeloConfiguracion(json);
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se elimina el documento modelo configuracion")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error eliminando el documento modelo configuracion:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> crearDocumentoModeloSeccion(String json) {
        JSONParser parse = new JSONParser();

        try {
            String documentosTramite = documentoRepository.crearDocumentoModeloSeccion(json);
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se crea el documento modelo seccion")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error creando el documento modelo seccion:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> crearDocumentoModeloSeccionMultiple(String json) {
        JSONParser parse = new JSONParser();

        try {
            String documentosTramite = documentoRepository.crearDocumentoModeloSeccionMultiple(json);
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se crea el documento modelo seccion")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error creando el documento modelo seccion:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumentoModeloSeccion(String json) {
        JSONParser parse = new JSONParser();

    	String procedimiento = "dbo.USP_DocumentoModeloSeccion_S(?,?)";
        try {
            Utils.cargarDriver(env.getProperty(driver));
            String documentosTramite;
            try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                    env.getProperty(url), env.getProperty(schema));
                 CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                String datoEntrada = jsonin;
                cStmt.setString(datoEntrada, json);
                cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);
                cStmt.execute();
                documentosTramite = cStmt.getString(jsonout);
            }
                
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) jsonResponse.get("mensaje");
                if (mensaje == null) {
                    return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta el documento modelo seccion")
                            .objectResponse(mensaje).statusCode(HttpStatus.NO_CONTENT.value()).build(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta el documento modelo seccion")
                            .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
                }
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error consultando el documento modelo seccion:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> actualizarDocumentoModeloSeccion(String json) {
        JSONParser parse = new JSONParser();

        try {
            String documentosTramite = documentoRepository.actualizarDocumentoModeloSeccion(json);
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se actualiza el documento modelo seccion")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error actualizando el documento modelo seccion:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> actualizarDocumentoModeloSeccionMultiple(String json) {
        JSONParser parse = new JSONParser();

        try {
            String documentosTramite = documentoRepository.actualizarDocumentoModeloSeccionMultiple(json);
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se actualiza el documento modelo seccion")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error actualizando el documento modelo seccion:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> eliminarDocumentoModeloSeccion(String json) {
        JSONParser parse = new JSONParser();

        try {
            String documentosTramite = documentoRepository.eliminarDocumentoModeloSeccion(json);
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se elimina el documento modelo seccion")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error eliminando el documento modelo seccion:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> crearDocumentoSeccion(String json) {
        JSONParser parse = new JSONParser();

        try {
            String documentosTramite = documentoRepository.crearDocumentoSeccion(json);
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se crea el documento seccion")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error creando el documento seccion:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumentoSeccion(String json) {
        JSONParser parse = new JSONParser();
        String procedimiento = "dbo.USP_DocumentoSeccion_S(?,?)";
        try {
            Utils.cargarDriver(env.getProperty(driver));
            String documentosTramite;
            try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                    env.getProperty(url), env.getProperty(schema));
                 CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                String datoEntrada = jsonin;
                cStmt.setString(datoEntrada, json);
                cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);
                cStmt.execute();
                documentosTramite = cStmt.getString(jsonout);
            }

            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta el documento seccion")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error  consultando el documento seccion:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> actualizarDocumentoSeccion(String json) {
        JSONParser parse = new JSONParser();

        try {
            String documentosTramite = documentoRepository.actualizarDocumentoSeccion(json);
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se actualiza el documento seccion")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error actualizando el documento seccion:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> eliminarDocumentoSeccion(String json) {
        JSONParser parse = new JSONParser();

        try {
            String documentosTramite = documentoRepository.eliminarDocumentoSeccion(json);
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se elimina el documento seccion")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error elimina el documento seccion:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> generarDocumento(String json) {
        JSONParser parse = new JSONParser();

        String procedimiento = "dbo.USP_DocumentoModelo_Q(?,?)";
        try {
            Utils.cargarDriver(env.getProperty(driver));
            String consultarPlantilla;
            try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                    env.getProperty(url), env.getProperty(schema));
                 CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                String datoEntrada = jsonin;
                cStmt.setString(datoEntrada, json);
                cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);
                cStmt.execute();
                consultarPlantilla = cStmt.getString(jsonout);
            }

            JSONObject respuesta = (JSONObject) parse.parse(consultarPlantilla);

            JSONObject respuestGenerica = (JSONObject) respuesta.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");
            String archivoOutput = (String) respuestGenerica.get("archivoOutput");
            String archivoGenerado = (String) respuestGenerica.get("archivoGenerado");
            String tipoant = "";
            String sHtml = "";
            String cssfile = env.getProperty("pathPDF") + "styles-word.css";

            sHtml += "<!DOCTYPE html>" + "<html lang=\"es\"> " + "<head>"
                    + "<style>.ql-align-center {text-align: center;} .ql-align-justify {text-align: justify;} .ql-align-right {text-align: right;}</style>"
                    + "<link href=\"" + cssfile + "\" rel=\"stylesheet\" type=\"text/css\" />" + "</head>" + "<body>";

            tipoant = "";

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) respuesta.get("mensaje");
                if (mensaje != null && !mensaje.isEmpty())
                    for (int i = 0; i < mensaje.size(); i++) {

                        JSONObject seccion = (JSONObject) mensaje.get(i);
                        JSONObject jsonPlantilla = (JSONObject) seccion.get("plantilla");
                        String tipo = (String) jsonPlantilla.get("tipo");
                        String tipoIntegracion = (String) jsonPlantilla.get("tipoIntegracion");
                        String documentoSeccion = (String) jsonPlantilla.get("documentoSeccion");
                        String integracion = (String) jsonPlantilla.get("integracion");
                        byte[] documentoSeccionBytes = Base64.getDecoder().decode(documentoSeccion);
                        String documentoSeccionString = new String(documentoSeccionBytes, StandardCharsets.UTF_8);

                        if ("JSON".equals(tipoIntegracion.toUpperCase()) || ("SQL".equals(tipoIntegracion.toUpperCase())
                                && (integracion.toUpperCase().contains("SELECT")
                                && integracion.toUpperCase().contains("FROM")))) {
                            System.out.println(" >>> LLAMANDO INTEGRACOION CON <<< " + tipoIntegracion);
                            documentoSeccionString = setVariablesDocumentos(json, parse, tipoIntegracion, integracion,
                                    documentoSeccionString);
                        }

                        if (!tipoant.equals(tipo)) {
                            if (tipoant.equals("Encabezado")) {
                                sHtml += "</header>";
                            } else if (tipoant.equals("Cuerpo")) {
                                sHtml += "</section>";
                            } else if (tipoant.equals("Firma")) {
                                sHtml += "</section>";
                            } else if (tipoant.equals("Pie")) {
                                sHtml += "</footer>";
                            }
                            if (tipo.equals("Encabezado")) {
                                sHtml += "<header> ";
                            } else if (tipo.equals("Cuerpo")) {
                                sHtml += "<section> ";
                            } else if (tipo.equals("Firma")) {
                                sHtml += "<section> ";
                            } else if (tipo.equals("Pie")) {
                                sHtml += "<footer>";
                            }
                            tipoant = tipo;
                        }

                        sHtml += documentoSeccionString;

                    }
            }
            if (tipoant.equals("Encabezado")) {
                sHtml += "</head>";
            } else if (tipoant.equals("Cuerpo")) {
                sHtml += "</section>";
            } else if (tipoant.equals("Firma")) {
                sHtml += "</section>";
            } else if (tipoant.equals("Pie")) {
                sHtml += "</footer>";
            }
            sHtml += "</body>";
            sHtml += "</html>";
            sHtml = sHtml.replaceAll("<br>", "<br/>");
            sHtml = sHtml.replaceAll("\"></strong>", "\"></img></strong>");
            sHtml = sHtml.replaceAll("&nbsp;", "");
            sHtml = sHtml.replaceAll("&nbsp", "");
            /*
             * log.info("----------------"); log.info(sHtml);
             */
            String htmlfile = env.getProperty("pathPDF") + archivoOutput + ".HTML";
            String docxfile = env.getProperty("pathPDF") + archivoOutput + ".docx";
            File f = new File(htmlfile);
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(sHtml);
            bw.close();

            /*
             * System.out.println(" >>> PASO 05 <<< ");
             * System.out.println(" >>> System.getProperty(\"user.dir\") <<< " +
             * System.getProperty("user.dir")); WordprocessingMLPackage wordMLPackage =
             * WordprocessingMLPackage.createPackage(); XHTMLImporterImpl XHTMLImporter =
             * new XHTMLImporterImpl(wordMLPackage);
             * wordMLPackage.getMainDocumentPart().getContent().addAll(XHTMLImporter.convert
             * (sHtml, null)); wordMLPackage.save(new java.io.File(docxfile));
             */

            System.out.println(" >>> PASO 06 <<< ");
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
            wordMLPackage.getMainDocumentPart().addAltChunk(AltChunkType.Xhtml, sHtml.getBytes());
            wordMLPackage.save(new java.io.File(docxfile));

            String plantillaDOCX = null;

            plantillaDOCX = Utils.filetoBase64(docxfile);

            File htmldel = new File(htmlfile);
            if (htmldel.delete()) {
                System.out.println("Archivo Borrado sin problema: " + htmlfile);
            } else {
                System.out.println("No de pudo Borrar el Archivo: " + htmlfile);
            }

            File docxdel = new File(docxfile);
            if (docxdel.delete()) {
                System.out.println("Archivo Borrado sin problema: " + docxfile);
            } else {
                System.out.println("No de pudo Borrar el Archivo: " + docxfile);
            }
            System.out.println(" >>> archivoGenerado <<< " + archivoGenerado);

            return new ResponseEntity<>(
                    GenericResponseDTO.builder().message(archivoGenerado)
                            .objectResponse(plantillaDOCX).statusCode(HttpStatus.OK.value()).build(),
                    HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder().message("Error consultando modelo de Plantilla :  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }


    /**
	 * @param json
	 * @param parse
	 * @param tipoIntegracion
	 * @param integracion
	 * @param documentoSeccionString
	 * @return
	 */

    private String setJsonToString(String documentoSeccionString, JSONObject mensajeIntegracion) {
        Set<Map.Entry<String, Object>> groupSet = mensajeIntegracion.entrySet();
        for (Map.Entry<String, Object> groupEntry : groupSet) {
            String keyStr = groupEntry.getKey();
            Object keyvalue = groupEntry.getValue();
            if (keyvalue == null)
                keyvalue = "";
            documentoSeccionString = documentoSeccionString.replaceAll("\\$\\{" + keyStr + "\\}", keyvalue.toString());
        }
        return documentoSeccionString;
    }



    @SuppressWarnings("unchecked")
	private String setVariablesDocumentos(String json, JSONParser parse, String tipoIntegracion, String integracion, String documentoSeccionString) {
        try {
            if ("SQL".equals(tipoIntegracion.toUpperCase())) {
                String procedimiento;

                procedimiento = "dbo.USP_DocumentoModelo_X(?,?)";
                JSONObject query = new JSONObject();
                JSONObject jsonIN = (JSONObject) parse.parse(json);
                query.put("query", integracion.replaceAll("[\t|\n|\r]", " "));
                query.put("idtramite", ((JSONObject) jsonIN.get("objOperacion")).get("idTramite"));
                query.put("idsolicitud", ((JSONObject) jsonIN.get("objOperacion")).get("idSolicitud"));

                String consultarIntegracion;
                try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                        env.getProperty(url), env.getProperty(schema));
                     CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                    String datoEntrada = jsonin;
                    cStmt.setString(datoEntrada, query.toJSONString());
                    cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);
                    cStmt.execute();
                    consultarIntegracion = cStmt.getString(jsonout);
                }
                JSONObject respuestaIntegracion = (JSONObject) parse.parse(consultarIntegracion);

                JSONObject respuestGenericaIntegracion = (JSONObject) respuestaIntegracion.get("respuesta");
                String codigoIntegracion = (String) respuestGenericaIntegracion.get("codigo");
                JSONObject mensajeIntegracion = (JSONObject) respuestaIntegracion.get("mensaje");
                if (codigoIntegracion.equals("00") && mensajeIntegracion != null) {
                    documentoSeccionString = setJsonToString(documentoSeccionString, mensajeIntegracion);
                }

            } else if ("JSON".equals(tipoIntegracion.toUpperCase())) {
                JSONObject mensajeIntegracion = (JSONObject) parse.parse(integracion);
                documentoSeccionString = setJsonToString(documentoSeccionString, mensajeIntegracion);
            }
        } catch (Exception e) {
            log.error("Error consultando Integración Documentos parametro: " + integracion);
        }
        return documentoSeccionString;
    }


    @Override
    public ResponseEntity<GenericResponseDTO> consultarNumeroFolios(String genericRequestDTO) {
        JSONParser parse = new JSONParser();

        try {
            String consultarDocumentoFiltro = documentoRepository.consultarNumeroFolios(genericRequestDTO);
            JSONObject json = (JSONObject) parse.parse(consultarDocumentoFiltro);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) json.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta número de folios para los documentos asociados").objectResponse(mensaje.get(0))
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {

                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error consultando número de folios:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<GenericResponseDTO> consultarDocGenFirmaYNotifica(String json) {
        JSONParser parse = new JSONParser();

        try {

            return new ResponseEntity<>(GenericResponseDTO.builder().message("Se realzia la Firma Exitosamente")
                    .objectResponse(true)
                    .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);

          /*  JSONObject json2 = (JSONObject) parse.parse(json);
            Long idTramite = (Long) json2.get("idTramite");
            String tipo = "G";

            ResponseEntity<GenericResponseDTO> docs = consultarDocumentos(idTramite.intValue(), tipo);

            JSONArray jsonArrayDoc = (JSONArray) docs.getBody().getObjectResponse();
            JSONArray registrosArray = new JSONArray();
            for (Object doc : jsonArrayDoc) {
                parse = new JSONParser();
                JSONObject jsonDoc = (JSONObject) doc;
                Long idDocumentoSharePoint = (Long) jsonDoc.get("idDocumentoSharePoint");
                String url = env.getProperty("descargarDocumentoSharePoint");
                url = url + idDocumentoSharePoint;

                Object respuesta = Utils.consultarEndPointGET(url);
                String docBase64 = (String) respuesta;

                String urlToPdf = env.getProperty("urlDocToPDf");
                String jsonIN = "{ \n" +
                        "\"firmaBase64\":1\n" +
                        "}";
                JSONObject jsonObjIN = (JSONObject) parse.parse(jsonIN);
                jsonObjIN.put("firmaBase64", docBase64);

                Object pdfDocBase64 = Utils.consultarEndPointPOST(urlToPdf, jsonObjIN.toJSONString());

                String jsonFirma = "{\"usuarioRed\": 0,\n" +
                        "\"firmaBase64\":0,\n" +
                        "\"idSolicitud\":0,\n" +
                        "\"imagenBase64\":\"\",\n" +
                        "}";
                Long idSolicitud = (Long) jsonDoc.get("idSolicitud");
                JSONObject jsonObjFirma = (JSONObject) parse.parse(jsonFirma);

                jsonObjFirma.put("firmaBase64", pdfDocBase64);
                jsonObjFirma.put("usuarioRed", json2.get("usuarioRed"));
                jsonObjFirma.put("idSolicitud", idSolicitud);
                jsonObjFirma.put("imagenBase64", json2.get("firmaImagen"));


                String urlFirma = env.getProperty("urlFirma");
                Object ObjetoFirmado = Utils.consultarEndPointPOST(urlFirma, jsonObjFirma.toJSONString());
                JSONObject pdfFirmadoBase64 = (JSONObject) ObjetoFirmado;
                String pdfStringFirmadoBase64 = (String) pdfFirmadoBase64.get("base64PDFFirmado");
                //TODO enviar a SeSuite
                parse = new JSONParser();
                String jsonRegistro = "\"descripcionCategoria\":\"Registro Sanitario\"\n" +
                        "\"descripcionCortaGrupoProducto\":\"ertert\"\n" +
                        "\"descripcionEstado\":\"Asignado para\"\n" +
                        "\"descripcionTipoNotificacion\":\"Notificación Electrónica\"\n" +
                        "\"descripcionTipoTramite\":\"Null\"\n" +
                        "\"fechaRadicado\":\"2021-11-30\"\n" +
                        "\"idDocumentoSharePoint\":20402,\n" +
                        "\"idTramite\":10157\n" +
                        "\"numeroRadicado\":\"20212000130\"\n" +
                        "\"claseNotificacion\":\"20212000130\"\n" +
                        "\"fechaNotificacion\":\"20212000130\"\n" +
                        "\"EstadoNotificacion\":\"20212000130\"";
                JSONObject jsonRegistroObj = (JSONObject) parse.parse(jsonRegistro);

                String urlConsultarTramiteId = env.getProperty("urlFirma");
                urlConsultarTramiteId = urlConsultarTramiteId + idTramite;
                Object tramiteObjcet = Utils.consultarEndPointGET(urlConsultarTramiteId);
                JSONObject jsonTramiteObj = (JSONObject) tramiteObjcet;

                jsonRegistroObj.put("descripcionCategoria", jsonTramiteObj.get("nombreCategoria"));
                jsonRegistroObj.put("descripcionCortaGrupoProducto", jsonTramiteObj.get("nombreGrupoProducto"));
                jsonRegistroObj.put("descripcionEstado", jsonTramiteObj.get("nombreEstado"));
                jsonRegistroObj.put("descripcionTipoTramite", jsonTramiteObj.get("nombreTipoTramite"));
                jsonRegistroObj.put("fechaRadicado", jsonTramiteObj.get("fechaRadicado"));
                jsonRegistroObj.put("idDocumentoSharePoint", jsonTramiteObj.get("idDocumentoSharePoint"));
                jsonRegistroObj.put("idTramite", idTramite);
                jsonRegistroObj.put("numeroRadicado", jsonTramiteObj.get("numeroRadicado"));


                registrosArray.add(jsonRegistroObj);
                log.info(pdfStringFirmadoBase64);

            }

            String urlNotificacionTramite = env.getProperty("notificacionTramite");

            JSONObject objAuditoria = new JSONObject();
            JSONObject jsonNotificacion = new JSONObject();
            objAuditoria.put("usuario", json2.get("usuarioRed"));
            objAuditoria.put("ip", "172.22.22.30");
            jsonNotificacion.put("objAuditoria", objAuditoria);
            jsonNotificacion.put("registros", registrosArray);


            Object ObjetoFirmado = Utils.consultarEndPointPOST(urlNotificacionTramite, jsonNotificacion.toJSONString());
            log.info(ObjetoFirmado.toString());

            return new ResponseEntity<>(GenericResponseDTO.builder().message("Se realzia la Firma Exitosamente")
                    .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
                    */


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error consultando número de folios:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> consultarDocumentoAutomatico(String json) throws ParseException {
        JSONParser parse = new JSONParser();
        String procedimiento = "dbo.USP_ConsultarDocumentoAutomatico_S(?,?)";
        JSONObject jsonIN = (JSONObject) parse.parse(json);
        Long idTramite = (Long) ((JSONObject) jsonIN.get("objOperacion")).get("idTramite");

        try {
            Utils.cargarDriver(env.getProperty(driver));
            String documentosTramite;
            try (Connection conn = Utils.realizarConexion(env.getProperty(username), env.getProperty(password),
                    env.getProperty(url), env.getProperty(schema));
                 CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}")) {
                cStmt.setString("idTramite_IN", String.valueOf(idTramite));
                cStmt.registerOutParameter(jsonout, Types.LONGVARCHAR);
                cStmt.execute();
                documentosTramite = cStmt.getString(jsonout);
            }
            JSONObject jsonResponse = (JSONObject) parse.parse(documentosTramite);

            JSONObject respuestGenerica = (JSONObject) jsonResponse.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONArray mensaje = (JSONArray) jsonResponse.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se consulta el documento modelo")
                        .objectResponse(mensaje).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    GenericResponseDTO.builder()
                            .message("Error  consultando el documento modelo:  " + e.getMessage())
                            .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
