package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class CrptApi {
    private final TimeUnit timeUnit;
    private final int requestLimit;
    private final AtomicInteger requestCounter = new AtomicInteger(0);
//    private Date lastResetTime = new Date();

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
    }

    public void createDocument(Object document, String signature)
            throws IOException, ParseException {

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(document);
        StringEntity entity = new StringEntity(json);
        HttpClient httpClient = HttpClients.createDefault();
        long currentTime = System.currentTimeMillis();

            while (requestCounter.get() <= requestLimit-1) {
                HttpPost httpPost = new HttpPost("https://ismp.crpt.ru/api/v3/lk/documents/create");
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Signature", signature);
                CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpPost);
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity, "UTF-8");
                requestCounter.incrementAndGet();
                System.out.println("Document creation response: " + responseString);
                long timePassed =  System.currentTimeMillis()-currentTime;

                if (timePassed >= timeUnit.toMillis(1)) {
                    requestCounter.set(requestLimit);
                }
            }
    }

    public static class Description {
        private String participantInn;

        public Description(String participantInn) {
            this.participantInn = participantInn;
        }
        public String getParticipantInn() {
            return participantInn;
        }
        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }
    }


    public static class Product {
        private String certificate_document;
        private  Date certificate_document_date;
        private String certificate_document_number;
        private String owner_inn;
        private String producer_inn;
        private Date production_date;
        private String tnved_code;
        private String uit_code;
        private String uitu_code;

        public Product(String certificate_document, Date certificate_document_date, String certificate_document_number, String owner_inn, String producer_inn, Date production_date, String tnved_code, String uit_code, String uitu_code) {
            this.certificate_document = certificate_document;
            this.certificate_document_date = certificate_document_date;
            this.certificate_document_number = certificate_document_number;
            this.owner_inn = owner_inn;
            this.producer_inn = producer_inn;
            this.production_date = production_date;
            this.tnved_code = tnved_code;
            this.uit_code = uit_code;
            this.uitu_code = uitu_code;
        }

        public String getCertificate_document() {
            return certificate_document;
        }

        public void setCertificate_document(String certificate_document) {
            this.certificate_document = certificate_document;
        }


        public String getCertificate_document_number() {
            return certificate_document_number;
        }

        public void setCertificate_document_number(String certificate_document_number) {
            this.certificate_document_number = certificate_document_number;
        }

        public String getOwner_inn() {
            return owner_inn;
        }

        public void setOwner_inn(String owner_inn) {
            this.owner_inn = owner_inn;
        }

        public String getProducer_inn() {
            return producer_inn;
        }

        public void setProducer_inn(String producer_inn) {
            this.producer_inn = producer_inn;
        }


        public Date getCertificate_document_date() {
            return certificate_document_date;
        }

        public void setCertificate_document_date(Date certificate_document_date) {
            this.certificate_document_date = certificate_document_date;
        }

        public Date getProduction_date() {
            return production_date;
        }

        public void setProduction_date(Date production_date) {
            this.production_date = production_date;
        }

        public String getTnved_code() {
            return tnved_code;
        }

        public void setTnved_code(String tnved_code) {
            this.tnved_code = tnved_code;
        }

        public String getUit_code() {
            return uit_code;
        }

        public void setUit_code(String uit_code) {
            this.uit_code = uit_code;
        }

        public String getUitu_code() {
            return uitu_code;
        }

        public void setUitu_code(String uitu_code) {
            this.uitu_code = uitu_code;
        }
    }

    public static class Root {

        public Root(Description description, String doc_id, String doc_status, String doc_type, boolean importRequest, String owner_inn, String participant_inn, String producer_inn, Date production_date, String production_type, ArrayList<Product> products, Date reg_date, String reg_number) {
            this.description = description;
            this.doc_id = doc_id;
            this.doc_status = doc_status;
            this.doc_type = doc_type;
            this.importRequest = importRequest;
            this.owner_inn = owner_inn;
            this.participant_inn = participant_inn;
            this.producer_inn = producer_inn;
            this.production_date = production_date;
            this.production_type = production_type;
            this.products = products;
            this.reg_date = reg_date;
            this.reg_number = reg_number;
        }

        private Description description;
        private String doc_id;
        private String doc_status;
        private String doc_type;
        private boolean importRequest;
        private String owner_inn;
        private String participant_inn;
        private String producer_inn;
        private Date production_date;
        private String production_type;
        private ArrayList<Product> products;
        private Date reg_date;
        private String reg_number;


        public Description getDescription() {
            return description;
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public String getDoc_id() {
            return doc_id;
        }

        public void setDoc_id(String doc_id) {
            this.doc_id = doc_id;
        }

        public String getDoc_status() {
            return doc_status;
        }

        public void setDoc_status(String doc_status) {
            this.doc_status = doc_status;
        }

        public String getDoc_type() {
            return doc_type;
        }

        public void setDoc_type(String doc_type) {
            this.doc_type = doc_type;
        }

        public boolean isImportRequest() {
            return importRequest;
        }

        public void setImportRequest(boolean importRequest) {
            this.importRequest = importRequest;
        }

        public String getOwner_inn() {
            return owner_inn;
        }

        public void setOwner_inn(String owner_inn) {
            this.owner_inn = owner_inn;
        }

        public String getParticipant_inn() {
            return participant_inn;
        }

        public void setParticipant_inn(String participant_inn) {
            this.participant_inn = participant_inn;
        }

        public String getProducer_inn() {
            return producer_inn;
        }

        public void setProducer_inn(String producer_inn) {
            this.producer_inn = producer_inn;
        }

        public Date getProduction_date() {
            return production_date;
        }

        public void setProduction_date(Date production_date) {
            this.production_date = production_date;
        }

        public Date getReg_date() {
            return reg_date;
        }

        public void setReg_date(Date reg_date) {
            this.reg_date = reg_date;
        }

        public String getProduction_type() {
            return production_type;
        }

        public void setProduction_type(String production_type) {
            this.production_type = production_type;
        }

        public ArrayList<Product> getProducts() {
            return products;
        }

        public void setProducts(ArrayList<Product> products) {
            this.products = products;
        }

        public String getReg_number() {
            return reg_number;
        }

        public void setReg_number(String reg_number) {
            this.reg_number = reg_number;
        }

    }

    public static void main(String[] args)
            throws IOException, InterruptedException, ParseException {

        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 30);


        Object document = new Root(
                new Description("string"),
                "string",
                "string",
                "string",
                true,
                "string",
                "string",
                "string",
                new Date(2020,1,23),
                "string",
                new ArrayList<>(List.of(new Product("string",
                                new Date(2020,1,23),
                                "string",
                                "string",
                                "string",
                                 new Date(2020,1,23),
                                "string",
                                "string",
                                "string")
                        )),

                new Date(2020,1,23),
                "string"
                );

        String signature = "signature";

        crptApi.createDocument(document, signature);
    }
}
