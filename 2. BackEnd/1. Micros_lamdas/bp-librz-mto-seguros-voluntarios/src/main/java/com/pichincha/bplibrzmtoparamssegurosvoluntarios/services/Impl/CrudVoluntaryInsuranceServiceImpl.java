package com.pichincha.bplibrzmtoparamssegurosvoluntarios.services.Impl;

import com.pichincha.bplibrzmtoparamssegurosvoluntarios.api.configuration.SqsAWSConfiguration;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.domain.bd.repositories.VoluntaryInsuranceCatalogRepository;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.VoluntaryInsuranceCatalogDto;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.model.dto.response.ResponseGeneralPichincha;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.services.CrudVoluntaryInsuranceService;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.utils.Constants;
import com.pichincha.bplibrzmtoparamssegurosvoluntarios.utils.UtilsSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class CrudVoluntaryInsuranceServiceImpl implements CrudVoluntaryInsuranceService {

    @Autowired
    private VoluntaryInsuranceCatalogRepository voluntaryInsuranceCatalogRepository;

    @Autowired
    private SqsAWSConfiguration sqsAWSConfiguration;

    @Value("${logs.transaction}")
    private String logsTransactionId;


    @Override
    public ResponseGeneralPichincha<List<VoluntaryInsuranceCatalogDto>> findVoluntaryInsurance() {
        ResponseGeneralPichincha<List<VoluntaryInsuranceCatalogDto>> finalResponse = new ResponseGeneralPichincha<>();
        try {
            finalResponse.setData(voluntaryInsuranceCatalogRepository.findAll());
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
    public ResponseGeneralPichincha<String> saveVoluntaryInsurance(VoluntaryInsuranceCatalogDto
                                                                           voluntaryInsuranceCatalogDto) {
        ResponseGeneralPichincha<String> finalResponse = new ResponseGeneralPichincha<>();
        try {
            Integer idGeneral = Math.toIntExact(voluntaryInsuranceCatalogRepository.save(voluntaryInsuranceCatalogDto));
            String messageFinal = UtilsSetting.objectSqsMessage(
                    voluntaryInsuranceCatalogDto.getUsuario(),
                    voluntaryInsuranceCatalogDto.getFechaHora(),
                    voluntaryInsuranceCatalogDto.getIp(),
                    logsTransactionId,
                    null,
                    Constants.Message.MESSAGE_CREATE_ACCION,
                    UtilsSetting.saveRegisternew(idGeneral, voluntaryInsuranceCatalogDto));

            sqsAWSConfiguration.sendMessage(messageFinal);

            finalResponse = generatedResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
            finalResponse.setData(Constants.Message.MESSAGE_SUCESSFULL_SAVE);

            log.info("Guardar Seguro de vid ===> create msg" + messageFinal);

        } catch (Exception e) {
            finalResponse = generatedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            finalResponse.setData(Constants.Message.MESSAGE_ERROR_SAVE);
            return finalResponse;
        }
        return finalResponse;
    }

    @Override
    public ResponseGeneralPichincha<String> updateVoluntaryInsurance(
            VoluntaryInsuranceCatalogDto voluntaryInsuranceCatalogDto) {
        ResponseGeneralPichincha<String> finalResponse = new ResponseGeneralPichincha<>();
        try {

            VoluntaryInsuranceCatalogDto voluntaryInsuranceInicial = voluntaryInsuranceCatalogRepository
                    .findById(voluntaryInsuranceCatalogDto.getId());

            if (voluntaryInsuranceInicial != null) {

                String messageFinal = UtilsSetting.objectSqsMessage(
                        voluntaryInsuranceCatalogDto.getUsuario(),
                        voluntaryInsuranceCatalogDto.getFechaHora(),
                        voluntaryInsuranceCatalogDto.getIp(),
                        logsTransactionId,
                        UtilsSetting.findDifferences(
                                voluntaryInsuranceCatalogDto.getId(),
                                voluntaryInsuranceCatalogDto, voluntaryInsuranceInicial,
                                false),
                        Constants.Message.MESSAGE_UPDATE_ACCION,
                        UtilsSetting.findDifferences(
                                voluntaryInsuranceCatalogDto.getId(),
                                voluntaryInsuranceCatalogDto, voluntaryInsuranceInicial, true)

                );
                log.info("UpdateCreditLine ===> create msg" + messageFinal);


                voluntaryInsuranceInicial.setUsuario(voluntaryInsuranceCatalogDto.getUsuario());
                voluntaryInsuranceInicial.setFechaHora(voluntaryInsuranceCatalogDto.getFechaHora());
                voluntaryInsuranceInicial.setNombrePlan(voluntaryInsuranceCatalogDto.getNombrePlan());
                voluntaryInsuranceInicial.setValorPrima(voluntaryInsuranceCatalogDto.getValorPrima());
                voluntaryInsuranceInicial.setFallecimientoAccidental(voluntaryInsuranceCatalogDto.getFallecimientoAccidental());
                voluntaryInsuranceInicial.setInhabilitacionTotal(voluntaryInsuranceCatalogDto.getInhabilitacionTotal());
                voluntaryInsuranceInicial.setEnfermedadesGraves(voluntaryInsuranceCatalogDto.getEnfermedadesGraves());
                voluntaryInsuranceInicial.setCanastaFallecimiento(voluntaryInsuranceCatalogDto.getCanastaFallecimiento());
                voluntaryInsuranceInicial.setEstado(voluntaryInsuranceCatalogDto.getEstado());
                voluntaryInsuranceInicial.setEstadoPlan(voluntaryInsuranceCatalogDto.getEstadoPlan());
                voluntaryInsuranceInicial.setAsalariado(voluntaryInsuranceCatalogDto.getAsalariado());
                voluntaryInsuranceInicial.setIndependiente(voluntaryInsuranceCatalogDto.getIndependiente());
                voluntaryInsuranceInicial.setPensionado(voluntaryInsuranceCatalogDto.getPensionado());
                voluntaryInsuranceInicial.setEdadMinima(voluntaryInsuranceCatalogDto.getEdadMinima());
                voluntaryInsuranceInicial.setEdadMinimaDias(voluntaryInsuranceCatalogDto.getEdadMinimaDias());
                voluntaryInsuranceInicial.setEdadMaxima(voluntaryInsuranceCatalogDto.getEdadMaxima());
                voluntaryInsuranceInicial.setEdadMaximaDias(voluntaryInsuranceCatalogDto.getEdadMaximaDias());


                sqsAWSConfiguration.sendMessage(messageFinal);

                voluntaryInsuranceCatalogRepository.update(voluntaryInsuranceCatalogDto);
                finalResponse = generatedResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                finalResponse.setData(Constants.Message.MESSAGE_SUCESSFULL_UPDATE);
            } else {
                finalResponse = generatedResponse(HttpStatus.NO_CONTENT.value(), HttpStatus.OK.getReasonPhrase());
                finalResponse.setData(Constants.Message.MESSAGE_ERROR_FINDBYID);
            }
        } catch (Exception e) {
            finalResponse = generatedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage());
            finalResponse.setData(Constants.Message.MESSAGE_ERROR_UPDATE);
        }
        return finalResponse;
    }

    @Override
    public ResponseGeneralPichincha<String> deleteVoluntaryInsurance(
            VoluntaryInsuranceCatalogDto voluntaryInsuranceCatalogDto) {
        ResponseGeneralPichincha<String> finalResponse = new ResponseGeneralPichincha<>();
        try {
            VoluntaryInsuranceCatalogDto voluntaryInsuranceInicial = voluntaryInsuranceCatalogRepository
                    .findById(voluntaryInsuranceCatalogDto.getId());

            if (voluntaryInsuranceInicial.getId() != null) {
                voluntaryInsuranceCatalogRepository.deleteById(voluntaryInsuranceCatalogDto.getId());
                finalResponse = generatedResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                finalResponse.setData(Constants.Message.MESSAGE_SUCESSFULL_DELETE);
            } else {
                finalResponse = generatedResponse(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.getReasonPhrase());
                finalResponse.setData(Constants.Message.MESSAGE_ERROR_FINDBYID);
            }
        } catch (Exception e) {
            finalResponse = generatedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            finalResponse.setData(Constants.Message.MESSAGE_ERROR_DELETE);
        }
        return finalResponse;
    }

    @Override
    public ResponseGeneralPichincha<String> updateStatusVoluntaryInsurance
            (VoluntaryInsuranceCatalogDto voluntaryInsuranceCatalogDto) {
        ResponseGeneralPichincha<String> finalResponse = new ResponseGeneralPichincha<>();
        try {
            VoluntaryInsuranceCatalogDto voluntaryInsuranceInicial = voluntaryInsuranceCatalogRepository
                    .findById(voluntaryInsuranceCatalogDto.getId());

            if (voluntaryInsuranceInicial != null) {

                String messageFinal = UtilsSetting.objectSqsMessage(
                        voluntaryInsuranceCatalogDto.getUsuario(),
                        voluntaryInsuranceCatalogDto.getFechaHora(),
                        voluntaryInsuranceCatalogDto.getIp(),
                        logsTransactionId,
                        UtilsSetting.findDifferencesDelete(
                                voluntaryInsuranceCatalogDto.getId(),
                                voluntaryInsuranceCatalogDto, voluntaryInsuranceInicial,
                                false),
                        Constants.Message.MESSAGE_DELETE_ACCION,
                        UtilsSetting.findDifferencesDelete(
                                voluntaryInsuranceCatalogDto.getId(),
                                voluntaryInsuranceCatalogDto, voluntaryInsuranceInicial, true));
                log.info("UpdateStateCreditLine ===> create msg" + messageFinal);

                    sqsAWSConfiguration.sendMessage(messageFinal);

                voluntaryInsuranceCatalogRepository.updateStatus(voluntaryInsuranceCatalogDto);
                finalResponse = generatedResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                finalResponse.setData(Constants.Message.MESSAGE_SUCESSFULL_DELETE);
            } else {
                finalResponse = generatedResponse(HttpStatus.NO_CONTENT.value(), HttpStatus.OK.getReasonPhrase());
                finalResponse.setData(Constants.Message.MESSAGE_ERROR_FINDBYID);
            }
        } catch (Exception e) {
            finalResponse = generatedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage());
            finalResponse.setData(Constants.Message.MESSAGE_ERROR_DELETE);
        }
        return finalResponse;
    }

    private ResponseGeneralPichincha<String> generatedResponse(Integer statusCode, String message) {
        ResponseGeneralPichincha<String> finalResponse = new ResponseGeneralPichincha<>();
        finalResponse.setStatusCode(statusCode);
        finalResponse.setMessage(message);
        finalResponse.setServiceName(Constants.GeneralMessage.GENERAL_MESSAGE_SERVICE_NAME);
        return finalResponse;
    }

}
