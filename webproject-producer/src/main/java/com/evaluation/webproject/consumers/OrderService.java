//package com.evaluation.webproject.consumers;
//
//import com.evaluation.webproject.models.Client;
//import com.evaluation.webproject.models.Proposal;
//import com.evaluation.webproject.repositories.ClientRepository;
//import com.evaluation.webproject.repositories.ProposalRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Component
//public class OrderService {
//
//    @Autowired
//    private ClientRepository clientRepository;
//
//    @RabbitListener(queues = {"${queue.order.name}"})
//    private void receive(@Payload Long clientCpf) {
//        log.info("Order: " + clientCpf);
//        //Client client = clientRepository.getOne(clientCpf);
//
//        //log.info(analyzeClient(client));
//
////        log.info("Sucess: " + proposal.getDateOfSubmission().toString());
//    }
//
//    private String analyzeClient(Client client) {
//        Double income = client.getIncome();
//        if (verifyIncome(income)) return "renda baixa";
//
//        if (client.getMaritalStatus().equals(Client.DIVORCED)) {
//            income = income - 1000;
//            if (verifyIncome(income)) return "reprovado pela política de crédito";
//        }
//
//        int dependentsNumber = client.getDependents();
//        if (dependentsNumber > 0) {
//            income = income / dependentsNumber;
//            if (verifyIncome(income)) return "reprovado pela política de crédito";
//        }
//
//        return "aprovado valor de: " +income;
//    }
//
//    private boolean verifyIncome(Double income) {
//        if (income <= 500 ) {
//            return true;
//        }
//        return false;
//    }
//
//}
