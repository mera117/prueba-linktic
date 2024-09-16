package com.pichincha.bplibrzmtorangotasas.services.impl;

import com.pichincha.bplibrzmtorangotasas.api.configuration.SqsAWSConfiguration;
import com.pichincha.bplibrzmtorangotasas.domain.db.repositories.TermRateRangeRepository;
import com.pichincha.bplibrzmtorangotasas.model.dto.TermRateRangeDto;
import com.pichincha.bplibrzmtorangotasas.model.dto.response.ResponseGeneralPichincha;
import com.pichincha.bplibrzmtorangotasas.services.CrudTermRateRangeService;
import com.pichincha.bplibrzmtorangotasas.utils.Constants;
import com.pichincha.bplibrzmtorangotasas.utils.UtilsSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class CrudTermRateRangeServiceImpl implements CrudTermRateRangeService {

	@Autowired
	private TermRateRangeRepository termRateRangeRepository;

	@Autowired
	private SqsAWSConfiguration sqsAWSConfiguration;

	@Value("${logs.transaction}")
	private String logsTransactionId;

	private static final String CREATEACCION = "Ingresar";
	private static final String UPDATEACCION = "Modificar";

	@Override
	public ResponseGeneralPichincha<List<TermRateRangeDto>> findTermRateRange() {
		ResponseGeneralPichincha<List<TermRateRangeDto>> finalResponse = new ResponseGeneralPichincha<>();
		try {
			finalResponse.setData(termRateRangeRepository.findAll());
			finalResponse.setStatusCode(HttpStatus.OK.value());
			finalResponse.setMessage(HttpStatus.OK.getReasonPhrase());
			finalResponse.setServiceName(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME);
		} catch (Exception e) {
			finalResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			finalResponse.setMessage(e.getMessage());
			finalResponse.setServiceName(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME);
			return finalResponse;
		}
		return finalResponse;
	}

	@Override
	public ResponseGeneralPichincha<String> saveTermRateRange(TermRateRangeDto termRateRangeDto) {
		ResponseGeneralPichincha<String> finalResponse = new ResponseGeneralPichincha<>();
		try {
			String messageFinal = UtilsSetting.objectSqsMessage(termRateRangeDto.getCreadoPor(),
					termRateRangeDto.getFechaCreacion(), termRateRangeDto.getIp(), logsTransactionId, null,
					CREATEACCION,
					UtilsSetting.saveRegisternew(termRateRangeDto.getIdPlazo(), termRateRangeDto));

			sqsAWSConfiguration.sendMessage(messageFinal);

			termRateRangeRepository.save(termRateRangeDto);
			finalResponse = generatedResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
			finalResponse.setData(Constants.Message.MESSAGE_SUCESSFULL_SAVE);

		} catch (Exception e) {
			finalResponse = generatedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
			finalResponse.setData(Constants.Message.MESSAGE_ERROR_SAVE);
			return finalResponse;
		}
		return finalResponse;
	}

	@Override
	public ResponseGeneralPichincha<String> updateTermRateRange(TermRateRangeDto termRateRangeDto) {
		ResponseGeneralPichincha<String> finalResponse = new ResponseGeneralPichincha<>();
		try {
			TermRateRangeDto termRateRangeDtoInicial = termRateRangeRepository.findById(termRateRangeDto.getIdPlazo());

			String messageFinal = UtilsSetting.objectSqsMessage(termRateRangeDto.getActualizadoPor(),
					termRateRangeDto.getFechaActualizacion(), termRateRangeDto.getIp(), logsTransactionId,
					UtilsSetting.findDifferences(termRateRangeDto.getIdPlazo(), termRateRangeDto,
							termRateRangeDtoInicial, false),
					UPDATEACCION, UtilsSetting.findDifferences(termRateRangeDto.getIdPlazo(), termRateRangeDto,
							termRateRangeDtoInicial, true));

			sqsAWSConfiguration.sendMessage(messageFinal);

			if (termRateRangeDtoInicial.getIdPlazo() != null) {
				termRateRangeRepository.update(termRateRangeDto);
				finalResponse = generatedResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
				finalResponse.setData(Constants.Message.MESSAGE_SUCESSFULL_UPDATE);
			} else {
				finalResponse = generatedResponse(HttpStatus.NO_CONTENT.value(), HttpStatus.OK.getReasonPhrase());
				finalResponse.setData(Constants.Message.MESSAGE_ERROR_FINDBYID);
			}
		} catch (Exception e) {
			finalResponse = generatedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
			finalResponse.setData(Constants.Message.MESSAGE_ERROR_UPDATE);
		}
		return finalResponse;
	}

	@Override
	public ResponseGeneralPichincha<String> deleteTermRateRange(TermRateRangeDto termRateRangeDto) {
		ResponseGeneralPichincha<String> finalResponse = new ResponseGeneralPichincha<>();
		try {
			TermRateRangeDto termRateRangeDtoInicial = termRateRangeRepository.findById(termRateRangeDto.getIdPlazo());

			if (termRateRangeDtoInicial.getIdPlazo() != null) {
				termRateRangeRepository.deleteById(termRateRangeDto.getIdPlazo());
				finalResponse = generatedResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
				finalResponse.setData(Constants.Message.MESSAGE_SUCESSFULL_DELETE);
			} else {
				finalResponse = generatedResponse(HttpStatus.NO_CONTENT.value(),
						HttpStatus.NO_CONTENT.getReasonPhrase());
				finalResponse.setData(Constants.Message.MESSAGE_ERROR_FINDBYID);
			}
		} catch (Exception e) {
			finalResponse = generatedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
			finalResponse.setData(Constants.Message.MESSAGE_ERROR_DELETE);

		}
		return finalResponse;
	}

	public ResponseGeneralPichincha<List<TermRateRangeDto>> findTermRateRangeByDescription(String rangoPlazo) {
		ResponseGeneralPichincha<List<TermRateRangeDto>> response = new ResponseGeneralPichincha<>();
		List<TermRateRangeDto> resultList = new ArrayList<>();

		try {
			if (isEmptyOrNull(rangoPlazo)) {
				throw new IllegalArgumentException(Constants.GeneralMessage.MESSAGE_ILEGAL_ARGUMEN);
			}

			if (!isIntegerParseable(rangoPlazo)) {
				throw new IllegalArgumentException(Constants.GeneralMessage.MESSAGE_ILEGAL_ARGUMEN_PARSE);
			}

			List<TermRateRangeDto> termRateRangeDtoFinal = termRateRangeRepository
					.findTermRateRangeByDescriptionActive();
			int rangoPlazoInt = Integer.parseInt(rangoPlazo);

			for (TermRateRangeDto termRateRangeDtoFor : termRateRangeDtoFinal) {
				if (matchesGroup(termRateRangeDtoFor.getDescriptionPlazo(), rangoPlazoInt)) {
					resultList.add(termRateRangeDtoFor);
				}
			}

			if (resultList.isEmpty()) {
				throw new NoSuchElementException(Constants.GeneralMessage.MESSAGE_ILEGAL_ARGUMEN_EMPTY);
			}

			response.setData(resultList);
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage(HttpStatus.OK.getReasonPhrase());
			response.setServiceName(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME);
		} catch (IllegalArgumentException e) {
			log.error("Error de argumento: '{}'", rangoPlazo, e);
			response.setStatusCode(HttpStatus.BAD_REQUEST.value());
			response.setMessage(e.getMessage());
			response.setServiceName(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME);
		} catch (Exception e) {
			log.error("Error al procesar la solicitud", e);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage(e.getMessage());
			response.setServiceName(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME);
		}
		return response;
	}

	public static boolean isEmptyOrNull(String str) {
		return str == null || str.isEmpty();
	}

	public static boolean isIntegerParseable(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean matchesGroup(String nombre, int value) {
		if (nombre.contains(",")) {
			return validateCommaSeparated(nombre, value);
		} else if (nombre.contains("-")) {
			return validateRango(nombre, value);
		} else if (nombre.contains(">")) {
			return validateGreaterThan(nombre, value);
		} else if (nombre.contains("<")) {
			return validateLessThan(nombre, value);
		}
		return false;
	}

	public boolean validateCommaSeparated(String nombre, int value) {
		String[] parts = nombre.replaceAll("[()]", "").split(",");
		for (String part : parts) {
			if (Integer.parseInt(part.trim()) == value) {
				return true;
			}
		}
		return false;
	}

	public boolean validateRango(String nombre, int value) {
		String[] parts = nombre.split("-");
		int lowerBound = Integer.parseInt(parts[0].trim());
		int upperBound = Integer.parseInt(parts[1].trim());
		return value >= lowerBound && value <= upperBound;
	}

	public boolean validateGreaterThan(String nombre, int value) {
		String number = nombre.replaceAll(">", "").trim();
		int threshold = Integer.parseInt(number);
		return value > threshold;
	}

	public boolean validateLessThan(String nombre, int value) {
		String number = nombre.replaceAll("<", "").trim();
		int threshold = Integer.parseInt(number);
		return value < threshold;
	}

	private ResponseGeneralPichincha<String> generatedResponse(Integer statusCode, String message) {
		ResponseGeneralPichincha<String> finalResponse = new ResponseGeneralPichincha<>();
		finalResponse.setStatusCode(statusCode);
		finalResponse.setMessage(message);
		finalResponse.setServiceName(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME);
		return finalResponse;
	}
}
