package com.evaluation.webproject.controllers;

import com.evaluation.webproject.http.ResponseTransfer;
import com.evaluation.webproject.models.Client;
import com.evaluation.webproject.models.Proposal;
import com.evaluation.webproject.repositories.ClientRepository;
import com.evaluation.webproject.repositories.ProposalRepository;
import com.evaluation.webproject.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

@RestController
public class ClientController {

    Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    @RequestMapping(value = "/client/{cpf}", method = RequestMethod.GET)
    public @ResponseBody
    List<Proposal> getProposals(@PathVariable Long cpf) {
        Client client = clientRepository.getOne(cpf);
        if (client == null) return null;
        return client.getProposalList();
    }

    @PostMapping(value = "/client", produces = "application/json", consumes = "application/json")
    public @ResponseBody
    ResponseTransfer receiveProposal(@RequestBody Client client) {
        if (!validadeClient(client)) {
            return new ResponseTransfer(Proposal.REJECTED, "Invalid arguments", 0D);
        };

        saveOrUpdateClient(client);
        Proposal proposal = createNewProposal(client);

        Proposal result = analyzeClient(client, proposal);

        proposalRepository.save(result);

//        send(client.getCpf());

        return new ResponseTransfer(result.getStatus(), result.getResultText(), result.getValue());
    }

    public void send(Long order) {
        rabbitTemplate.convertAndSend(this.queue.getName(), order);
    }

    private Proposal createNewProposal(Client client) {
        Proposal proposal = new Proposal();

        proposal.setClientCpf(client.getCpf());
        proposal.setDateOfSubmission(new Date(System.currentTimeMillis()));
        proposal.setStatus(Proposal.NOT_PROCESSED);

        proposalRepository.save(proposal);

        List<Proposal> proposalList = client.getProposalList();
        proposalList.add(proposal);
        client.setProposalList(proposalList);

        return proposal;
    }

    private void saveOrUpdateClient(Client client) {
        List<Proposal> proposalList = null;
        if (clientRepository.existsById(client.getCpf())) {
            Client oldCLient = clientRepository.getOne(client.getCpf());
            if (oldCLient != null) {
                proposalList = oldCLient.getProposalList();
            }
        } else {
            proposalList = new LinkedList<>();
        }
        client.setProposalList(proposalList);
        clientRepository.save(client);
    }

    private boolean validadeClient(Client client) {
        return (
                Util.validadeCPF(client.getCpf())
                        && client.getIncome() != null
                        && Util.validateStringValue(client.getName())
                        && Util.validateIntegerValue(client.getAge(),
                        client.getGender(), client.getMaritalStatus(),
                        client.getDependents())
        );
    }

    private Proposal analyzeClient(Client client, Proposal proposal) {
        Double credit = client.getIncome();
        if (verifyCredit(credit)) {
            return fillProposal(proposal, Proposal.REJECTED, "renda baixa");
        }

        if (client.getMaritalStatus().equals(Client.DIVORCED)) {
            credit = credit - 1000;
            if (verifyCredit(credit)) {
                return fillProposal(proposal, Proposal.REJECTED, "reprovado pela política de crédito");
            }
        }

        int dependentsNumber = client.getDependents();
        if (dependentsNumber > 0) {
            if (dependentsNumber == 1) {
                credit = credit / 2;
            } else {
                credit = credit / dependentsNumber;
            }
        }

        if (verifyCredit(credit)) {
            return fillProposal(proposal, Proposal.REJECTED, "reprovado pela política de crédito");
        }

        return fillProposal(proposal, Proposal.APPROVED, "Aprovado", credit);
    }

    private Proposal fillProposal(Proposal proposal, Integer status, String message) {
        proposal.setDateOfProcessing(new Date(System.currentTimeMillis()));
        proposal.setStatus(status);
        proposal.setResultText(message);
        return proposal;
    }

    private Proposal fillProposal(Proposal proposal, Integer status, String message, Double value) {
        proposal.setDateOfProcessing(new Date(System.currentTimeMillis()));
        proposal.setStatus(status);
        proposal.setResultText(message);
        proposal.setValue(value);
        return proposal;
    }

    private boolean verifyCredit(Double credit) {
        if (credit <= 500 ) {
            return true;
        }
        return false;
    }

}
