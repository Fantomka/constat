package ru.autoopt.constat.util.kontur.enrichers;

import com.fasterxml.jackson.databind.JsonNode;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

import static ru.autoopt.constat.util.common.CommonHelper.dateIsBefore2022_07_01;
import static ru.autoopt.constat.util.common.CommonHelper.getString;

public class ReqEnricher implements Enricher {
    @Override
    public ContractorDTO enrich(ContractorDTO contractorDTO, KonturConnector connector) {
        JsonNode response = connector.getApi(contractorDTO, "req");

        String orgName = getString(response.get(0).get("UL").get("legalName").get("full"));
        Boolean foundingDate = dateIsBefore2022_07_01(response.get(0).get("UL").get("registrationDate"));
        Boolean isStatusOk = getString(response.get(0).get("UL").get("status").get("statusString")).equals("Действующее");
        // TODO Возможно будут доработки по этому полю

        JsonNode addresses = response.get(0).get("UL").get("history").get("legalAddresses");

        Boolean hasMassOrgAddress;
        if (addresses != null) hasMassOrgAddress = addresses.size() > 1;
        else hasMassOrgAddress = false;

        Boolean lastHeadChangeDateOk = dateIsBefore2022_07_01(response.get(0).get("UL").get("heads").get(0).get("firstDate"));


        contractorDTO.setOrgName(orgName);
        contractorDTO.setFoundingDateOk(foundingDate);
        contractorDTO.setIsStatusOk(isStatusOk);
        contractorDTO.setHasMassOrgAddress(hasMassOrgAddress);
        contractorDTO.setLastHeadChangeDateOk(lastHeadChangeDateOk);

        return contractorDTO;
    }
}
