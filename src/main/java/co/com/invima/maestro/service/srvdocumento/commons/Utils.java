package co.com.invima.maestro.service.srvdocumento.commons;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Base64;
import java.util.Properties;

import co.com.invima.canonico.modeloCanonico.dto.generic.GenericResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.multipart.MultipartFile;

import co.com.invima.maestro.service.srvdocumento.service.DocumentoService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;

@Slf4j
public class Utils {

    public boolean tieneCSVFormat(MultipartFile file) {

        if (!DocumentoService.TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static String filetoBase64(String inputFile) {
        String base64File = "";
        File file = new File(inputFile);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a file from file system
            byte fileData[] = new byte[(int) file.length()];
            imageInFile.read(fileData);
            base64File = Base64.getEncoder().encodeToString(fileData);
        } catch (FileNotFoundException e) {
            System.out.println("No Existe Archivo" + e);
        } catch (IOException ioe) {
            System.out.println("No puede Leer el Archivo " + ioe);
        }
        return base64File;
    }
    
    public static InputStream base64toFile(String base64File) {
    	byte[] decodedBytes = Base64.getDecoder().decode(base64File);
    	return new ByteArrayInputStream(decodedBytes);
    }
    
    public static void cargarDriver(String clasName) {
        try {
            Class.forName(clasName);
        } catch (ClassNotFoundException e1) {
            log.error("Error 1: " + e1);
            e1.printStackTrace();
        }
    }

    public static Connection realizarConexion(String user, String password, String url, String schema) {
        Properties connectionProps = new Properties();
        Connection conn;
        try {
            connectionProps.put("user", user);
            connectionProps.put("password", password);
            conn = DriverManager.getConnection(url, connectionProps);
            conn.setSchema(schema);
            return conn;
        } catch (Exception e) {
            log.error("Error 2: " + e);
            e.printStackTrace();
            return null;
        }
    }
    
	/**
	 * Convierte el número que recibe como argumento a su representación escrita con letra.
	 *
	 * @param s     Una cadena de texto que contiene los dígitos de un número.
	 * @param texto Una cadena de texto para adicionar al final de la conversión del numero.
	 * @return Una cadena de texto que contiene la representación con letra de la parte entera del número que se recibió como argumento.
	 */
	public static String cantidadConLetra(String s, String texto) {
		StringBuilder result = new StringBuilder();
		BigDecimal totalBigDecimal = new BigDecimal(s).setScale(2);
		long parteEntera = totalBigDecimal.toBigInteger().longValue();
		int triUnidades = (int) ((parteEntera % 1000));
		int triMiles = (int) ((parteEntera / 1000) % 1000);
		int triMillones = (int) ((parteEntera / 1000000) % 1000);
		int triMilMillones = (int) ((parteEntera / 1000000000) % 1000);

		if (parteEntera == 0) {
			result.append("Cero").append(texto);
			return result.toString().toUpperCase();
		}

		if (triMilMillones > 0) {
			result.append(triTexto(triMilMillones).toString() + "Mil ");
		}
		if (triMillones > 0) {
			result.append(triTexto(triMillones).toString());
		}

		if (triMilMillones == 0 && triMillones == 1) {
			result.append("Millón ");
		} else if (triMilMillones > 0 || triMillones > 0) {
			result.append("Millones ");
		}

		if (triMiles > 0) {
			result.append(triTexto(triMiles).toString() + "Mil ");
		}
		if (triUnidades > 0) {
			result.append(triTexto(triUnidades).toString());
		}

		result.append(texto);

		return result.toString().toUpperCase();
	}

	/**
	 * Convierte una cantidad de tres cifras a su representación escrita con letra.
	 *
	 * @param n La cantidad a convertir.
	 * @return Una cadena de texto que contiene la representación con letra del número que se recibió como argumento.
	 */
	private static StringBuilder triTexto(int n) {
		StringBuilder result = new StringBuilder();
		int centenas = n / 100;
		int decenas = (n % 100) / 10;
		int unidades = (n % 10);

		switch (centenas) {
		case 1:
			if (decenas == 0 && unidades == 0) {
				result.append("Cien ");
				return result;
			} else
				result.append("Ciento ");
			break;
		case 2:
			result.append("Doscientos ");
			break;
		case 3:
			result.append("Trescientos ");
			break;
		case 4:
			result.append("Cuatrocientos ");
			break;
		case 5:
			result.append("Quinientos ");
			break;
		case 6:
			result.append("Seiscientos ");
			break;
		case 7:
			result.append("Setecientos ");
			break;
		case 8:
			result.append("Ochocientos ");
			break;
		case 9:
			result.append("Novecientos ");
			break;
		default:
			break;

		}

		switch (decenas) {
		case 1:
			if (unidades == 0) {
				result.append("Diez ");
				return result;
			} else if (unidades == 1) {
				result.append("Once ");
				return result;
			} else if (unidades == 2) {
				result.append("Doce ");
				return result;
			} else if (unidades == 3) {
				result.append("Trece ");
				return result;
			} else if (unidades == 4) {
				result.append("Catorce ");
				return result;
			} else if (unidades == 5) {
				result.append("Quince ");
				return result;
			} else
				result.append("Dieci");
			break;
		case 2:
			if (unidades == 0) {
				result.append("Veinte ");
				return result;
			} else
				result.append("Veinti");
			break;
		case 3:
			result.append("Treinta ");
			break;
		case 4:
			result.append("Cuarenta ");
			break;
		case 5:
			result.append("Cincuenta ");
			break;
		case 6:
			result.append("Sesenta ");
			break;
		case 7:
			result.append("Setenta ");
			break;
		case 8:
			result.append("Ochenta ");
			break;
		case 9:
			result.append("Noventa ");
			break;
		default:
			break;
		}

		if (decenas > 2 && unidades > 0)
			result.append("y ");

		switch (unidades) {
		case 1:
			result.append("Un ");
			break;
		case 2:
			result.append("Dos ");
			break;
		case 3:
			result.append("Tres ");
			break;
		case 4:
			result.append("Cuatro ");
			break;
		case 5:
			result.append("Cinco ");
			break;
		case 6:
			result.append("Seis ");
			break;
		case 7:
			result.append("Siete ");
			break;
		case 8:
			result.append("Ocho ");
			break;
		case 9:
			result.append("Nueve ");
			break;
		default:
			break;
		}

		return result;
	}
	public static Object consultarEndPointGET(String url) throws JsonProcessingException, ParseException {

		JSONParser parser = new JSONParser();
		ObjectMapper obj = new ObjectMapper();
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);
		Invocation.Builder solicitud = target.request();
		Response response = solicitud.get();
		String responseJson = response.readEntity(String.class);
		Gson g = new Gson();
		GenericResponseDTO s = g.fromJson(responseJson, GenericResponseDTO.class);
		Object respuestaRequest = s.getObjectResponse();
		String jsonStr = obj.writeValueAsString(respuestaRequest);
		Object respuesta = parser.parse(jsonStr);

		return respuesta;
	}
	public static Object consultarEndPointPOST(String url, String json) throws JsonProcessingException, ParseException {

		ObjectMapper obj = new ObjectMapper();
		JSONParser parser = new JSONParser();
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);
		Invocation.Builder solicitud = target.request();
		Response response = solicitud.post(Entity.json(json));
		String responseJson = response.readEntity(String.class);
		Gson g = new Gson();
		GenericResponseDTO s = g.fromJson(responseJson, GenericResponseDTO.class);
		Object respuestaRequest = s.getObjectResponse();
		String jsonStr = obj.writeValueAsString(respuestaRequest);
		Object respuesta = parser.parse(jsonStr);

		return respuesta;
	}
}
