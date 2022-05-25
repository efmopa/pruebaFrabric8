package co.com.invima.maestro.service.srvdocumento.commons.converter;

import co.com.invima.maestro.modeloTransversal.dto.documento.DocumentoDTO;
import co.com.invima.maestro.modeloTransversal.entities.documento.DocumentoDAO;
import co.com.invima.maestro.modeloTransversal.dto.tipoDocumental.TipoDocumentalDTO;
import co.com.invima.maestro.service.srvdocumento.commons.Utils;
import javassist.NotFoundException;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentoConverter {

    private final ModelMapper modelMapper;
    @Autowired
    public DocumentoConverter(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    /**
     * @param documentoDTO
     * @return documentoDAO
     * @author Jpeña
     * method to convert tipoTramiteDTO to tipoTramiteDAO
     */
    public DocumentoDAO documentoDTOtoDAO(DocumentoDTO documentoDTO, ModelMapper modelMapper) {
        DocumentoDAO documentoDAO = new DocumentoDAO();
        modelMapper.map(documentoDTO, documentoDAO);
        return documentoDAO;

    }

    /**
     * @return ParameterDAO
     * @author Sortiz
     * method to convert tipoTramiteDAO to tipoTramiteDTO
     */
    public DocumentoDTO documentoDAOtoDTO(DocumentoDAO documentoDAO,
                                                  ModelMapper modelMapper) throws NotFoundException {
        DocumentoDTO documentoDTO = new DocumentoDTO();
        modelMapper.map(documentoDAO, documentoDTO);

        return documentoDTO;
    }



}
