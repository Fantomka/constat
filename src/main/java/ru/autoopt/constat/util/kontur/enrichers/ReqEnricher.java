package ru.autoopt.constat.util.kontur.enrichers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

import static ru.autoopt.constat.util.common.CommonHelper.dateIsBeforeYear;
import static ru.autoopt.constat.util.common.CommonHelper.getString;

@Component
@AllArgsConstructor
public class ReqEnricher implements Enricher {

    private final KonturConnector connector;

    @Override
    public void enrich(ContractorDTO contractorDTO) {
        JsonNode response = connector.getByApi(contractorDTO.getINN(), "req");

        String orgName = getString(response.get(0).get("UL").get("legalName").get("full"));
        Boolean foundingDate = dateIsBeforeYear(response.get(0).get("UL").get("registrationDate"));
        Boolean isStatusOk = getString(response.get(0).get("UL").get("status").get("statusString")).equals("Действующее");

        JsonNode addresses = response.get(0).get("UL").get("history").get("legalAddresses");

        Boolean hasMassOrgAddress;
        if (addresses != null) hasMassOrgAddress = addresses.size() > 1;
        else hasMassOrgAddress = false;

        Boolean lastHeadChangeDateOk = dateIsBeforeYear(response.get(0).get("UL").get("heads").get(0).get("firstDate"));

        contractorDTO.setFocusHref(getString(response.get(0).get("focusHref")));
        contractorDTO.setOrgName(orgName);
        contractorDTO.setFoundingDateOk(foundingDate);
        contractorDTO.setIsStatusOk(isStatusOk);
        contractorDTO.setHasMassOrgAddress(hasMassOrgAddress);
        contractorDTO.setLastHeadChangeDateOk(lastHeadChangeDateOk);
    }

    public String getOrgName(String inn) {
        JsonNode response = connector.getByApi(inn, "req");
        return getString(response.get(0).get("UL").get("legalName").get("full"));

    }
}
