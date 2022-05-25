package co.com.invima.maestro.service.srvdocumento.service;

import co.com.invima.canonico.modeloCanonico.dto.documento.DocumentoDTO;
import co.com.invima.canonico.modeloCanonico.dto.generic.GenericRequestDTO;
import co.com.invima.canonico.modeloCanonico.dto.generic.GenericResponseDTO;

import co.com.invima.maestro.service.srvdocumento.commons.ConfiguradorSpring;
import co.com.invima.maestro.service.srvdocumento.commons.converter.DocumentoConverter;
import co.com.invima.maestro.service.srvdocumento.web.api.rest.v1.DocumentoController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfiguradorSpring.class})
public class DocumentoServiceTest {

    @Autowired
    DocumentoController documentoController;

    @Autowired
    DocumentoConverter documentoConverter;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void consultarDocumento() {

        List<DocumentoDTO> documentoDTO = new ArrayList<>();
        try {
            ResponseEntity<GenericResponseDTO> response = documentoController.consultarDocumento();
            System.out.println("Respuesta BD" + response);
            List<Object> lista = (List<Object>) response.getBody().getObjectResponse();
            for (Object tipoDocumento : lista) {
                DocumentoDTO tipoDocumentoDTO1 = new DocumentoDTO();
                modelMapper.map(tipoDocumento, tipoDocumentoDTO1);
                documentoDTO.add(tipoDocumentoDTO1);
            }
            assertEquals(documentoDTO.size(), 5);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Test
    public void consultarPorId() {
        Integer id = 1;
        DocumentoDTO documentoDTO = new DocumentoDTO();
        try {
            ResponseEntity<GenericResponseDTO> response = documentoController.consultarPorID(id);
            Object respuesta = response.getBody().getObjectResponse();
            modelMapper.map(respuesta, documentoDTO);
            assertEquals(documentoDTO.getId(), id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void crear() {
        GenericRequestDTO genericRequestDTO = new GenericRequestDTO();
        try {
            DocumentoDTO documentoDTO = new DocumentoDTO();

            documentoDTO.setFechaCreacionDocumento(new Date());
            documentoDTO.setFechaDocumento(new Date());
            documentoDTO.setIdEcm("usuario prueba");
            documentoDTO.setNombre("usuario prueba");
            documentoDTO.setNumeroFolios(1);
            documentoDTO.setIdExpediente(1);
            documentoDTO.setTipoDocumento(1);
            documentoDTO.setIdTramite(1);
            documentoDTO.setPruebaConcepto("usuario que modifica");
            documentoDTO.setFechaModifica(new Date());
            documentoDTO.setUsuarioModifica("usuario que modifica");
            documentoDTO.setFechaCreacion(new Date());
            documentoDTO.setUsuarioCrea("usuario prueba");


            genericRequestDTO.setRequest(documentoDTO);
            ResponseEntity<GenericResponseDTO> response = documentoController.crear(genericRequestDTO);
            Object respuesta = response.getBody().getObjectResponse();

            assertEquals(respuesta, true);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void actualizar() {
        GenericRequestDTO genericRequestDTO = new GenericRequestDTO();
        try {
            DocumentoDTO documentoDTO = new DocumentoDTO();

            documentoDTO.setId(1);
            documentoDTO.setFechaCreacionDocumento(new Date());
            documentoDTO.setFechaDocumento(new Date());
            documentoDTO.setIdEcm("usuario prueba2");
            documentoDTO.setNombre("usuario prueba2");
            documentoDTO.setNumeroFolios(1);
            documentoDTO.setIdExpediente(1);
            documentoDTO.setTipoDocumento(1);
            documentoDTO.setIdTramite(1);
            documentoDTO.setPruebaConcepto("usuario que modifica");
            documentoDTO.setFechaModifica(new Date());
            documentoDTO.setUsuarioModifica("usuario que modifica");
            documentoDTO.setFechaCreacion(new Date());
            documentoDTO.setUsuarioCrea("usuario prueba");

            genericRequestDTO.setRequest(documentoDTO);
            ResponseEntity<GenericResponseDTO> response = documentoController.actualizar(genericRequestDTO);
            Object respuesta = response.getBody().getObjectResponse();

            assertEquals(respuesta, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void eliminadoLogicoPorId() {
        Integer id = 1;
        try {
            ResponseEntity<GenericResponseDTO> response = documentoController.eliminadoLogicoPorId(id);
            Object respuesta = response.getBody().getObjectResponse();
            assertEquals(respuesta, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void documentosAsociadosTramite() {
        Integer id = 3;
        try {
            ResponseEntity<GenericResponseDTO> response = documentoController.documentosAsociadosTramite(id);
            Object respuesta = response.getBody().getObjectResponse();
            assertEquals(respuesta, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void consultarArticuloporidDocumento() {
        Integer idDocumento = 1;
        try {
            ResponseEntity<GenericResponseDTO> response = documentoController.consultarArticuloporidDocumento(idDocumento);
            Object respuesta = response.getBody().getObjectResponse();
            System.out.println("LA RESPUESTA" + respuesta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Test
    public void consultarDocumentoGenerar() {
        Integer idEtapa = 1;
        Integer idRol = 8;
        Integer idConcepto = 1;

        try {
            ResponseEntity<GenericResponseDTO> response = documentoController.consultarDocumentoGenerar(idEtapa, idRol, idConcepto);
            Object respuesta = response.getBody().getObjectResponse();
            System.out.println("LA RESPUESTA" + respuesta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Test
    public void guardarDocumento() {
        String json = "{\n"
                + "     \"nombre\": \"Documento,\""
                + "     \"detalle\": \"prueba,\""
                + "     \"numeroFolios\":3,"
                + "     \"fechaCreacionDocumento\": \"2021-07-14 12:16:00.000\"," +
                "}";
        try {

            ResponseEntity<GenericResponseDTO> response = documentoController.guardarDocumento(json);
            Object respuesta = response.getBody().getObjectResponse();
            System.out.println("LA RESPUESTA" + respuesta);
            //assertEquals(response.getBody().getStatusCode(), 200);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void consultarDocumentosAsociados() {
        List<DocumentoDTO> documentoDTOS = new ArrayList<>();
        Integer id = 2;
        try {
            ResponseEntity<GenericResponseDTO> response = documentoController.consultarDocumentosAsociados(id);
            System.out.println("Respuesta Base de Datos" + response);
            List<Object> lista = (List<Object>) response.getBody().getObjectResponse();
            for (Object documento : lista) {
                DocumentoDTO documentoDTO = new DocumentoDTO();
                modelMapper.map(documento, documentoDTO);
                documentoDTOS.add(documentoDTO);
            }
            assertEquals(documentoDTOS.size(), 2);

            // modelMapper.map(this.service.findById(1).getBody().getObjectResponse(),
            // pago);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void newDocument(){
        String json="";
        //NewDocumentResponseType response = newDocument.newDocument(json);
    }


    @Test
    public void consultarDocumentoAsociadoLegalTecnico() {
        Integer idTramite = 10157;
        Integer idPersona = 24;

        try {
            ResponseEntity<GenericResponseDTO> response = documentoController.consultarDocumentoAsociadoLegalTecnico(idTramite, idPersona);
            Object respuesta = response.getBody().getObjectResponse();
            System.out.println("Respuesta Base de Datos" + respuesta);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Test
    public void consultarDocumentoAsociadoCoordinador() {
        Integer idTramite = 3;

        try {
            ResponseEntity<GenericResponseDTO> response = documentoController.consultarDocumentoAsociadoCoordinador(idTramite);
            Object respuesta = response.getBody().getObjectResponse();
            System.out.println("Respuesta Base de Datos" + respuesta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void consultarDocumentoGeneradosTramite() {
        Integer idTramite = 10157;

        try {
            ResponseEntity<GenericResponseDTO> response = documentoController.consultarDocumentoGeneradosTramite(idTramite);
            Object respuesta = response.getBody().getObjectResponse();
            System.out.println("Respuesta Base de Datos" + respuesta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void consultarInformacionDocumentoProducirTramite() {
        Integer idTramite = 10157;

        try {
            ResponseEntity<GenericResponseDTO> response = documentoController.consultarInformacionDocumentoProducirTramite(idTramite);
            Object respuesta = response.getBody().getObjectResponse();
            System.out.println("Respuesta Base de Datos" + respuesta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void consultarInformacionDocumentoModeloTipoDocumental() {
        String json = "{\r\n"
                + "\"objAuditoria\":{\r\n"
                + "\"usuario\":\"ccamilo\",\r\n"
                + "\"ip\":\"192.123.123.1\"\r\n"
                + "}\r\n"
                + "}";

        try {
            ResponseEntity<GenericResponseDTO> response = documentoController.consultarDocumentoModeloTipoDocumental(json);
            Object respuesta = response.getBody().getObjectResponse();
            System.out.println("Respuesta Base de Datos" + respuesta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void guardarAprobacionDocumento() {
        String json = "{\n " +
                "\"ObservacionDocumento\": {" +
                "\n \"idAprobacionDocumentoRol\" : 1,  " +
                "\n \"idDocumento\" : 24472 , " +
                "\n \"observacion\" : \"observacion\", " +
                "\n \"aprobacion\"  : true , " +
                "\n \"idPersona\"   : 24 , " +
                "\n \"idRolPersona\": 8 , " +
                "\n \"activo\" : 1 , " +
                "\n \"usuarioCrea\" : \"naguilar\" , " +
                "\n \"usuarioModifica\" : \"naguilar \"" +
                "\n }" +
                "}";
        try {

            ResponseEntity<GenericResponseDTO> response = documentoController.guardarAprobacionDocumento(json);
            Object respuesta = response.getBody().getObjectResponse();
            System.out.println("LA RESPUESTA" + respuesta);
            //assertEquals(response.getBody().getStatusCode(), 200);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
